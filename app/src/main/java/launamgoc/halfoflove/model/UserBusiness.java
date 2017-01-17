package launamgoc.halfoflove.model;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import launamgoc.halfoflove.R;
import launamgoc.halfoflove.activity.ChatActivity;
import launamgoc.halfoflove.activity.LoginActivity;
import launamgoc.halfoflove.helper.FirebaseAPIHelper;
import launamgoc.halfoflove.helper.FirebaseHelper;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static android.R.id.message;

/**
 * Created by Admin on 12/18/2016.
 */

public class UserBusiness {
    public enum UserBusinessResult {
        SUCCESS,
        FAILED
    };

    public User mUser = new User();
    public Relationship mRelationship = new Relationship();
    public User pUser = new User();
    public List<User> mFollowers = new ArrayList<>();
    public List<User> mFollowings = new ArrayList<>();
    public List<Follow> following_objects = new ArrayList<>();
    public List<Follow> follower_objects = new ArrayList<>();
    public int mNum_followers = 0;
    public List<UserEvent>mEvents = new ArrayList<>();
    public List<UserEvent>allEvents = new ArrayList<>();
    public List<ChatMessage>mChatMessages = new ArrayList<>();

    public void logout(){
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
//        Auth.GoogleSignInApi.signOut(LoginActivity.mGoogleApiClient);
    }

