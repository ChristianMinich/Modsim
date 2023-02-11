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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.assertj.core.api.Assertions.*;


class RandomizerTestExponentialDistribution
{

	@Mock
	private Randomizer r;

	@BeforeEach
	void init()
	{
		r = new Randomizer();
	}

	@Test
	void testMean()
	{
		int numberOfSamples = 100;
		double sumSamples = 0.0;
		double lambda = 5;									// events per standard time unit e.g., one day
		double meanTimeBetweenEvents = 1 / lambda;

		for (int i = 0; i < numberOfSamples; i++)
			sumSamples += r.getExponential(lambda); 	// fraction of time unit

		// System.out.println("Mean: " + Time.stepsToString(Time.convertStandardTimeUnitToSteps(meanTimeBetweenEvents, 24 * 60)));
		// System.out.println("Computed Mean: " + Time.stepsToString(Time.convertStandardTimeUnitToSteps(sumSamples / numberOfSamples, 24 * 60)));

		assertThat(sumSamples / numberOfSamples).isCloseTo(meanTimeBetweenEvents, within(0.1));
	}
}
