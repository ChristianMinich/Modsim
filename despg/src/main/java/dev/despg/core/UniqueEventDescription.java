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
 * Interface for event description. Event descriptions serve as filters in
 * {@link EventQueue#getNextEvent(int, boolean, UniqueEventDescription, Class, SimulationObject) }
 */
public interface UniqueEventDescription
{
	String get();
}
