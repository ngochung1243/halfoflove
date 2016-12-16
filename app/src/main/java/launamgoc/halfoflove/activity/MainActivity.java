package launamgoc.halfoflove.activity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TabHost;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import launamgoc.halfoflove.R;
import launamgoc.halfoflove.adapter.ChatListAdapter;
import launamgoc.halfoflove.adapter.NewFeedAdapter;
import launamgoc.halfoflove.model.ChatElement;
import launamgoc.halfoflove.model.NewFeedElement;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_timeline)
    ImageButton btnTimeline;
    @BindView(R.id.btn_timeline_partner)
    ImageButton btnTimelinePartner;
    @BindView(R.id.btn_account_setting)
    ImageButton btnAccountSet;
    @BindView(R.id.btn_relationship_setting)
    ImageButton btnRelaSet;
    EditText etSearch;

    private RecyclerView recyclerView;
    private ChatListAdapter chatListAdapter;
    private NewFeedAdapter newFeedAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<ChatElement> chatElementList = new ArrayList<ChatElement>();
    private List<NewFeedElement> newFeedElementList = new ArrayList<NewFeedElement>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        btnTimeline.setOnClickListener(this);
        btnTimelinePartner.setOnClickListener(this);
        btnAccountSet.setOnClickListener(this);
        btnRelaSet.setOnClickListener(this);

        // Set ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_main);
        etSearch = (EditText) findViewById(R.id.ab_et_search);
        etSearch.setOnClickListener(this);

        // Set RecyclerView
        setChatListRecyclerView();
        setNewFeedRecyclerView();

        loadTabs();
    }

    @Override
    public void onClick(View v) {
        if (v == btnTimeline) {
            Intent intent = new Intent(getBaseContext(), TimelineActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
        if (v == btnTimelinePartner) {
            Intent intent = new Intent(getBaseContext(), FriendTimelineActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
        if (v == btnAccountSet) {
            Intent intent = new Intent(getBaseContext(), EditProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
        if (v == btnRelaSet) {
            Intent intent = new Intent(getBaseContext(), RelationshipActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
        if (v == etSearch) {
            showSetNotiDialog();
        }
    }

    public void onChatListClick() {
        Intent intent = new Intent(MainActivity.this, ChatActivity.class);
        startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void loadTabs() {
        final TabHost tab = (TabHost) findViewById(android.R.id.tabhost);

        tab.setup();
        TabHost.TabSpec spec;

        spec = tab.newTabSpec("t1");
        spec.setContent(R.id.tab_newfeed);
        spec.setIndicator("", getApplicationContext().getDrawable(R.drawable.ic_newfeed));
        tab.addTab(spec);

        spec = tab.newTabSpec("t2");
        spec.setContent(R.id.tab_chatlist);
        spec.setIndicator("", getApplicationContext().getDrawable(R.drawable.ic_chat));
        tab.addTab(spec);

        spec = tab.newTabSpec("t3");
        spec.setContent(R.id.tab_settings);
        spec.setIndicator("", getApplicationContext().getDrawable(R.drawable.ic_menu));
        tab.addTab(spec);

        tab.setCurrentTab(0);
    }

    private void setNewFeedRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_newfeed);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        newFeedAdapter = new NewFeedAdapter(newFeedElementList);
        initializeNewFeedList();
        recyclerView.setAdapter(newFeedAdapter);
    }

    private void setChatListRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_chat);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        chatListAdapter = new ChatListAdapter(chatElementList);
        initializeChatList();
        recyclerView.setAdapter(chatListAdapter);
    }

    private void initializeNewFeedList() {
        newFeedAdapter.addItem(newFeedElementList.size(),
                new NewFeedElement(R.drawable.ava, "Kha Tran", "12/07", "Nội dung", R.drawable.calendar_header_2, newFeedElementList.size()));
        newFeedAdapter.addItem(newFeedElementList.size(),
                new NewFeedElement(R.drawable.ava, "Kha Tran", "12/07", "Nội dung", 0, newFeedElementList.size()));
        newFeedAdapter.addItem(newFeedElementList.size(),
                new NewFeedElement(R.drawable.ava, "Kha Tran", "12/07", "Nội dung", 0, newFeedElementList.size()));
        newFeedAdapter.addItem(newFeedElementList.size(),
                new NewFeedElement(R.drawable.ava, "Kha Tran", "12/07", "Nội dung", 0, newFeedElementList.size()));
        newFeedAdapter.addItem(newFeedElementList.size(),
                new NewFeedElement(R.drawable.ava, "Kha Tran", "12/07", "Nội dung", R.drawable.cover, newFeedElementList.size()));
        newFeedAdapter.addItem(newFeedElementList.size(),
                new NewFeedElement(R.drawable.ava, "Kha Tran", "12/07", "Nội dung", 0, newFeedElementList.size()));
        newFeedAdapter.addItem(newFeedElementList.size(),
                new NewFeedElement(R.drawable.ava, "Kha Tran", "12/07", "Nội dung", 0, newFeedElementList.size()));
    }

    private void initializeChatList() {
        chatListAdapter.addItem(chatElementList.size(),
                new ChatElement(R.drawable.ava, "Kha Tran", chatElementList.size()));
        chatListAdapter.addItem(chatElementList.size(),
                new ChatElement(R.drawable.ava, "Kha Tran", chatElementList.size()));
        chatListAdapter.addItem(chatElementList.size(),
                new ChatElement(R.drawable.ava, "Kha Tran", chatElementList.size()));
        chatListAdapter.addItem(chatElementList.size(),
                new ChatElement(R.drawable.ava, "Kha Tran", chatElementList.size()));
        chatListAdapter.addItem(chatElementList.size(),
                new ChatElement(R.drawable.ava, "Kha Tran", chatElementList.size()));
        chatListAdapter.addItem(chatElementList.size(),
                new ChatElement(R.drawable.ava, "Kha Tran", chatElementList.size()));
        chatListAdapter.addItem(chatElementList.size(),
                new ChatElement(R.drawable.ava, "Kha Tran", chatElementList.size()));
    }

    private void showSetNotiDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_search, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setTitle("Search Options");
        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }
}