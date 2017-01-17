package launamgoc.halfoflove.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import launamgoc.halfoflove.R;
import launamgoc.halfoflove.adapter.ChatListAdapter;
import launamgoc.halfoflove.helper.FirebaseAPIHelper;
import launamgoc.halfoflove.helper.FirebaseHelper;
import launamgoc.halfoflove.model.Message;
import launamgoc.halfoflove.model.MessageResponse;
import launamgoc.halfoflove.model.MyBundle;
import launamgoc.halfoflove.model.User;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static launamgoc.halfoflove.helper.FirebaseHelper.getToken;

public class LoginActivity extends AppCompatActivity implements FirebaseHelper.FirebaseLoginHelperDelegate {

    final int RC_GG_SIGN_IN = 100;
    public static int REQUEST_LOGOUT = 500;

    EditText edtEmail;
    EditText edtPassword;
    Button btnLogin;
    Button btnSignInFb;
    Button btnSignInGG;
    TextView btnRegister;
    ProgressDialog progressDialog;

    CallbackManager callbackManager;

    public static GoogleApiClient mGoogleApiClient;

    Handler hd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        hd = new Handler(getMainLooper());

        callbackManager = CallbackManager.Factory.create();

//        MyBundle.mUserBusiness.logout();

        // Callback registration
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d("Facebook", "facebook:onSuccess:" + loginResult);
                AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                FirebaseHelper.loginWithSocial(credential);
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e("Facebook", "facebook:onFailed:" + exception.getMessage());
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        MapController();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                if (email == null || email == "" || password == null || password == ""){
                    Toast.makeText(LoginActivity.this, "Please input all fields!!!", Toast.LENGTH_SHORT).show();
                }

                FirebaseHelper.loginWithUser(email,password);
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Loading");
                progressDialog.show();
//                TestGetToken();

                //TestSendPostAPI();
            }
        });
        FirebaseHelper.loginDelegate = this;
        btnSignInFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginWithFb();
            }
        });

        btnSignInGG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginWithGG();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegisterActivity();
            }
        });
    }

    private void TestGetToken(){
        String token = FirebaseHelper.getToken();
        Toast.makeText(LoginActivity.this, "Token: ["+token+"]", Toast.LENGTH_SHORT).show();
        Log.d("FirebaseToken","Token: ["+token+"]");
    }

    private void TestSendPostAPI(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fcm.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FirebaseAPIHelper apiHelper = retrofit.create(FirebaseAPIHelper.class);
        Message testMessage = new Message();
        Map<String, String> data = new HashMap<>();
        data.put("userID", "123456789");
        data.put("kind", "message");
        data.put("message", "hung dep trai");

        testMessage.setData(data);
        testMessage.setTo("chtmjx4Hiuk:APA91bGxdHe5NDhmfGRl-TaBq3u2bKsNuqTaRYTn_PIUnhxCvdte-7Cjl-8Ed9h9GYZuL-whGabzsQAEBtpv4D_QfTiaM9oflD9T4-KCs8Ym_NPoObkAS7KNhKz4uKVpgtPBhGI3l5aP");
        Gson gson = new Gson();
        String jsonString = gson.toJson(testMessage);
        try {
            JSONObject json = new JSONObject(jsonString);
            Log.d("json", jsonString);

            String test = "{to : chtmjx4Hiuk:APA91bGxdHe5NDhmfGRl-TaBq3u2bKsNuqTaRYTn_PIUnhxCvdte-7Cjl-8Ed9h9GYZuL-whGabzsQAEBtpv4D_QfTiaM9oflD9T4-KCs8Ym_NPoObkAS7KNhKz4uKVpgtPBhGI3l5aP}";
            Call<MessageResponse> call =apiHelper.sendMessage(testMessage);
            call.enqueue(new Callback<MessageResponse>() {
                @Override
                public void onResponse(Response<MessageResponse> response, Retrofit retrofit) {
                    Log.d("Respone", response.toString());
                }

                @Override
                public void onFailure(Throwable t) {
                    int a = 0;
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

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

    private void MapController()
    {
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtPassword =(EditText)findViewById(R.id.edtPassword);
        btnLogin =  (Button)findViewById(R.id.btnLogin);
        btnSignInFb = (Button)findViewById(R.id.btnSignInFb);
        btnSignInGG = (Button)findViewById(R.id.btnSignInGG);
        btnRegister = (TextView)findViewById(R.id.btnRegister);
    }

    public void loginWithFb(){
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
    }

    public void loginWithGG(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GG_SIGN_IN);
    }

    private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void startRegisterActivity(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void testChatMessage(){
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }

    private void testChatList(){
        Intent intent = new Intent(this, ChatListTabActivity.class);
        startActivity(intent);
    }

    @Override
    public void onLoginSuccess(final User user) {
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
                if(progressDialog != null) {
                    progressDialog.dismiss();
                }
                MyBundle.mUserBusiness.getToken();
                startMainActivity();
//                testChatMessage();
//                testChatList();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GG_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                FirebaseHelper.loginWithSocial(credential);
            } else {
                Toast.makeText(LoginActivity.this, "Login Google Account Failed!!!", Toast.LENGTH_SHORT).show();
            }
        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onLoginFailed(final Exception ex) {
        hd.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, "Failed: " + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onLogoutSuccess() {

    }

    @Override
    public void onLogoutFailed(Exception ex) {

    }


}
