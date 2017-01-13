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
import launamgoc.halfoflove.activity.MainActivity;
import launamgoc.halfoflove.helper.FirebaseHelper;
import launamgoc.halfoflove.model.Message;
import launamgoc.halfoflove.model.MyBundle;
import launamgoc.halfoflove.model.User;
import retrofit.http.Url;

import static android.R.attr.data;
import static android.R.id.message;
import static com.google.android.gms.internal.zzs.TAG;

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
            String sender_id = data.get("senderID");
            FirebaseHelper.findUser(sender_id, new FirebaseHelper.FirebaseUserDelegate() {
                @Override
                public void onFindUserSuccess(User user) {
                    postNotification(data.get("message"), user.fullname);
                }

                @Override
                public void onFindUserFailed() {

                }
            });

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void postNotification(String message, String sender_name){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_chat)
                        .setContentTitle(sender_name)
                        .setContentText(message)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, ChatActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(1000, mBuilder.build());
    }

    private class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... photo_urls) {
            URL ava_url = null;
            try {
                Bitmap ava_bmp = null;
                if (photo_urls[0] != null && !photo_urls[0].equals("")){
                    ava_url = new URL(photo_urls[0]);
                    ava_bmp = BitmapFactory.decodeStream(ava_url.openConnection().getInputStream());
                }

                return ava_bmp;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap == null){
                bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
            }
        }
    }
}
