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

/**
 * toString should be implemented if something meaningful should be printed
 * (after simulation (step)).
 * Class (attributes / methods) should not be final because mocking can't mock them.
 */
public abstract class SimulationObject
{
	private Long timeUtilized = 0L;
	private Long utilStart;

	public abstract boolean simulate(long timeStep);

	/**
	 * set timeUtilized.
	 * @param timeUtilized
	 */
	public void setTimeUtilized(Long timeUtilized)
	{
		this.timeUtilized = timeUtilized;
	}

	/**
	 * set utilStart.
	 * @param utilStart
	 */
	public void setUtilStart(Long utilStart)
	{
		this.utilStart = utilStart;
	}

	/**
	 * get utilStart.
	 * @return utilStart
	 */
	public Long getUtilStart()
	{
		return utilStart;
	}

	/**
	 * get timeUtilized.
	 * @return timeUtilized
	 */
	public Long getTimeUtilized()
	{
		return timeUtilized;
	}

	/**
	 * start utilization.
	 * @param timeStep
	 */
	public void utilStart(long timeStep)
	{
		utilStart = timeStep;
	}

	/**
	 * stop utilization.
	 * @param timeStep
	 */
	public void utilStop(long timeStep)
	{
		timeUtilized += timeStep - utilStart;
		utilStart = null;
	}

	/**
	 * increase utilization.
	 * @param timeUtilizedDelta
	 * @return increased utilization
	 */
	public long addUtilization(long timeUtilizedDelta)
	{
		timeUtilized += timeUtilizedDelta;
		return timeUtilizedDelta;
	}
}
