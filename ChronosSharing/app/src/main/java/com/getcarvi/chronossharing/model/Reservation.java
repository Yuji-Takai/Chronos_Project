package com.getcarvi.chronossharing.model;

import java.util.Date;

public class Reservation {
    private String vehicle;
    private String user;
    private Date date;

    /**
     * empty constructor for Firebase Database construction
     */
    public Reservation() {

    }

    public Reservation(String vehicle, String user, Date date) {
        this.vehicle = vehicle;
        this.user = user;
        this.date = date;
    }

    public String getVehicle() { return this.vehicle; }
    public void setVehicle(String vehicle) { this.vehicle = vehicle; }

    public String getUser() { return this.user; }
    public void setUser(String user) { this.user = user; }


    public Date getDate() { return this.date; }
    public void setDate(Date date) { this.date = date; }

}

