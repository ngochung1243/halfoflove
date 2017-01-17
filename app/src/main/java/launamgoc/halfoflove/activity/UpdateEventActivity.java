package launamgoc.halfoflove.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import launamgoc.halfoflove.R;

/**
 * Created by KhaTran on 12/10/2016.
 */

public class UpdateEventActivity extends AppCompatActivity implements View.OnClickListener {
    static int PICK_IMAGE = 1;
    static int PICK_VIDEO = 2;

    @BindView(R.id.et_name)
    EditText mName;
    @BindView(R.id.et_content)
    EditText mContent;
    @BindView(R.id.rdb_public)
    RadioButton mPublic;
    @BindView(R.id.rdb_private)
    RadioButton mPrivate;
    @BindView(R.id.layout_image)
    LinearLayout mImage;
    @BindView(R.id.layout_video)
    LinearLayout mVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_status);
        ButterKnife.bind(this);

        mPublic.setOnClickListener(this);
        mPrivate.setOnClickListener(this);
        mImage.setOnClickListener(this);
        mVideo.setOnClickListener(this);

        // Set ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar);

        TextView title = (TextView) findViewById(R.id.ab_tv_title);
        title.setText("Update AppEvent");

        ImageButton btnBack = (ImageButton) findViewById(R.id.ab_btn_back);
        btnBack.setImageResource(getResources()
                .getIdentifier("ic_arrow_back", "drawable", getPackageName()));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageButton btnNext = (ImageButton) findViewById(R.id.ab_btn_next);
        btnNext.setImageResource(getResources()
                .getIdentifier("ic_send", "drawable", getPackageName()));
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mPublic) {

        }
        if (v == mPrivate) {

        }
        if (v == mImage) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        }
        if (v == mVideo) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_VIDEO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE) {

            }
            else if(requestCode == PICK_VIDEO) {

            }
        }
    }
}
