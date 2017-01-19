package launamgoc.halfoflove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import launamgoc.halfoflove.R;
import launamgoc.halfoflove.adapter.InformationEditAdapter;
import launamgoc.halfoflove.adapter.InformationFirstEditAdapter;
import launamgoc.halfoflove.model.Information;
import launamgoc.halfoflove.model.MyBundle;

/**
 * Created by Admin on 1/19/2017.
 */

public class FirstEditProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private InformationFirstEditAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Information> listView = new ArrayList<>();

    public static int REQUEST_CODE = 102;

    public Handler hd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_edit_profile);

        hd = new Handler(getMainLooper());

        setRecyclerView();

        Button btnFinish = (Button)findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            Bundle bundle = data.getExtras();
            Information changeInfo = new Information(bundle.getString("title"), bundle.getString("content"), bundle.getInt("id"));
            adapter.updateItem(changeInfo.getId(), changeInfo.getContent());
        }
    }

    public void onClickPost(String title, String content, int position) {

        Intent intent = new Intent(FirstEditProfileActivity.this, EditDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putInt("id", position);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void setRecyclerView() {

        // Set RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new InformationFirstEditAdapter(listView);

        initializeView();

        recyclerView.setAdapter(adapter);
    }

    private void initializeView()
    {
        adapter.addItem(listView.size(),
                new Information("Full Name", MyBundle.mUserBusiness.mUser.fullname, listView.size()));
        adapter.addItem(listView.size(),
                new Information("Mood", MyBundle.mUserBusiness.mUser.mood, listView.size()));
        adapter.addItem(listView.size(),
                new Information("Mobile", MyBundle.mUserBusiness.mUser.mobile, listView.size()));
        adapter.addItem(listView.size(),
                new Information("Location", MyBundle.mUserBusiness.mUser.location, listView.size()));
        adapter.addItem(listView.size(),
                new Information("Bio", MyBundle.mUserBusiness.mUser.bio, listView.size()));
        adapter.addItem(listView.size(),
                new Information("Email", MyBundle.mUserBusiness.mUser.email, listView.size()));
        adapter.addItem(listView.size(),
                new Information("Birthday", MyBundle.mUserBusiness.mUser.birthday, listView.size()));
        adapter.addItem(listView.size(),
                new Information("Gender", MyBundle.mUserBusiness.mUser.gender, listView.size()));
        adapter.addItem(listView.size(),
                new Information("Interested in", MyBundle.mUserBusiness.mUser.interested, listView.size()));
    }

    private void startMainActivity(){
        Intent i = new Intent(FirstEditProfileActivity.this, MainActivity.class); // Your list's Intent
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
