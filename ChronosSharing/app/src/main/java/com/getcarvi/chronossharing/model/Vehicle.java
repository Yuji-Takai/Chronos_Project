package com.getcarvi.chronossharing.model;

/**
 * An object representing the Vehicle. A new User object is created for each new account and stored
 * in the Firebase Database. Users are accessed by their unique User ID, created by a hash function
 * from Firebase Authentication.
 *
 * @author Yuji Takai
 * @version 1
 */
public class Vehicle {
    private int year;
    private String make;
    private String model;
    private String vin;
    private String building;

    public Vehicle() {

    }

    public Vehicle(String make, String model, int year, String vin, String building) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.vin = vin;
        this.building = building;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Vehicle.class.isAssignableFrom(obj.getClass())) {
            return false;
        }
        final Vehicle other = (Vehicle) obj;
        return other.vin.equals(this.vin);
    }

    @Override
    public String toString() {
        return this.make + " " + this.model + " " + this.year;
    }

    public int getYear() { return this.year; }
    public void setYear(int year) { this.year = year; }
    public String getMake() { return this.make; }
    public void setMake(String make) { this.make = make; }
    public String getModel() { return this.model; }
    public void setModel(String model) { this.model = model; }
    public String getVin() { return this.vin; }
    public void setVin(String vin) { this.vin = vin; }
    public String getBuilding() { return this.building; }
    public void setBuilding(String location) { this.building = location; }

}
