package launamgoc.halfoflove.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import launamgoc.halfoflove.R;
import launamgoc.halfoflove.adapter.DiaryViewAdapter;
import launamgoc.halfoflove.model.AppEvent;
import launamgoc.halfoflove.model.DiaryContent;
import launamgoc.halfoflove.model.MyBundle;
import launamgoc.halfoflove.model.NewFeedElement;
import launamgoc.halfoflove.model.User;
import launamgoc.halfoflove.model.UserBusiness;
import launamgoc.halfoflove.helper.FirebaseHelper;
import launamgoc.halfoflove.model.UserEvent;

import static launamgoc.halfoflove.R.id.num_follower;

/**
 * Created by KhaTran on 12/16/2016.
 */

public class TimelineActivity extends AppCompatActivity implements View.OnClickListener {

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

    private RecyclerView recyclerView;
    private DiaryViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    private List<DiaryContent> listView = new ArrayList<>();

    Handler hd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);

        hd = new Handler(getMainLooper());

        iv_avatar.setOnClickListener(this);
        layout_cover.setOnClickListener(this);

        setActionBar();
        setActions();
        setRecyclerView();
        setTabs();

        loadInfo();

        loadCover();

        loadAvatar();

        loadNumFollower();

        loadBeingLove();
    }

    private void setActionBar(){
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

    private void setRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DiaryViewAdapter(listView);
        initializeDiary();
        recyclerView.setAdapter(adapter);
    }

    private void setTabs()
    {
        final TabHost tab=(TabHost) findViewById (android.R.id.tabhost);

        tab.setup();
        TabHost.TabSpec spec;

        spec = tab.newTabSpec("t1");
        spec.setContent(R.id.tab_infor);
        spec.setIndicator("Information");
        tab.addTab(spec);

        spec = tab.newTabSpec("t2");
        spec.setContent(R.id.tab_diary);
        spec.setIndicator("Love Diary");
        tab.addTab(spec);

        tab.setCurrentTab(0);
    }

    private void setActions(){
        tv_name_partner.setPaintFlags(tv_name_partner.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_num_follower.setPaintFlags(tv_name_partner.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tv_name_partner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tv_num_follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view == iv_avatar) {
            Toast.makeText(this, "Change ava", Toast.LENGTH_SHORT).show();
        }
        else if(view == layout_cover) {
            Toast.makeText(this, "Change cover", Toast.LENGTH_SHORT).show();
        }
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

    private void loadBeingLove() {
        tv_first_date.setText(MyBundle.mUserBusiness.mRelationship.start_time);
        tv_name_partner.setText(MyBundle.mUserBusiness.pUser.fullname);
    }

    private void initializeDiary()
    {
//        adapter.addItem(listView.size(),
//                new DiaryContent(R.drawable.ava, 0, "17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
//        adapter.addItem(listView.size(),
//                new DiaryContent(0, R.raw.video, "17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
//        adapter.addItem(listView.size(),
//                new DiaryContent(0, 0, "17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.clear();
        MyBundle.mUserBusiness.getMyEvents(new UserBusiness.UserBusinessListener() {
            @Override
            public void onComplete(UserBusiness.UserBusinessResult result) {
                if (result == UserBusiness.UserBusinessResult.SUCCESS){
                    for (int i = 0; i < MyBundle.mUserBusiness.mEvents.size(); i ++){
                        AppEvent targetEvent = MyBundle.mUserBusiness.mEvents.get(i).event;
                        adapter.addItem(i, new DiaryContent(targetEvent.post_time, targetEvent.description,
                                targetEvent.photo_url, targetEvent.video_url, i));
                    }
                }
            }
        });
    }

    private class DownloadCoverImageAsyncTask extends AsyncTask<Void, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(Void... voids) {
            URL url = null;
            try {
                if (MyBundle.mUserBusiness.mUser.cover_url != null && !MyBundle.mUserBusiness.mUser.cover_url.equals("")){
                    url = new URL(MyBundle.mUserBusiness.mUser.cover_url);
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
                if (MyBundle.mUserBusiness.mUser.photo_url != null && !MyBundle.mUserBusiness.mUser.photo_url.equals("")){
                    url = new URL(MyBundle.mUserBusiness.mUser.photo_url);
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
                iv_avatar.setImageBitmap(bitmap);
            }
        }
    }
}
