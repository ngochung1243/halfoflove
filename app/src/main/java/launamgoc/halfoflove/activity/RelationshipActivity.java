package launamgoc.halfoflove.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import launamgoc.halfoflove.R;
import launamgoc.halfoflove.adapter.SearchAdapter;
import launamgoc.halfoflove.helper.FirebaseHelper;
import launamgoc.halfoflove.model.Follow;
import launamgoc.halfoflove.model.MyBundle;
import launamgoc.halfoflove.model.Relationship;
import launamgoc.halfoflove.model.User;

import static android.R.attr.data;
import static android.R.attr.thickness;
import static launamgoc.halfoflove.activity.DivorceActivity.senderUser;

public class RelationshipActivity extends AppCompatActivity {

    @BindView(R.id.spnRelatioship)
    Spinner spinRelationship;
    @BindView(R.id.spnYear)
    Spinner spinYear;
    @BindView(R.id.spnMonth)
    Spinner spinMonth;
    @BindView(R.id.spnDay)
    Spinner spinDay;
    @BindView(R.id.edtSearchName)
    EditText edtSearchName;
    @BindView(R.id.edtLoveStatement)
    EditText edtLoveStatement;
    @BindView(R.id.btnSearchName)
    ImageButton btnSearchName;
    @BindView(R.id.cardviewSearch)
    CardView cardview_search;
    @BindView(R.id.cv_search_avatar)
    ImageView imvSearchAva;
    @BindView(R.id.cv_search_name)
    TextView tvSearchName;
    @BindView(R.id.cv_search_location)
    TextView tvSearchLocation;
    @BindView(R.id.lnLayoutSetRelationship)
    LinearLayout lnLayoutSetRelationship;
    @BindView(R.id.btnSendRequest)
    Button btnSendRequest;
    @BindView(R.id.btnCancel)
    Button btnCancel;


    public static int RELATIONSHIP_CODE = 101;
    public static User requestUser = new User();

    String spnRelationship[]={
            "Single",
            "In a relationship"};

    String spnYear[]={
            "Year",
            "2010",
            "2011",
            "2012",
            "2013",
            "2014",
            "2015",
            "2016",
            "2017"
    };

    String spnMonth[]={
            "Month",
            "01",
            "02",
            "03",
            "04",
            "05",
            "06",
            "07",
            "08",
            "09",
            "10",
            "11",
            "12"
    };

    String spnDay[]={
            "Day", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15",
            "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29",
            "30", "31"
    };

    ArrayAdapter<String> adapterYear;

    ArrayAdapter<String> adapterMonth;

