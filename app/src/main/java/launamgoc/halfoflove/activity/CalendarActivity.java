package launamgoc.halfoflove.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import launamgoc.halfoflove.R;

import static launamgoc.halfoflove.R.id.num_events_per_day;

public class CalendarActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.btn_nextMonth)
    ImageView mNextMonth;
    @BindView(R.id.btn_prevMonth)
    ImageView mPrevMonth;
    @BindView(R.id.tv_currentMonth)
    TextView mCurrentMonth;
    @BindView(R.id.btn_back)
    TextView mBack;
    @BindView(R.id.iv_calendarheader)
    ImageView mCalendarHeader;
    @BindView(R.id.gv_calendar)
    GridView mCalendarView;

    private GridCellAdapter _adapter;
    private Calendar _calendar;

    @SuppressLint("NewApi")
    private int _month, _year;
    @SuppressWarnings("unused")
    @SuppressLint({"NewApi", "NewApi", "NewApi", "NewApi"})
    private final DateFormat _dateFormatter = new DateFormat();
    private static final String _dateTemplate = "MMMM yyyy";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);

        _calendar = Calendar.getInstance(Locale.getDefault());
        _month = _calendar.get(Calendar.MONTH) + 1;
        _year = _calendar.get(Calendar.YEAR);

        mPrevMonth.setOnClickListener(this);
        mNextMonth.setOnClickListener(this);
        mBack.setOnClickListener(this);

        mCalendarHeader.setScaleType(ImageView.ScaleType.FIT_XY);
        mCurrentMonth.setText(DateFormat.format(_dateTemplate, _calendar.getTime()));

        _adapter = new GridCellAdapter(getApplicationContext(), R.id.calendar_day_gridcell, _month, _year);
        _adapter.notifyDataSetChanged();
        mCalendarView.setAdapter(_adapter);
    }

    private void setGridCellAdapterToDate(int month, int year) {
        _adapter = new GridCellAdapter(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
        _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
        _adapter.notifyDataSetChanged();

        mCurrentMonth.setText(DateFormat.format(_dateTemplate, _calendar.getTime()));
        mCalendarView.setAdapter(_adapter);
    }

    @Override
    public void onClick(View v) {
        if (v == mPrevMonth) {
            if (_month <= 1) {
                _month = 12;
                _year--;
            } else {
                _month--;
            }

            setGridCellAdapterToDate(_month, _year);
        }
        if (v == mNextMonth) {
            if (_month > 11) {
                _month = 1;
                _year++;
            } else {
                _month++;
            }

            setGridCellAdapterToDate(_month, _year);
        }
        if (v == mBack) {
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void showCustomDialog() {
        AlertDialog chooseDialog;

        final CharSequence[] items = {"Create a new event", "Set notification"};

        AlertDialog.Builder builder = new AlertDialog.Builder(CalendarActivity.this);
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                switch (item) {
                    case 0:
                        Intent intent = new Intent(CalendarActivity.this, UpdateEventActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        showSetNotiDialog();
                        break;
                }
                dialog.dismiss();
            }
        });
        chooseDialog = builder.create();
        chooseDialog.show();
    }

    private void showSetNotiDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CalendarActivity.this);
        LayoutInflater inflater = CalendarActivity.this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_set_notification, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.noti_title);

        dialogBuilder.setTitle("Set notification");
        dialogBuilder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    public class GridCellAdapter extends BaseAdapter implements View.OnClickListener {
        private static final int DAY_OFFSET = 1;

        private final Context _context;

        private int _daysInMonth;
        private int _currentDayOfMonth;
        private int _currentWeekDay;

        private Button _gridcell;
        private TextView _num_events_per_day;

        private final List<String> _list;
        private final HashMap<String, Integer> _eventsPerMonthMap;

        private final SimpleDateFormat _dateFormatter = new SimpleDateFormat(
                "dd-MMM-yyyy");
        private final String[] _weekdays = new String[]{"Sun", "Mon", "Tue",
                "Wed", "Thu", "Fri", "Sat"};
        private final String[] _months = {"January", "February", "March",
                "April", "May", "June", "July", "August", "September",
                "October", "November", "December"};
        private final int[] _daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30,
                31, 30, 31};

        public GridCellAdapter(Context context, int textViewResourceId,
                               int month, int year) {
            super();
            this._context = context;
            this._list = new ArrayList<String>();

            Calendar calendar = Calendar.getInstance();
            setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));

            printMonth(month, year);

            _eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
        }

        private String getMonthAsString(int i) {
            return _months[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return _daysOfMonth[i];
        }

        public String getItem(int position) {
            return _list.get(position);
        }

        public int getCurrentDayOfMonth() {
            return _currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth) {
            this._currentDayOfMonth = currentDayOfMonth;
        }

        public void setCurrentWeekDay(int currentWeekDay) {
            this._currentWeekDay = currentWeekDay;
        }

        @Override
        public int getCount() {
            return _list.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;

            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) _context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.gridcell_calendar_day, parent, false);
            }

            _gridcell = (Button) row.findViewById(R.id.calendar_day_gridcell);
            _gridcell.setOnClickListener(this);

            String[] day_color = _list.get(position).split("-");
            String theday = day_color[0];
            String themonth = day_color[2];
            String theyear = day_color[3];
            if ((!_eventsPerMonthMap.isEmpty()) && (_eventsPerMonthMap != null)) {
                if (_eventsPerMonthMap.containsKey(theday)) {
                    _num_events_per_day = (TextView) row
                            .findViewById(num_events_per_day);
                    Integer numEvents = (Integer) _eventsPerMonthMap.get(theday);
                    _num_events_per_day.setText(numEvents.toString());
                }
            }

            _gridcell.setText(theday);
            _gridcell.setTag(theday + "-" + themonth + "-" + theyear);

            if (day_color[1].equals("GREY")) {
                _gridcell.setTextColor(getResources()
                        .getColor(R.color.pink2));
                _gridcell.setTextSize(15);
            }
            if (day_color[1].equals("WHITE")) {
                _gridcell.setTextColor(getResources()
                        .getColor(R.color.pink));
                _gridcell.setTextSize(15);
            }
            if (day_color[1].equals("BLUE")) {
                _gridcell.setTextColor(getResources()
                        .getColor(R.color.violet));
                _gridcell.setTextSize(18);
                _gridcell.setTypeface(null, Typeface.BOLD_ITALIC);
            }
            return row;
        }

        @Override
        public void onClick(View view) {
            String date_month_year = (String) view.getTag();

//            if(date_month_year.compareTo("11-December-2016") == 0)
//            {
//                Intent intent = new Intent(CalendarActivity.this, EventInformationActivity.class);
//                startActivity(intent);
//            }
//            else
//            {
//                showCustomDialog();
//            }
//
//            try {
//                Date parsedDate = _dateFormatter.parse(date_month_year);
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
        }

        private void printMonth(int mm, int yy) {

            int trailingSpaces = 0;
            int daysInPrevMonth = 0;
            int prevMonth = 0;
            int prevYear = 0;
            int mNextMonth = 0;
            int nextYear = 0;
            int currentMonth = mm - 1;

            String currentMonthName = getMonthAsString(currentMonth);
            _daysInMonth = getNumberOfDaysOfMonth(currentMonth);
            GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);

            if (currentMonth == 11) {
                prevMonth = currentMonth - 1;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                mNextMonth = 0;
                prevYear = yy;
                nextYear = yy + 1;
            } else if (currentMonth == 0) {
                prevMonth = 11;
                prevYear = yy - 1;
                nextYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                mNextMonth = 1;
            } else {
                prevMonth = currentMonth - 1;
                mNextMonth = currentMonth + 1;
                nextYear = yy;
                prevYear = yy;
                daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
            }

            int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
            trailingSpaces = currentWeekDay;

            if (cal.isLeapYear(cal.get(Calendar.YEAR)))
                if (mm == 2)
                    ++_daysInMonth;
                else if (mm == 3)
                    ++daysInPrevMonth;

            // Trailing Month days
            for (int i = 0; i < trailingSpaces; i++) {
                _list.add(String
                        .valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET)
                                + i)
                        + "-GREY"
                        + "-"
                        + getMonthAsString(prevMonth)
                        + "-"
                        + prevYear);
            }

            for (int i = 1; i <= _daysInMonth; i++) {
                if (i == getCurrentDayOfMonth()) {
                    _list.add(String.valueOf(i) + "-BLUE" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                } else {
                    _list.add(String.valueOf(i) + "-WHITE" + "-"
                            + getMonthAsString(currentMonth) + "-" + yy);
                }
            }

            for (int i = 0; i < _list.size() % 7; i++) {
                _list.add(String.valueOf(i + 1) + "-GREY" + "-"
                        + getMonthAsString(mNextMonth) + "-" + nextYear);
            }
        }

        private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,
                                                                    int month) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();

            return map;
        }
    }
}