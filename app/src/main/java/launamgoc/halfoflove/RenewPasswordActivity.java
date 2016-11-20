package launamgoc.halfoflove;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by KhaTran on 11/20/2016.
 */

public class RenewPasswordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renew_password);

        // Set ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar);
        TextView title = (TextView) findViewById(R.id.actionbar_title);
        title.setText("Renew password");

        final EditText et_pass = (EditText) findViewById(R.id.et_pass);
        final EditText et_repass = (EditText) findViewById(R.id.et_repass);
        Button btn_continue = (Button) findViewById(R.id.btn_continue);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(et_repass.getText().toString().matches("")
                        || et_pass.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(),
                            "Don't let pass and repass empty!!!", Toast.LENGTH_SHORT).show();
                }
                else if (!et_repass.getText().toString().matches(et_pass.getText().toString())) {
                    Toast.makeText(getApplicationContext(),
                            "Re-pass doesn't match with pass.\nPlease type again.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "Set password successfully!!!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
