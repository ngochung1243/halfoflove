package launamgoc.halfoflove.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import launamgoc.halfoflove.R;

/**
 * Created by KhaTran on 12/11/2016.
 */

public class EventInformationActivity extends AppCompatActivity {
    @BindView(R.id.vv_video)
    VideoView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_information);
        ButterKnife.bind(this);

        // Set ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.darkpink)));
        ImageButton btnBack = (ImageButton) findViewById(R.id.ab_btn_back);
        btnBack.setImageResource(getResources()
                .getIdentifier("ic_arrow_back", "drawable", getPackageName()));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        TextView actionBar_title = (TextView) findViewById(R.id.ab_tv_title);
        actionBar_title.setText("Edit");
        actionBar_title.setGravity(Gravity.RIGHT);
        actionBar_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventInformationActivity.this, UpdateEventActivity.class);
                startActivity(intent);
                finish();
            }
        });

        String uriPath = "android.resource://launamgoc.halfoflove/" + R.raw.video;
        Uri uri = Uri.parse(uriPath);
        video.setVideoURI(uri);
        video.requestFocus();
        video.start();
    }
}
