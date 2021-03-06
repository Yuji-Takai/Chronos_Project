package com.getcarvi.chronossharing.model;

import java.util.ArrayList;
import java.util.List;

/**
 * An object representing the building
 */
public class Building {
    private String name;
    private String address;
    private double longitude;
    private double latitude;


    /**
     * Empty constructor needed by the Firebase Database construction
     */
    public Building () {

    }

    /**
     * The constructor taking in the name and address of the building
     * @param name
     * @param address
     */
    public Building(String name, String address) { this(name, address, 0, 0);}


    public Building(String name, double longitude, double latitude) { this(name, null, longitude, latitude);}


    public Building(String name, String address, double longitude, double latitude) {
        this.name = name;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public String toString() { return this.name; }

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return this.address; }
    public void setAddress(String address) { this.address = address; }

    public double getLongitude() { return this.longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getLatitude() { return this.latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

}
