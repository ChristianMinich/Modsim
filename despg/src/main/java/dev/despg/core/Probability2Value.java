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

public final class Probability2Value<T> implements Comparable<Probability2Value<T>>
{
	private Double probabilityUpperLimit;
	private T value;

	/**
	 * Assigns the upper limit of a probability to a value.
	 *
	 * @param to    Upper limit of probability
	 * @param value Value of that probability
	 */
	public Probability2Value(Double to, T value)
	{
		this.probabilityUpperLimit = to;
		this.value = value;
	}

	@Override
	public int compareTo(Probability2Value<T> o)
	{
		return probabilityUpperLimit.compareTo(o.probabilityUpperLimit);
	}

	public Double getProbabilityUpperLimit()
	{
		return probabilityUpperLimit;
	}

	public T getValue()
	{
		return value;
	}
}
