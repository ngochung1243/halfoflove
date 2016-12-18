package launamgoc.halfoflove.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
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
import launamgoc.halfoflove.model.NewFeedElement;

/**
 * Created by KhaTran on 12/14/2016.
 */

public class NewFeedAdapter  extends
        RecyclerView.Adapter<NewFeedAdapter.NewFeedHolder>{
    private List<NewFeedElement> listView = new ArrayList<NewFeedElement>();

    public NewFeedAdapter(List<NewFeedElement> listView) {
        this.listView = listView;
    }

    @Override
    public int getItemCount() {
        return listView.size();
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
        NewFeedElement data = listView.get(position);
        viewHolder.load(data);
    }

    public void addItem(int position, NewFeedElement data) {
        listView.add(position, data);
        notifyItemInserted(position);
    }

    public void clear(){
        listView.clear();
    }

    public class NewFeedHolder extends RecyclerView.ViewHolder {

        public ImageView ivAvatar;
        public TextView tvName;
        public TextView tvDay;
        public TextView tvContent;
        public ImageView ivImage;

        public NewFeedHolder(View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.cv_nf_ava);
            tvName = (TextView) itemView.findViewById(R.id.cv_nf_name);
            tvDay = (TextView) itemView.findViewById(R.id.cv_nf_day);
            tvContent = (TextView) itemView.findViewById(R.id.cv_nf_content);
            ivImage = (ImageView) itemView.findViewById(R.id.cv_nf_image);
        }

        public void load(@NonNull final NewFeedElement data) {
            tvName.setText(data.getName());
            tvDay.setText(data.getDay());
            tvContent.setText(data.getContent());

            new DownloadImageAsyncTask().execute(data);
        }

        private class DownloadImageAsyncTask extends AsyncTask<NewFeedElement, Void, Bitmap[]> {

            @Override
            protected Bitmap[] doInBackground(NewFeedElement... newFeedElements) {
                URL ava_url = null;
                URL photo_url = null;
                Bitmap[] bitmaps = new Bitmap[2];
                try {
                    Bitmap ava_bmp = null;
                    Bitmap photo_bmp = null;
                    if (newFeedElements[0].getAva() != null && !newFeedElements[0].getAva().equals("")){
                        ava_url = new URL(newFeedElements[0].getAva());
                        ava_bmp = BitmapFactory.decodeStream(ava_url.openConnection().getInputStream());
                    }
                    if (newFeedElements[0].getImage() != null && !newFeedElements[0].getImage().equals("")){
                        photo_url = new URL(newFeedElements[0].getImage());
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
