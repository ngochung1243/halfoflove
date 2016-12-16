package launamgoc.halfoflove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import launamgoc.halfoflove.R;
import launamgoc.halfoflove.helper.FirebaseHelper;
import launamgoc.halfoflove.model.Information;
import launamgoc.halfoflove.model.MyBundle;

/**
 * Created by KhaTran on 11/13/2016.
 */

public class EditDetailActivity extends AppCompatActivity {

    public static int TAG = 1;

    Information changeInfo;
    TextView tvTitle;
    EditText etText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_detail);

        Bundle bundle = getIntent().getExtras();
        changeInfo = new Information(bundle.getString("title"), bundle.getString("content"), bundle.getInt("id"));

        tvTitle = (TextView) findViewById(R.id.tv_title);
        etText = (EditText) findViewById(R.id.et_content);

        tvTitle.setText(changeInfo.getTitle());
        etText.setText(changeInfo.getContent());

        setActionBar();
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar);

        TextView actionBar_title = (TextView) findViewById(R.id.actionbar_title);
        actionBar_title.setText(changeInfo.getTitle());
        actionBar_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = changeInfo.getTitle();
                String value = etText.getText().toString();
                while (title.indexOf(" ") != -1) {
                    title = title.replaceAll(" ", "");
                }
                title = title.toLowerCase();
                changeInfo.setContent(value);
                FirebaseHelper.changeInfoOfUser(MyBundle.mUser.fid,title, value);
                sendIntent();
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

    private void sendIntent() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("title", changeInfo.getTitle());
        bundle.putString("content", changeInfo.getContent());
        bundle.putInt("id", changeInfo.getId());
        intent.putExtras(bundle);
        setResult(TAG, intent);
    }
}
