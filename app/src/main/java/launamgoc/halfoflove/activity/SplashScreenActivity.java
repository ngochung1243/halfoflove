package launamgoc.halfoflove.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.util.Base64;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import launamgoc.halfoflove.R;
import launamgoc.halfoflove.model.AppEvent;
import launamgoc.halfoflove.model.MyBundle;
import launamgoc.halfoflove.model.User;
import launamgoc.halfoflove.model.UserBusiness;

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

        getKeyHash();

        TestData();

        Handler hd = new Handler(getMainLooper());
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            }
        }, 3000);
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

    void TestData(){
        User mUser = new User();
        mUser.fid = "db17jjCUowTHWqjbhZlO61lV5wW2";
        mUser.photo_url = "http://www.w3schools.com/css/img_fjords.jpg";
        mUser.cover_url = "http://www.w3schools.com/css/img_fjords.jpg";
        MyBundle.mUserBusiness.mUser = mUser;

        getEvent();
    }

    void createEvent(){
        AppEvent event = new AppEvent();
        event.id = "dgdfjhsfkf";
        event.fid = MyBundle.mUserBusiness.mUser.fid;
        event.name = "Alo";
        event.description = "fdvjhcxkjv";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        event.start_time = currentDateandTime;
        event.end_time = currentDateandTime;
        event.post_time = currentDateandTime;
        MyBundle.mUserBusiness.addEvent(event);
    }

    void getEvent(){
        // Test get event
        MyBundle.mUserBusiness.getMyEvents(new UserBusiness.UserBusinessListener() {
            @Override
            public void onComplete(UserBusiness.UserBusinessResult result) {
                if (result == UserBusiness.UserBusinessResult.SUCCESS){
                    for (int i = 0 ; i < MyBundle.mUserBusiness.mEvents.size(); i++){
                        Log.d("test", MyBundle.mUserBusiness.mEvents.get(i).name);
                    }

                }
            }
        });
    }
}