    public void load(final UserBusinessListener listener){
        getRelationShip(new UserBusinessListener() {
            @Override
            public void onComplete(UserBusinessResult result) {
                if (result == UserBusinessResult.SUCCESS){
                    getMyEvents(new UserBusinessListener() {
                        @Override
                        public void onComplete(UserBusinessResult result) {
                            if (result == UserBusinessResult.SUCCESS){
                                getAllEvents(new UserBusinessListener() {
                                    @Override
                                    public void onComplete(UserBusinessResult result) {
                                        if (result == UserBusinessResult.SUCCESS){
                                            listener.onComplete(UserBusinessResult.SUCCESS);
                                        }else {
                                            listener.onComplete(UserBusinessResult.FAILED);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

    }

    public void getRelationShip(final UserBusinessListener listener){
        FirebaseHelper.findRelationship(mUser.fid, new FirebaseHelper.FirebaseRelationshipDelegate() {
            @Override
            public void onFindRelationshipSuccess(Relationship relationship, User partner) {
                mRelationship = relationship;
                pUser = partner;
                listener.onComplete(UserBusinessResult.SUCCESS);
            }

            @Override
            public void onFindRelationshipFailed(String error) {
                Log.d("Firebase", "Firebase:FindRelationshipError: " + error);
                listener.onComplete(UserBusinessResult.FAILED);
            }
        });
    }

    public void setRelationship(Relationship relationship){
        FirebaseHelper.addRelationship(relationship);
    }

    public void removeRelationship(){
        FirebaseHelper.removeRelationship(mRelationship.id);
    }

    public void addFollow(Follow follow){
        FirebaseHelper.addFollow(follow);
    }

    public void removeFollow(String follow_id){
        FirebaseHelper.removeFollow(follow_id);
    }

    public void getFollowers(final UserBusinessListener listener){
        mFollowers.clear();
        FirebaseHelper.findFollower(mUser.fid, new FirebaseHelper.FirebaseFollowerDelegate() {

            @Override
            public void onFindNumFollower(final int num_follower) {
                mNum_followers = num_follower;
                if (num_follower == 0){
                    listener.onComplete(UserBusinessResult.SUCCESS);
                }
            }

            @Override
            public void onFindFollowerObjectSuccess(List<Follow> followers) {
                follower_objects = followers;
            }

            @Override
            public void onFindFollowerSuccess(User user) {
                mFollowers.add(user);
                if (mFollowers.size() == mNum_followers){
                    listener.onComplete(UserBusinessResult.SUCCESS);
                }
            }

            @Override
            public void onFindFollowerFailed(String error) {
                Log.d("Firebase", "Firebase:FindFollowError: " + error);
                listener.onComplete(UserBusinessResult.FAILED);
            }
        });
    }

    public void getFollowings(final UserBusinessListener listener){
        mFollowings.clear();
        FirebaseHelper.findFollowings(mUser.fid, new FirebaseHelper.FirebaseFollowingDelegate() {

            @Override
            public void onFindNumFollowing(final int num_follower) {
                mNum_followers = num_follower;
                if (num_follower == 0){
                    listener.onComplete(UserBusinessResult.SUCCESS);
                }
            }

            @Override
            public void onFindFollowingObjectSuccess(List<Follow> followings) {
                following_objects = followings;
            }

            @Override
            public void onFindFollowingSuccess(User user) {
                mFollowings.add(user);
                if (mFollowings.size() == mNum_followers){
                    listener.onComplete(UserBusinessResult.SUCCESS);
                }
            }

            @Override
            public void onFindFollowingFailed(String error) {
                Log.d("Firebase", "Firebase:FindFollowError: " + error);
                mFollowings.clear();
                following_objects.clear();
                listener.onComplete(UserBusinessResult.FAILED);
            }
        });
    }

    public boolean addEvent(AppEvent event){
        return FirebaseHelper.addEvent(event);
    }

    public void removeEvent(String follow_id){
        FirebaseHelper.removeEvent(follow_id);
    }

    public void getMyEvents(final UserBusinessListener listener){
        FirebaseHelper.getEvent(mUser, new FirebaseHelper.FirebaseEventDelegate() {
            @Override
            public void onFindEventSuccess(List<UserEvent> events) {
                mEvents = events;
                listener.onComplete(UserBusinessResult.SUCCESS);
            }

            @Override
            public void onFindEventFailed(String error) {
                listener.onComplete(UserBusinessResult.FAILED);
            }
        });
    }

    int count = 0;

    public void getAllEvents(final UserBusinessListener listener){
        count = 0;
//        allEvents.addAll(mEvents);

        getFollowings(new UserBusinessListener() {
            @Override
            public void onComplete(UserBusinessResult result) {

                allEvents.clear();
                if (mFollowings.size() != 0){
                    for (int i = 0; i < mFollowings.size(); i ++){
                        User targetUser = mFollowings.get(i);
                        if (targetUser.allow_see_timeline){
                            FirebaseHelper.getEvent(mFollowings.get(i), new FirebaseHelper.FirebaseEventDelegate() {
                                @Override
                                public void onFindEventSuccess(List<UserEvent> events) {
                                    if (events != null){
                                        allEvents.addAll(events);
                                    }
                                    count ++;
                                    if (count == mFollowings.size()){
                                        listener.onComplete(UserBusinessResult.SUCCESS);
                                    }
                                }

                                @Override
                                public void onFindEventFailed(String error) {
                                    listener.onComplete(UserBusinessResult.FAILED);
                                }
                            });
                        }
                    }
                }else{
                    listener.onComplete(UserBusinessResult.FAILED);
                }
            }
        });
    }

    public void getToken(){
        mUser.token = FirebaseHelper.getToken();
        FirebaseHelper.changeInfoOfUser(mUser.fid, "token", mUser.token);
    }

    public void actionWhenReceiveMessage(Map<String, String> message){

    }

    public void getChatMessages(final UserBusinessListener listener){
        FirebaseHelper.getChatMessage(mUser.fid, new FirebaseHelper.FirebaseChatMessageDelegate() {
            @Override
            public void onFindChatMessageSuccess(List<ChatMessage> chatMessages) {
                mChatMessages = chatMessages;
                sortChatMessage();
                listener.onComplete(UserBusinessResult.SUCCESS);
            }

            @Override
            public void onFindUserMessageFailed(String error) {
                listener.onComplete(UserBusinessResult.FAILED);
            }
        });
    }

    private void sortChatMessage(){
        Collections.sort(mChatMessages, new Comparator<ChatMessage>() {
            @Override
            public int compare(ChatMessage o1, ChatMessage o2) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                try {
                    Date d1 = format.parse(o1.datetime);
                    Date d2 = format.parse(o2.datetime);
                    return d1.compareTo(d2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return 0;
            }
        });
    }

    public void postNewMessage(User receive_user, String content_message){
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.id_sender = mUser.fid;
        chatMessage.id_receiver = receive_user.fid;
        chatMessage.name_sender = mUser.fullname;
        chatMessage.name_receiver = receive_user.fullname;
        chatMessage.message = content_message;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        chatMessage.datetime = format.format(c.getTime());
        FirebaseHelper.createNewChatMessageInDb(chatMessage);
    }

    public void sendMessageToUser(final User to_user, final String content_message){
        Message message = new Message();
        Map<String, String> data = new HashMap<>();
        data.put("senderID", mUser.fid);
        data.put("kind", "message");
        data.put("message", content_message);

        message.setData(data);
        message.setTo(to_user.token);
        FirebaseHelper.sendMessage(message, new FirebaseHelper.FirebaseSendMessageDelegate() {
            @Override
            public void onSendMessageSuccess() {
                postNewMessage(to_user, content_message);
            }

            @Override
            public void onSendMessageFailed() {
            }
        });
    }

    public void sendRelationshipRequestToUser(final User to_user, final Relationship relationship){
        Message message = new Message();
        Map<String, String> data = new HashMap<>();
        data.put("senderID", mUser.fid);
        data.put("kind", "relationship");
        data.put("start_time", relationship.start_time);
        data.put("love_statement", relationship.love_statement);

        message.setData(data);
        message.setTo(to_user.token);
        FirebaseHelper.sendMessage(message, new FirebaseHelper.FirebaseSendMessageDelegate() {
            @Override
            public void onSendMessageSuccess() {

            }

            @Override
            public void onSendMessageFailed() {
            }
        });
    }

    public void sendRelationshipAcceptance(User to_user){
        Message message = new Message();
        Map<String, String> data = new HashMap<>();
        data.put("senderID", mUser.fid);
        data.put("kind", "relationship_acceptance");

        message.setData(data);
        message.setTo(to_user.token);
        FirebaseHelper.sendMessage(message, new FirebaseHelper.FirebaseSendMessageDelegate() {
            @Override
            public void onSendMessageSuccess() {

            }

            @Override
            public void onSendMessageFailed() {
            }
        });
    }

    public void sendRelationshipDenial(User to_user){
        Message message = new Message();
        Map<String, String> data = new HashMap<>();
        data.put("senderID", mUser.fid);
        data.put("kind", "relationship_denial");

        message.setData(data);
        message.setTo(to_user.token);
        FirebaseHelper.sendMessage(message, new FirebaseHelper.FirebaseSendMessageDelegate() {
            @Override
            public void onSendMessageSuccess() {

            }

            @Override
            public void onSendMessageFailed() {
            }
        });
    }

    public void sendDivorceRequestToUser(final User to_user){
        Message message = new Message();
        Map<String, String> data = new HashMap<>();
        data.put("senderID", mUser.fid);
        data.put("kind", "divorce");
        data.put("relationship_id", mRelationship.id);

        message.setData(data);
        message.setTo(to_user.token);
        FirebaseHelper.sendMessage(message, new FirebaseHelper.FirebaseSendMessageDelegate() {
            @Override
            public void onSendMessageSuccess() {

            }

            @Override
            public void onSendMessageFailed() {
            }
        });
    }

    public void sendDivorceAcceptance(User to_user){
        Message message = new Message();
        Map<String, String> data = new HashMap<>();
        data.put("senderID", mUser.fid);
        data.put("kind", "divorce_acceptance");

        message.setData(data);
        message.setTo(to_user.token);
        FirebaseHelper.sendMessage(message, new FirebaseHelper.FirebaseSendMessageDelegate() {
            @Override
            public void onSendMessageSuccess() {

            }

            @Override
            public void onSendMessageFailed() {
            }
        });
    }

    public void sendDivorceDenial(User to_user){
        Message message = new Message();
        Map<String, String> data = new HashMap<>();
        data.put("senderID", mUser.fid);
        data.put("kind", "divorce_denial");

        message.setData(data);
        message.setTo(to_user.token);
        FirebaseHelper.sendMessage(message, new FirebaseHelper.FirebaseSendMessageDelegate() {
            @Override
            public void onSendMessageSuccess() {

            }

            @Override
            public void onSendMessageFailed() {
            }
        });
    }

    public void findUserByName(String fullname){
        FirebaseHelper.findUserByName(fullname, new FirebaseHelper.FirebaseFindUserDelegate() {
            @Override
            public void onFindUserByNameSuccess(List<User> users) {

            }

            @Override
            public void onFindUserFailed() {
            }
        });
    }

    public interface UserBusinessListener {
        void onComplete(UserBusinessResult result);
    }
}
