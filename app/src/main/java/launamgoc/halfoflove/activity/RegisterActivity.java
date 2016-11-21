package launamgoc.halfoflove.activity;

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

public class RegisterActivity extends AppCompatActivity {

    Button btnDangKy;
    EditText edtEmail;
    EditText edtReEmail;
    EditText edtPassword;
    EditText edtRePassword;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        AnhXa();
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });
    }

    private void AnhXa()
    {
        btnDangKy = (Button)findViewById(R.id.btnRegister);
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtReEmail = (EditText)findViewById(R.id.edtReEmail);
        edtPassword = (EditText)findViewById(R.id.edtPassword);
        edtRePassword = (EditText)findViewById(R.id.edtRePassword);
    }

    private void Register(){
        String email, password;
        email = edtEmail.getText().toString();
        password = edtRePassword.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Thành công",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Thất bại",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}
