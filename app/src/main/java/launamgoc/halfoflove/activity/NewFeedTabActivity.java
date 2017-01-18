package launamgoc.halfoflove.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import launamgoc.halfoflove.R;
import launamgoc.halfoflove.adapter.NewFeedAdapter;
import launamgoc.halfoflove.model.AppEvent;
import launamgoc.halfoflove.model.MyBundle;
import launamgoc.halfoflove.model.NewFeedElement;
import launamgoc.halfoflove.model.User;
import launamgoc.halfoflove.model.UserBusiness;

/**
 * Created by KhaTran on 12/17/2016.
 */

public class NewFeedTabActivity extends Activity {

    private RecyclerView recyclerView;
    private NewFeedAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tablelayout_newfeed);

        setRecyclerView();

        initializeView();
    }

    private void setRecyclerView(){
        // Set RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NewFeedAdapter(MyBundle.mUserBusiness.allEvents);
        recyclerView.setAdapter(adapter);

    }

    private void initializeView() {
        MyBundle.mUserBusiness.getAllEvents(new UserBusiness.UserBusinessListener() {
            @Override
            public void onComplete(UserBusiness.UserBusinessResult result) {
                if (result == UserBusiness.UserBusinessResult.SUCCESS){
                    adapter.setUserEvents(MyBundle.mUserBusiness.allEvents);
                }
            }
        });


//        adapter.addItem(listView.size(),
//                new NewFeedElement(R.drawable.ava, "Kha Tran", "12/07", "Nội dung", 0, listView.size()));
//        adapter.addItem(listView.size(),
//                new NewFeedElement(R.drawable.ava, "Kha Tran", "12/07", "Nội dung", 0, listView.size()));
//        adapter.addItem(listView.size(),
//                new NewFeedElement(R.drawable.ava, "Kha Tran", "12/07", "Nội dung", 0, listView.size()));
//        adapter.addItem(listView.size(),
//                new NewFeedElement(R.drawable.ava, "Kha Tran", "12/07", "Nội dung", R.drawable.cover, listView.size()));
//        adapter.addItem(listView.size(),
//                new NewFeedElement(R.drawable.ava, "Kha Tran", "12/07", "Nội dung", 0, listView.size()));
//        adapter.addItem(listView.size(),
//                new NewFeedElement(R.drawable.ava, "Kha Tran", "12/07", "Nội dung", 0, listView.size()));
    }
}
