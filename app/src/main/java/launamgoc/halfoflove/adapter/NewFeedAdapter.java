package launamgoc.halfoflove.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import launamgoc.halfoflove.R;
import launamgoc.halfoflove.model.AppEvent;
import launamgoc.halfoflove.model.NewFeedElement;
import launamgoc.halfoflove.model.User;
import launamgoc.halfoflove.model.UserEvent;

/**
 * Created by KhaTran on 12/14/2016.
 */

public class NewFeedAdapter  extends
        RecyclerView.Adapter<NewFeedAdapter.NewFeedHolder>{
    private List<UserEvent> userEvents = new ArrayList<UserEvent>();

    public NewFeedAdapter(List<UserEvent> userEvents) {
        this.userEvents = userEvents;
    }

    @Override
    public int getItemCount() {
        return userEvents.size();
    }

    @Override
    public NewFeedAdapter.NewFeedHolder onCreateViewHolder(ViewGroup viewGroup,
                                                             int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater
                .inflate(R.layout.cardview_newfeed, viewGroup, false);
        return new NewFeedAdapter.NewFeedHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewFeedAdapter.NewFeedHolder viewHolder, int position) {
        UserEvent data = userEvents.get(position);
        viewHolder.load(data);
    }

    public void setUserEvents(List<UserEvent> userEvents){
        this.userEvents = userEvents;
        notifyDataSetChanged();
    }

    public void clear(){
        userEvents.clear();
        notifyDataSetChanged();
    }

    public class NewFeedHolder extends RecyclerView.ViewHolder {

        public ImageView ivAvatar;
        public TextView tvName;
        public TextView tvPostTime;
        public TextView tvStartTime;
        public TextView tvEndTime;
        public TextView tvEventName;
        public TextView tvDescription;
        public ImageView ivImage;
        public VideoView vvContent;

        public NewFeedHolder(View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.cv_nf_ava);
            tvName = (TextView) itemView.findViewById(R.id.cv_nf_name);
            tvPostTime = (TextView) itemView.findViewById(R.id.tvPostTime);
            tvStartTime = (TextView) itemView.findViewById(R.id.cv_nf_starttime);
            tvEndTime = (TextView) itemView.findViewById(R.id.cv_nf_endtime);
            tvEventName = (TextView) itemView.findViewById(R.id.tvEventName);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            ivImage = (ImageView) itemView.findViewById(R.id.cv_nf_image);
            vvContent = (VideoView) itemView.findViewById(R.id.video);
        }

        public void load(@NonNull final UserEvent data) {
            User user = data.user;
            AppEvent event = data.event;
            tvName.setText(user.fullname);
            tvPostTime.setText(event.post_time);
            tvStartTime.setText(event.start_time);
            tvEndTime.setText(event.end_time);
            tvEventName.setText(event.name);
            tvDescription.setText(event.description);

            new DownloadImageAsyncTask().execute(data);

            if(event.video_url != null && !event.video_url.equals("")) {
                String uriPath = event.video_url;
                Uri uri = Uri.parse(uriPath);
                vvContent.setVideoURI(uri);
                vvContent.requestFocus();
                vvContent.start();
            }else{
                vvContent.setVisibility(View.GONE);
            }
        }

        private class DownloadImageAsyncTask extends AsyncTask<UserEvent, Void, Bitmap[]> {

            @Override
            protected Bitmap[] doInBackground(UserEvent... userEvents) {
                URL ava_url = null;
                URL photo_url = null;
                Bitmap[] bitmaps = new Bitmap[2];
                try {
                    Bitmap ava_bmp = null;
                    Bitmap photo_bmp = null;
                    if (userEvents[0].user.photo_url != null && !userEvents[0].user.photo_url.equals("")){
                        ava_url = new URL(userEvents[0].user.photo_url);
                        ava_bmp = BitmapFactory.decodeStream(ava_url.openConnection().getInputStream());
                    }
                    if (userEvents[0].event.photo_url != null && !userEvents[0].event.photo_url.equals("")){
                        photo_url = new URL(userEvents[0].event.photo_url);
                        photo_bmp = BitmapFactory.decodeStream(photo_url.openConnection().getInputStream());
                    }

                    bitmaps[0] = ava_bmp;
                    bitmaps[1] = photo_bmp;
                    return bitmaps;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Bitmap[] bitmaps) {
                if (bitmaps[0] != null){
                    ivAvatar.setImageBitmap(bitmaps[0]);
                    ivAvatar.setScaleType(ImageView.ScaleType.FIT_XY);
                }

                if (bitmaps[1] != null){
                    ivImage.setImageBitmap(bitmaps[1]);
                    ivImage.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }
        }
    }
}
