package launamgoc.halfoflove.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import launamgoc.halfoflove.R;
import launamgoc.halfoflove.adapter.DiaryViewAdapter;
import launamgoc.halfoflove.helper.FirebaseHelper;
import launamgoc.halfoflove.model.AppEvent;
import launamgoc.halfoflove.model.DiaryContent;
import launamgoc.halfoflove.model.MyBundle;
import launamgoc.halfoflove.model.UserBusiness;

import static launamgoc.halfoflove.R.id.num_follower;
import static launamgoc.halfoflove.model.MyBundle.mUserBusiness;

/**
 * Created by KhaTran on 12/16/2016.
 */

public class TimelineActivity extends AppCompatActivity implements View.OnClickListener {

    private static int SELECT_AVATAR_CODE = 100;
    private static int SELECT_COVER_CODE = 101;
    public static int TIMELINE_CODE = 103;

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
    @BindView(R.id.btn_diary)
    Button btn_diary;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

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

        setActionBar();
        setRecyclerView();
        setActions();
        setTabs();

        loadInfo();

        loadAvatar();

        loadCover();

        loadNumFollower();

        loadBeingLove();

        tv_num_follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowingActivity.listView = MyBundle.mUserBusiness.mFollowers;

                Handler handler = new Handler(getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent i_Following = new Intent(TimelineActivity.this, FollowingActivity.class);
                        startActivity(i_Following);
                    }
                });
            }
        });
    }

    private void setActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar);

        TextView actionBarTitle = (TextView) findViewById(R.id.ab_tv_title);
        actionBarTitle.setText("Timeline");

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

    private void setRecyclerView(){
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DiaryViewAdapter(listView);
        initializeDiary();
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(0);
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

    private void setActions() {
        tv_name_partner.setPaintFlags(tv_name_partner.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_num_follower.setPaintFlags(tv_name_partner.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        iv_avatar.setOnClickListener(this);
        layout_cover.setOnClickListener(this);
        tv_name_partner.setOnClickListener(this);
        tv_num_follower.setOnClickListener(this);
        btn_diary.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent;

        switch (id) {
            case R.id.avatar:
                intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_AVATAR_CODE);
                break;
            case R.id.cover:
                intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_COVER_CODE);
                break;
            case R.id.name_partner:
                break;
            case R.id.num_follower:
                break;
            case R.id.btn_diary:
                intent = new Intent(getBaseContext(), CalendarActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == SELECT_AVATAR_CODE) {
                Uri uri = data.getData();
                try {
                    FirebaseHelper.uploadImage(mUserBusiness.mUser.fid, "photo_url", convertUriToByteArray(uri),
                            new FirebaseHelper.FirebaseUploadImagepDelegate() {
                                @Override
                                public void onUploadImageSuccess(String imageUrl) {
                                    mUserBusiness.mUser.photo_url = imageUrl;
                                    loadAvatar();
                                }

                                @Override
                                public void onUploadImageFailed(String error) {

                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_COVER_CODE) {
                Uri uri = data.getData();
                try {
                    FirebaseHelper.uploadImage(mUserBusiness.mUser.fid, "cover_url", convertUriToByteArray(uri),
                            new FirebaseHelper.FirebaseUploadImagepDelegate() {
                                @Override
                                public void onUploadImageSuccess(String imageUrl) {
                                    mUserBusiness.mUser.cover_url = imageUrl;
                                    loadCover();
                                }

                                @Override
                                public void onUploadImageFailed(String error) {

                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadCover(){
        try {
            new DownloadCoverImageAsyncTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void loadAvatar(){
        try {
            new DownloadAvatarImageAsyncTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void loadInfo(){
        tv_name.setText(mUserBusiness.mUser.fullname);
        tv_location.setText(mUserBusiness.mUser.location);
        tv_birthday.setText(mUserBusiness.mUser.birthday);
        tv_email.setText(mUserBusiness.mUser.email);
        tv_interested.setText(mUserBusiness.mUser.email);
        tv_bio.setText(mUserBusiness.mUser.bio);
    }

    private void loadNumFollower(){
        mUserBusiness.getFollowers(new UserBusiness.UserBusinessListener() {
            @Override
            public void onComplete(UserBusiness.UserBusinessResult result) {
                if (result == UserBusiness.UserBusinessResult.SUCCESS){
                    tv_num_follower.setText(String.valueOf(mUserBusiness.mNum_followers));
                }
            }
        });
    }

    private void loadBeingLove() {
        tv_first_date.setText(mUserBusiness.mRelationship.start_time);
        tv_name_partner.setText(mUserBusiness.pUser.fullname);
    }

    private void initializeDiary()
    {
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

    private byte[] convertUriToByteArray(Uri uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private class DownloadCoverImageAsyncTask extends AsyncTask<Void, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(Void... voids) {
            URL url = null;
            try {
                if (mUserBusiness.mUser.cover_url != null && !mUserBusiness.mUser.cover_url.equals("")){
                    url = new URL(mUserBusiness.mUser.cover_url);
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
