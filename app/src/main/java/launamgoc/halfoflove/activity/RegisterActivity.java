package launamgoc.halfoflove.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                        FirebaseHelper.createNewUser(email, password, RegisterActivity.this);
                    }
                }

            }
        });
        FirebaseHelper.delegate = this;
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

    @Override
    public void onCreateNewAccountSuccess() {
        Handler hd = new Handler(getMainLooper());
        hd.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, "Create account successfuly!!!", Toast.LENGTH_LONG).show();
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
                Toast.makeText(RegisterActivity.this, "Account existed or network had problem!!!", Toast.LENGTH_LONG).show();
            }
        });

    }
}
