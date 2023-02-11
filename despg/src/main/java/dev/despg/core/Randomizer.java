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

import java.util.ArrayList;
import java.util.Random;

public final class Randomizer
{
	private static final double MIN_PROBABILITY = 0.0;
	private static final double MAX_PROBABILITY = 1.0;

	public static double getMinProbability()
	{
		return MIN_PROBABILITY;
	}

	public static double getMaxProbability()
	{
		return MAX_PROBABILITY;
	}

	private static Random random = new Random();
	private ArrayList<Probability2Value<Integer>> prob2Int = new ArrayList<Probability2Value<Integer>>();

	/**
	 *
	 * @return defined probability intervals -> integer values
	 */
	public ArrayList<Probability2Value<Integer>> getProb2Int()
	{
		return prob2Int;
	}

	/**
	 * This method is used to assign Integer values to a certain probability.
	 *
	 * @param to    Upper limit for that probability to occur
	 * @param value Assigned value of that probability
	 */
	public void addProbInt(double to, int value) throws SimulationException
	{
		for (Probability2Value<Integer> prob2value : prob2Int)
		{
			if (prob2value.getProbabilityUpperLimit() == to)
				throw new SimulationException("Probability " + to + " already exists");
		}

		if (to >= MIN_PROBABILITY && to <= MAX_PROBABILITY)
			prob2Int.add(new Probability2Value<Integer>(to, value));
		else
			throw new SimulationException(
					"Probability " + to + " is out of bounds (" + MIN_PROBABILITY + "-" + MAX_PROBABILITY + ")");
	}

	/**
	 * This method iterates through an ArrayList of Probability2Value objects and
	 * compares its probabilityUpperLimit against a random Double.
	 *
	 * @return This returns the Integer value that had been assigned to the occurred
	 *         probability
	 */
	public int nextInt() throws SimulationException
	{
		if (prob2Int.isEmpty())
			throw new SimulationException("No probabilities in ArrayList");

		double r = random.nextDouble();

		for (Probability2Value<Integer> pI : prob2Int)
		{
			if (pI.getProbabilityUpperLimit() < MIN_PROBABILITY || pI.getProbabilityUpperLimit() > MAX_PROBABILITY)
				throw new SimulationException("Probability " + pI.getProbabilityUpperLimit() + " is out of bounds ("
						+ MIN_PROBABILITY + "-" + MAX_PROBABILITY + ")");
			else if (r <= pI.getProbabilityUpperLimit())
				return pI.getValue();
		}

		throw new SimulationException("Probability not covered");
	}

	/**
	 * returns a (uniform) number between min and max.
	 * @param min
	 * @param max
	 * @return
	 */
	public double getUniform(double min, double max)
	{
		 assert (max > min);
	    return random.nextDouble() * max + min;
	}

	/**
	 * returns a (triangular) number between min and max with mode.
	 * @param min
	 * @param max
	 * @param mode
	 * @return
	 */
	public double getTriangular(double min, double max, double mode)
	{
	    double f = (mode - min) / (max - min);
	    double rand = Math.random();

	    return rand < f
	   		 ? min + Math.sqrt(rand * (max - min) * (mode - min))
	   		 : max - Math.sqrt((1 - rand) * (max - min) * (max - mode));
	}

	public Double getExponential(double lambda)
	{
	    double u;
	    do
	    {
	        // Get a uniformly-distributed random double between
	        // zero (inclusive) and 1 (exclusive)
	        u = random.nextDouble();
	    } while (u == 0d); // Reject zero, u must be positive for this to work.

	    return -(Math.log(u) / (lambda));
	}

	public int getPoisson(double lambda)
	{
		double l = Math.exp(-lambda);
		double p = 1.0;
		int k = 0;

		do
		{
			k++;
			p *= random.nextDouble();
		} while (p > l);

		return k - 1;
	}

}
