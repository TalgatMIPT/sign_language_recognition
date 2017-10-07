package com.future_prospects.mike.signlanguagerecognition.server;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;

import com.future_prospects.mike.signlanguagerecognition.model.User;
import com.future_prospects.mike.signlanguagerecognition.presentors.AuthorizePresentor;

import java.io.IOException;
import java.util.Properties;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AuthorizeUserAsyncTask extends AsyncTask<String, Void, String> {
    private static final String TAG = AuthorizeUserAsyncTask.class.toString();

    private AuthorizePresentor presentor;

    private String serverUrl;

    private String login;

    private String password;

    public AuthorizeUserAsyncTask(AuthorizePresentor presentor, Context context) {
        this.presentor = presentor;
        AssetManager assetManager = context.getAssets();
        Properties properties = new Properties();
        try {
            properties.load(assetManager.open("ServerConnection.properties"));
        } catch (IOException e) {
            Log.e(TAG, "Could not load server connection properties", e);
        }

        serverUrl = properties.getProperty("authPoint");
    }

    public AuthorizeUserAsyncTask(AuthorizePresentor presentor, String url) {
        this.presentor = presentor;
        serverUrl = url;
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "do in bg...");
        login = strings[0];
        password = strings[1];
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), createJson(login, password));
        Request request = new Request.Builder().url(serverUrl)
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                Log.w(TAG, "Response is not successful");
            }

            if (response.body() != null) {
                Log.d(TAG, response.message());
                return response.message();
            } else {
                Log.w(TAG, "Response body is null");
            }
        } catch (IOException e) {
            Log.e(TAG, "Could not execute server request", e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(String answer) {
        super.onPostExecute(answer);
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        user.setRegistered(answer.equals("OK"));
        presentor.publicResult(user);
    }

    private String createJson(String login, String password) {
        String str = "{\"username\": \""+login+"\", \"password\": \""+password+"\"}";
        Log.d(TAG, str);
        return str;
    }
}
