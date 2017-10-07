package com.future_prospects.mike.signlanguagerecognition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.abbyy.mobile.imaging.MILicenser;

import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.sign_in)
    Button loginButton;
    @BindView(R.id.sign_up)
    Button regButton;

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
    }

    @OnClick(R.id.sign_up)
    public void SignUpClick(){
        startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
    }
}
