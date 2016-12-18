package launamgoc.halfoflove.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import launamgoc.halfoflove.R;
import launamgoc.halfoflove.adapter.ChatListAdapter;
import launamgoc.halfoflove.model.ChatElement;

/**
 * Created by KhaTran on 12/18/2016.
 */

public class ChatListTabActivity extends Activity {

    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<ChatElement> listView = new ArrayList<ChatElement>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tablelayout_newfeed);

        // Set RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatListAdapter(listView);
        initializeView();
        recyclerView.setAdapter(adapter);
    }

    private void initializeView() {
        adapter.addItem(listView.size(),
                new ChatElement(R.drawable.ava, "Kha Tran", listView.size()));
        adapter.addItem(listView.size(),
                new ChatElement(R.drawable.ava, "Kha Tran", listView.size()));
        adapter.addItem(listView.size(),
                new ChatElement(R.drawable.ava, "Kha Tran", listView.size()));
        adapter.addItem(listView.size(),
                new ChatElement(R.drawable.ava, "Kha Tran", listView.size()));
        adapter.addItem(listView.size(),
                new ChatElement(R.drawable.ava, "Kha Tran", listView.size()));
        adapter.addItem(listView.size(),
                new ChatElement(R.drawable.ava, "Kha Tran", listView.size()));
        adapter.addItem(listView.size(),
                new ChatElement(R.drawable.ava, "Kha Tran", listView.size()));
    }

    public void onItemClick() {
        Intent intent = new Intent(ChatListTabActivity.this, ChatActivity.class);
        startActivity(intent);
    }
}
