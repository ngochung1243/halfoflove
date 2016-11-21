package launamgoc.halfoflove.helper;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import launamgoc.halfoflove.activity.RegisterActivity;
import launamgoc.halfoflove.model.User;

/**
 * Created by Admin on 11/21/2016.
 */

public class FirebaseHelper {

    static public FirebaseHelperDelegate delegate;
    static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    /**
     * Create new User with email and password
     * @param email
     * @param password
     * @return
     */
    static public boolean createNewUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (delegate != null){
                                delegate.onCreateNewAccountSuccess();
                            }
                        }
                        else {
                            if (delegate != null) {
                                delegate.onCreateNewAccountFailed();
                            }
                        }
                    }
                });
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

        // change return when operate code
        return null;
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

    public interface FirebaseHelperDelegate{
        void onCreateNewAccountSuccess();
        void onCreateNewAccountFailed();
    }
}


