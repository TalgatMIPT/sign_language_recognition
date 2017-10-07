package com.future_prospects.mike.signlanguagerecognition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.sign_in)
    Button loginButton;
    @BindView(R.id.sign_up)
    Button regButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.sign_in)
    public void signInClick(){
        /** sign in process */
    }

    @OnClick(R.id.sign_up)
    public void SignUpClick(){
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }
}
