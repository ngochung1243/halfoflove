package launamgoc.halfoflove.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import launamgoc.halfoflove.R;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Set ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar);

        TextView actionBar_title = (TextView) findViewById(R.id.ab_tv_title);
        actionBar_title.setText("NameUser");
        actionBar_title.setGravity(Gravity.RIGHT);
        actionBar_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        ImageButton btnBack = (ImageButton) findViewById(R.id.ab_btn_back);
        btnBack.setImageResource(getResources()
                .getIdentifier("ic_arrow_back", "drawable", getPackageName()));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }
}
