package launamgoc.halfoflove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

import launamgoc.halfoflove.R;
import launamgoc.halfoflove.helper.FirebaseHelper;

public class LoginActivity extends AppCompatActivity implements FirebaseHelper.FirebaseLoginHelperDelegate {

    EditText edtEmail;
    EditText edtPassword;
    Button btnLogin;
    Button btnSignInFb;
    Button btnSignInGG;

    CallbackManager callbackManager;

    Handler hd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        hd = new Handler(getMainLooper());

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logOut();

        // Callback registration
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d("Facebook", "facebook:onSuccess:" + loginResult);
                FirebaseHelper.loginWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

        MapController();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                FirebaseHelper.loginWithUser(email,password);
            }
        });
        FirebaseHelper.loginDelegate = this;
        btnSignInFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginWithFb();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseHelper.mAuth.addAuthStateListener(FirebaseHelper.mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (FirebaseHelper.mAuthListener != null) {
            FirebaseHelper.mAuth.removeAuthStateListener(FirebaseHelper.mAuthListener);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void MapController()
    {
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtPassword =(EditText)findViewById(R.id.edtPassword);
        btnLogin =  (Button)findViewById(R.id.btnLogin);
        btnSignInFb = (Button)findViewById(R.id.btnSignInFb);
        btnSignInGG = (Button)findViewById(R.id.btnSignInGG);
    }

    public void loginWithFb(){
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
    }

    @Override
    public void onLoginSuccess() {
        hd.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, "Successfuly", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onLoginFailed() {
        hd.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onLogoutSuccess() {

    }

    @Override
    public void onLogoutFailed() {

    }


}
