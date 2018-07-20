package com.getcarvi.chronossharing.modelManager;

import android.support.annotation.NonNull;

import com.getcarvi.chronossharing.model.Building;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BuildingManager {
    private List<String> buildingList;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    public BuildingManager () {
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("building");
        buildingList = new ArrayList<>();
        queryInit();
    }

    private void queryInit() {
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot buildingSnapShot: dataSnapshot.getChildren()) {
                    String building = buildingSnapShot.getKey();
                    if (!buildingList.contains(building)) {
                        buildingList.add(building);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public List<String> getBuildingList() { return buildingList; }
}
