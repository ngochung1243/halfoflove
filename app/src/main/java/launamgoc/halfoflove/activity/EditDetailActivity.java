package launamgoc.halfoflove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import launamgoc.halfoflove.R;
import launamgoc.halfoflove.helper.FirebaseHelper;

/**
 * Created by KhaTran on 11/13/2016.
 */

public class EditDetailActivity extends AppCompatActivity {

    public static int TAG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_detail);

        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        EditText etText = (EditText) findViewById(R.id.et_content);

        tvTitle.setText(getIntent().getExtras().getString("title"));
        etText.setText(getIntent().getExtras().getString("content"));

        setActionBar(tvTitle, etText);
    }

    private void setActionBar(final TextView changedValue, final EditText changedData) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar);

        TextView actionBar_title = (TextView) findViewById(R.id.actionbar_title);
        actionBar_title.setText("Save");
        actionBar_title.setGravity(Gravity.RIGHT);
        actionBar_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = changedData.getText().toString();
                String value = changedValue.getText().toString().toLowerCase();
                while (value.indexOf(" ") != -1) {
                    value = value.replaceAll(" ", "");
                }
                FirebaseHelper.changeInfoOfUser(value, data);
                sendIntent(changedData.getText().toString());
                finish();
            }
        });

        ImageButton btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnBack.setImageResource(getResources()
                .getIdentifier("ic_arrow_back", "drawable", getPackageName()));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    private void sendIntent(String changedData) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("title", changedData.toString());
        bundle.putInt("id", getIntent().getExtras().getInt("id"));
        intent.putExtras(bundle);
        setResult(TAG, intent);
    }
}
