package com.u689.timetableapp.itemdao;

import java.io.Serializable;
import java.util.Date;

class UserEntry implements Serializable{
    public final Date creationTime;
    public final String route;
    public final double lat;
    public final double lng;

    public UserEntry(String route, double lat, double lng) {
        this.route = route;
        this.lat = lat;
        this.lng = lng;
        this.creationTime = new Date();
    }
}
