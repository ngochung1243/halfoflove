package launamgoc.halfoflove.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import launamgoc.halfoflove.R;
import launamgoc.halfoflove.adapter.ChatMessageAdapter;
import launamgoc.halfoflove.model.ChatMessage;
import launamgoc.halfoflove.model.MyBundle;
import launamgoc.halfoflove.model.User;
import launamgoc.halfoflove.model.UserBusiness;

import static android.R.attr.bitmap;
import static android.R.attr.filter;
import static launamgoc.halfoflove.R.drawable.ava;

public class ChatActivity extends AppCompatActivity {

    ImageButton btn_send;
    EditText edt_input;
    RecyclerView rcv_chat;

    ChatMessageAdapter messageAdapter = new ChatMessageAdapter();
    static public User targetUser = MyBundle.mUserBusiness.mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

//        ButterKnife.bind(this);

        btn_send = (ImageButton) findViewById(R.id.btnSend);
        edt_input = (EditText)findViewById(R.id.edtInput);
        rcv_chat = (RecyclerView) findViewById(R.id.rcvChatView);

        setActionBar();

        new DownloadImageAsyncTask().execute(targetUser.photo_url,MyBundle.mUserBusiness.mUser.photo_url);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void setActionBar(){
        // Set ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar);

        TextView actionBarTitle = (TextView) findViewById(R.id.ab_tv_title);
        actionBarTitle.setText(targetUser.fullname);
        actionBarTitle.setGravity(Gravity.RIGHT);

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

    private void setBeginChat(Bitmap ava, Bitmap my_ava){
        messageAdapter.setImage(ava, my_ava);

        rcv_chat.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rcv_chat.setLayoutManager(new LinearLayoutManager(this));
        rcv_chat.setAdapter(messageAdapter);

        MyBundle.mUserBusiness.getChatMessages(new UserBusiness.UserBusinessListener() {
            @Override
            public void onComplete(UserBusiness.UserBusinessResult result) {
                messageAdapter.setList(getMessageOfTargerUser(MyBundle.mUserBusiness.mChatMessages));
                rcv_chat.scrollToPosition(messageAdapter.getItemCount()-1);
            }
        });
    }

    private List<ChatMessage> getMessageOfTargerUser(List<ChatMessage> allMessages){
        List<ChatMessage> filterMessages = new ArrayList<ChatMessage>();

        for (ChatMessage chatMessage:allMessages
             ) {
            if (chatMessage.id_sender.equals(targetUser.fid) || chatMessage.id_receiver.equals(targetUser.fid)){
                filterMessages.add(chatMessage);
            }
        }

        return filterMessages;
    }

    private void sendMessage(){
        String message = edt_input.getText().toString();
        MyBundle.mUserBusiness.sendMessageToUser(targetUser, message);
        edt_input.setText("");
    }

    private class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap[]> {

        @Override
        protected Bitmap[] doInBackground(String... photo_urls) {
            URL ava_url = null;
            Bitmap[] avas = new Bitmap[2];
            try {
                Bitmap ava_bmp = null;
                Bitmap my_ava_bmp = null;
                if (photo_urls[0] != null && !photo_urls[0].equals("")) {
                    ava_url = new URL(photo_urls[0]);
                    ava_bmp = BitmapFactory.decodeStream(ava_url.openConnection().getInputStream());
                }

                if (photo_urls[1] != null && !photo_urls[1].equals("")) {
                    ava_url = new URL(photo_urls[1]);
                    my_ava_bmp = BitmapFactory.decodeStream(ava_url.openConnection().getInputStream());
                }

                avas[0] = ava_bmp;
                avas[1] = my_ava_bmp;

                return avas;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap[] bitmaps) {
            setBeginChat(bitmaps[0], bitmaps[1]);
        }
    }
}
