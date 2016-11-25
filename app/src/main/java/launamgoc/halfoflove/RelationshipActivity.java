package launamgoc.halfoflove;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class RelationshipActivity extends AppCompatActivity {

    String spnRelationship[]={
            "Single",
            "In a relationship",
            "Divorced"};

    String spnTime[]={
            "Day",
            "Month",
            "Year",
            "Forever"
    };

    String spnYear[]={
            "Year",
            "2010",
            "2011",
            "2012",
            "2013",
            "2014",
            "2015",
            "2016"
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relationship);
        Spinner spinRelationship=(Spinner) findViewById(R.id.spnRelatioship);
        Spinner spinTime =  (Spinner) findViewById(R.id.spnTime);
        Spinner spinYear = (Spinner) findViewById(R.id.spnYear);
        Spinner spinMonth = (Spinner) findViewById(R.id.spnMonth);
        Spinner spinDay  = (Spinner) findViewById(R.id.spnDay);
        ArrayAdapter<String> adapterRelationship=new ArrayAdapter<String>
                (
                        this,
                        android.R.layout.simple_spinner_item,
                        spnRelationship
                );
        adapterRelationship.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        spinRelationship.setAdapter(adapterRelationship);
        spinRelationship.setOnItemSelectedListener(new MyProcessEvent());

        ArrayAdapter<String> adapterTime=new ArrayAdapter<String>
                (
                        this,
                        android.R.layout.simple_spinner_item,
                        spnTime
                );
        adapterTime.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        spinTime.setAdapter(adapterTime);
        spinTime.setOnItemSelectedListener(new MyProcessEvent());

        ArrayAdapter<String> adapterYear=new ArrayAdapter<String>
                (
                        this,
                        android.R.layout.simple_spinner_item,
                        spnYear
                );
        adapterYear.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        spinYear.setAdapter(adapterYear);
        spinYear.setOnItemSelectedListener(new MyProcessEvent());

        ArrayAdapter<String> adapterMonth=new ArrayAdapter<String>
                (
                        this,
                        android.R.layout.simple_spinner_item,
                        spnMonth
                );
        adapterMonth.setDropDownViewResource
                (android.R.layout.simple_list_item_single_choice);
        spinMonth.setAdapter(adapterMonth);
        spinMonth.setOnItemSelectedListener(new MyProcessEvent());

        ArrayAdapter<String> adapterDay=new ArrayAdapter<String>
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

    private class MyProcessEvent implements
            AdapterView.OnItemSelectedListener
    {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
}
