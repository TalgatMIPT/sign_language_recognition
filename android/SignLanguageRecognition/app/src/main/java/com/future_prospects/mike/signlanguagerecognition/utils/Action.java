// Copyright (С) ABBYY (BIT Software), 1993 - 2012. All rights reserved.
// Author: Michael Rozumyanskiy

package com.future_prospects.mike.signlanguagerecognition.utils;

/**
 * Interface of functor that takes no arguments and returns no results.
 * 
 * @param <T>
 *            Type of argument.
 */
public interface Action<T> {
	void perform(T argument);
}
