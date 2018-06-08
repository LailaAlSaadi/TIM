package com.u689.timetableapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import static android.R.layout.simple_spinner_item;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewById
    Spinner citySpinner;

    @ViewById
    Spinner routeSpinner;

    @ViewById
    TextView textTimer;

    @ViewById (R.id.startTimer)
    TextView startTimerBtn;

    @ViewById (R.id.stopTimer)
    TextView stopTimerBtn;



    ArrayAdapter<CharSequence> adapter;
    private Handler myHandler = new Handler();

    @AfterViews
    public void onChangeCitySpinner() {

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = "routes_" + position;
                int identifier = getResources().getIdentifier(s, "array", getPackageName());
                adapter = ArrayAdapter.createFromResource(getApplicationContext(), identifier, simple_spinner_item);
                routeSpinner.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private long startTime;
    private long timeInMillies;
    private long finalTime;
    private long timeSwap;

    @Click
    public void startTimer() {
        stopTimerBtn.setEnabled(true);
        startTimerBtn.setEnabled(false);
        citySpinner.setEnabled(false);
        routeSpinner.setEnabled(false);
        startTime = SystemClock.uptimeMillis();
        myHandler.postDelayed(updateTimerMethod, 0);
    }

    @Click
    public void stopTimer() {
        citySpinner.setEnabled(true);
        routeSpinner.setEnabled(true);
        stopTimerBtn.setEnabled(false);
        startTimerBtn.setEnabled(true);
        finalTime = 0;
        myHandler.removeCallbacks(updateTimerMethod);
    }

    private Runnable updateTimerMethod = new Runnable() {
        public void run() {
            timeInMillies = SystemClock.uptimeMillis() - startTime;
            finalTime = timeSwap + timeInMillies;

            int seconds = (int) (finalTime / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            seconds = seconds % 60;
            textTimer.setText(getFormatedTime(seconds, minutes, hours));
            myHandler.postDelayed(this, 0);
        }

    };

    @NonNull
    private String getFormatedTime(int seconds, int minutes, int hours) {
        return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":"
                + String.format("%02d", seconds);
    }

}
