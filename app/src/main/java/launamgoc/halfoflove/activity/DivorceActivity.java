package launamgoc.halfoflove.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import launamgoc.halfoflove.R;
import launamgoc.halfoflove.model.Follow;
import launamgoc.halfoflove.model.MyBundle;
import launamgoc.halfoflove.model.User;

import static launamgoc.halfoflove.activity.ChatActivity.targetUser;

public class DivorceActivity extends AppCompatActivity {

    public static User senderUser;

    @BindView(R.id.tvSenderName)
    TextView tvSenderName;
    @BindView(R.id.btnAccept)
    Button btnAccept;
    @BindView(R.id.btnCancel)
    Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_divorce);

        ButterKnife.bind(this);

        setActionBar();

        setListener();
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar);

        TextView actionBarTitle = (TextView) findViewById(R.id.ab_tv_title);
        actionBarTitle.setText("Divorce");

        ImageButton btnBack = (ImageButton) findViewById(R.id.ab_btn_back);
        btnBack.setImageResource(getResources()
                .getIdentifier("ic_arrow_back", "drawable", getPackageName()));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(1);
                finish();
            }
        });

        TextView tvTitle = (TextView)findViewById(R.id.ab_tv_title);
        tvTitle.setText("Half Of Love");
    }

    private void setListener(){
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptRequest();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRequest();
            }
        });
    }

    private void setSenderInfo(){
        tvSenderName.setText(senderUser.fullname);
    }

    private void acceptRequest(){
        MyBundle.mUserBusiness.removeRelationship();

        removeFollow();

        MyBundle.mUserBusiness.sendDivorceAcceptance(senderUser);

        setResult(1);
        finish();
    }

    private void removeFollow(){
        for (Follow follow : MyBundle.mUserBusiness.follower_objects){
            if (follow.id_follower.equals(senderUser.fid)){
                MyBundle.mUserBusiness.removeFollow(follow.id);
                break;
            }
        }

        for (Follow follow : MyBundle.mUserBusiness.following_objects){
            if (follow.id_following.equals(senderUser.fid)){
                MyBundle.mUserBusiness.removeFollow(follow.id);
                break;
            }
        }
    }

    private void cancelRequest(){
        MyBundle.mUserBusiness.sendDivorceDenial(senderUser);
        setResult(1);
        finish();
    }
}
