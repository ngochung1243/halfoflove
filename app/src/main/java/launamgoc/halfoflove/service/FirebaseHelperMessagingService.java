package launamgoc.halfoflove.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import launamgoc.halfoflove.R;
import launamgoc.halfoflove.activity.ChatActivity;
import launamgoc.halfoflove.activity.DivorceActivity;
import launamgoc.halfoflove.activity.MainActivity;
import launamgoc.halfoflove.activity.RelationshipPreviewActivity;
import launamgoc.halfoflove.helper.FirebaseHelper;
import launamgoc.halfoflove.model.Message;
import launamgoc.halfoflove.model.MyBundle;
import launamgoc.halfoflove.model.Relationship;
import launamgoc.halfoflove.model.User;
import retrofit.http.Url;

import static android.R.attr.data;
import static android.R.id.message;
import static com.google.android.gms.internal.zzs.TAG;
import static launamgoc.halfoflove.activity.RelationshipPreviewActivity.relationship;

/**
 * Created by Admin on 1/9/2017.
 */

public class FirebaseHelperMessagingService extends FirebaseMessagingService{
    private static final String TAG = "FCM Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//            MyBundle.mUserBusiness.actionWhenReceiveMessage(remoteMessage.getData());
            final Map<String, String> data = remoteMessage.getData();
            if (data.get("kind").equals("message")){
                String sender_id = data.get("senderID");
                FirebaseHelper.findUser(sender_id, new FirebaseHelper.FirebaseUserDelegate() {
                    @Override
                    public void onFindUserSuccess(User user) {
                        ChatActivity.targetUser = user;
                        postMessageNotification(data.get("message"), user.fullname);
                    }

                    @Override
                    public void onFindUserFailed() {

                    }
                });
            }else if (data.get("kind").equals("relationship")){
                String sender_id = data.get("senderID");
                final Relationship relationship = new Relationship();
                relationship.start_time = data.get("start_time");
                relationship.love_statement = data.get("love_statement");
                relationship.id_request = sender_id;
                relationship.id_accept = MyBundle.mUserBusiness.mUser.fid;
                FirebaseHelper.findUser(sender_id, new FirebaseHelper.FirebaseUserDelegate() {
                    @Override
                    public void onFindUserSuccess(User user) {
                        postRelationshipNotification(user, relationship);
                    }

                    @Override
                    public void onFindUserFailed() {

                    }
                });
            }else if (data.get("kind").equals("divorce")){
                String sender_id = data.get("senderID");
                final String relationship_id = data.get("relationship_id");
                FirebaseHelper.findUser(sender_id, new FirebaseHelper.FirebaseUserDelegate() {
                    @Override
                    public void onFindUserSuccess(User user) {
                        postDivorceNotification(user, relationship_id);
                    }

                    @Override
                    public void onFindUserFailed() {

                    }
                });
            }else if (data.get("kind").equals("divorce_acceptance")){
                String sender_id = data.get("senderID");
                FirebaseHelper.findUser(sender_id, new FirebaseHelper.FirebaseUserDelegate() {
                    @Override
                    public void onFindUserSuccess(User user) {
                        postDivorceResponseNotification(user, true);
                    }

                    @Override
                    public void onFindUserFailed() {

                    }
                });
            }else if (data.get("kind").equals("divorce_denial")) {
                String sender_id = data.get("senderID");
                FirebaseHelper.findUser(sender_id, new FirebaseHelper.FirebaseUserDelegate() {
                    @Override
                    public void onFindUserSuccess(User user) {
                        postDivorceResponseNotification(user, false);
                    }

                    @Override
                    public void onFindUserFailed() {

                    }
                });
            }else if (data.get("kind").equals("relationship_acceptance")) {
                String sender_id = data.get("senderID");
                FirebaseHelper.findUser(sender_id, new FirebaseHelper.FirebaseUserDelegate() {
                    @Override
                    public void onFindUserSuccess(User user) {
                        postRelationshipResponseNotification(user, true);
                    }

                    @Override
                    public void onFindUserFailed() {

                    }
                });
            }else if (data.get("kind").equals("relationship_denial")) {
                String sender_id = data.get("senderID");
                FirebaseHelper.findUser(sender_id, new FirebaseHelper.FirebaseUserDelegate() {
                    @Override
                    public void onFindUserSuccess(User user) {
                        postRelationshipResponseNotification(user, false);
                    }

                    @Override
                    public void onFindUserFailed() {

                    }
                });
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void postMessageNotification(String message, String sender_name){

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_chat)
                        .setContentTitle(sender_name)
                        .setContentText(message)
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setAutoCancel(true);

        Intent resultIntent = new Intent(this, ChatActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(ChatActivity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1000, mBuilder.build());
    }

    private void postRelationshipNotification(User sender, Relationship relationship){
        RelationshipPreviewActivity.relationship = relationship;
        RelationshipPreviewActivity.sendRequestUser = sender;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_relationshiprequest)
                        .setContentTitle("Relationship Request!!!")
                        .setContentText(sender.fullname + " wants to being love with you")
                        .setSound(Settings.System.DEFAULT_ALARM_ALERT_URI)
                        .setAutoCancel(true);

        Intent resultIntent = new Intent(this, RelationshipPreviewActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(RelationshipPreviewActivity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1001, mBuilder.build());
    }

    private void postDivorceNotification(User sender, String relationship_id){

        DivorceActivity.senderUser = sender;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_divorce)
                        .setContentTitle("Divorce Request!!!")
                        .setContentText(sender.fullname + " wants to divorce you")
                        .setSound(Settings.System.DEFAULT_ALARM_ALERT_URI)
                        .setAutoCancel(true);

        Intent resultIntent = new Intent(this, DivorceActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(DivorceActivity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1002, mBuilder.build());
    }

    private void postDivorceResponseNotification(User sender, boolean accept){

        String title = "";
        String content = "";
        if (accept){
            title = "Divorce Acceptance!!!";
            content = " has accepted your divorce request";
        }else {
            title = "Divorce Denial!!!";
            content = " has denied your divorce request";
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_divorce)
                        .setContentTitle("Divorce Acceptance!!!")
                        .setContentText(sender.fullname + " has accepted your divorce request")
                        .setSound(Settings.System.DEFAULT_ALARM_ALERT_URI)
                        .setAutoCancel(true);

        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1003, mBuilder.build());
    }

    private void postRelationshipResponseNotification(User sender, boolean accept){

        String title = "";
        String content = "";
        if (accept){
            title = "Relationship Acceptance!!!";
            content = " has accepted your relationship request";
        }else {
            title = "Relationship Denial!!!";
            content = " has denied your relationship request";
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_relationshiprequest)
                        .setContentTitle(title)
                        .setContentText(sender.fullname + content)
                        .setSound(Settings.System.DEFAULT_ALARM_ALERT_URI)
                        .setAutoCancel(true);

        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1004, mBuilder.build());
    }
}
