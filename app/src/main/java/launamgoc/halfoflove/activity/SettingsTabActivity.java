package launamgoc.halfoflove.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import launamgoc.halfoflove.R;

/**
 * Created by KhaTran on 12/18/2016.
 */

public class SettingsTabActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.layout_timeline)
    LinearLayout mTimeline;
    @BindView(R.id.layout_timeline_partner)
    LinearLayout mTimelinePartner;
    @BindView(R.id.layout_account_setting)
    LinearLayout mAccountSet;
    @BindView(R.id.layout_relationship_setting)
    LinearLayout mRelaSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tablelayout_settings);
        ButterKnife.bind(this);

        mTimeline.setOnClickListener(this);
        mTimelinePartner.setOnClickListener(this);
        mAccountSet.setOnClickListener(this);
        mRelaSet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mTimeline) {
            Intent intent = new Intent(getBaseContext(), TimelineActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
        if (v == mTimelinePartner) {
            Intent intent = new Intent(getBaseContext(), FriendTimelineActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
        if (v == mAccountSet) {
            Intent intent = new Intent(getBaseContext(), EditProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
        if (v == mRelaSet) {
            Intent intent = new Intent(getBaseContext(), RelationshipActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    }
}
