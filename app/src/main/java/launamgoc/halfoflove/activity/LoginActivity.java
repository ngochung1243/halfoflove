package launamgoc.halfoflove.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import launamgoc.halfoflove.R;
import launamgoc.halfoflove.helper.FirebaseHelper;

public class LoginActivity extends AppCompatActivity implements FirebaseHelper.FirebaseHelperDelegate{

    EditText edtEmail;
    EditText edtPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MapController();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                FirebaseHelper.loginWithUser(email,password);
            }
        });
        FirebaseHelper.delegate = this;
    }

    private void MapController()
    {
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtPassword =(EditText)findViewById(R.id.edtPassword);
        btnLogin =  (Button)findViewById(R.id.btnLogin);
    }

    @Override
    public void onCreateNewAccountSuccess() {
        Handler hd = new Handler(getMainLooper());
        hd.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, "Successfuly", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onCreateNewAccountFailed() {
        Handler hd = new Handler(getMainLooper());
        hd.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });
    }
}
