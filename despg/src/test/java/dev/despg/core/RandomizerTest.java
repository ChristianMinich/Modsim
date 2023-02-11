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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;
import static org.assertj.core.api.Assertions.*;


class RandomizerTest
{

	@Mock
	private Randomizer r;

	@BeforeEach
	void init()
	{
		r = new Randomizer();

	}

	/**
	 * Checks if addProb2Int adds new Probability2Value objects correctly. Creates
	 * an expected Randomizer that sets its private class member "prob2Int" through
	 * reflection.
	 */
	@Test
	void addProb2IntShouldAddProbability()
	{
		Randomizer rExpected = new Randomizer();
		ArrayList<Probability2Value<Integer>> p = new ArrayList<Probability2Value<Integer>>();
		p.add(new Probability2Value<Integer>(0.5, 2));

		ReflectionTestUtils.setField(rExpected, "prob2Int", p);
		r.addProbInt(0.5, 2);
		Probability2Value<Integer> actual = r.getProb2Int().get(0);
		Probability2Value<Integer> expected = r.getProb2Int().get(0);
		assertThat(actual.getValue()).isEqualTo(expected.getValue());
		assertThat(actual.getProbabilityUpperLimit()).isEqualTo(expected.getProbabilityUpperLimit());
	}

	/**
	 * Checks if addProbInt throws the correct SimulationException when the
	 * probability is out of the defined bounds.
	 */
	@Test
	void addProbIntShouldThrowBecauseOutOfBounds()
	{
		assertThatThrownBy(() ->
		{
			r.addProbInt(Randomizer.getMinProbability() - 0.1, 5);
		}).isInstanceOf(SimulationException.class).hasMessageContaining("is out of bounds");
		assertThatThrownBy(() ->
		{
			r.addProbInt(Randomizer.getMaxProbability() + 0.1, 5);
		}).isInstanceOf(SimulationException.class).hasMessageContaining("is out of bounds");

	}

	/**
	 * Checks if addProbInt throws the correct SimulationException if the
	 * parameterized probability already exists.
	 */
	@Test
	void addProbIntShouldThrowBecauseDuplicate()
	{
		assertThatThrownBy(() ->
		{
			r.addProbInt(1, 5);
			r.addProbInt(1, 7);
		}).isInstanceOf(SimulationException.class).hasMessageContaining("already exists");
	}

	/**
	 * Checks if nextInt picks the correct value.
	 */
	@Test
	void nextIntPickedCorrect()
	{
		int expected = 2;
		ArrayList<Probability2Value<Integer>> p = new ArrayList<Probability2Value<Integer>>();
		p.add(new Probability2Value<Integer>(1.0, 2));

		ReflectionTestUtils.setField(r, "prob2Int", p);

		assertThat(r.nextInt()).isEqualTo(expected);

	}

	/**
	 * Checks if nextInt throws the correct SimulationException when the probability
	 * is out of bounds.
	 */
	@Test
	void nextIntShouldThrowBecauseOutOfBounds()
	{
		ArrayList<Probability2Value<Integer>> p = new ArrayList<Probability2Value<Integer>>();
		p.add(new Probability2Value<Integer>(Randomizer.getMaxProbability() + 0.1, 2));
		ReflectionTestUtils.setField(r, "prob2Int", p);

		assertThatThrownBy(() ->
		{
			r.nextInt();
		}).isInstanceOf(SimulationException.class).hasMessageContaining("is out of bounds");


	}

	/**
	 * Checks if nextInt throws the correct SimulationException when the randomized
	 * probability couldn't find a matching upper limit in the Randomizer. Every
	 * Randomizer should have a value assigned to the probability of MAX_PROBABILITY
	 */
	@Test
	void nextIntShouldThrowBecauseProbabilityNotCovered()
	{
		ArrayList<Probability2Value<Integer>> p = new ArrayList<Probability2Value<Integer>>();
		p.add(new Probability2Value<Integer>(0.0, 2));
		ReflectionTestUtils.setField(r, "prob2Int", p);

		assertThatThrownBy(() ->
		{
			r.nextInt();
		}).isInstanceOf(SimulationException.class).hasMessageContaining("Probability not covered");


	}

	/**
	 * Checks if nextInt throws the correct SimulationException when the probability
	 * list is empty.
	 */
	@Test
	void nextIntShouldThrowBecauseEmptyArrayList()
	{
		ArrayList<Probability2Value<Integer>> p = new ArrayList<Probability2Value<Integer>>();
		ReflectionTestUtils.setField(r, "prob2Int", p);

		assertThatThrownBy(() ->
		{
			r.nextInt();
		}).isInstanceOf(SimulationException.class).hasMessageContaining("No probabilities");
	}
}
