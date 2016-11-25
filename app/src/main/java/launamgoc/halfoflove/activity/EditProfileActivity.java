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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import launamgoc.halfoflove.R;
import launamgoc.halfoflove.adapter.InformationEditAdapter;
import launamgoc.halfoflove.model.Information;
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

         // Set Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mapsrefrence = database.getReference().child("user1");
        mapsrefrence.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChildren()) {
                            @SuppressWarnings("unchecked")
                            Map<String, String> value = (Map<String, String>) dataSnapshot.getValue();
                            initializeView(value);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        //initializeView(FirebaseHelper.findUser());
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

    private void initializeView(Map<String, String> value)
    {
        adapter.addItem(listView.size(),
                new Information("Full Name", value.get("fullname"), listView.size()));
        adapter.addItem(listView.size(),
                new Information("Mood", value.get("mood"), listView.size()));
        adapter.addItem(listView.size(),
                new Information("Mobile", value.get("mobile"), listView.size()));
        adapter.addItem(listView.size(),
                new Information("Location", value.get("location"), listView.size()));
        adapter.addItem(listView.size(),
                new Information("Bio", value.get("bio"), listView.size()));
        adapter.addItem(listView.size(),
                new Information("Email", value.get("email"), listView.size()));
        adapter.addItem(listView.size(),
                new Information("Birthday", value.get("birthday"), listView.size()));
        adapter.addItem(listView.size(),
                new Information("Gender", value.get("gender"), listView.size()));
        adapter.addItem(listView.size(),
                new Information("Hobby", value.get("hobby"), listView.size()));
    }

    private void initializeView(User user)
    {
        adapter.addItem(listView.size(),
            new Information("Full Name", user.fullname, listView.size()));
        adapter.addItem(listView.size(),
                new Information("Mood", user.mood, listView.size()));
        adapter.addItem(listView.size(),
                new Information("Mobile", user.mobile, listView.size()));
        adapter.addItem(listView.size(),
                new Information("Location", user.location, listView.size()));
        adapter.addItem(listView.size(),
                new Information("Bio", user.bio, listView.size()));
        adapter.addItem(listView.size(),
                new Information("Email", user.email, listView.size()));
        adapter.addItem(listView.size(),
                new Information("Birthday", user.birthday, listView.size()));
        adapter.addItem(listView.size(),
                new Information("Gender", user.gender, listView.size()));
        adapter.addItem(listView.size(),
                new Information("Hobby", user.hobby, listView.size()));
    }
}