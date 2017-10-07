package com.future_prospects.mike.signlanguagerecognition.server;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import com.future_prospects.mike.signlanguagerecognition.model.ProcessImage;
import com.future_prospects.mike.signlanguagerecognition.presentors.ImagePresentor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Properties;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ImageSenderAsyncTask extends AsyncTask<ProcessImage, Void, String> {
    private static final String TAG = ImageSenderAsyncTask.class.toString();

    private ImagePresentor imagePresentor;

    private String serverUrl;

    public ImageSenderAsyncTask(ImagePresentor imagePresentor, Context context) {
        this.imagePresentor = imagePresentor;
        AssetManager assetManager = context.getAssets();
        Properties properties = new Properties();
        try {
            properties.load(assetManager.open("ServerConnection.properties"));
        } catch (IOException e) {
            Log.e(TAG, "Could not load server connection properties", e);
        }

        serverUrl = properties.getProperty("host");
    }

    @Override
    protected String doInBackground(ProcessImage... processImages) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), processImages[0].getData());
        Request request = new Request.Builder().url(serverUrl)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.w(TAG, "Response is not successful");
            }

            if (response.body() != null) try {
                JSONObject jsonResponse = new JSONObject(response.body().string());
                return jsonResponse.getString("character");
            } catch (JSONException e) {
                Log.w(TAG, "Could not parse json");
            }
            else {
                Log.w(TAG, "Response body is null");
            }
        } catch (IOException e) {
            Log.e(TAG, "Could not execute server request", e);
        }

        return null;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        imagePresentor.publicResult(s);
    }
}
