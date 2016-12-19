package launamgoc.halfoflove.model;

import android.util.Log;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

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
    public User pUser = null;
    public List<User> mFollowers = new ArrayList<>();
    public List<User> mFollowings = new ArrayList<>();
    public int mNum_followers = 0;
    public List<UserEvent>mEvents = new ArrayList<>();
    public List<UserEvent>allEvents = new ArrayList<>();

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
            public void onFindFollowingSuccess(User user) {
                mFollowings.add(user);
                if (mFollowings.size() == mNum_followers){
                    listener.onComplete(UserBusinessResult.SUCCESS);
                }
            }

            @Override
            public void onFindFollowingFailed(String error) {
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
        allEvents.clear();
//        allEvents.addAll(mEvents);

        getFollowings(new UserBusinessListener() {
            @Override
            public void onComplete(UserBusinessResult result) {
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
                    listener.onComplete(UserBusinessResult.SUCCESS);
                }
            }
        });
    }

    public interface UserBusinessListener {
        void onComplete(UserBusinessResult result);
    }
}
