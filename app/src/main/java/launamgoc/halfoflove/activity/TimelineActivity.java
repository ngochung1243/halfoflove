package launamgoc.halfoflove.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import launamgoc.halfoflove.R;
import launamgoc.halfoflove.model.MyBundle;
import launamgoc.halfoflove.model.UserBusiness;

import static launamgoc.halfoflove.R.id.num_follower;

/**
 * Created by KhaTran on 12/16/2016.
 */

public class TimelineActivity extends AppCompatActivity {

    @BindView(R.id.avatar)
    ImageView iv_avatar;
    @BindView(R.id.name)
    TextView tv_name;
    @BindView(R.id.name_partner)
    TextView tv_name_partner;
    @BindView(R.id.location)
    TextView tv_location;
    @BindView(R.id.first_date)
    TextView tv_first_date;
    @BindView(R.id.birthday)
    TextView tv_birthday;
    @BindView(R.id.email)
    TextView tv_email;
    @BindView(num_follower)
    TextView tv_num_follower;
    @BindView(R.id.interested_in)
    TextView tv_interested;
    @BindView(R.id.bio)
    TextView tv_bio;
    @BindView(R.id.cover)
    LinearLayout layout_cover;

    Handler hd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        hd = new Handler(getMainLooper());

        ButterKnife.bind(this);

        setActionBar();

        loadInfo();

        loadCover();

        loadAvatar();

        loadNumFollower();

        loadBeingLove();
    }

    private void setActionBar(){
        // Set ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar);
        ImageButton btnBack = (ImageButton) findViewById(R.id.ab_btn_back);
        btnBack.setImageResource(getResources()
                .getIdentifier("ic_arrow_back", "drawable", getPackageName()));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button btnDiary = (Button) findViewById(R.id.btn_diary);
        btnDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CalendarActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    private void loadCover(){
        new DownloadCoverImageAsyncTask().execute();
    }

    private void loadAvatar(){
        new DownloadAvatarImageAsyncTask().execute();
    }

    private void loadInfo(){
        tv_name.setText(MyBundle.mUserBusiness.mUser.fullname);
        tv_location.setText(MyBundle.mUserBusiness.mUser.location);
        tv_birthday.setText(MyBundle.mUserBusiness.mUser.birthday);
        tv_email.setText(MyBundle.mUserBusiness.mUser.email);
        tv_interested.setText(MyBundle.mUserBusiness.mUser.email);
        tv_bio.setText(MyBundle.mUserBusiness.mUser.bio);
    }

    private void loadNumFollower(){
        MyBundle.mUserBusiness.getFollowers(new UserBusiness.UserBusinessListener() {
            @Override
            public void onComplete(UserBusiness.UserBusinessResult result) {
                if (result == UserBusiness.UserBusinessResult.SUCCESS){
                    tv_num_follower.setText(String.valueOf(MyBundle.mUserBusiness.mNum_followers));
                }
            }
        });
    }

    private void loadBeingLove(){
        MyBundle.mUserBusiness.getRelationShip(new UserBusiness.UserBusinessListener() {
            @Override
            public void onComplete(UserBusiness.UserBusinessResult result) {
                if (result == UserBusiness.UserBusinessResult.SUCCESS){
                    tv_first_date.setText(MyBundle.mUserBusiness.mRelationship.start_time);
                    tv_name_partner.setText(MyBundle.mUserBusiness.pUser.fullname);
                }
            }
        });
    }


    private class DownloadCoverImageAsyncTask extends AsyncTask<Void, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL(MyBundle.mUserBusiness.mUser.cover_url);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return bmp;
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
                ImageView imv = new ImageView(TimelineActivity.this);
                imv.setImageBitmap(bitmap);
                layout_cover.setBackground(imv.getDrawable());
            }
        }
    }

    private class DownloadAvatarImageAsyncTask extends AsyncTask<Void, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL(MyBundle.mUserBusiness.mUser.photo_url);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return bmp;
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
                iv_avatar.setImageBitmap(bitmap);
            }

        }
    }
}
