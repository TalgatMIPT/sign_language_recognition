package com.future_prospects.mike.signlanguagerecognition;

import android.util.Log;

import com.future_prospects.mike.signlanguagerecognition.model.User;
import com.future_prospects.mike.signlanguagerecognition.presentors.AuthorizePresentor;
import com.future_prospects.mike.signlanguagerecognition.server.AuthorizeUserAsyncTask;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    AuthorizePresentor authorizePresentor = new AuthorizePresentor() {
        @Override
        public void publicResult(User user) {
            Log.d("AuthorizeTest", "login "+user.getLogin()+" password "+user.getPassword()+" registred "+user.isRegistered());
        }
    };

    @Test
    public void getCredential() {
        AuthorizeUserAsyncTask userAsyncTask = new AuthorizeUserAsyncTask(authorizePresentor, "http://localhost:8080/");
        userAsyncTask.execute("login", "password");
    }

}