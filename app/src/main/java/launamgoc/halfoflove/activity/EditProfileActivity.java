package launamgoc.halfoflove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import launamgoc.halfoflove.model.MyBundle;
import launamgoc.halfoflove.model.User;

/**
 * Created by KhaTran on 11/11/2016.
 */

public class EditProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private InformationEditAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Information> listView = new ArrayList<Information>();

    public static int REQUEST_CODE = 1;

    public Handler hd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        hd = new Handler(getMainLooper());
//        FirebaseHelper.databaseDelegate = this;

        setActionBar();
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

//    @Override
//    public void onFindUserSuccess(final User user) {
//        hd.post(new Runnable() {
//            @Override
//            public void run() {
//                setDataRecyclerView(user);
//            }
//        });
//    }
//
//    @Override
//    public void onFindUserFailed() {
//        hd.post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(EditProfileActivity.this, "Can't find user's information!!!", Toast.LENGTH_LONG).show();
//            }
//        });
//    }

    public void onClickPost(String title, String content, int position) {

        Intent intent = new Intent(EditProfileActivity.this, EditDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putInt("id", position);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar);
        TextView title = (TextView) findViewById(R.id.ab_tv_title);
        title.setText("Edit profile");
        ImageButton btnBack = (ImageButton) findViewById(R.id.ab_btn_back);
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

    private void initializeView()
    {
        adapter.addItem(listView.size(),
                new Information("Full Name", MyBundle.mUser.fullname, listView.size()));
        adapter.addItem(listView.size(),
                new Information("Mood", MyBundle.mUser.mood, listView.size()));
        adapter.addItem(listView.size(),
                new Information("Mobile", MyBundle.mUser.mobile, listView.size()));
        adapter.addItem(listView.size(),
                new Information("Location", MyBundle.mUser.location, listView.size()));
        adapter.addItem(listView.size(),
                new Information("Bio", MyBundle.mUser.bio, listView.size()));
        adapter.addItem(listView.size(),
                new Information("Email", MyBundle.mUser.email, listView.size()));
        adapter.addItem(listView.size(),
                new Information("Birthday", MyBundle.mUser.birthday, listView.size()));
        adapter.addItem(listView.size(),
                new Information("Gender", MyBundle.mUser.gender, listView.size()));
        adapter.addItem(listView.size(),
                new Information("Interested in", MyBundle.mUser.interested, listView.size()));
    }
}