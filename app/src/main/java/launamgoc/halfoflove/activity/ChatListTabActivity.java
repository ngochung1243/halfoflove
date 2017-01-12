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
import launamgoc.halfoflove.helper.FirebaseHelper;
import launamgoc.halfoflove.model.ChatElement;
import launamgoc.halfoflove.model.ChatMessage;
import launamgoc.halfoflove.model.MyBundle;
import launamgoc.halfoflove.model.User;
import launamgoc.halfoflove.model.UserBusiness;

/**
 * Created by KhaTran on 12/18/2016.
 */

public class ChatListTabActivity extends Activity {

    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<ChatMessage> chatMessages = new ArrayList<ChatMessage>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tablelayout_newfeed);

        // Set RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatListAdapter();
//        initializeView();
        recyclerView.setAdapter(adapter);

        getChatMessages();
    }

    private void getChatMessages(){
        MyBundle.mUserBusiness.getChatMessages(new UserBusiness.UserBusinessListener() {
            @Override
            public void onComplete(UserBusiness.UserBusinessResult result) {
                if (result == UserBusiness.UserBusinessResult.SUCCESS){
                    adapter.setUserIds(filterChatUser(MyBundle.mUserBusiness.mChatMessages));
                }
            }
        });
    }

    private List<String> filterChatUser(List<ChatMessage> allChatMessages){
        List<String> filterUserIds = new ArrayList<String>();

        for (ChatMessage chatMessage:allChatMessages){
            String target_id = checkChatMessage(chatMessage, filterUserIds);
            if (!target_id.equals("")){
                filterUserIds.add(target_id);
            }
        }
        return filterUserIds;
    }

    private String checkChatMessage(ChatMessage chatMessage, final List<String> filterUserIds){
        final String target_id = chatMessage.id_sender.equals(MyBundle.mUserBusiness.mUser.fid) ? chatMessage.id_receiver : chatMessage.id_sender;
        for (String filterUserId:filterUserIds){
            if (target_id.equals(filterUserId)){
                return "";
            }
        }

        return target_id;
    }

    public void onItemClick(User user) {
        ChatActivity.targetUser = user;
        Intent intent = new Intent(ChatListTabActivity.this, ChatActivity.class);
        startActivity(intent);
    }
}
