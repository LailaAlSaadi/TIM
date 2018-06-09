package com.u689.timetableapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.u689.timetableapp.itemdao.ItemDao;
import com.u689.timetableapp.itemdao.UserEntry;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static android.R.layout.simple_spinner_item;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    ItemDao itemDao = new ItemDao();

    @ViewById
    Spinner citySpinner;

    @ViewById
    Spinner routeSpinner;

    @ViewById
    TextView textTimer;

    @ViewById(R.id.startTimer)
    TextView startTimerBtn;

    @ViewById(R.id.stopTimer)
    TextView stopTimerBtn;

    ArrayAdapter<CharSequence> adapter;
    private Handler timerHandler = new Handler();
    final Handler trackingHandler = new Handler();
    private long finalTime;
    private long startTime;
    String tripId;

    @AfterViews
    public void defaultBehavior() {
        stopTimerBtn.setEnabled(false);
    }

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

    @Click
    public void startTimer() {
        tripId = getTripId();
        stopTimerBtn.setEnabled(true);
        startTimerBtn.setEnabled(false);
        citySpinner.setEnabled(false);
        routeSpinner.setEnabled(false);
        startTime = SystemClock.uptimeMillis();
        timerHandler.postDelayed(updateTimerMethod, 0);
        trackingHandler.postDelayed(timerRepeat, 5000);
    }

    @Click
    public void stopTimer() {
        citySpinner.setEnabled(true);
        routeSpinner.setEnabled(true);
        stopTimerBtn.setEnabled(false);
        startTimerBtn.setEnabled(true);
        finalTime = 0;
        timerHandler.removeCallbacks(updateTimerMethod);
        trackingHandler.removeCallbacks(timerRepeat);
    }

    private Runnable updateTimerMethod = new Runnable() {
        public void run() {
            finalTime = SystemClock.uptimeMillis() - startTime;
            int seconds = (int) (finalTime / 1000);
            int minutes = seconds / 60;
            int hours = minutes / 60;
            seconds = seconds % 60;
            textTimer.setText(getFormatedTime(seconds, minutes, hours));
            timerHandler.postDelayed(this, 0);
        }

    };

    @NonNull
    private String getFormatedTime(int seconds, int minutes, int hours) {
        return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":"
                + String.format("%02d", seconds);
    }

    private Runnable timerRepeat = new Runnable() {
        @Override
        public void run() {
            Log.i("timer loop", "*****" + new Date() + "******");
            Location location = updateCurrentLocation();

            if (location != null) {
                UserEntry entry = new UserEntry(
                        tripId,
                        routeSpinner.getSelectedItem().toString(),
                        location.getLatitude(),
                        location.getLongitude()
                );

                itemDao.addEntry(entry);
            }
            trackingHandler.postDelayed(this, 5000);
        }
    };

    public String getTripId() {
        return UUID.randomUUID().toString();
    }

    LocationManager lm;

    public Location getLastKnownLocation(){
        List<String> providers = lm.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = lm.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }


    @AfterViews
    public void preLoc() {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    public Location updateCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);// TODO: Consider calling
            return null;
        }
        return getLastKnownLocation();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateCurrentLocation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }

    }
}





























