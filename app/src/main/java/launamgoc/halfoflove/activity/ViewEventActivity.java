package launamgoc.halfoflove.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import launamgoc.halfoflove.R;

public class ViewEventActivity extends AppCompatActivity {
    @BindView(R.id.txtPostTime)
    TextView txtPostTime;
    @BindView(R.id.txtDescription)
    TextView txtDescription;
    @BindView(R.id.imgPic)
    ImageView imgPic;
    @BindView(R.id.video)
    VideoView videoView;
    @BindView(R.id.imgBtnPlayBack)
    ImageButton imgBtnPlayBack;
    String photoUrl, videoUrl;
    Boolean isPlaying = false;
    Boolean isStart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        txtPostTime.setText(bundle.getString("PostTime"));
        txtDescription.setText(bundle.getString("Description"));
        photoUrl = bundle.getString("PhotoUrl");
        videoUrl = bundle.getString("VideoUrl");
        if(photoUrl.length() != 0){
            new GeImageAsyncTask().execute(photoUrl);
            imgPic.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            imgBtnPlayBack.setVisibility(View.GONE);
        }else if(videoUrl.length() != 0){
            Uri uri=Uri.parse(videoUrl);
            videoView.setVideoURI(uri);
            imgPic.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            imgBtnPlayBack.setVisibility(View.VISIBLE);
        }else{
            imgPic.setVisibility(View.GONE);
            videoView.setVisibility(View.GONE);
            imgBtnPlayBack.setVisibility(View.GONE);
        }

        imgBtnPlayBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isStart == false){
                    videoView.start();
                    isStart = true;
                    imgBtnPlayBack.setImageResource(R.drawable.ic_pause);
                }else {
                    if (isPlaying == false) {
                        videoView.resume();
                        isPlaying = true;
                        imgBtnPlayBack.setImageResource(R.drawable.ic_pause);
                    } else {
                        videoView.pause();
                        isPlaying = false;
                        imgBtnPlayBack.setImageResource(R.drawable.ic_play_back);
                    }
                }
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                isStart = false;
                isPlaying = false;
                imgBtnPlayBack.setImageResource(R.drawable.ic_play_back);
            }
        });
    }

    private class GeImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... photoUrl) {
            URL ava_url = null;
            URL photo_url = null;
            Bitmap bitmap = null;
            try {
                if (photoUrl[0] != null && !photoUrl[0].equals("")){
                    ava_url = new URL(photoUrl[0]);
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
                imgPic.getLayoutParams().height = 500;
                imgPic.getLayoutParams().width = 500;
                imgPic.requestLayout();
                imgPic.setImageBitmap(bitmap);
                imgPic.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
    }
}
