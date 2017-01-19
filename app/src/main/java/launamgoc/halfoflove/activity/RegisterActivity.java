package launamgoc.halfoflove.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import launamgoc.halfoflove.R;
import launamgoc.halfoflove.helper.FirebaseHelper;
import launamgoc.halfoflove.model.MyBundle;
import launamgoc.halfoflove.model.User;

import static launamgoc.halfoflove.activity.SplashScreenActivity.isLogin;

public class RegisterActivity extends AppCompatActivity implements FirebaseHelper.FirebaseHelperDelegate, FirebaseHelper.FirebaseLoginHelperDelegate{

    Button btnDangKy;
    EditText edtEmail;
    EditText edtReEmail;
    EditText edtPassword;
    EditText edtRePassword;

    ProgressDialog progressDialog;

    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        MapControler();
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password , reemail, repassword;
                email = edtEmail.getText().toString();
                reemail = edtReEmail.getText().toString();
                password = edtPassword.getText().toString();
                repassword = edtRePassword.getText().toString();

                if(!reemail.equals(email))
                {
                    Toast.makeText(RegisterActivity.this, "Re-email isn't correct", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(!repassword.equals(password))
                    {
                        Toast.makeText(RegisterActivity.this, "Re-password isn't correct", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        progressDialog.show();
                        FirebaseHelper.createNewUser(email, password, RegisterActivity.this);
                    }
                }

            }
        });

        setProgressDialog();

        FirebaseHelper.delegate = this;
        FirebaseHelper.loginDelegate = this;
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

    private void setProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering...");
    }

    private void MapControler()
    {
        btnDangKy = (Button)findViewById(R.id.btnRegister);
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtReEmail = (EditText)findViewById(R.id.edtReEmail);
        edtPassword = (EditText)findViewById(R.id.edtPassword);
        edtRePassword = (EditText)findViewById(R.id.edtRePassword);
    }

    private void refreshView(){
        edtEmail.setText("");
        edtReEmail.setText("");
        edtPassword.setText("");
        edtRePassword.setText("");
    }

    private void startFirstEditProfile(){
        Intent intent = new Intent(this, FirstEditProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreateNewAccountSuccess() {
        Handler hd = new Handler(getMainLooper());
        hd.post(new Runnable() {
            @Override
            public void run() {
                FirebaseHelper.loginWithUser(edtEmail.getText().toString(), edtPassword.getText().toString());
            }
        });
    }

    @Override
    public void onCreateNewAccountFailed() {
        Handler hd = new Handler(getMainLooper());
        hd.post(new Runnable() {
            @Override
            public void run() {
                refreshView();
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Account existed or network had problem!!!", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onLoginSuccess(User user) {
        progressDialog.dismiss();
        MyBundle.mUserBusiness.mUser = user;
        if (isLogin){
            return;
        }
        isLogin = true;
        startFirstEditProfile();
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
