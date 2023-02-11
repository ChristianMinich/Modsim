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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;


class SimulationTest
{
	private EventQueue e;
	private SimulationObjects simObjects;
	private ArrayList<Event> toAdd;
	private Simulation sim;
	private SimulationObject simObject;
	private boolean answered;

	@BeforeEach
	void init()
	{
		sim = new Simulation()
		{
			@Override
			protected void printEveryStep(long numberOfSteps, long timeStep)
			{
			}
		};
		e = EventQueue.getInstance();
		toAdd = new ArrayList<Event>();
		simObjects = SimulationObjects.getInstance();
		simObject = Mockito.mock(SimulationObject.class);
		ReflectionTestUtils.setField(simObject, "timeUtilized", 0L);
		ReflectionTestUtils.setField(simObject, "utilStart", 0L);
		simObjects.add(simObject);
	}

	/**
	 * Clears EventQueue and SimulationObjects after each test.
	 */
	@AfterEach
	void clear()
	{
		e.clear();
		simObjects.clear();
	}

	/**
	 * Checks if simulate returns 0 when the EventQueue is empty.
	 */
	@Test
	void noEventInQueue()
	{
		long actual = sim.simulate();
		long expected = 0L;

		assertThat(actual).isEqualTo(expected);
	}

	/**
	 * Checks if the simulate method returns the correct timeStep when Events got
	 * simulated.
	 */
	@Test
	void eventGotSimulated()
	{
		toAdd.add(new Event(1L, null, null, null, null));
		e.addAll(toAdd);
		when(simObject.simulate(1)).thenAnswer(invocation ->
		{
			if (!answered)
			{
				e.clear();
				answered = true;
				return true;
			}
			else
			{
				return false;
			}


		});


		long expected = 1;
		long actual = sim.simulate();

		assertThat(actual).isEqualTo(expected);
	}

	@Test
	@Disabled
	@DisplayName("TBD")
	void eventInQueueUnassignable()
	{
		toAdd.add(new Event(0L, null, null, null, null));
		e.addAll(toAdd);

		assertThatThrownBy(() ->
		{
			sim.simulate();
		}).isInstanceOf(SimulationException.class).hasMessageContaining("didn't get consumed");
	}
}
