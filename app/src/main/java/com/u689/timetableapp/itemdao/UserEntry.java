package com.u689.timetableapp.itemdao;

import java.io.Serializable;
import java.util.Date;

public class UserEntry implements Serializable {
    public final Date creationTime;
    public final String route;
    public final double lat;
    public final double lng;
    public final String tripId;

    public UserEntry(String tripId, String route, double lat, double lng) {
        this.tripId = tripId;
        this.route = route;
        this.lat = lat;
        this.lng = lng;
        this.creationTime = new Date();
    }
}
