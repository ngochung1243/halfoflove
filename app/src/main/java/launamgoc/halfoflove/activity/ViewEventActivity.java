package launamgoc.halfoflove.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
    String photoUrl, videoUrl;

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
        if(photoUrl != "" && photoUrl != null){
            new GeImageAsyncTask().execute(photoUrl);
            imgPic.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
        }else if(videoUrl != "" && videoUrl != null){
            imgPic.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
        }else{
            imgPic.setVisibility(View.GONE);
            videoView.setVisibility(View.GONE);
        }
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
