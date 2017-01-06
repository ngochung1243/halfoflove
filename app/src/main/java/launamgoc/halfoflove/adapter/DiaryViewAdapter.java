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
import launamgoc.halfoflove.model.DiaryContent;
import launamgoc.halfoflove.model.NewFeedElement;

/**
 * Created by KhaTran on 11/18/2016.
 */

public class DiaryViewAdapter extends
        RecyclerView.Adapter<DiaryViewAdapter.InformationHolder>{
    private List<DiaryContent> listView = new ArrayList<>();

    public DiaryViewAdapter(List<DiaryContent> listView) {
        this.listView = listView;
    }

    @Override
    public int getItemCount() {
        return listView.size();
    }

    @Override
    public DiaryViewAdapter.InformationHolder onCreateViewHolder(ViewGroup viewGroup,
                                                                       int position) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater
                .inflate(R.layout.cardview_diary, viewGroup, false);
        return new DiaryViewAdapter.InformationHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DiaryViewAdapter.InformationHolder viewHolder, int position) {
        DiaryContent data = listView.get(position);
        viewHolder.load(data);
    }

    public void addItem(int position, DiaryContent data) {
        listView.add(position, data);
        notifyItemInserted(position);
    }

    public void clear(){
        listView.clear();
    }

    public class InformationHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public TextView tvContent;
        public ImageView ivContent;
        public VideoView vvContent;

        public InformationHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.date);
            tvContent = (TextView) itemView.findViewById(R.id.content);
            ivContent = (ImageView) itemView.findViewById(R.id.image);
            vvContent = (VideoView) itemView.findViewById(R.id.video);
        }

        public void load(@NonNull final DiaryContent data) {
            tvTitle.setText(data.getTitle());
            tvContent.setText(data.getContent());

            if(data.getImage() != "" && data.getImage() != null) {
                new GeImageAsyncTask().execute(data);
            }

            if(data.getVideo()!= "" && data.getVideo() != null) {
                String uriPath = "android.resource://launamgoc.halfoflove/" + data.getVideo();
                Uri uri = Uri.parse(uriPath);
                vvContent.getLayoutParams().height = 500;
                vvContent.getLayoutParams().width = 500;
                vvContent.requestLayout();
                vvContent.setVideoURI(uri);
                vvContent.requestFocus();
                vvContent.start();
            }
        }

        private class GeImageAsyncTask extends AsyncTask<DiaryContent, Void, Bitmap> {

            @Override
            protected Bitmap doInBackground(DiaryContent... diaryContent) {
                URL ava_url = null;
                URL photo_url = null;
                Bitmap bitmap = null;
                try {
                    if (diaryContent[0].getImage() != null && !diaryContent[0].getImage().equals("")){
                        ava_url = new URL(diaryContent[0].getImage());
                        bitmap = BitmapFactory.decodeStream(ava_url.openConnection().getInputStream());
                    }
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
                if (bitmap != null){
                    ivContent.getLayoutParams().height = 500;
                    ivContent.getLayoutParams().width = 500;
                    ivContent.requestLayout();
                    ivContent.setImageBitmap(bitmap);
                    ivContent.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }
        }
    }
}
