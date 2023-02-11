/**
 * Copyright (C) 2021 despg.dev, Ralf Buschermöhle
 *
 * DESPG is made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * see LICENSE
 *
 */
package dev.despg.core;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Simulation
{
	private static Logger logger = Logger.getLogger("dev.despg.core.Simulation");

	static
	{
		try
		{
			String path = Simulation.class.getClassLoader().getResource("logging.properties").getFile();
			System.setProperty("java.util.logging.config.file", path);
		}
		catch (Exception e)
		{
			logger.setLevel(Level.INFO);
			System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s %n");
		}
	}

	/**
	 * Called at every timeStep where one or more events occurred.
	 *
	 * @param numberOfSteps number of simulation steps (main-loop)
	 * @param timeStep current timeStep
	 */
	protected abstract void printEveryStep(long numberOfSteps, long timeStep);

	/**
	 * The simulate() method contains the main loop of the simulation. For every
	 * timeStep it iterates over every SimulationObject and triggers its simulate()
	 * method. If no Event occurred during that timeStep, it switches to the
	 * timeStep of the next Event in the EventQueue.
	 *
	 * @return timeStep after the simulation is over
	 */
	public long simulate()
	{
		EventQueue eventqueue = EventQueue.getInstance();
		SimulationObjects simulationObjects = SimulationObjects.getInstance();
		long numberOfSteps = 1;
		long timeStep = 0;
		Event e = null;

		do
		{
			printEveryStep(numberOfSteps, timeStep);

			numberOfSteps++;

			boolean oneSwitched;
			do
			{
				oneSwitched = false;

				for (SimulationObject so : simulationObjects)
				{
					if (so.simulate(timeStep))
					{
						oneSwitched = true;
						logger.log(Level.FINEST, timeStep + " switched" + so);
					}
				}
			} while (oneSwitched);

			// progressing in time (step is 'done')
			timeStep++;

			// switch time to next event
			e = eventqueue.getNextEvent(timeStep, false, null, null, null);
			if (e != null)
				timeStep = e.getTimeStep();

		} while (e != null);

		timeStep--; // correction after last step
		printPostSimStats(timeStep);
		return timeStep;
	}

	/**
	 * After the Simulation loop is done, this method prints the utilization
	 * statistic of every Server as well as the average utilization of every Server
	 * class that initialized two or more Objects.
	 *
	 * @param timeStep TimeStep at the end of the simulation
	 */
	private void printPostSimStats(long timeStep)
	{
		logger.log(Level.INFO, "----------------------------------");
		double utilSumPerSimClass = 0.0;
		int sumObjectsSimClass = 0;
		Class<? extends SimulationObject> simulationObjectClass = null;
		final SimulationObjects simulationObjects = SimulationObjects.getInstance();

		// only works all simulation objects of one class are adjacent stored in
		// simulationobjects
		for (SimulationObject simulationObject : simulationObjects)
		{
			double utilSimObject = simulationObject.getTimeUtilized().doubleValue() / timeStep * 100;

			if (simulationObjectClass == simulationObject.getClass())
			{
				utilSumPerSimClass += utilSimObject;
				sumObjectsSimClass++;
			}
			else
			{
				if (simulationObjectClass != null && sumObjectsSimClass > 1)
					logger.log(Level.INFO, String.format("Utilization Class %s = %.2f %%",
							simulationObjectClass.getName(), utilSumPerSimClass / sumObjectsSimClass));

				simulationObjectClass = simulationObject.getClass();
				utilSumPerSimClass = utilSimObject;
				sumObjectsSimClass = 1;
			}

			logger.log(Level.INFO, String.format("Utilization %s = %.2f %%", simulationObject, utilSimObject));
		}

		if (sumObjectsSimClass > 1)
			logger.log(Level.INFO, String.format("Utilization Class %s = %.2f %%", simulationObjectClass.getName(),
					utilSumPerSimClass / sumObjectsSimClass));
		logger.log(Level.INFO, "----------------------------------");
	}
}
