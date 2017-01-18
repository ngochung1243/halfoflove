package launamgoc.halfoflove.activity;

import android.annotation.TargetApi;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.List;

import launamgoc.halfoflove.R;
import launamgoc.halfoflove.fragment.SearchFragment;
import launamgoc.halfoflove.helper.FirebaseHelper;
import launamgoc.halfoflove.model.MyBundle;
import launamgoc.halfoflove.model.User;

import static launamgoc.halfoflove.activity.ChatActivity.targetUser;

public class MainActivity extends AppCompatActivity {

    TabHost tabHost;
    public static LocalActivityManager mLocalActivityManager;
    FrameLayout frameLayout;

    LinearLayout lnLayoutSearchBar;
    TextView actionBarTitle;
    EditText ed_Search;

    private boolean readySearch = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLocalActivityManager = new LocalActivityManager(this, true);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        MyBundle.test = 3;
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(mLocalActivityManager);

        frameLayout = (FrameLayout)findViewById(R.id.search_placeholder);

        setTabs();
        setActionBar();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setTabs() {
        tabHost.addTab(tabHost.newTabSpec("t1")
                .setIndicator("", getApplicationContext().getDrawable(R.drawable.newfeed_tab))
                .setContent(new Intent(this, NewFeedTabActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("t2")
                .setIndicator("", getApplicationContext().getDrawable(R.drawable.chat_tab))
                .setContent(new Intent(this, ChatListTabActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("t3")
                .setIndicator("", getApplicationContext().getDrawable(R.drawable.setting_tab))
                .setContent(new Intent(this, SettingsTabActivity.class)));
        tabHost.setCurrentTab(0);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("t1")){
                    actionBarTitle.setText("New Feed");
                }else if (tabId.equals("t2")){
                    actionBarTitle.setText("Chat List");
                }else if (tabId.equals("t3")){
                    actionBarTitle.setText("Setting");
                }
            }
        });
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_main);

        lnLayoutSearchBar = (LinearLayout)findViewById(R.id.lnLayoutSearchBar);

        actionBarTitle = (TextView) findViewById(R.id.ab_tv_title);
        actionBarTitle.setText("New Feed");

        ed_Search = (EditText) findViewById(R.id.ab_et_search);
        final ImageButton btnBack = (ImageButton)findViewById(R.id.ab_btn_back);

//        ed_Search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                btnBack.setVisibility(View.VISIBLE);
//                setSearchFragment();
//            }
//        });

//        ed_Search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus){
//                    btnBack.setVisibility(View.VISIBLE);
//                    setSearchFragment();
//                }
//            }
//        });

        ImageView im_Seach = (ImageView) findViewById(R.id.im_Seach);
        im_Seach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!readySearch){
                    lnLayoutSearchBar.setVisibility(View.VISIBLE);
                    actionBarTitle.setVisibility(View.GONE);
                    setSearchFragment();
                    readySearch = true;
                }else {
                    readySearch = false;
                    String fullname = ed_Search.getText().toString();
                    FirebaseHelper.findUserByName(fullname, new FirebaseHelper.FirebaseFindUserDelegate() {
                        @Override
                        public void onFindUserByNameSuccess(List<User> users) {
                            SearchActivity.listView = users;
                            Handler handler = new Handler(getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i_Seach = new Intent(MainActivity.this, SearchActivity.class);
                                    startActivityForResult(i_Seach, 2000);
                                }
                            });
                        }
                        @Override
                        public void onFindUserFailed() {

                        }
                    });
                }

            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetMainView();
            }
        });
    }

    public void setSearchFragment(){

        frameLayout.setVisibility(View.VISIBLE);

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
// Replace the contents of the container with the new fragment
        ft.replace(R.id.search_placeholder, new SearchFragment());
// or ft.add(R.id.your_placeholder, new FooFragment());
// Complete the changes added above
        ft.commit();
    }

    public void resetMainView(){
        ed_Search.setText("");
        lnLayoutSearchBar.setVisibility(View.GONE);
        frameLayout.setVisibility(View.GONE);

        actionBarTitle.setVisibility(View.VISIBLE);

        readySearch = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2000){
            resetMainView();
        }else {
            SettingsTabActivity settingsTabActivity = (SettingsTabActivity) mLocalActivityManager.getCurrentActivity();
            settingsTabActivity.onActivityResult(requestCode, resultCode, data);
        }
    }
}