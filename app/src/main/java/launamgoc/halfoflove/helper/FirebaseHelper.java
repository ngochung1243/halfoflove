package launamgoc.halfoflove.helper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import launamgoc.halfoflove.model.AppEvent;
import launamgoc.halfoflove.model.ChatMessage;
import launamgoc.halfoflove.model.Follow;
import launamgoc.halfoflove.model.Message;
import launamgoc.halfoflove.model.MessageResponse;
import launamgoc.halfoflove.model.Relationship;
import launamgoc.halfoflove.model.User;
import launamgoc.halfoflove.model.UserEvent;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static android.R.attr.key;

/**
 * Created by Admin on 11/21/2016.
 */

public class FirebaseHelper {
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d("Firebase", "onAuthStateChanged:signed_in:" + user.getUid());
                checkExitedUser(user, loginDelegate);
            } else {
                // User is signed out
                Log.d("Firebase", "onAuthStateChanged:signed_out");
            }
            // ...
        }
    };

    static public FirebaseHelperDelegate delegate;
    static public FirebaseLoginHelperDelegate loginDelegate;

    static private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://fcm.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    static private FirebaseAPIHelper apiHelper = retrofit.create(FirebaseAPIHelper.class);

    /**
     * Create new User with email and password
     *
     * @param email
     * @param password
     * @return
     */


    static public boolean createNewUser(String email, String password, final FirebaseHelperDelegate delegate) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (delegate != null) {
                                delegate.onCreateNewAccountSuccess();
                            }
                        } else {
                            if (delegate != null) {
                                delegate.onCreateNewAccountFailed();
                            }
                        }
                    }
                });
        return true;
    }

    static public void checkExitedUser(final FirebaseUser fuser, final FirebaseLoginHelperDelegate delegate) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mapsrefrence = database.getReference().child("users").child(fuser.getUid());
        mapsrefrence.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User mUser = null;
                        if (dataSnapshot.hasChildren()) {
                            mUser = dataSnapshot.getValue(User.class);

                        }else {
                            mUser = createUserDatabase(fuser);
                        }
                        if (delegate != null) {
                            delegate.onLoginSuccess(mUser);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    static private User createUserDatabase(FirebaseUser fuser){
        User user = new User();
        user.fid = fuser.getUid();
        if (fuser.getDisplayName() != null){
            user.fullname = fuser.getDisplayName();
        }
        user.email = fuser.getEmail();

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(fuser.getUid()).setValue(user);

        return user;
    }
    /**
     * Login user with email and password
     *
     * @param email
     * @param password
     * @return
     */
    static public boolean loginWithUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            if (loginDelegate != null) {
                                loginDelegate.onLoginFailed(task.getException());
                            }
                        }
                    }
                });

        // change return when operate code
        return true;
    }

    /**
     * Login user when login social (add necessary parameter)
     *
     * @return
     */
    static public User loginWithSocial(final AuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Firebase", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("Firebase", "signInWithCredential:onFailed", task.getException());
                            if (loginDelegate != null) {
                                loginDelegate.onLoginFailed(task.getException());
                            }
                        }
                        // ...
                    }
                });
        // change return when operate code
        return null;
    }

    /**
     * Renew Password when User forget (add necessary parameter)
     *
     * @return
     */
    static public boolean renewPassword() {

        // change return when operate code
        return true;
    }

    /**
     * Change Infomation of User (add necessary parameter)
     *
     * @return
     */
    static public void changeInfoOfUser(String fid, String changedValue, Object changedData) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mapsrefrence = database.getReference().child("users").child(fid);
        mapsrefrence.child(changedValue).setValue(changedData);
    }

    static public void getAllUser(final FirebaseGetAllUserDelegate getAllUserDelegate){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mapsrefrence = database.getReference().child("users");
        mapsrefrence.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    List<User> users = new ArrayList<User>();
                    Map snapshot = (Map)dataSnapshot.getValue();
                    snapshot.keySet();
                    for (Object entry : snapshot.entrySet())
                    {
                        Entry<String, Map<String, Object>> entry1 = (Entry<String, Map<String, Object>>)entry;
                        Map<String, Object> user_map = entry1.getValue();
                        User user = new User();
                        user.fid = (String)user_map.get("fid");
                        user.fullname = (String)user_map.get("fullname");
                        user.birthday = (String)user_map.get("birthday");
                        user.bio = (String)user_map.get("bio");
                        user.hobby = (String)user_map.get("hobby");
                        user.mobile = (String)user_map.get("mobile");
                        user.email = (String)user_map.get("email");
                        user.gender = (String)user_map.get("gender");
                        user.interested = (String)user_map.get("interested");
                        user.allow_find = (boolean)user_map.get("allow_find");
                        user.allow_see_timeline = (boolean)user_map.get("allow_see_timeline");
                        user.photo_url = (String)user_map.get("photo_url");
                        user.cover_url = (String)user_map.get("cover_url");
                        user.mood = (String)user_map.get("mood");
                        user.location = (String)user_map.get("location");
                        user.token = (String)user_map.get("token");

                        users.add(user);
                    }

                    getAllUserDelegate.onGetAllUserSuccess(users);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                getAllUserDelegate.onGetAllUserFailed();
            }
        });
    }


    /**
     * Find an User (add necessary parameter)
     *
     * @return
     */
    static public void findUser(String fid, final FirebaseUserDelegate userDelegate) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mapsrefrence = database.getReference().child("users").child(fid);
        mapsrefrence.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChildren()) {
                            User mUser = dataSnapshot.getValue(User.class);
                            if (mUser.allow_find){
                                userDelegate.onFindUserSuccess(mUser);
                            }else {
                                userDelegate.onFindUserSuccess(null);
                            }
                        } else {
                            userDelegate.onFindUserSuccess(null);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        userDelegate.onFindUserFailed();
                    }
                }
        );
    }

    /**
     * Upload an Image (add necessary parameter)
     *
     * @return
     */

    static public void uploadImage(final String fid, final String title, final byte[] data, final FirebaseUploadImagepDelegate uploadDelegate) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child(title + "/" + fid + ".jpg");
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                uploadDelegate.onUploadImageFailed(exception.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                changeInfoOfUser(fid, title, downloadUrl);
                uploadDelegate.onUploadImageSuccess(downloadUrl);
            }
        });
    }

    /**
     * Adding an AppEvent when User setup (add necessary parameter)
     *
     * @return
     */
    static public boolean addEvent(AppEvent event) {

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("event").child(event.id).setValue(event);
        // change return when operate code
        return true;
    }

    static public boolean removeEvent(String event_id) {

        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("event").child(event_id).removeValue();
        // change return when operate code
        return true;
    }

    static public void getEvent(final User user, final FirebaseEventDelegate eventDelegate){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mapsrefrence = database.getReference().child("event");
        Query query_event = mapsrefrence.orderByChild("fid").equalTo(user.fid);
        query_event.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<UserEvent>events = new ArrayList<UserEvent>();
                Map snapshot = (Map)dataSnapshot.getValue();
                if (snapshot != null){
                    for (int i = 0; i < snapshot.size();i ++){
                        final AppEvent event = new AppEvent();
                        event.id = (String)snapshot.keySet().toArray()[i];
                        Map value = (Map)snapshot.get(event.id);
                        event.fid = user.fid;
                        event.name = (String)value.get("name");
                        event.description = (String)value.get("description");
                        event.start_time = (String)value.get("start_time");
                        event.end_time = (String)value.get("end_time");
                        event.post_time = (String)value.get("post_time");
                        event.photo_url = (String)value.get("photo_url");
                        UserEvent u_event = new UserEvent(user, event);
                        events.add(u_event);
                    }
                    eventDelegate.onFindEventSuccess(events);
                }else {
                    eventDelegate.onFindEventSuccess(new ArrayList<UserEvent>());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                eventDelegate.onFindEventFailed(databaseError.getMessage());
            }
        });
    }


    static public void findUserByName(final String fullname, final FirebaseFindUserDelegate findUserDelegate){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mapsrefrence = database.getReference().child("users");
        Query query_event = mapsrefrence.orderByChild("fullname").equalTo(fullname);
        query_event.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<User>();
                Map snapshot = (Map)dataSnapshot.getValue();
                if (snapshot != null){
                    for (int i = 0; i < snapshot.size();i ++){
                        final User user = new User();
                        user.fid = (String)snapshot.keySet().toArray()[i];
                        Map value = (Map)snapshot.get(user.fid);
                        user.allow_find = (Boolean)value.get("allow_find");
                        if(user.allow_find){
                            user.fullname = (String)value.get("fullname");
                            user.birthday = (String)value.get("birthday");
                            user.photo_url = (String)value.get("photo_url");
                            user.cover_url = (String)value.get("cover_url");
                            user.gender = (String)value.get("gender");
                            user.location = (String)value.get("location");
                            user.mobile = (String)value.get("mobile");
                            user.email = (String)value.get("email");
                            user.hobby = (String)value.get("hobby");
                            user.bio = (String)value.get("bio");
                            user.mood = (String)value.get("mood");
                            user.interested = (String)value.get("interested");
                            user.allow_see_timeline = (Boolean)value.get("allow_see_timeline");
                            user.token = (String)value.get("token");
                            users.add(user);
                        }

                    }
                    findUserDelegate.onFindUserByNameSuccess(users);
                }else {
                    findUserDelegate.onFindUserByNameSuccess(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                findUserDelegate.onFindUserFailed();
            }
        });
    }

    /**
     * Addind a Relationship between 2 Users (add necessary parameter)
     *
     * @return
     */
    static public boolean addRelationship(Relationship relationship) {
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("relationship").child(relationship.id).setValue(relationship);
        // change return when operate code
        return true;
    }

    /**
     * Removing a Relationship between 2 Users (add necessary parameter)
     *
     * @return
     */
    static public boolean removeRelationship(String relationship_id) {
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("relationship").child(relationship_id).removeValue();
        // change return when operate code
        return true;
    }

    static public void findRelationship(final String fid, final FirebaseRelationshipDelegate relationshipDelegate){

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mapsrefrence = database.getReference().child("relationship");
        Query query_relationship1 = mapsrefrence.orderByChild("id_request").equalTo(fid);
        query_relationship1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map snapshot = (HashMap)dataSnapshot.getValue();
                if (snapshot != null){
                    final Relationship relationship = new Relationship();
                    relationship.id = (String)snapshot.keySet().toArray()[0];
                    Map value = (Map)snapshot.get(relationship.id);
                    relationship.id_request = fid;
                    relationship.id_accept = (String)value.get("id_accept");
                    relationship.start_time = (String)value.get("start_time");
                    relationship.love_statement = (String)value.get("love_statement");
                    findUser(relationship.id_accept, new FirebaseUserDelegate() {
                        @Override
                        public void onFindUserSuccess(User user) {
                            relationshipDelegate.onFindRelationshipSuccess(relationship, user);
                        }

                        @Override
                        public void onFindUserFailed() {

                        }
                    });

                }else {
                    Query query_relationship2 = mapsrefrence.orderByChild("id_accept").equalTo(fid);
                    query_relationship2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map snapshot = (HashMap)dataSnapshot.getValue();
                            if (snapshot != null){
                                final Relationship relationship = new Relationship();
                                relationship.id = (String)snapshot.keySet().toArray()[0];
                                Map value = (Map)snapshot.get(relationship.id);
                                relationship.id_request = (String)value.get("id_request");
                                relationship.id_accept = fid;
                                relationship.start_time = (String)value.get("start_time");
                                relationship.love_statement = (String)value.get("love_statement");
                                findUser(relationship.id_request, new FirebaseUserDelegate() {
                                    @Override
                                    public void onFindUserSuccess(User user) {
                                        relationshipDelegate.onFindRelationshipSuccess(relationship, user);
                                    }

                                    @Override
                                    public void onFindUserFailed() {

                                    }
                                });

                            }else {
                                relationshipDelegate.onFindRelationshipFailed("Can't find anything");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            relationshipDelegate.onFindRelationshipFailed(databaseError.getMessage());
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                relationshipDelegate.onFindRelationshipFailed(databaseError.getMessage());
            }
        });
    }

    /**
     * Adding follow when an User want to follow another user (add necessary parameter)
     *
     * @return
     */
    static public boolean addFollow(Follow follow) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mapsrefrence = database.getReference().child("follow");
        mapsrefrence.child(follow.id).setValue(follow);
        // change return when operate code
        return true;
    }

    /**
     * Remove follow when an User don't want to follow another user anymore (add necessary parameter)
     *
     * @return
     */
    static public boolean removeFollow(String follow_id) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mapsrefrence = database.getReference().child("follow");
        mapsrefrence.child(follow_id).removeValue();
        // change return when operate code
        return true;
    }

    static public void findFollower(String fid, final FirebaseFollowerDelegate followDelegate){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mapsrefrence = database.getReference().child("follow");

        Query query_follower = mapsrefrence.orderByChild("id_following").equalTo(fid);
        query_follower.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Map snapshot = (HashMap)dataSnapshot.getValue();
                if (snapshot != null){
                    final List<User>follow_users = new ArrayList<User>();
                    followDelegate.onFindNumFollower(snapshot.size());

                    for (int i = 0; i < snapshot.size(); i ++){
                        String key = snapshot.keySet().toArray()[i].toString();
                        Map value = (Map) snapshot.get(key);
                        String id_follower =  (String)value.get("id_follower");
                        findUser(id_follower, new FirebaseUserDelegate() {
                            @Override
                            public void onFindUserSuccess(User user) {
                                followDelegate.onFindFollowerSuccess(user);
                            }

                            @Override
                            public void onFindUserFailed() {
                            }
                        });
                    }
                }else {
                    followDelegate.onFindFollowerFailed("Can't find anything");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                followDelegate.onFindFollowerFailed(databaseError.getMessage());
            }
        });
    }

    static public void findFollowings(String fid, final FirebaseFollowingDelegate followDelegate){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mapsrefrence = database.getReference().child("follow");

        Query query_following = mapsrefrence.orderByChild("id_follower").equalTo(fid);
        query_following.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Map snapshot = (HashMap)dataSnapshot.getValue();
                if (snapshot != null){
                    final List<User>follow_users = new ArrayList<User>();
                    final List<Follow> followings = new ArrayList<Follow>();
                    followDelegate.onFindNumFollowing(snapshot.size());

                    for (int i = 0; i < snapshot.size(); i ++){
                        Follow follow = new Follow();
                        String key = snapshot.keySet().toArray()[i].toString();
                        follow.id = key;
                        Map value = (Map) snapshot.get(key);
                        String id_following =  (String)value.get("id_following");
                        follow.id_following = id_following;
                        follow.id_follower = (String)value.get("id_follower");

                        followings.add(follow);

                        findUser(id_following, new FirebaseUserDelegate() {
                            @Override
                            public void onFindUserSuccess(User user) {
                                followDelegate.onFindFollowingSuccess(user);
                            }

                            @Override
                            public void onFindUserFailed() {
                            }
                        });
                    }

                    followDelegate.onFindFollowingObjectSuccess(followings);

                }else {
                    followDelegate.onFindFollowingFailed("Can't find anything");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                followDelegate.onFindFollowingFailed(databaseError.getMessage());
            }
        });
    }

    static public void getChatMessage(final String userId, final FirebaseChatMessageDelegate chatMessageDelegate){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mapsrefrence = database.getReference().child("chat_message");

        final Query query_chat_sender = mapsrefrence.orderByChild("id_sender").equalTo(userId);
        query_chat_sender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                final List<ChatMessage> chatMessages = new ArrayList<ChatMessage>();
                Map snapshot = (Map)dataSnapshot.getValue();
                if (snapshot != null){
                    for (int i = 0; i < snapshot.size();i ++){
                        final ChatMessage chatMessage = new ChatMessage();
                        chatMessage.id = (String)snapshot.keySet().toArray()[i];
                        Map value = (Map)snapshot.get(chatMessage.id);
                        chatMessage.id_sender = (String)value.get("id_sender");
                        chatMessage.id_receiver = (String)value.get("id_receiver");
                        chatMessage.id_receiver = (String)value.get("id_receiver");
                        chatMessage.name_sender = (String)value.get("name_sender");
                        chatMessage.name_receiver = (String)value.get("name_receiver");
                        chatMessage.message = (String)value.get("message");
                        chatMessage.datetime = (String)value.get("datetime");
                        chatMessages.add(chatMessage);
                    }

                }
                    Query query_chat_receiver = mapsrefrence.orderByChild("id_receiver").equalTo(userId);
                    query_chat_receiver.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {

                            Map snapshot = (Map)dataSnapshot.getValue();
                            if (snapshot != null) {
                                for (int i = 0; i < snapshot.size(); i++) {
                                    final ChatMessage chatMessage = new ChatMessage();
                                    chatMessage.id = (String) snapshot.keySet().toArray()[i];
                                    Map value = (Map) snapshot.get(chatMessage.id);
                                    chatMessage.id_sender = (String) value.get("id_sender");
                                    chatMessage.id_receiver = (String) value.get("id_receiver");
                                    chatMessage.id_receiver = (String) value.get("id_receiver");
                                    chatMessage.name_sender = (String) value.get("name_sender");
                                    chatMessage.name_receiver = (String) value.get("name_receiver");
                                    chatMessage.message = (String) value.get("message");
                                    chatMessage.datetime = (String) value.get("datetime");
                                    chatMessages.add(chatMessage);
                                }
                            }

                            chatMessageDelegate.onFindChatMessageSuccess(chatMessages);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            chatMessageDelegate.onFindUserMessageFailed(databaseError.getMessage());
                        }
                    });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                chatMessageDelegate.onFindUserMessageFailed(databaseError.getMessage());
            }
        });
    }

    static public String getToken() {
        String token = FirebaseInstanceId.getInstance().getToken();
        return token;
    }

    static public void sendRequestRelationshipToUser(User user_to){

    }

    static public void createNewChatMessageInDb(ChatMessage chatMessage){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mapsrefrence = database.getReference().child("chat_message");
        mapsrefrence.child(chatMessage.id).setValue(chatMessage);
    }

    /**
     * Chat with another User (add necessary parameter)
     *
     * @return
     */
    static public boolean sendMessage(Message message, final FirebaseSendMessageDelegate sendMessageDelegate) {
        Call<MessageResponse> call = apiHelper.sendMessage(message);
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Response<MessageResponse> response, Retrofit retrofit) {
                Log.d("Respone", response.toString());
                sendMessageDelegate.onSendMessageSuccess();
            }

            @Override
            public void onFailure(Throwable t) {
                sendMessageDelegate.onSendMessageFailed();
            }
        });
        // change return when operate code
        return true;
    }

    public interface FirebaseLoginHelperDelegate {
        void onLoginSuccess(User user);

        void onLoginFailed(Exception ex);

        void onLogoutSuccess();

        void onLogoutFailed(Exception ex);
    }

    public interface FirebaseUserDelegate {
        void onFindUserSuccess(User user);

        void onFindUserFailed();
    }

    public interface FirebaseGetAllUserDelegate{
        void onGetAllUserSuccess(List<User> users);
        void onGetAllUserFailed();
    }

    public interface FirebaseFindUserDelegate{
        void onFindUserByNameSuccess(List<User> users);
        void onFindUserFailed();
    }

    public interface FirebaseEventDelegate{
        void onFindEventSuccess(List<UserEvent>events);
        void onFindEventFailed(String error);
    }

    public interface FirebaseFollowerDelegate {
        void onFindNumFollower(int num_follower);
        void onFindFollowerSuccess(User user);
        void onFindFollowerFailed(String error);
    }

    public interface FirebaseFollowingDelegate {
        void onFindNumFollowing(int num_follower);
        void onFindFollowingObjectSuccess(List<Follow> followings);
        void onFindFollowingSuccess(User user);
        void onFindFollowingFailed(String error);
    }

    public interface FirebaseRelationshipDelegate{
        void onFindRelationshipSuccess(Relationship relationship, User partner);
        void onFindRelationshipFailed(String error);
    }

    public interface FirebaseUploadImagepDelegate {
        void onUploadImageSuccess(String imageUrl);

        void onUploadImageFailed(String error);
    }

    public interface FirebaseChatMessageDelegate{
        void onFindChatMessageSuccess(List<ChatMessage> chatMessages);
        void onFindUserMessageFailed(String error);
    }

    public interface FirebaseCreateNewChatMessageDelegate{
        void onCreateNewChatMessageSuccess();
        void onCreateNewChatMessageFailed();
    }

    public interface FirebaseSendMessageDelegate{
        void onSendMessageSuccess();
        void onSendMessageFailed();
    }

    public interface FirebaseHelperDelegate {
        void onCreateNewAccountSuccess();

        void onCreateNewAccountFailed();
    }

}


