package launamgoc.halfoflove.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import launamgoc.halfoflove.R;

/**
 * Created by KhaTran on 12/10/2016.
 */

public class UpdateEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_status);

        // Set ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.darkpink)));

        TextView title = (TextView) findViewById(R.id.ab_tv_title);
        title.setText("Update Event");

        ImageButton btnBack = (ImageButton) findViewById(R.id.ab_btn_back);
        btnBack.setImageResource(getResources()
                .getIdentifier("ic_clear_back", "drawable", getPackageName()));
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
}
