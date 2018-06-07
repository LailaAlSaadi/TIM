package com.u689.timetableapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewById
    Spinner citySpinner;


    @ViewById
    Spinner routeSpinner;

    ArrayAdapter<String> routsAdapter;


    @Background
    public void citySpinner(View view) {
        String[] routes = this.getResources().getStringArray(R.array.routes_1);
        routsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, routes);
        citySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                routeSpinner.setAdapter(routsAdapter);
            }
        });
    }
}