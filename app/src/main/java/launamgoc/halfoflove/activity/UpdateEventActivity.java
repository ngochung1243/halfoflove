package launamgoc.halfoflove.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import launamgoc.halfoflove.R;
import launamgoc.halfoflove.helper.FirebaseHelper;
import launamgoc.halfoflove.model.AppEvent;

import static launamgoc.halfoflove.model.MyBundle.mUserBusiness;

/**
 * Created by KhaTran on 12/10/2016.
 */

public class UpdateEventActivity extends AppCompatActivity implements View.OnClickListener {
    static int PICK_IMAGE = 1;
    static int PICK_VIDEO = 2;
    Boolean chooseImage = false;
    Boolean chooseVideo = false;
    String date;
    Uri uri;

//    @BindView(R.id.et_name)
//    EditText mName;
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
    @BindView(R.id.btn_Post)
    Button mBtnPost;
    @BindView(R.id.uploadImage)
    ImageView uploadImage;
    @BindView(R.id.uploadVideo)
    VideoView uploadVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        date = getIntent().getStringExtra("PostDate");

        setContentView(R.layout.activity_update_status);
        ButterKnife.bind(this);

        mPublic.setOnClickListener(this);
        mPrivate.setOnClickListener(this);
        mImage.setOnClickListener(this);
        mVideo.setOnClickListener(this);

        uploadImage.setVisibility(View.VISIBLE);
        uploadVideo.setVisibility(View.GONE);

        // Set ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.darkpink)));

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

        mBtnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog progressDialog = new ProgressDialog(UpdateEventActivity.this);
                progressDialog.setMessage("Uploading");
                progressDialog.show();
                final AppEvent event = new AppEvent();
                event.description = mContent.getText().toString();
                event.fid = mUserBusiness.mUser.fid;
                Calendar now = Calendar.getInstance();
                String time = now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE);
                event.post_time = formatDate(date) + " " + time;
                if(chooseImage == true){
                    try {
                        FirebaseHelper.uploadImage(mUserBusiness.mUser.fid + event.id, "Love in peace", convertUriToByteArray(uri),
                                new FirebaseHelper.FirebaseUploadImagepDelegate() {
                                    @Override
                                    public void onUploadImageSuccess(String imageUrl) {
                                        event.photo_url = imageUrl;
                                        Boolean success = mUserBusiness.addEvent(event);
                                        if(success){
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Event is added in calendar", Toast.LENGTH_SHORT).show();
                                        }else{
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Fail in proccess upload event", Toast.LENGTH_SHORT).show();
                                        }
                                        finish();
                                    }

                                    @Override
                                    public void onUploadImageFailed(String error) {
                                        Toast.makeText(getApplicationContext(), "Fail in proccess upload photo", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if(chooseVideo == true){
                    try {
                        FirebaseHelper.uploadImage(mUserBusiness.mUser.fid + event.id,
                                "Love in peace video", convertVideoUriToByteArray(uri),
                            new FirebaseHelper.FirebaseUploadImagepDelegate() {
                                @Override
                                public void onUploadImageSuccess(String imageUrl) {
                                    event.video_url = imageUrl;
                                    Boolean success = mUserBusiness.addEvent(event);
                                    if (success) {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),
                                                "Event is added in calendar", Toast.LENGTH_SHORT).show();
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),
                                                "Fail in proccess upload event", Toast.LENGTH_SHORT).show();
                                    }
                                    finish();
                                }

                                @Override
                                public void onUploadImageFailed(String error) {
                                    Toast.makeText(getApplicationContext(),
                                            "Fail in proccess upload video", Toast.LENGTH_SHORT).show();
                                }
                            });
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Boolean success = mUserBusiness.addEvent(event);
                    if(success){
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Event is added in calendar", Toast.LENGTH_SHORT).show();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Fail in proccess upload event", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                }
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
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_VIDEO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                uri = data.getData();
                uploadImage.setImageURI(uri);
                chooseImage = true;
                uploadImage.setVisibility(View.VISIBLE);
                uploadVideo.setVisibility(View.GONE);
            }
            else if(requestCode == PICK_VIDEO) {
                uri = data.getData();
                uploadVideo.setVideoURI(uri);
                chooseVideo = true;
                uploadVideo.setVisibility(View.VISIBLE);
                uploadImage.setVisibility(View.GONE);
            }else{
                uploadImage.setVisibility(View.GONE);
                uploadVideo.setVisibility(View.GONE);
            }
        }
    }

    private byte[] convertUriToByteArray(Uri uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private byte[] convertVideoUriToByteArray(Uri uri) throws IOException{
        InputStream fis= this.getContentResolver().openInputStream(uri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buf = new byte[1024];
        int n;
        while (-1 != (n = fis.read(buf)))
            baos.write(buf, 0, n);

        return baos.toByteArray();
    }

    public String formatDate(String postDate){
        String result = "";
        String day = postDate.substring(0, postDate.indexOf("-"));
        String month = postDate.substring(postDate.indexOf("-") + 1, postDate.lastIndexOf("-"));
        String year = postDate.substring(postDate.lastIndexOf("-") + 1, postDate.length());
        switch (month){
            case "January":
                month = "01";
                break;
            case "February":
                month = "02";
                break;
            case "March":
                month = "03";
                break;
            case "April":
                month = "04";
                break;
            case "May":
                month = "05";
                break;
            case "June":
                month = "06";
                break;
            case "July":
                month = "07";
                break;
            case "August":
                month = "08";
                break;
            case "September":
                month = "09";
                break;
            case "October":
                month = "10";
                break;
            case "November":
                month = "11";
                break;
            case "December":
                month = "12";
                break;
            default:
                break;
        }
        result = day + "/" + month + "/" + year;
        return result;
    }
}
