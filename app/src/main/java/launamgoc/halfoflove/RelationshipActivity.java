package launamgoc.halfoflove;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class RelationshipActivity extends AppCompatActivity {

    String arr[]={
            "Single",
            "In a relationship",
            "Divorced"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relationship);
        Spinner spin=(Spinner) findViewById(R.id.spnRelatioship);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>
                (
                        this,
                        android.R.layout.simple_spinner_item,
                        arr
                );
        adapter.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(new MyProcessEvent());
    }

    private class MyProcessEvent implements
            AdapterView.OnItemSelectedListener
    {
        public void onItemSelected(AdapterView<?> arg0,
                                   View arg1,
                                   int arg2,
                                   long arg3) {
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
}
