package com.getcarvi.chronossharing.controller;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.getcarvi.chronossharing.model.Vehicle;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class VehicleManager {
    private List<Vehicle> vehicleList;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    public VehicleManager (String buildingName) {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("building").child(buildingName);
        vehicleList = new ArrayList<>();
        queryInit();
    }

    private void queryInit() {
        mRef.child("vehicles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot vehicleSnapShot: dataSnapshot.getChildren()) {
                    Vehicle vehicle = new Vehicle();
                    vehicle.setMake((String) vehicleSnapShot.child("make").getValue());
                    vehicle.setModel((String) vehicleSnapShot.child("model").getValue());
                    vehicle.setYear(Integer.parseInt(vehicleSnapShot.child("year").getValue().toString()));
                    vehicle.setVin((String) vehicleSnapShot.child("vin").getValue());
                    if (!vehicleList.contains(vehicle)) {
                        vehicleList.add(vehicle);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public List<Vehicle> getVehicleList() { return vehicleList; }

}
