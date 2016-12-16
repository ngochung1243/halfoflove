package launamgoc.halfoflove.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import launamgoc.halfoflove.R;
import launamgoc.halfoflove.adapter.DiaryViewAdapter;
import launamgoc.halfoflove.model.Information;

/**
 * Created by KhaTran on 11/16/2016.
 */

public class FriendTimelineActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DiaryViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Information> listView = new ArrayList<Information>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_timeline);

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

        // Set RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DiaryViewAdapter(listView);
        initializeDiary();
        recyclerView.setAdapter(adapter);

        TextView tv_name = (TextView) findViewById(R.id.name);
        TextView tv_name_partner = (TextView) findViewById(R.id.name_partner);
        TextView tv_followers = (TextView) findViewById(R.id.followers);

        tv_name_partner.setPaintFlags(tv_name_partner.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tv_followers.setPaintFlags(tv_name_partner.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tv_name_partner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        tv_followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        loadTabs();
    }

    private void loadTabs()
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
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("17 August 2016", "Cuộc đời là những cuộc chơi.", listView.size()));
    }
}
