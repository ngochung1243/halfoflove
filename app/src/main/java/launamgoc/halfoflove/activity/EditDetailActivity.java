package launamgoc.halfoflove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import launamgoc.halfoflove.R;
import launamgoc.halfoflove.helper.FirebaseHelper;
import launamgoc.halfoflove.model.Information;
import launamgoc.halfoflove.model.User;

import static launamgoc.halfoflove.model.MyBundle.mUserBusiness;

/**
 * Created by KhaTran on 11/13/2016.
 */

public class EditDetailActivity extends AppCompatActivity {
    public static int TAG = 1;

    @BindView(R.id.title)
    TextView tvTitle;
    @BindView(R.id.edittext)
    EditText etText;
    @BindView(R.id.btn_save)
    TextView btnSave;

    Information changeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_detail);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        changeInfo = new Information(bundle.getString("title"), bundle.getString("content"), bundle.getInt("id"));

        tvTitle.setText(changeInfo.getTitle());
        etText.setText(changeInfo.getContent());
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = changeInfo.getTitle();
                String value = etText.getText().toString();
                while (title.indexOf(" ") != -1) {
                    title = title.replaceAll(" ", "");
                }
                title = title.toLowerCase();
                changeInfo.setContent(value);
                FirebaseHelper.changeInfoOfUser(mUserBusiness.mUser.fid,title, value);
                FirebaseHelper.findUser(mUserBusiness.mUser.fid, new FirebaseHelper.FirebaseUserDelegate() {
                    @Override
                    public void onFindUserSuccess(User user) {
                        mUserBusiness.mUser = user;
                        Handler hd = new Handler(Looper.getMainLooper());
                        hd.post(new Runnable() {
                            @Override
                            public void run() {
                                sendIntent();
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onFindUserFailed() {

                    }
                });
            }
        });

        setActionBar();
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar);

        TextView actionBarTitle = (TextView) findViewById(R.id.ab_tv_title);
        actionBarTitle.setText("Edit " + changeInfo.getTitle());

        ImageButton btnBack = (ImageButton) findViewById(R.id.ab_btn_back);
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
