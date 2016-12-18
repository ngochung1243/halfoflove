package launamgoc.halfoflove.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import launamgoc.halfoflove.helper.FirebaseHelper;

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
    public int mNum_followers = 0;

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

    public void addEvent(AppEvent event){
        FirebaseHelper.addEvent(event);
    }

    public void removeEvent(String follow_id){
        FirebaseHelper.removeEvent(follow_id);
    }

    public void getMyEvents(UserBusinessListener listener){

    }

    public void getAllEvents(){
        
    }

    public interface UserBusinessListener {
        void onComplete(UserBusinessResult result);
    }
}
