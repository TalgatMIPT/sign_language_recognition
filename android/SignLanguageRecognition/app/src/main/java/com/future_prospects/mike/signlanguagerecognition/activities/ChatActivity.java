package com.future_prospects.mike.signlanguagerecognition.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.future_prospects.mike.signlanguagerecognition.R;
import com.future_prospects.mike.signlanguagerecognition.presentors.MessagePresentor;
import com.future_prospects.mike.signlanguagerecognition.server.SendMessageTask;
import com.github.bassaer.chatmessageview.models.Message;
import com.github.bassaer.chatmessageview.models.User;
import com.github.bassaer.chatmessageview.views.ChatView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity implements MessagePresentor {
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
                startActivity(new Intent(ChatActivity.this, CameraActivity.class));
            }
        });

        chatView.setOnClickSendButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = chatView.getInputText();
                new SendMessageTask(ChatActivity.this, getApplicationContext()).execute("admin", "1", message);
            }
        });
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
}
