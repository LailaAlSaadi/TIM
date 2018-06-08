package com.u689.timetableapp.itemdao;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class ItemDao {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference entries = database.getReference("entries");

    public void addEntry(String route, double lat, double lng) {
        //time
        UserEntry userEntry = new UserEntry(route, lat, lng);
        entries.child(getID()).setValue(userEntry);    }

    public String getID() {
        return UUID.randomUUID().toString();
    }
}