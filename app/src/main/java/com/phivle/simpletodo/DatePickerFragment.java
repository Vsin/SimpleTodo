package com.phivle.simpletodo;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Vsin on 8/21/17.
 */

public class DatePickerFragment extends DialogFragment {
    private Date mDate;
    String dateString;
    private int year, month, day, hour, min;
    private final String EXTRA_DATE = "date";
    private final DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static interface Listener {
        void onDatePicked(String date);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v;
        DatePicker datePicker;
        TimePicker timePicker;
        Calendar calendar = Calendar.getInstance();

        dateString = getArguments().getString(EXTRA_DATE);
        Log.w("Test", "dateString: " + dateString);
        if (dateString != null) {
            try {
                mDate = iso8601Format.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            mDate = calendar.getTime();
        }

        calendar.setTime(mDate);

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        min = calendar.get(Calendar.MINUTE);

        v = getActivity().getLayoutInflater().inflate(R.layout.datetime, null);

        datePicker = (DatePicker) v.findViewById(R.id.datePicker);
        timePicker = (TimePicker)v.findViewById(R.id.timePicker);

        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            public void onDateChanged(DatePicker view, int year, int month, int day) {
                DatePickerFragment.this.year = year;
                DatePickerFragment.this.month = month;
                DatePickerFragment.this.day = day;
                updateDateTime();
            }
        });

        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(min);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            public void onTimeChanged(TimePicker view, int hour, int min) {
                DatePickerFragment.this.hour = hour;
                DatePickerFragment.this.min = min;
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String editItemDueDate = iso8601Format.format(new GregorianCalendar(year, month, day, hour, min).getTime());
                        ((Listener) getActivity()).onDatePicked(editItemDueDate);
                    }
                })
                .create();

    }

    public void updateDateTime() {
        dateString = iso8601Format.format(new GregorianCalendar(year, month, day, hour, min).getTime());
        getArguments().putString(EXTRA_DATE, dateString);
    }



}
