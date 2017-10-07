// Copyright (c) ABBYY (BIT Software), 1993 - 2012. All rights reserved.
// Author: Yuri Kopachevskiy

package com.future_prospects.mike.signlanguagerecognition.utils;

import android.content.Context;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;

public class UriHelper {
	public static final String ASSETS_FOLDER = "sample_images";

	private static final String ASSETS_URI_SCHEME = "assets";

	public static Uri createAssetsUri(final String filePath ) {
		return new Uri.Builder()
				.scheme( ASSETS_URI_SCHEME )
				.appendPath( filePath )
				.build();
	}

	public static InputStream openInputStream(final Context context, final Uri uri ) throws IOException {
		if( isAssetsUri( uri ) ) {
			final String path = uri.getPath().substring( 1 );
			return context.getAssets().open( path );
		} else {
			return context.getContentResolver().openInputStream( uri );
		}
	}

	private static boolean isAssetsUri( final Uri uri ) {
		return uri.getScheme().equals( ASSETS_URI_SCHEME );
	}
}
