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
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import launamgoc.halfoflove.model.User;

/**
 * Created by Admin on 11/21/2016.
 */

public class FirebaseHelper {

    public static  FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                Log.d("Firebase", "onAuthStateChanged:signed_in:" + user.getUid());
                if (loginDelegate != null){
                    loginDelegate.onLoginSuccess();
                }
            } else {
                // User is signed out
                Log.d("Firebase", "onAuthStateChanged:signed_out");
            }
            // ...
        }
    };
    public static FirebaseLoginHelperDelegate loginDelegate;
    public static FirebaseDatabaseHelperDelegate databaseDelegate;


    /**
     * Create new User with email and password
     * @param email
     * @param password
     * @return
     */
    static public boolean createNewUser(String email, String password){

        // change return when operate code
        return true;
    }

    /**
     * Login user with email and password
     * @param email
     * @param password
     * @return
     */
    static public boolean loginWithUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (loginDelegate != null){
                                loginDelegate.onLoginSuccess();
                            }
                        }
                        else {
                            if (loginDelegate != null) {
                                loginDelegate.onLoginFailed();
                            }
                        }
                    }
                });

        // change return when operate code
        return true;
    }

    /**
     * Login user when login social (add necessary parameter)
     * @return
     */
    static public User loginWithSocial(AuthCredential credential){

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Firebase", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("Firebase", "signInWithCredential", task.getException());
                            if(loginDelegate != null){
                                loginDelegate.onLoginFailed();
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
     * @return
     */
    static public boolean renewPassword(){

        // change return when operate code
        return true;
    }

    /**
     * Change Infomation of User (add necessary parameter)
     * @return
     */
    static public void changeInfoOfUser(String changedValue, String changedData){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mapsrefrence = database.getReference().child("user1");
        mapsrefrence.child(changedValue).setValue(changedData);
    }

    /**
     * Find an User (add necessary parameter)
     * @return
     */
    static public void findUser(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mapsrefrence = database.getReference().child("user1");
        mapsrefrence.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChildren()) {
                            Map<String, String> value = (Map<String, String>) dataSnapshot.getValue();
                            if (databaseDelegate != null){
                                databaseDelegate.onFindUserSuccess(new User(value));
                            }
                        }
                        else {
                            if(databaseDelegate != null) {
                                databaseDelegate.onFindUserFailed();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    /**
     * Upload an Image (add necessary parameter)
     * @return
     */
    static public String uploadImage(){

        // change return when operate code
        return null;
    }

    /**
     * Adding an Event when User setup (add necessary parameter)
     * @return
     */
    static public boolean addEventWithUser(){

        // change return when operate code
        return true;
    }

    /**
     * Addind a Relationship between 2 Users (add necessary parameter)
     * @return
     */
    static public boolean addRelationship(){

        // change return when operate code
        return true;
    }

    /**
     * Removing a Relationship between 2 Users (add necessary parameter)
     * @return
     */
    static public boolean removeRelationship(){

        // change return when operate code
        return true;
    }

    /**
     * Adding follow when an User want to follow another user (add necessary parameter)
     * @return
     */
    static public boolean addFollow(){

        // change return when operate code
        return true;
    }

    /**
     * Remove follow when an User don't want to follow another user anymore (add necessary parameter)
     * @return
     */
    static public boolean removeFollow(){

        // change return when operate code
        return true;
    }

    /**
     * Adding New Feed when User adds an event (add necessary parameter)
     * @return
     */
    static public boolean addNewFeed(){

        // change return when operate code
        return true;
    }

    /**
     * Chat with another User (add necessary parameter)
     * @return
     */
    static public boolean chatToUser(){

        // change return when operate code
        return true;
    }

    public interface FirebaseLoginHelperDelegate {
        void onLoginSuccess();
        void onLoginFailed();
        void onLogoutSuccess();
        void onLogoutFailed();
    }

    public interface FirebaseDatabaseHelperDelegate {
        void onFindUserSuccess(User user);
        void onFindUserFailed();
    }
}
