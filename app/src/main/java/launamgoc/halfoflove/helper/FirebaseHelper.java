package launamgoc.halfoflove.helper;

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
    static public User loginWithUser(String email, String password){

        // change return when operate code
        return null;
    }

    /**
     * Login user when login social (add necessary parameter)
     * @return
     */
    static public User loginWithSocial(){

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
    static public boolean changeInfoOfUser(){

        // change return when operate code
        return true;
    }

    /**
     * Find an User (add necessary parameter)
     * @return
     */
    static public User findUser(){

        final User[] user = new User[1];

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mapsrefrence = database.getReference().child("user1");
        mapsrefrence.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChildren()) {
                            Map<String, String> value = (Map<String, String>) dataSnapshot.getValue();
                            user[0] = setUserValue(value);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        return user[0];
    }

    static private User setUserValue(Map<String, String> value) {
        User tmp = new User();

        tmp.fullname = value.get("fullname");
        tmp.mood = value.get("mood");
        tmp.mobile = value.get("mobile");
        tmp.location = value.get("location");
        tmp.bio = value.get("bio");
        tmp.email = value.get("email");
        tmp.birthday = value.get("birthday");
        tmp.gender = value.get("gender");
        tmp.hobby = value.get("hobby");

        return tmp;
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
}
