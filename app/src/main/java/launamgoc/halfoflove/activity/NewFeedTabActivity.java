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

    private List<NewFeedElement> listView = new ArrayList<NewFeedElement>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tablelayout_newfeed);

        // Set RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new NewFeedAdapter(listView);
        initializeView();
        recyclerView.setAdapter(adapter);
    }

    private void initializeView() {
        adapter.clear();
        MyBundle.mUserBusiness.getAllEvents(new UserBusiness.UserBusinessListener() {
            @Override
            public void onComplete(UserBusiness.UserBusinessResult result) {
                if (result == UserBusiness.UserBusinessResult.SUCCESS){
                    for (int i = 0; i < MyBundle.mUserBusiness.allEvents.size(); i ++){
                        User targetUser = MyBundle.mUserBusiness.allEvents.get(i).user;
                        AppEvent targetEvent = MyBundle.mUserBusiness.allEvents.get(i).event;
                        adapter.addItem(listView.size(),
                                new NewFeedElement(targetUser.photo_url, targetUser.fullname, targetEvent.post_time, targetEvent.description, targetEvent.photo_url, listView.size()));
                    }
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
