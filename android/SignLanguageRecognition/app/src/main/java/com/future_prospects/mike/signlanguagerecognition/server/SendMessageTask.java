package com.future_prospects.mike.signlanguagerecognition.server;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.future_prospects.mike.signlanguagerecognition.presentors.ImagePresentor;
import com.future_prospects.mike.signlanguagerecognition.presentors.MessagePresentor;

import java.io.IOException;
import java.util.Properties;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by mike on 10/8/17.
 */

public class SendMessageTask extends AsyncTask<String, Void, String> {
    private static final String TAG = SendMessageTask.class.toString();

    private MessagePresentor messagePresentor;

    private String serverUrl;

    public SendMessageTask(MessagePresentor messagePresentor, Context context) {
        this.messagePresentor = messagePresentor;
        AssetManager assetManager = context.getAssets();
        Properties properties = new Properties();
        try {
            properties.load(assetManager.open("ServerConnection.properties"));
        } catch (IOException e) {
            Log.e(TAG, "Could not load server connection properties", e);
        }

        serverUrl = properties.getProperty("msgPoint");
    }

    @Override
    protected String doInBackground(String... params) {
        String sender = params[0];
        String receiver = params[1];
        String message = params[2];
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                createJson(message, receiver, sender));
        Request request = new Request.Builder().url(serverUrl)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.w(TAG, "Response is successful");
            }

            if (response.body() != null) {
                return response.body().string();
            } else {
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
        messagePresentor.publicResult(s);
    }

    private String createJson(String message, String receiver, String sender) {
        return "{\"client_s\": \""+ sender  +"\"," +
                "\"client_r\": \""+ receiver + "\"," +
                "\"message\": \""+ message + "\"}";
    }
}
