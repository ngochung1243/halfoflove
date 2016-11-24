package launamgoc.halfoflove.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDex;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;

import launamgoc.halfoflove.R;

public class SplashScreenActivity extends Activity {

    LoginButton loginButton;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        MultiDex.install(this);

        setContentView(R.layout.activity_splashscreen);

        Handler hd = new Handler(getMainLooper());
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
            }
        }, 3000);
    }
}
