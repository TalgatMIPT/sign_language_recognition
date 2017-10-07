package com.future_prospects.mike.signlanguagerecognition.utils;

import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;

public class ClientProxy implements MediaScannerConnectionClient {
	private final String _path;
	private final String _mimeType;
	private MediaScannerConnection _connection;

	public ClientProxy(final String filePath, final String fileMimeType ) {
		_path = filePath;
		_mimeType = fileMimeType;
	}

	@Override
	public void onMediaScannerConnected() {
		_connection.scanFile( _path, _mimeType );
	}

	@Override
	public void onScanCompleted(final String filePath, final Uri uri ) {
		_connection.disconnect();
	}

	public void startScan( final MediaScannerConnection mediaScannerConnection ) {
		_connection = mediaScannerConnection;
		_connection.connect();
	}
}
