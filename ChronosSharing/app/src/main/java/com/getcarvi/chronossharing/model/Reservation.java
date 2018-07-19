package com.getcarvi.chronossharing.model;

import java.util.Date;

public class Reservation {
    private Vehicle vehicle;
    private Date date;

    /**
     * empty constructor for Firebase Database construction
     */
    public Reservation() {

    }

    public Reservation(Vehicle vehicle, Date date) {
        this.vehicle = vehicle;
        this.date = date;
    }

    public Vehicle getVehicle() { return this.vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }


    public Date getDate() { return this.date; }
    public void setDate(Date date) { this.date = date; }

}

