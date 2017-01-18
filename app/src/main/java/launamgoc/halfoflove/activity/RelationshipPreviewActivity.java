package launamgoc.halfoflove.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import launamgoc.halfoflove.R;
import launamgoc.halfoflove.model.Follow;
import launamgoc.halfoflove.model.MyBundle;
import launamgoc.halfoflove.model.Relationship;
import launamgoc.halfoflove.model.User;

import static launamgoc.halfoflove.R.id.btnAccept;
import static launamgoc.halfoflove.R.id.btnCancel;
import static launamgoc.halfoflove.R.id.iv_avatar;
import static launamgoc.halfoflove.R.id.tvLoveStatement;
import static launamgoc.halfoflove.activity.FriendTimelineActivity.userBusiness;

public class RelationshipPreviewActivity extends AppCompatActivity {

    @BindView(R.id.cover)
    LinearLayout lnLayoutCover;
    @BindView(R.id.avatar)
    ImageView imvAvatar;
    @BindView(R.id.name)
    TextView tvName;
    @BindView(R.id.tvStartTime)
    TextView tvStartTime;
    @BindView(R.id.tvLoveStatement)
    TextView tvLoveStatement;
    @BindView(R.id.btnAccept)
    Button btnAccept;
    @BindView(R.id.btnCancel)
    Button btnCancel;

    public static Relationship relationship;
    public static User sendRequestUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relationship_preview);

        ButterKnife.bind(this);

        setActionBar();

        setInfoOfRelationshipRequest();

        setListener();
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar);

        TextView actionBarTitle = (TextView) findViewById(R.id.ab_tv_title);
        actionBarTitle.setText("Relationship Preview");

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

    private void setInfoOfRelationshipRequest(){
        new DownloadCoverImageAsyncTask().execute();
        new DownloadAvatarImageAsyncTask().execute();
        tvName.setText(sendRequestUser.fullname);
        tvStartTime.append(relationship.start_time);
        tvLoveStatement.append(relationship.love_statement);
    }

    private void acceptRequest(){
        MyBundle.mUserBusiness.setRelationship(relationship);

        addFollow();

        MyBundle.mUserBusiness.sendRelationshipAcceptance(sendRequestUser);
        setResult(1);
        finish();
    }

    private void cancelRequest(){
        MyBundle.mUserBusiness.sendRelationshipDenial(sendRequestUser);
        setResult(1);
        finish();
    }

    private void addFollow(){
        Follow newFollow1= new Follow();
        newFollow1.id_follower = MyBundle.mUserBusiness.mUser.fid;
        newFollow1.id_following = sendRequestUser.fid;

        Follow newFollow2= new Follow();
        newFollow2.id_following = MyBundle.mUserBusiness.mUser.fid;
        newFollow2.id_follower = sendRequestUser.fid;

        MyBundle.mUserBusiness.addFollow(newFollow1);
        MyBundle.mUserBusiness.addFollow(newFollow2);
    }

    private class DownloadCoverImageAsyncTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... voids) {
            URL url = null;
            try {
                if (sendRequestUser.cover_url != null && !sendRequestUser.cover_url.equals("")) {
                    url = new URL(sendRequestUser.cover_url);
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    return bmp;
                } else {
                    return null;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                ImageView imv = new ImageView(RelationshipPreviewActivity.this);
                imv.setImageBitmap(bitmap);
                lnLayoutCover.setBackground(imv.getDrawable());
            }
        }
    }

    private class DownloadAvatarImageAsyncTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... voids) {
            URL url = null;
            try {
                if (sendRequestUser.photo_url != null && !sendRequestUser.photo_url.equals("")){
                    url = new URL(sendRequestUser.photo_url);
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    return bmp;
                }else {
                    return null;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null){
                imvAvatar.setImageBitmap(bitmap);
            }
        }
    }
}
