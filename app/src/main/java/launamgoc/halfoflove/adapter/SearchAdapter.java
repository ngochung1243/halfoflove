package launamgoc.halfoflove.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import launamgoc.halfoflove.activity.FriendTimelineActivity;
import launamgoc.halfoflove.activity.RelationshipActivity;
import launamgoc.halfoflove.model.Relationship;
import launamgoc.halfoflove.model.User;
import launamgoc.halfoflove.model.UserBusiness;

/**
 * Created by Huy on 09-Jan-17.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {

    private List<User> listView = new ArrayList<User>();
    private Activity context;
    public boolean isSearchInMain = true;

    public SearchAdapter(List<User> listView, Activity context, boolean isSearchInMain) {
        this.context = context;
        this.listView = listView;
        this.isSearchInMain = isSearchInMain;
    }

    @Override
    public SearchHolder onCreateViewHolder(ViewGroup parent, int position) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater
                .inflate(R.layout.cardview_seach, parent, false);
        return new SearchAdapter.SearchHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.SearchHolder holder, int position) {
        User data = listView.get(position);
        holder.load(data);

    }

    public void addItem(int position, User data) {
        listView.add(position, data);
        notifyItemInserted(position);
    }

    public void setListView(List<User> users){
        listView = users;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return listView.size();
    }

    public class SearchHolder extends RecyclerView.ViewHolder {

        public CardView cvSearch;
        public ImageView ivAvatar;
        public TextView tvName;
        public TextView tvLocation;


        public SearchHolder(View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.cv_search_avatar);
            tvName = (TextView) itemView.findViewById(R.id.cv_search_name);
            tvLocation = (TextView) itemView.findViewById(R.id.cv_search_location);
            cvSearch = (CardView) itemView.findViewById(R.id.cv_Search);
        }

        public void load(@NonNull final User data) {
            ivAvatar.setScaleType(ImageView.ScaleType.FIT_XY);
            tvName.setText(data.fullname);
            tvLocation.setText("Address: " + data.location);
            new DownloadImageAsyncTask().execute(data);
            cvSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isSearchInMain){
                        if(FriendTimelineActivity.userBusiness == null){
                            FriendTimelineActivity.userBusiness = new UserBusiness();
                        }

                        UserBusiness userBusiness = new UserBusiness();
                        userBusiness.mUser = data;
                        FriendTimelineActivity.userBusiness = userBusiness;

                        Intent intent = new Intent(context, FriendTimelineActivity.class);
                        context.startActivity(intent);
                    }else {
                        RelationshipActivity.requestUser = data;
                        context.setResult(1);
                        context.finish();
                    }

                }
            });
        }

        private class DownloadImageAsyncTask extends AsyncTask<User, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(User... users) {
                URL ava_url = null;
                Bitmap bitmap;
                try {
                    Bitmap ava_bmp = null;
                    if (users[0].photo_url != null && !users[0].photo_url.equals("")) {
                        ava_url = new URL(users[0].photo_url);
                        ava_bmp = BitmapFactory.decodeStream(ava_url.openConnection().getInputStream());
                    }
                    bitmap = ava_bmp;
                    return bitmap;
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
                if (bitmap != null) {
                    ivAvatar.setImageBitmap(bitmap);
                    ivAvatar.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }
        }
    }


}
