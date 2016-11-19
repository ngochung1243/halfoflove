package launamgoc.halfoflove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import launamgoc.halfoflove.R;
import launamgoc.halfoflove.adapter.InformationEditAdapter;
import launamgoc.halfoflove.model.Information;

/**
 * Created by KhaTran on 11/11/2016.
 */

public class EditProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private InformationEditAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Information> listView = new ArrayList<Information>();

    public static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Set ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar);
        TextView title = (TextView) findViewById(R.id.actionbar_title);
        title.setText("Edit profile");
        ImageButton btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnBack.setImageResource(getResources()
                .getIdentifier("ic_clear_back", "drawable", getPackageName()));
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
        adapter = new InformationEditAdapter(listView);
        initializeView();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            adapter.updateItem(data.getExtras().getInt("id"),
                    data.getExtras().getString("title"));
        }
    }

    public void onClickPost(String title, String content, int position) {

        Intent intent = new Intent(EditProfileActivity.this, EditDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putInt("id", position);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void initializeView()
    {
        adapter.addItem(listView.size(),
                new Information("Full Name", "Kha Tran", listView.size()));
        adapter.addItem(listView.size(),
                new Information("Mood", "Being loved <3.", listView.size()));
        adapter.addItem(listView.size(),
                new Information("Mobile", "0909162189", listView.size()));
        adapter.addItem(listView.size(),
                new Information("Location", "Ho Chi Minh City", listView.size()));
        adapter.addItem(listView.size(),
                new Information("Bio", "", listView.size()));
        adapter.addItem(listView.size(),
                new Information("Email", "khatran216@gmail.com", listView.size()));
        adapter.addItem(listView.size(),
                new Information("Birthday", "25/06/1995", listView.size()));
        adapter.addItem(listView.size(),
                new Information("Gender", "Female", listView.size()));
        adapter.addItem(listView.size(),
                new Information("Interested in", "", listView.size()));
    }
}