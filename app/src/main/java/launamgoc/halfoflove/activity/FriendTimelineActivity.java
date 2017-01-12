package launamgoc.halfoflove.activity;

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
import launamgoc.halfoflove.model.UserBusiness;

import static launamgoc.halfoflove.R.id.btnChat;
import static launamgoc.halfoflove.R.id.num_follower;

/**
 * Created by KhaTran on 11/16/2016.
 */

public class FriendTimelineActivity extends AppCompatActivity {

    @BindView(R.id.avatar)
    ImageView iv_avatar;
    @BindView(R.id.name)
    TextView tv_name;
    @BindView(R.id.name_partner)
    TextView tv_name_partner;
    @BindView(R.id.btn_follow)
    Button btn_follow;
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
    @BindView(R.id.btnChat)
    Button btnChat;
    
    private RecyclerView recyclerView;
    private DiaryViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<DiaryContent> listView = new ArrayList<>();

    Handler hd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_timeline);

        hd = new Handler(getMainLooper());

        ButterKnife.bind(this);

        setActionBar();
        setRecyclerView();
        setActions();
        setTabs();

//        loadInfo();
//
//        loadCover();
//
//        loadAvatar();
//
//        loadNumFollower();
//
//        loadBeingLove();
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

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
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

    private void initializeDiary()
    {
//        adapter.addItem(listView.size(),
//                new DiaryContent(R.drawable.ava, 0, "17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
//        adapter.addItem(listView.size(),
//                new DiaryContent(0, R.raw.video, "17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
//        adapter.addItem(listView.size(),
//                new DiaryContent(0, 0, "17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.clear();
        MyBundle.pUserBusiness.getMyEvents(new UserBusiness.UserBusinessListener() {
            @Override
            public void onComplete(UserBusiness.UserBusinessResult result) {
                if (result == UserBusiness.UserBusinessResult.SUCCESS){
                    for (int i = 0; i < MyBundle.pUserBusiness.mEvents.size(); i ++){
                        AppEvent targetEvent = MyBundle.pUserBusiness.mEvents.get(i).event;
                        adapter.addItem(i, new DiaryContent(targetEvent.post_time, targetEvent.description,
                                targetEvent.photo_url, targetEvent.video_url, i));
                    }
                }
            }
        });
    }

    private void loadCover(){
        new FriendTimelineActivity.DownloadCoverImageAsyncTask().execute();
    }

    private void loadAvatar(){
        new FriendTimelineActivity.DownloadAvatarImageAsyncTask().execute();
    }

    private void loadInfo(){
        tv_name.setText(MyBundle.pUserBusiness.mUser.fullname);
        tv_location.setText(MyBundle.pUserBusiness.mUser.location);
        tv_birthday.setText(MyBundle.pUserBusiness.mUser.birthday);
        tv_email.setText(MyBundle.pUserBusiness.mUser.email);
        tv_interested.setText(MyBundle.pUserBusiness.mUser.email);
        tv_bio.setText(MyBundle.pUserBusiness.mUser.bio);
    }

    private void loadNumFollower(){
        MyBundle.pUserBusiness.getFollowers(new UserBusiness.UserBusinessListener() {
            @Override
            public void onComplete(UserBusiness.UserBusinessResult result) {
                if (result == UserBusiness.UserBusinessResult.SUCCESS){
                    tv_num_follower.setText(String.valueOf(MyBundle.pUserBusiness.mNum_followers));
                }
            }
        });
    }

    private void loadBeingLove(){
        tv_first_date.setText(MyBundle.mUserBusiness.mRelationship.start_time);
        tv_name_partner.setText(MyBundle.pUserBusiness.pUser.fullname);
    }


    private class DownloadCoverImageAsyncTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... voids) {
            URL url = null;
            try {
                if (MyBundle.pUserBusiness.mUser.cover_url != null && !MyBundle.pUserBusiness.mUser.cover_url.equals("")){
                    url = new URL(MyBundle.pUserBusiness.mUser.cover_url);
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
                ImageView imv = new ImageView(FriendTimelineActivity.this);
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
                if (MyBundle.pUserBusiness.mUser.photo_url != null && !MyBundle.pUserBusiness.mUser.photo_url.equals("")){
                    url = new URL(MyBundle.pUserBusiness.mUser.cover_url);
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
