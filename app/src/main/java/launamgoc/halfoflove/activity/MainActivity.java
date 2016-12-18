package launamgoc.halfoflove.activity;

import android.annotation.TargetApi;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;

import launamgoc.halfoflove.R;

public class MainActivity extends AppCompatActivity {

    TabHost tabHost;

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocalActivityManager mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);
        tabHost = (TabHost)findViewById(android.R.id.tabhost);
        tabHost.setup(mLocalActivityManager);

        setTabs();
        setActionBar();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setTabs() {
        tabHost.addTab(tabHost.newTabSpec("t1")
                .setIndicator("", getApplicationContext().getDrawable(R.drawable.ic_newfeed))
                .setContent(new Intent(this, NewFeedTabActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("t2")
                .setIndicator("", getApplicationContext().getDrawable(R.drawable.ic_chat))
                .setContent(new Intent(this, ChatListTabActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("t3")
                .setIndicator("", getApplicationContext().getDrawable(R.drawable.ic_menu))
                .setContent(new Intent(this, SettingsTabActivity.class)));
        tabHost.setCurrentTab(0);
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_main);

        EditText search = (EditText) findViewById(R.id.ab_et_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }
}