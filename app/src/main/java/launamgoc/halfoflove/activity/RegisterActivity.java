package launamgoc.halfoflove.activity;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import launamgoc.halfoflove.R;
import launamgoc.halfoflove.helper.FirebaseHelper;

public class RegisterActivity extends AppCompatActivity implements FirebaseHelper.FirebaseHelperDelegate{

    Button btnDangKy;
    EditText edtEmail;
    EditText edtReEmail;
    EditText edtPassword;
    EditText edtRePassword;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AnhXa();
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;
                email = edtEmail.getText().toString();
                password = edtRePassword.getText().toString();
                FirebaseHelper.createNewUser(email, password);
            }
        });
        FirebaseHelper.delegate = this;
    }

    private void AnhXa()
    {
        btnDangKy = (Button)findViewById(R.id.btnRegister);
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtReEmail = (EditText)findViewById(R.id.edtReEmail);
        edtPassword = (EditText)findViewById(R.id.edtPassword);
        edtRePassword = (EditText)findViewById(R.id.edtRePassword);
    }

    @Override
    public void onCreateNewAccountSuccess() {
        Handler hd = new Handler(getMainLooper());
        hd.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, "Successfuly", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onCreateNewAccountFailed() {
        Handler hd = new Handler(getMainLooper());
        hd.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, "Failed", Toast.LENGTH_LONG).show();
            }
        });

    }
}
