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
import android.util.Base64;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.FirebaseApp;
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

//        TestData();



        Handler hd = new Handler(getMainLooper());
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivityForResult(new Intent(SplashScreenActivity.this, LoginActivity.class), 200);
            }
        }, 2000);

//        getUser();
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

    void TestData(){
        User mUser = new User();
        mUser.fid = "db17jjCUowTHWqjbhZlO61lV5wW2";
        mUser.photo_url = "http://www.w3schools.com/css/img_fjords.jpg";
        mUser.cover_url = "http://www.w3schools.com/css/img_fjords.jpg";
        mUser.fullname = "Hung Mai";
        MyBundle.mUserBusiness.mUser = mUser;

        getEvent();
    }

    User anoUser = null;
    void getUser(){
        FirebaseUser fuser = new FirebaseUser() {
            @NonNull
            @Override
            public String getUid() {
                return "db17jjCUowTHWqjbhZlO61lV5wW2";
            }

            @NonNull
            @Override
            public String getProviderId() {
                return null;
            }

            @Override
            public boolean isAnonymous() {
                return false;
            }

            @Nullable
            @Override
            public List<String> getProviders() {
                return null;
            }

            @NonNull
            @Override
            public List<? extends UserInfo> getProviderData() {
                return null;
            }

            @NonNull
            @Override
            public FirebaseUser zzaq(@NonNull List<? extends UserInfo> list) {
                return null;
            }

            @Override
            public FirebaseUser zzcu(boolean b) {
                return null;
            }

            @NonNull
            @Override
            public FirebaseApp zzcow() {
                return null;
            }

            @Nullable
            @Override
            public String getDisplayName() {
                return null;
            }

            @Nullable
            @Override
            public Uri getPhotoUrl() {
                return null;
            }

            @Nullable
            @Override
            public String getEmail() {
                return null;
            }

            @NonNull
            @Override
            public GetTokenResponse zzcox() {
                return null;
            }

            @Override
            public void zza(@NonNull GetTokenResponse getTokenResponse) {

            }

            @NonNull
            @Override
            public String zzcoy() {
                return null;
            }

            @NonNull
            @Override
            public String zzcoz() {
                return null;
            }

            @Override
            public boolean isEmailVerified() {
                return false;
            }
        };

        FirebaseHelper.checkExitedUser(fuser, new FirebaseHelper.FirebaseLoginHelperDelegate() {
            @Override
            public void onLoginSuccess(User user) {
                MyBundle.mUserBusiness.mUser = user;
                FirebaseHelper.findUser("ogx5wuS7FwefoAURIYrrQ6dQ8fl1", new FirebaseHelper.FirebaseUserDelegate() {
                    @Override
                    public void onFindUserSuccess(User user) {
                        anoUser = user;
                        getEvent();
                    }

                    @Override
                    public void onFindUserFailed() {

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
        });
//        FirebaseHelper.findUser("db17jjCUowTHWqjbhZlO61lV5wW2", new FirebaseHelper.FirebaseUserDelegate() {
//            @Override
//            public void onFindUserSuccess(User user) {
//            }
//
//            @Override
//            public void onFindUserFailed() {
//
//            }
//        });
    }

    void createFollow(){
        Follow follow = new Follow();
        follow.id_follower = anoUser.fid;
        follow.id_following = MyBundle.mUserBusiness.mUser.fid;
        MyBundle.mUserBusiness.addFollow(follow);
    }

    void getFollowers(){
        MyBundle.mUserBusiness.getFollowers(new UserBusiness.UserBusinessListener() {
            @Override
            public void onComplete(UserBusiness.UserBusinessResult result) {
                Log.d("test", "followers: " + MyBundle.mUserBusiness.mFollowers.size());
            }
        });
    }



    void createEvent(String fid){
        AppEvent event = new AppEvent();
        event.fid = fid;
        event.name = "Alo";
        event.description = "sdlfhsdjkfhskjfhkdjsfhsdkjfsdhjkfhsdkjflhsdklfdsfkjsdhfkjsdfdshfkjshdfkjsdfhjklsdhfkjsdhfkjlsdhjfkl";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        event.start_time = currentDateandTime;
        event.end_time = currentDateandTime;
        event.post_time = currentDateandTime;
        MyBundle.mUserBusiness.addEvent(event);
    }

    void getEvent(){
        // Test get event
        MyBundle.mUserBusiness.getFollowers(new UserBusiness.UserBusinessListener() {
            @Override
            public void onComplete(UserBusiness.UserBusinessResult result) {
                MyBundle.mUserBusiness.getMyEvents(new UserBusiness.UserBusinessListener() {
                    @Override
                    public void onComplete(UserBusiness.UserBusinessResult result) {
                        if (result == UserBusiness.UserBusinessResult.SUCCESS){

                            MyBundle.mUserBusiness.getAllEvents(new UserBusiness.UserBusinessListener() {
                                @Override
                                public void onComplete(UserBusiness.UserBusinessResult result) {
                                    for (int i = 0 ; i < MyBundle.mUserBusiness.allEvents.size(); i++){
                                        Log.d("test", MyBundle.mUserBusiness.allEvents.get(i).event.name);
                                    }
                                    Log.d("test", "ok");
                                    Handler hd = new Handler(Looper.getMainLooper());
                                    hd.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}
