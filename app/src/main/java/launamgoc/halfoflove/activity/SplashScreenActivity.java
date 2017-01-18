package launamgoc.halfoflove.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.api.model.GetTokenResponse;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import launamgoc.halfoflove.R;
import launamgoc.halfoflove.helper.FirebaseHelper;
import launamgoc.halfoflove.model.AppEvent;
import launamgoc.halfoflove.model.Follow;
import launamgoc.halfoflove.model.MyBundle;
import launamgoc.halfoflove.model.User;
import launamgoc.halfoflove.model.UserBusiness;

import static launamgoc.halfoflove.R.id.btnLogin;
import static launamgoc.halfoflove.R.id.edtEmail;
import static launamgoc.halfoflove.R.id.edtPassword;
import static launamgoc.halfoflove.activity.LoginActivity.mGoogleApiClient;

public class SplashScreenActivity extends AppCompatActivity implements FirebaseHelper.FirebaseLoginHelperDelegate{

    public static boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        MultiDex.install(this);

        setContentView(R.layout.activity_splashscreen);

        getKeyHash();

//        TestData();

        FirebaseHelper.loginDelegate = this;

        Handler hd = new Handler(getMainLooper());
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isLogin){
                    startActivityForResult(new Intent(SplashScreenActivity.this, LoginActivity.class), 200);
                }
            }
        }, 3000);

//        getUser();
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
        finish();
    }

    public void getKeyHash(){
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("launamgoc.halfoflove", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginSuccess(final User user) {
        isLogin = true;
        Handler hd = new Handler(getMainLooper());
        hd.post(new Runnable() {
            @Override
            public void run() {
                MyBundle.mUserBusiness.mUser = user;
//                MyBundle.mUserBusiness.load(new UserBusiness.UserBusinessListener() {
//                    @Override
//                    public void onComplete(UserBusiness.UserBusinessResult result) {
//                        if (result == UserBusiness.UserBusinessResult.SUCCESS){
//                            MyBundle.pUserBusiness.mUser = MyBundle.mUserBusiness.pUser;
//                            startMainActivity();
//                        }
//                    }
//                });

                MyBundle.mUserBusiness.getToken();
                startMainActivity();
//                testChatMessage();
//                testChatList();
            }
        });
    }



    @Override
    public void onLoginFailed(Exception ex) {
    }

    @Override
    public void onLogoutSuccess() {

    }

    @Override
    public void onLogoutFailed(Exception ex) {

    }
}
