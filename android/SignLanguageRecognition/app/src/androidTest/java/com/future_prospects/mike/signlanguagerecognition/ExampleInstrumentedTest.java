package com.future_prospects.mike.signlanguagerecognition;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.abbyy.mobile.imaging.MICallback;
import com.abbyy.mobile.imaging.MIExporter;
import com.abbyy.mobile.imaging.MIImage;
import com.abbyy.mobile.imaging.MIListener;
import com.abbyy.mobile.imaging.defects.MIBlurInfo;
import com.abbyy.mobile.imaging.errors.MIGenericException;
import com.abbyy.mobile.imaging.operations.FineDetectBlur;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static final String TAG = "Test";

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.future_prospects.mike.signlanguagerecognition", appContext.getPackageName());
    }

    @Test
    public void checkPhoto() throws IOException, MIGenericException {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.future_prospects.mike.signlanguagerecognition", appContext.getPackageName());

        AssetManager assetManager = appContext.getAssets();
        InputStream io = assetManager.open("testJPG.jpg");
        MIImage image = MIExporter.importJPEG(io);

        FineDetectBlur blurDetect = new FineDetectBlur(new MIListener<MIBlurInfo>() {
            @Override
            public void onProgressFinished(MIBlurInfo miBlurInfo) {
                Log.d(TAG, "blur detect result: isDetected: "+ miBlurInfo.isDetected);
            }
        }, false, false);

        blurDetect.processImageWithResult(image, new MICallback() {
            @Override
            public int onProgressUpdated(int i) {
                Log.d(TAG, "on progress...");
                return i;
            }
        });
    }
}
