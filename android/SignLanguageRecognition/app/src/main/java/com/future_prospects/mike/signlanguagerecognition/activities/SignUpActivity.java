package com.future_prospects.mike.signlanguagerecognition.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.future_prospects.mike.signlanguagerecognition.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity {
    @BindView(R.id.sign_in1)
    Button loginButton;
    @BindView(R.id.sign_up1)
    Button regButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.sign_in1)
    public void signInClick(){
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
    }

    @OnClick(R.id.sign_up1)
    public void SignUpClick(){
        /** sign up process */
    }
}
