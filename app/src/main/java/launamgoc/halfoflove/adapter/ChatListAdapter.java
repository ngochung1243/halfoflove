package launamgoc.halfoflove.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import launamgoc.halfoflove.R;
import launamgoc.halfoflove.activity.ChatListTabActivity;
import launamgoc.halfoflove.helper.FirebaseHelper;
import launamgoc.halfoflove.model.ChatElement;
import launamgoc.halfoflove.model.ChatMessage;
import launamgoc.halfoflove.model.MyBundle;
import launamgoc.halfoflove.model.NewFeedElement;
import launamgoc.halfoflove.model.User;

import static android.R.attr.data;

/**
 * Created by KhaTran on 12/14/2016.
 */

public class ChatListAdapter extends
        RecyclerView.Adapter<ChatListAdapter.ChatListHolder>{
    private List<String> chat_user_ids = new ArrayList<String>();

    @Override
    public int getItemCount() {
        return chat_user_ids.size();
    }

    @Override
    public ChatListAdapter.ChatListHolder onCreateViewHolder(ViewGroup viewGroup,
                                                                 int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater
                .inflate(R.layout.cardview_chatlist, viewGroup, false);
        return new ChatListAdapter.ChatListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatListAdapter.ChatListHolder viewHolder, int position) {
        String userId = chat_user_ids.get(position);
        viewHolder.load(userId);
    }

    public void setUserIds(List<String> userIds){
        this.chat_user_ids = userIds;
        notifyDataSetChanged();
    }

    public class ChatListHolder extends RecyclerView.ViewHolder {

        public CardView cvChat;
        public ImageView ivAvatar;
        public TextView tvName;

        public ChatListHolder(View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.cv_chat_ava);
            tvName = (TextView) itemView.findViewById(R.id.cv_chat_name);
            cvChat = (CardView) itemView.findViewById(R.id.cv_chat);
        }

        public void load(@NonNull final String userId) {
            FirebaseHelper.findUser(userId, new FirebaseHelper.FirebaseUserDelegate() {
                @Override
                public void onFindUserSuccess(final User user) {
                    Handler hd = new Handler(Looper.getMainLooper());
                    hd.post(new Runnable() {
                        @Override
                        public void run() {
                            ivAvatar.setScaleType(ImageView.ScaleType.FIT_XY);
                            tvName.setText(user.fullname);

                            cvChat.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ChatListTabActivity activity = (ChatListTabActivity) view.getContext();
                                    activity.onItemClick(user);
                                }
                            });

                            new DownloadImageAsyncTask().execute(user.photo_url);
                        }
                    });
                }

                @Override
                public void onFindUserFailed() {

                }
            });

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
                if (bitmap != null){
                    ivAvatar.setImageBitmap(bitmap);
                    ivAvatar.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }
        }
    }
}
