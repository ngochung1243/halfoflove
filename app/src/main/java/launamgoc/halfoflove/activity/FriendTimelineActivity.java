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
import launamgoc.halfoflove.model.Follow;
import launamgoc.halfoflove.model.MyBundle;
import launamgoc.halfoflove.model.User;
import launamgoc.halfoflove.model.UserBusiness;

import static launamgoc.halfoflove.R.id.btnChat;
import static launamgoc.halfoflove.R.id.btn_follow;
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

    public static UserBusiness userBusiness;

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

        loadInfo();

        loadCover();

        loadAvatar();

        loadNumFollower();

        loadBeingLove();

        processFollowButton();

        tv_num_follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowingActivity.listView = userBusiness.mFollowers;

                Handler handler = new Handler(getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent i_Following = new Intent(FriendTimelineActivity.this, FollowingActivity.class);
                        startActivity(i_Following);
                    }
                });
            }
        });

        btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btn_follow.getText().toString().equals("UNFOLLOW")){
                    //lấy danh sách theo dõi
                    List<User> lwFollowing = MyBundle.mUserBusiness.mFollowings;
                    for (int i = 0; i < lwFollowing.size(); i++) {
                        if (userBusiness.mUser.fid.equals(lwFollowing.get(i).fid)) {
                            String id = "";
                            for (int j = 0; j < MyBundle.mUserBusiness.following_objects.size(); j++) {
                                if (MyBundle.mUserBusiness.following_objects.get(j).id_following.equals(userBusiness.mUser.fid)) {
                                    id = MyBundle.mUserBusiness.following_objects.get(j).id;
                                }
                            }
                            MyBundle.mUserBusiness.removeFollow(id);
                            btn_follow.setText("FOLLOW");
                            break;
                        }
                    }
                }else {
                    Follow newFollow = new Follow();
                    newFollow.id_following = userBusiness.mUser.fid;
                    newFollow.id_follower = MyBundle.mUserBusiness.mUser.fid;
                    MyBundle.mUserBusiness.addFollow(newFollow);
                    btn_follow.setText("UNFOLLOW");
                }

            }
        });
    }

    private void setActionBar() {
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

    private void setRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DiaryViewAdapter(listView);
        initializeDiary();
        recyclerView.setAdapter(adapter);
    }

    private void setActions() {
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
                startChatActivity();
            }
        });
    }

    private void setTabs() {
        final TabHost tab = (TabHost) findViewById(android.R.id.tabhost);

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

    private void initializeDiary() {
//        adapter.addItem(listView.size(),
//                new DiaryContent(R.drawable.ava, 0, "17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
//        adapter.addItem(listView.size(),
//                new DiaryContent(0, R.raw.video, "17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
//        adapter.addItem(listView.size(),
//                new DiaryContent(0, 0, "17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.clear();
        userBusiness.getMyEvents(new UserBusiness.UserBusinessListener() {
            @Override
            public void onComplete(UserBusiness.UserBusinessResult result) {
                if (result == UserBusiness.UserBusinessResult.SUCCESS) {
                    for (int i = 0; i < userBusiness.mEvents.size(); i++) {
                        AppEvent targetEvent = userBusiness.mEvents.get(i).event;
                        adapter.addItem(i, new DiaryContent(targetEvent.post_time, targetEvent.description,
                                targetEvent.photo_url, targetEvent.video_url, i));
                    }
                }
            }
        });
    }

    private void startChatActivity() {
        ChatActivity.targetUser = userBusiness.mUser;
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }

    private void loadCover() {
        new FriendTimelineActivity.DownloadCoverImageAsyncTask().execute();
    }

    private void loadAvatar() {
        new FriendTimelineActivity.DownloadAvatarImageAsyncTask().execute();
    }

    private void loadInfo() {
        tv_name.setText(userBusiness.mUser.fullname);
        tv_location.setText(userBusiness.mUser.location);
        tv_birthday.setText(userBusiness.mUser.birthday);
        tv_email.setText(userBusiness.mUser.email);
        tv_interested.setText(userBusiness.mUser.email);
        tv_bio.setText(userBusiness.mUser.bio);
    }

    private void loadNumFollower() {
        userBusiness.getFollowers(new UserBusiness.UserBusinessListener() {
            @Override
            public void onComplete(UserBusiness.UserBusinessResult result) {
                if (result == UserBusiness.UserBusinessResult.SUCCESS) {
                    tv_num_follower.setText(String.valueOf(userBusiness.mNum_followers));
                }
            }
        });
    }

    private void processFollowButton() {
        List<User> lwFollower = MyBundle.mUserBusiness.mFollowers;
        int Dem = 0;
        for (int i = 0; i < lwFollower.size(); i++) {
            Dem++;
            if (userBusiness.mUser.fid.equals(lwFollower.get(i).fid) && MyBundle.pUserBusiness.mUser.fid.equals(lwFollower.get(i).fid)) {
                Dem = 0;
                btn_follow.setVisibility(View.INVISIBLE);
                break;
            } else {
                if (userBusiness.mUser.fid.equals(lwFollower.get(i).fid) && !MyBundle.pUserBusiness.mUser.fid.equals(lwFollower.get(i).fid)) {
                    Dem = 0;
                    btn_follow.setText("UNFOLLOW");
                    break;
                }
            }
        }
        if (Dem != 0 || lwFollower.size() == 0) {
            btn_follow.setText("FOLLOW");
        }
    }

    private void loadBeingLove() {
        userBusiness.getRelationShip(new UserBusiness.UserBusinessListener() {
            @Override
            public void onComplete(UserBusiness.UserBusinessResult result) {
                if (result == UserBusiness.UserBusinessResult.SUCCESS) {
                    Handler hd = new Handler(getMainLooper());
                    hd.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_first_date.setText(userBusiness.mRelationship.start_time);
                            tv_name_partner.setText(userBusiness.pUser.fullname);
                        }
                    });

                }
            }
        });

    }


    private class DownloadCoverImageAsyncTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... voids) {
            URL url = null;
            try {
                if (userBusiness.mUser.cover_url != null && !userBusiness.mUser.cover_url.equals("")) {
                    url = new URL(userBusiness.mUser.cover_url);
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
                ImageView imv = new ImageView(FriendTimelineActivity.this);
                imv.setImageBitmap(bitmap);
                layout_cover.setBackground(imv.getDrawable());
            }
        }
    }

    private class DownloadAvatarImageAsyncTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... voids) {
            URL url = null;
            try {
                if (userBusiness.mUser.photo_url != null && !userBusiness.mUser.photo_url.equals("")) {
                    url = new URL(userBusiness.mUser.photo_url);
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
                iv_avatar.setImageBitmap(bitmap);
            }

        }
    }
}
