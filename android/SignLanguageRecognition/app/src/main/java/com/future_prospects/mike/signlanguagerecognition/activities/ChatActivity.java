package com.future_prospects.mike.signlanguagerecognition.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.future_prospects.mike.signlanguagerecognition.R;
import com.future_prospects.mike.signlanguagerecognition.presentors.ChatPresentor;
import com.future_prospects.mike.signlanguagerecognition.presentors.MessagePresentor;
import com.future_prospects.mike.signlanguagerecognition.server.CheckMessagesTask;
import com.future_prospects.mike.signlanguagerecognition.server.SendMessageTask;
import com.github.bassaer.chatmessageview.models.Message;
import com.github.bassaer.chatmessageview.models.User;
import com.github.bassaer.chatmessageview.views.ChatView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity implements MessagePresentor, ChatPresentor {
    private final static String TAG = ChatActivity.class.toString();

    @BindView(R.id.chat_view)
    ChatView chatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        chatView.setOnClickOptionButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("foo", "bar");
                startActivityForResult(new Intent(ChatActivity.this, CameraActivity.class), 1);
            }
        });

        chatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = chatView.getInputText();
                new SendMessageTask(ChatActivity.this, getApplicationContext()).execute("admin", "1", message);
            }
        });
        new CheckMessagesTask(this, getApplicationContext()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "admin");
    }

    @Override
    public void publicResult(String s) {
        Log.d("response", "ok");
        Message msg = new Message.Builder()
                .setUser(new User(0, "admin", null))
                .setRightMessage(true)
                .setMessageText(chatView.getInputText())
                .hideIcon(true)
                .build();
        chatView.send(msg);
        chatView.setInputText("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String message = data.getExtras().getString("message");
        chatView.setInputText(message);
        Log.d(TAG, "Message is "+message);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void msgCame(String s) {
        if(!s.equals("")){
            Message msg = null;
            try {
                msg = new Message.Builder()
                        .setUser(new User(0, "1", null))
                        .setRightMessage(false)
                        .setMessageText(new JSONObject(s).getString("message"))
                        .hideIcon(true)
                        .build();
                chatView.send(msg);
                chatView.setInputText("");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("message", s);

    }
}
