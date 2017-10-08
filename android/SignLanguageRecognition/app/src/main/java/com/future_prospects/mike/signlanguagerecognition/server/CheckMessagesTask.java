package com.future_prospects.mike.signlanguagerecognition.server;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import com.future_prospects.mike.signlanguagerecognition.presentors.ChatPresentor;

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

public class CheckMessagesTask extends AsyncTask<String, Void, String> {
    private static final String TAG = CheckMessagesTask.class.toString();

    private ChatPresentor chatPresentor;

    private String serverUrl;

    private Context context;

    public CheckMessagesTask(ChatPresentor chatPresentor, Context context){
        this.context = context;
        this.chatPresentor = chatPresentor;
        AssetManager assetManager = context.getAssets();
        Properties properties = new Properties();
        try {
            properties.load(assetManager.open("ServerConnection.properties"));
        } catch (IOException e) {
            Log.e(TAG, "Could not load server connection properties", e);
        }

        serverUrl = properties.getProperty("cgeckMsgPoint");
    }

    @Override
    protected String doInBackground(String... params) {
        String receiver = params[0];
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), "{\"client_r\": \"" + receiver + "\"}");
        Request request = new Request.Builder().url(serverUrl)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.w(TAG, "Response is successful");
            }

            if (response.body() != null && !response.message().equals("INTERNAL SERVER ERROR")) {
                return response.body().string();
            } else {
                Log.w(TAG, "Response body is null");
                return "";
            }
        } catch (IOException e) {
            Log.e(TAG, "Could not execute server request", e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s){
        chatPresentor.msgCame(s);
        try {
            Thread.sleep(500);
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
        new CheckMessagesTask(chatPresentor, context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "admin");
    }
}
