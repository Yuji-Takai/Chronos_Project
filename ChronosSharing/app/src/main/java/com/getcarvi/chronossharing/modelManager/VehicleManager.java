package com.getcarvi.chronossharing.modelManager;

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
    private DatabaseReference mRef;

    public VehicleManager () {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("vehicle");
        vehicleList = new ArrayList<>();
        queryInit();
    }

    private void queryInit() {
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot vehicleSnapShot: dataSnapshot.getChildren()) {
                    Vehicle vehicle = vehicleSnapShot.getValue(Vehicle.class);
                    if (!vehicleList.contains(vehicle)) { vehicleList.add(vehicle); }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public List<Vehicle> getVehicleList(String building) {
        List<Vehicle> list = new ArrayList<>();
        for (Vehicle vehicle: vehicleList) {
            if (vehicle.getBuilding().equals(building)) { list.add(vehicle); }
        }
        return list;
    }
}
