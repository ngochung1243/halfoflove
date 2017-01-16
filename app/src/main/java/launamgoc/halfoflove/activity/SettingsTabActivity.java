package launamgoc.halfoflove.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import launamgoc.halfoflove.R;
import launamgoc.halfoflove.helper.FirebaseHelper;
import launamgoc.halfoflove.model.MyBundle;
import launamgoc.halfoflove.model.Relationship;
import launamgoc.halfoflove.model.User;
import launamgoc.halfoflove.model.UserBusiness;

/**
 * Created by KhaTran on 12/18/2016.
 */

public class SettingsTabActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.cardview_partner)
    CardView cardview_partner;
    @BindView(R.id.cardview_logout)
    CardView cardview_logout;

    @BindView(R.id.layout_timeline)
    LinearLayout mTimeline;
    @BindView(R.id.layout_timeline_partner)
    LinearLayout mTimelinePartner;
    @BindView(R.id.layout_account_setting)
    LinearLayout mAccountSet;
    @BindView(R.id.layout_relationship_setting)
    LinearLayout mRelaSet;

    @BindView(R.id.tv_daytogether)
    TextView tv_daytogether;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_name_partner)
    TextView tv_name_partner;
    @BindView(R.id.ll_been_together)
    LinearLayout ll_been_together;

    @BindView(R.id.iv_avatar)
    ImageView iv_avatar;
    @BindView(R.id.iv_avatar_partner)
    ImageView iv_avatar_partner;

    @BindView(R.id.switch_find)
    Switch mSwitchFind;
    @BindView(R.id.switch_timeline)
    Switch mSwitchTimeline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tablelayout_settings);
        ButterKnife.bind(this);

        cardview_logout.setOnClickListener(this);

        mTimeline.setOnClickListener(this);
        mTimelinePartner.setOnClickListener(this);
        mAccountSet.setOnClickListener(this);
        mRelaSet.setOnClickListener(this);

        mSwitchFind.setChecked(MyBundle.mUserBusiness.mUser.allow_find);
        mSwitchTimeline.setChecked(MyBundle.mUserBusiness.mUser.allow_see_timeline);

        mSwitchFind.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                MyBundle.mUserBusiness.mUser.allow_find = b;
                FirebaseHelper.changeInfoOfUser(MyBundle.mUserBusiness.mUser.fid, "allow_find", MyBundle.mUserBusiness.mUser.allow_find);
            }
        });

        mSwitchTimeline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                MyBundle.mUserBusiness.mUser.allow_see_timeline = b;
                FirebaseHelper.changeInfoOfUser(MyBundle.mUserBusiness.mUser.fid, "allow_see_timeline", MyBundle.mUserBusiness.mUser.allow_see_timeline);
            }
        });

        setMyLayout();

        loadRelationship();

//        if (MyBundle.mUserBusiness.pUser == null){
//            cardview_partner.setVisibility(View.GONE);
//        }else {
//            setPartnerLayout();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    void setMyLayout(){
        tv_name.setText(MyBundle.mUserBusiness.mUser.fullname);
        new DownloadMyAvatarImageAsyncTask().execute();
    }

    void setPartnerLayout(){
        tv_name_partner.setText(MyBundle.mUserBusiness.pUser.fullname);
        new DownloadPartnerAvatarImageAsyncTask().execute();
    }

    void setDayTogether() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Date startTime = null;
        Date currentTime = null;
        try {
            startTime = dateFormat.parse(MyBundle.mUserBusiness.mRelationship.start_time);
            currentTime = dateFormat.parse(dateFormat.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar date1 = Calendar.getInstance();
        date1.setTime(startTime);
        Calendar date2 = Calendar.getInstance();
        date2.setTime(currentTime);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();
        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        tv_daytogether.setText(String.valueOf((int) dayCount));
    }

    private void loadRelationship(){
        MyBundle.mUserBusiness.getRelationShip(new UserBusiness.UserBusinessListener() {
            @Override
            public void onComplete(UserBusiness.UserBusinessResult result) {
                Handler hd = new Handler(getMainLooper());
                if (result == UserBusiness.UserBusinessResult.SUCCESS){
                    hd.post(new Runnable() {
                        @Override
                        public void run() {
                            cardview_partner.setVisibility(View.VISIBLE);
                            ll_been_together.setVisibility(View.VISIBLE);
                            setPartnerLayout();
                            setDayTogether();

                            MyBundle.pUserBusiness.mUser = MyBundle.mUserBusiness.pUser;
                            MyBundle.pUserBusiness.pUser = MyBundle.mUserBusiness.mUser;
                        }
                    });

                }else {
                    hd.post(new Runnable() {
                        @Override
                        public void run() {
                            ll_been_together.setVisibility(View.GONE);
                            cardview_partner.setVisibility(View.GONE);
                            MyBundle.mUserBusiness.pUser = new User();
                            MyBundle.pUserBusiness.mUser = new User();
                        }
                    });

                }
            }
        });
    }

    void loadDaysTogether(){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1){
            if (requestCode == RelationshipActivity.RELATIONSHIP_CODE) {
                loadDaysTogether();
            }
            if (requestCode == EditProfileActivity.REQUEST_CODE){
                setMyLayout();
            }
            if (requestCode == TimelineActivity.TIMELINE_CODE){
                setMyLayout();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mTimeline) {
            Intent intent = new Intent(getBaseContext(), TimelineActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            getParent().startActivityForResult(intent, TimelineActivity.TIMELINE_CODE);
        }
        if (v == mTimelinePartner) {
            FriendTimelineActivity.userBusiness = MyBundle.pUserBusiness;
            Intent intent = new Intent(getBaseContext(), FriendTimelineActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);

        }
        if (v == mAccountSet) {
            Intent intent = new Intent(this, EditProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            getParent().startActivityForResult(intent, EditProfileActivity.REQUEST_CODE);
        }
        if (v == mRelaSet) {
            RelationshipActivity.requestUser = MyBundle.pUserBusiness.mUser;
            Intent intent = new Intent(getBaseContext(), RelationshipActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            getParent().startActivityForResult(intent, RelationshipActivity.RELATIONSHIP_CODE);
        }
        if (v == cardview_logout){
            MyBundle.mUserBusiness.logout();
            getParent().finish();
        }
    }

    private class DownloadMyAvatarImageAsyncTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... voids) {
            URL ava_url = null;
            try {
                Bitmap ava_bmp = null;
                if (MyBundle.mUserBusiness.mUser.photo_url != null && !MyBundle.mUserBusiness.mUser.photo_url.equals("")){
                    ava_url = new URL(MyBundle.mUserBusiness.mUser.photo_url);
                    ava_bmp = BitmapFactory.decodeStream(ava_url.openConnection().getInputStream());
                    return ava_bmp;
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
                iv_avatar.setImageBitmap(bitmap);
                iv_avatar.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
    }

    private class DownloadPartnerAvatarImageAsyncTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... voids) {
            URL ava_url = null;
            try {
                Bitmap ava_bmp = null;
                if (MyBundle.mUserBusiness.pUser.photo_url != null && !MyBundle.mUserBusiness.pUser.photo_url.equals("")){
                    ava_url = new URL(MyBundle.mUserBusiness.pUser.photo_url);
                    ava_bmp = BitmapFactory.decodeStream(ava_url.openConnection().getInputStream());
                    return ava_bmp;
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
                iv_avatar_partner.setImageBitmap(bitmap);
                iv_avatar_partner.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
    }
}