    ArrayAdapter<String> adapterDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relationship);
        ButterKnife.bind(this);

        setActionBar();
        setSpinner();
        setListener();
        checkRelationship();
    }

    private void setSpinner(){
        ArrayAdapter<String> adapterRelationship=new ArrayAdapter<String>
                (
                        this,
                        android.R.layout.simple_spinner_item,
                        spnRelationship
                );
        adapterRelationship.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        spinRelationship.setAdapter(adapterRelationship);
        spinRelationship.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        lnLayoutSetRelationship.setVisibility(View.GONE);
                        btnSendRequest.setText("OK");
                        break;
                    case 1:
                        if (!MyBundle.pUserBusiness.mUser.fid.equals("")){
                            btnSendRequest.setText("DIVORCE");
                        }else {
                            btnSendRequest.setText("SEND REQUEST");
                        }
                        lnLayoutSetRelationship.setVisibility(View.VISIBLE);

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapterYear = new ArrayAdapter<String>
                (
                        this,
                        android.R.layout.simple_spinner_item,
                        spnYear
                );
        adapterYear.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        spinYear.setAdapter(adapterYear);
        spinYear.setOnItemSelectedListener(new MyProcessEvent());

        adapterMonth = new ArrayAdapter<String>
                (
                        this,
                        android.R.layout.simple_spinner_item,
                        spnMonth
                );
        adapterMonth.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        spinMonth.setAdapter(adapterMonth);
        spinMonth.setOnItemSelectedListener(new MyProcessEvent());

        adapterDay = new ArrayAdapter<String>
                (
                        this,
                        android.R.layout.simple_spinner_item,
                        spnDay
                );
        adapterDay.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        spinDay.setAdapter(adapterDay);
        spinDay.setOnItemSelectedListener(new MyProcessEvent());
    }

    private void setListener(){
        btnSearchName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullname = edtSearchName.getText().toString();
                FirebaseHelper.findUserByName(fullname, new FirebaseHelper.FirebaseFindUserDelegate() {
                    @Override
                    public void onFindUserByNameSuccess(List<User> users) {
                        SearchRelationshipActivity.listView = users;
                        Handler handler = new Handler(getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(RelationshipActivity.this, SearchRelationshipActivity.class);
                                startActivityForResult(intent, RELATIONSHIP_CODE);
                            }
                        });
                    }
                    @Override
                    public void onFindUserFailed() {

                    }
                });

            }
        });
        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnSendRequest.getText().toString().equals("SEND REQUEST")){
                    String message = "Do you wants to being love with \"" + requestUser.fullname + "\"?";
                    showDecisionDialog(message, RequestType.Relationship);
                }else if (btnSendRequest.getText().toString().equals("DIVORCE")){
                    String message = "Do you wants to divorce \"" + requestUser.fullname + "\"?";
                    showDecisionDialog(message, RequestType.Divorce);
                }else {
                    String message = "Do you wants be single?";
                    showDecisionDialog(message, RequestType.Single);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });
    }

    private void setActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar);

        TextView actionBarTitle = (TextView) findViewById(R.id.ab_tv_title);
        actionBarTitle.setText("Relationship");

        ImageButton btnBack = (ImageButton) findViewById(R.id.ab_btn_back);
        btnBack.setImageResource(getResources()
                .getIdentifier("ic_arrow_back", "drawable", getPackageName()));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(1);
                finish();
            }
        });
    }

    private void checkRelationship(){
        if(!MyBundle.pUserBusiness.mUser.fid.equals("")){
            requestUser = MyBundle.pUserBusiness.mUser;
            setInfoCardViewSearch();
            spinRelationship.setSelection(1);
            setInfoOfRelationship();
        }
    }

    private void setInfoOfRelationship(){
        Relationship relationship = MyBundle.mUserBusiness.mRelationship;
        String[] time = relationship.start_time.split("/");
        int dayPosition = adapterDay.getPosition(time[0]);
        int monthPosition = adapterMonth.getPosition(time[1]);
        int yearPosition = adapterYear.getPosition(time[2]);
        spinDay.setSelection(dayPosition);
        spinMonth.setSelection(monthPosition);
        spinYear.setSelection(yearPosition);

        edtSearchName.setVisibility(View.GONE);
        btnSearchName.setVisibility(View.GONE);

        edtLoveStatement.setText(relationship.love_statement);
        edtLoveStatement.setFocusable(false);
        edtLoveStatement.setCursorVisible(false);
        edtLoveStatement.setKeyListener(null);
        edtLoveStatement.setBackgroundColor(Color.WHITE);
    }

    public void setInfoCardViewSearch(){
        cardview_search.setVisibility(View.VISIBLE);
        imvSearchAva.setImageResource(android.R.color.transparent);

        imvSearchAva.setScaleType(ImageView.ScaleType.FIT_XY);
        tvSearchName.setText(requestUser.fullname);
        tvSearchLocation.setText("Address: " + requestUser.location);
        new DownloadImageAsyncTask().execute(requestUser);
    }

    private void sendRelationshipRequest(){
        Relationship relationship = new Relationship();
        relationship.id_request = MyBundle.mUserBusiness.mUser.fid;
        relationship.id_accept = requestUser.fid;
        relationship.start_time = spinDay.getSelectedItem().toString() + "/" + spinMonth.getSelectedItem().toString() + "/" + spinYear.getSelectedItem().toString();
        relationship.love_statement = edtLoveStatement.getText().toString();
        MyBundle.mUserBusiness.sendRelationshipRequestToUser(requestUser, relationship);
    }

    private void sendDivorceRequest(){
        MyBundle.mUserBusiness.sendDivorceRequestToUser(requestUser);
    }

    private void setSingle(){
        MyBundle.mUserBusiness.removeRelationship();
        removeFollow();
        setResult(1);
        finish();
    }

    private void removeFollow(){
        for (Follow follow : MyBundle.mUserBusiness.follower_objects){
            if (follow.id_follower.equals(requestUser.fid)){
                MyBundle.mUserBusiness.removeFollow(follow.id);
                break;
            }
        }

        for (Follow follow : MyBundle.mUserBusiness.following_objects){
            if (follow.id_following.equals(requestUser.fid)){
                MyBundle.mUserBusiness.removeFollow(follow.id);
                break;
            }
        }
    }

    private void showDecisionDialog(String message, final RequestType type){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message);
                alertDialogBuilder.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (type == RequestType.Relationship){
                                    sendRelationshipRequest();
                                    String toastMessage = "Your relationship request was sended!!!";
                                    showToast(toastMessage, R.drawable.ic_relationshiprequest);
                                }else if (type == RequestType.Divorce){
                                    sendDivorceRequest();
                                    String toastMessage = "Your divorce request was sended!!!";
                                    showToast(toastMessage, R.drawable.ic_divorce);
                                }else if (type == RequestType.Single){
                                    setSingle();
                                    String toastMessage = "You has already been single now!!!";
                                    showToast(toastMessage, R.drawable.ic_single);
                                }
                                finish();
                            }
                        });

        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showToast(String message, int icon_drawable){
        // get your custom_toast.xml ayout
        LayoutInflater inflater = getLayoutInflater();

        View layout = inflater.inflate(R.layout.toast_notification,
                (ViewGroup) findViewById(R.id.lnLayoutNotification));

        // set a dummy image
        ImageView imvIcon = (ImageView) layout.findViewById(R.id.imvIcon);
        imvIcon.setImageResource(icon_drawable);

        // set a message
        TextView tvMessage = (TextView) layout.findViewById(R.id.tvMessage);
        tvMessage.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RELATIONSHIP_CODE && resultCode == 1) {
            setInfoCardViewSearch();
        }
    }



    private class MyProcessEvent implements
            AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
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
                imvSearchAva.setImageBitmap(bitmap);
                imvSearchAva.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
    }

    enum RequestType{
        Relationship,
        Divorce,
        Single
    }
}
