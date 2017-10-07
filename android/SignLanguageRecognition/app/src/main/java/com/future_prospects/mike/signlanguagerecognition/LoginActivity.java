package com.future_prospects.mike.signlanguagerecognition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.abbyy.mobile.imaging.MILicenser;
import com.future_prospects.mike.signlanguagerecognition.model.User;
import com.future_prospects.mike.signlanguagerecognition.presentors.AuthorizePresentor;
import com.future_prospects.mike.signlanguagerecognition.server.AuthorizeUserAsyncTask;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements AuthorizePresentor {
    @BindView(R.id.sign_in)
    Button loginButton;
    @BindView(R.id.sign_up)
    Button regButton;
    @BindView(R.id.username)
    EditText usernameEdit;
    @BindView(R.id.password)
    EditText passwordEdit;

    private static final String TAG = "MIApplication";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        System.loadLibrary( "MobileImagingEngine" );
        try {
            Log.v( TAG, "MILicenser.getVersionInfo(): " + MILicenser.getVersionInfo() );
            final InputStream licenseStream = getAssets().open( "MIRT-0100-0006-3268-0632-9491.ABBYY.LICENSE" );
            MILicenser.setLicense( licenseStream, "Android_ID" );
            Log.v( TAG, "MILicenser.setLicense() succeeded. " + MILicenser.getLicenseInfo() );
        } catch( final Exception exception ) {
            Log.e( TAG, "MILicenser.setLicense() failed", exception );
        }
        MIContext.createInstance();
    }

    @OnClick(R.id.sign_in)
    public void signInClick(){
        /** sign in process */
        new AuthorizeUserAsyncTask(this, getApplicationContext()).
                execute(usernameEdit.getText().toString(), passwordEdit.getText().toString());
    }

    @OnClick(R.id.sign_up)
    public void SignUpClick(){
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }

    @Override
    public void publicResult(User user) {
        startActivity(new Intent(LoginActivity.this, ChatActivity.class));
    }
}
