package com.u689.timetableapp.itemdao;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class ItemDao {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference entries = database.getReference("entries");

    public void addEntry(UserEntry entry) {
        entries.child(getID()).setValue(entry);
    }

    public String getID() {
        return UUID.randomUUID().toString();
    }

}