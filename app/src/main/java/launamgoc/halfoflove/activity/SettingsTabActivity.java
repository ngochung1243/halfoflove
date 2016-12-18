package launamgoc.halfoflove.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import launamgoc.halfoflove.R;

/**
 * Created by KhaTran on 12/18/2016.
 */

public class SettingsTabActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.btn_timeline)
    ImageButton btnTimeline;
    @BindView(R.id.btn_timeline_partner)
    ImageButton btnTimelinePartner;
    @BindView(R.id.btn_account_setting)
    ImageButton btnAccountSet;
    @BindView(R.id.btn_relationship_setting)
    ImageButton btnRelaSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tablelayout_settings);
        ButterKnife.bind(this);

        btnTimeline.setOnClickListener(this);
        btnTimelinePartner.setOnClickListener(this);
        btnAccountSet.setOnClickListener(this);
        btnRelaSet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnTimeline) {
            Intent intent = new Intent(getBaseContext(), TimelineActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
        if (v == btnTimelinePartner) {
            Intent intent = new Intent(getBaseContext(), FriendTimelineActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
        if (v == btnAccountSet) {
            Intent intent = new Intent(getBaseContext(), EditProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
        if (v == btnRelaSet) {
            Intent intent = new Intent(getBaseContext(), RelationshipActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    }
}
