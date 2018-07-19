package com.getcarvi.chronossharing.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * An object representing the User. A new User object is created for each new account and stored
 * in the Firebase Database. Users are accessed by their unique User ID, created by a hash function
 * from Firebase Authentication.
 *
 * @author Yuji Takai
 * @version 1
 */
public class User {
    private String name;
    private String email;
    private boolean isAdmin;
    private String building;
    private List<Reservation> reservations;
    private double reservedHours;
    /**
     * An empty constructor, required by the Firebase Authentication service.
     */
    public User() {
    }

    /**
     * The default constructor, takes in a Name and Email from signup
     * @param name - User's name
     * @param email - User's email (and email linked with authentication)
     */
    public User(String name, String email) {
        this(name, email, false, null, new ArrayList<Reservation>(), 0); }

    /**
     * Another constructor, taking in the reserveHours
     * @param name - User's name
     * @param email - User's email
     * @param isAdmin - if the user is an admin
     * @param building - building the user works at
     * @param reservations - list of reservations made by the user
     * @param reserveHours - Number of reservation hours made by the user in the month
     */
    public User(String name, String email, boolean isAdmin, String building,
                List<Reservation> reservations, double reserveHours) {
        this.name = name;
        this.email = email;
        this.isAdmin = isAdmin;
        this.building = building;
        this.reservations = reservations;
        this.reservedHours = 0;
    }

    /**
     * Getter for Name
     * @return user's name
     */
    public CharSequence getName() { return this.name; }

    /**
     * Setter for Name
     * @param name user's name
     */
    public void setName(String name) { this.name = name; }

    /**
     * Getter for Email
     * @return user's email
     */
    public CharSequence getEmail() { return this.email; }

    /**
     * Setter for Email
     * @param email user's email
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Getter for isAdmin
     * @return if the user is an admin
     */
    public boolean getIsAdmin() { return this.isAdmin; }

    /**
     * Setter for isAdmin
     * @param isAdmin if the user is an admin
     */
    public void setIsAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }

    /**
     * Getter for building
     * @return building where the user works at
     */
    public String getBuilding() { return this.building; }

    /**
     * Setter for building
     * @param building the building the user works at
     */
    public void setBuilding(String building) { this.building = building; }

    /**
     * Getter for reservations
     * @return the reservations made by the user
     */
    public List<Reservation> getReservations() { return this.reservations; }

    /**
     * Setter for reservations
     * @param reservations user's reservations
     */
    public void setReservations(List<Reservation> reservations) { this.reservations = reservations; }


    /**
     * Getter for ReservedHours
     * @return user's reserved hours for the month
     */
    public double getReservedHours() { return this.reservedHours; }

    /**
     * Setter for ReservedHours
     * @param reservedHours user's reserved hours
     */
    public void setReservedHours(double reservedHours) { this.reservedHours = reservedHours; }
}
