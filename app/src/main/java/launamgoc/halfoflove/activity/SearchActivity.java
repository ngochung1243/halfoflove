package launamgoc.halfoflove.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import launamgoc.halfoflove.R;
import launamgoc.halfoflove.adapter.ChatListAdapter;
import launamgoc.halfoflove.model.ChatElement;

/**
 * Created by KhaTran on 12/18/2016.
 */

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<ChatElement> listView = new ArrayList<ChatElement>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setActionBar();

        // Set RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatListAdapter(listView);

        initializeView();

        recyclerView.setAdapter(adapter);
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
    }
}
