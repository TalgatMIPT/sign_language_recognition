// Copyright (С) ABBYY (BIT Software), 1993 - 2012. All rights reserved.
// Author: Michael Rozumyanskiy

/*
 * Copyright (C) 2007 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.future_prospects.mike.signlanguagerecognition.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * The base class that provides can register or unregister an observer any type. Also, this class allows you
 * to get a list of registered observers.
 * 
 * The class is thread safe.
 * 
 * @param T
 *            Type of observer.
 */
public abstract class Observable<T> {
	/** Collection of registered observers. */
	private final Collection<T> _observers = new CopyOnWriteArraySet<T>();

	/**
	 * Add the observer to the collection of registered observers.
	 * 
	 * @param observer
	 *            Registered observer.
	 * @throws IllegalArgumentException
	 *             Passed object is {@code null}.
	 * @throws IllegalStateException
	 *             Observer is already registered"
	 */
	public void registerObserver( final T observer ) {
		if( observer == null ) {
			throw new IllegalArgumentException( "The observer is null." );
		}

		if( !_observers.add( observer ) ) {
			throw new IllegalStateException( "Observer " + observer + " is already registered" );
		}
	}

	/**
	 * Remove the observer from the collection of registered observers.
	 * 
	 * @param observer
	 *            Deleted observer.
	 * @throws IllegalArgumentException
	 *             Passed object is {@code null}.
	 * @throws IllegalStateException
	 *             Observer was not registered
	 */
	public void unregisterObserver( final T observer ) {
		if( observer == null ) {
			throw new IllegalArgumentException( "The observer is null." );
		}

		if( !_observers.remove( observer ) ) {
			throw new IllegalStateException( "Observer " + observer + " was not registered" );
		}
	}

	/**
	 * Clean the collection of registered observers.
	 */
	public void unregisterAll() {
		_observers.clear();
	}

	/**
	 * @return Unmodifiable list of registered observers.
	 */
	public Collection<T> getObservers() {
		return Collections.unmodifiableCollection( _observers );
	}
}
