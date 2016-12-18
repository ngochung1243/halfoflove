package launamgoc.halfoflove.helper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import launamgoc.halfoflove.model.AppEvent;
import launamgoc.halfoflove.model.Follow;
import launamgoc.halfoflove.model.Relationship;
import launamgoc.halfoflove.model.User;

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
                checkExitedUser(user);
            } else {
                // User is signed out
                Log.d("Firebase", "onAuthStateChanged:signed_out");
            }
            // ...
        }
    };
    public static FirebaseLoginHelperDelegate loginDelegate;
    public static FirebaseUserDelegate databaseDelegate;

    static public FirebaseHelperDelegate delegate;

    /**
     * Create new User with email and password
     *
     * @param email
     * @param password
     * @return
     */


    static public boolean createNewUser(String email, String password) {
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

    static private void checkExitedUser(final FirebaseUser fuser) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mapsrefrence = database.getReference().child(fuser.getUid());
        mapsrefrence.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User mUser = null;
                        if (dataSnapshot.exists()) {
                            Map<String, String> value = (Map<String, String>) dataSnapshot.getValue();
                            mUser = dataSnapshot.getValue(User.class);

                        }else {
                            mUser = createUserDatabase(fuser);
                        }
                        if (loginDelegate != null) {
                            loginDelegate.onLoginSuccess(mUser);
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
    static public void changeInfoOfUser(String fid, String changedValue, String changedData) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mapsrefrence = database.getReference().child("users").child(fid);
        mapsrefrence.child(changedValue).setValue(changedData);
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
//                            Map<String, String> value = (Map<String, String>) dataSnapshot.getValue();
                            User mUser = dataSnapshot.getValue(User.class);
                            if (userDelegate != null) {
                                userDelegate.onFindUserSuccess(mUser);
                            }
                        } else {
                            if (userDelegate != null) {
                                userDelegate.onFindUserFailed();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    /**
     * Upload an Image (add necessary parameter)
     *
     * @return
     */

    static public String uploadImage() {

        // change return when operate code
        return null;
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
        mDatabase.child("event").child(relationship_id).removeValue();
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
                    relationship.end_time = (String)value.get("end_time");
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
                                relationship.end_time = (String)value.get("end_time");
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

        Query query_follower = mapsrefrence.orderByChild("id_follower").equalTo(fid);
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

    /**
     * Adding New Feed when User adds an event (add necessary parameter)
     *
     * @return
     */
    static public boolean addNewFeed() {

        // change return when operate code
        return true;
    }

    /**
     * Chat with another User (add necessary parameter)
     *
     * @return
     */
    static public boolean chatToUser() {

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

    public interface FirebaseFollowerDelegate {
        void onFindNumFollower(int num_follower);
        void onFindFollowerSuccess(User user);
        void onFindFollowerFailed(String error);
    }

    public interface FirebaseRelationshipDelegate{
        void onFindRelationshipSuccess(Relationship relationship, User partner);
        void onFindRelationshipFailed(String error);
    }

    public interface FirebaseHelperDelegate {
        void onCreateNewAccountSuccess();

        void onCreateNewAccountFailed();
    }

}


