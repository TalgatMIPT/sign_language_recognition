package com.future_prospects.mike.signlanguagerecognition;

// Copyright (c) ABBYY (BIT Software), 1993 - 2012. All rights reserved.
// Author: Yuri Kopachevskiy

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.abbyy.mobile.imaging.FineOperation;
import com.abbyy.mobile.imaging.MICallback;
import com.abbyy.mobile.imaging.MIExporter;
import com.abbyy.mobile.imaging.MIImage;
import com.abbyy.mobile.imaging.MIProcessor;
import com.abbyy.mobile.imaging.errors.MIExecutionResult;
import com.abbyy.mobile.imaging.errors.MIGenericException;
import com.future_prospects.mike.signlanguagerecognition.utils.CloseableUtils;
import com.future_prospects.mike.signlanguagerecognition.utils.ObservableData;
import com.future_prospects.mike.signlanguagerecognition.utils.Observer;
import com.future_prospects.mike.signlanguagerecognition.utils.UriHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The context for the retention of large / important objects between recreating an Activity during the
 * orientation changes.
 *
 * Singleton.
 */
public class MIContext {
    /** Logging tag */
    private static final String TAG = "MIContext";
    /** Class instance */
    private static MIContext _instance;

    /** The image displayed on the ImageView widget */
    private Bitmap _bitmap;

    /** Processor that performs operations on an image */
    private MIProcessor _miProcessor;
    /** The original image, which is sent to the processing */
    private MIImage _sourceImage;
    /** The result image of the processing */
    private MIImage _resultImage;

    /** Output stream for logging purposes */
    private static final String DATA_FOLDER_NAME = "MobileImaging";
    private static final String LOG_FILE_NAME = "Results.txt";
    private FileOutputStream _logStream;

    private ObservableData _observable;
    private Thread _thread;

    private final AtomicBoolean _needStop = new AtomicBoolean( false );

    private MIContext() {
    }

    public static MIContext createInstance() {
        if( _instance == null ) {
            _instance = new MIContext();
        }
        return null;
    }

    public static MIContext getInstance() {
        if( _instance == null ) {
            throw new NullPointerException( "MIContext instance is null" );
        }
        return _instance;
    }

    /**
     * Clear all MIContext-objects
     */
    public void clearAll() {
        clearBitmap();
        if( _sourceImage != null ) {
            _sourceImage.destroy();
            _sourceImage = null;
        }
        if( _resultImage != null ) {
            _resultImage.destroy();
            _resultImage = null;
        }
        _needStop.set( false );
        _observable = null;
    }

    /**
     * Load source image to MIImage by Uri
     */
    public void loadSourceImage(final Uri imageUri, final Context context ) {
        if( _sourceImage != null ) {
            _sourceImage.destroy();
            _sourceImage = null;
        }
        if( _resultImage != null ) {
            _resultImage.destroy();
            _resultImage = null;
        }

        InputStream inputStream = null;
        try {
            inputStream = UriHelper.openInputStream( context, imageUri );
            _sourceImage = MIExporter.importJPEG( inputStream );
            // _resultImage = new MIImage( _sourceImage );
        } catch( final FileNotFoundException exception ) {
            Log.e( TAG, "loadSourceImage() failed", exception );
        } catch( final MIGenericException exception ) {
            Log.e( TAG, "loadSourceImage() failed", exception );
        } catch( final IOException exception ) {
            Log.e( TAG, "loadSourceImage() failed", exception );
        } finally {
            CloseableUtils.close( inputStream );
        }
    }

    /**
     * Convert result image to source image
     */
    public void updateSourceImage() {
        if( _resultImage != null ) {
            if( _sourceImage != null ) {
                _sourceImage.destroy();
                _sourceImage = null;
            }
            _sourceImage = new MIImage( _resultImage );
        }
    }

    public void run( final FineOperation... operations ) {
        _observable.notifyProcessingUpdated( _miProcessor, 0, 0 );
        if( _resultImage != null ) {
            _resultImage.destroy();
            _resultImage = null;
        }
        _resultImage = new MIImage( _sourceImage );
        _thread = new Thread( new Runnable() {
            @Override
            public void run() {
                _needStop.set( false );
                // MI-engine processes images in place. Copy image to keep source
                _miProcessor = new MIProcessor( operations );
                try {
                    _miProcessor.processImage( _resultImage, createMICallback() );
                } catch( final MIGenericException exception ) {
                    if( exception.code != 1 ) { // code=1 is ER_Cancelled
                        Log.w( TAG, "run failed", exception );
                        _observable
                                .notifyProcessingFinished( _miProcessor, MIExecutionResult.values()[exception.code] );
                    }
                }
                if( !_needStop.get() ) {
                    _observable.notifyProcessingFinished( _miProcessor, MIExecutionResult.ER_OK );
                } else {
                    _observable.notifyProcessingFinished( _miProcessor, MIExecutionResult.ER_Cancelled );
                }
            }
        } );
        _thread.start();
    }

    private MICallback createMICallback() {
        return new MICallback() {
            @Override
            public int onProgressUpdated( final int arg0 ) {
                _observable.notifyProcessingUpdated( _miProcessor, 0, arg0 );
                if( _needStop.get() ) {
                    Log.d( TAG, "stop" );
                    return 1;
                } else {
                    return 0;
                }
            }
        };
    }

    /**
     * Cancel a running operation
     */
    public void cancel() {
        _needStop.set( true );
        _observable.clearResult();
    }

    public void setBitmap( final Bitmap bitmap ) {
        _bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return _bitmap;
    }

    /**
     * Destroy Bitmap object, release the memory
     */
    public void clearBitmap() {
        // need to do this "manually", to prevent OOM
        if( _bitmap != null ) {
            _bitmap.recycle();
            _bitmap = null;
            System.gc();
        }
    }

    public MIImage getResult() {
        return _resultImage;
    }

    public MIImage getSource() {
        return _sourceImage;
    }

    public void setObservableData( final ObservableData observableData ) {
        if( _observable == null ) {
            _observable = observableData;
        }
    }

    /**
     * Register the observer of MI-processor events
     */
    public void registerObserver( final Observer observer ) {
        _observable.registerObserver( observer );
    }

    /**
     * Unregister the observer of MI-processor events
     */
    public void unregisterObserver( final Observer observer ) {
        _observable.unregisterObserver( observer );
    }

    public synchronized FileOutputStream getLogStream() {
        if( _logStream == null ) {
            try {
                final File dataFolder =
                        new File( Environment.getExternalStorageDirectory(), DATA_FOLDER_NAME );
                dataFolder.mkdir();
                final File logFile = new File( dataFolder, LOG_FILE_NAME );
                _logStream = new FileOutputStream( logFile, true );
            } catch( final FileNotFoundException exception ) {
                Log.e( TAG, "MIContext() failed", exception );
            }
        }
        return _logStream;
    }

    public void appendToLog(final Date timestamp, final String operationName, final String resultString ) {
        try {
            final SimpleDateFormat dateFormat = new SimpleDateFormat( "HH:mm:ss" );
            final FileOutputStream logStream = getLogStream();
            logStream.write( ( "[" + dateFormat.format( timestamp ) + ", "
                    + operationName + ": " + resultString + "]\r\n" ).getBytes() );
            logStream.flush();
        } catch( final IOException exception ) {
            Log.e( TAG, "appendToLog() failed", exception );
        }
    }
}

