// Copyright (c) ABBYY (BIT Software), 1993 - 2012. All rights reserved.
// Author: Yuri Kopachevskiy

package com.future_prospects.mike.signlanguagerecognition.utils;

import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

/**
 * Helper class to work with {@link Closeable}.
 */
public class CloseableUtils {
	/** Logging tag. */
	private static final String TAG = "ClosableUtils";

	/**
	 * Safely close the object {@link Closeable}.
	 * 
	 * @param closeable
	 *            Closeable object. {@code null} allowed.
	 */
	public static void close( final Closeable closeable ) {
		try {
			if( closeable != null ) {
				closeable.close();
			}
		} catch( final IOException exception ) {
			Log.w( TAG, "Failed to close object", exception );
		}
	}

	private CloseableUtils() {
		// The class is not designed for instantiation.
	}
}
