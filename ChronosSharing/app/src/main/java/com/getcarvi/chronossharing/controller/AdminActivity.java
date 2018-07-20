package com.getcarvi.chronossharing.controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.getcarvi.chronossharing.R;
import com.getcarvi.chronossharing.model.Vehicle;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AdminActivity extends AppCompatActivity {
    private ListView mListView;
    private String building;

    private List<Vehicle> vehicleList;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mListView = findViewById(R.id.vehicleList);
        vehicleList = new ArrayList<>();
        DatabaseReference mVehicleRef = mDatabase.getReference("vehicle");
        mVehicleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                vehicleList.clear();
                for (DataSnapshot vehicleSnapShot: dataSnapshot.getChildren()) {
                    vehicleList.add(vehicleSnapShot.getValue(Vehicle.class));
                }
                listAdapter = new ArrayAdapter <>(AdminActivity.this, android.R.layout.simple_list_item_1, vehicleList);
                mListView.setAdapter(listAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        building = getIntent().getStringExtra("building");
        Button mManageBtn = (Button) findViewById(R.id.addBtn);
        mManageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, AddVehicleActivity.class);
                intent.putExtra("building", building);
                startActivity(intent);
            }
        });
    }
}
