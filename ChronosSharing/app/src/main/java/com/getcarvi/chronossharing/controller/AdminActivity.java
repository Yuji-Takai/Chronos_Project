package com.getcarvi.chronossharing.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.getcarvi.chronossharing.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    private Button mManageBtn;
    private ListView mListView;

    private VehicleManager vehicleManager;
    private String building;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("user");
        mListView = findViewById(R.id.vehicleList);

        building = getIntent().getStringExtra("building");
        vehicleManager = new VehicleManager(building);
        ListAdapter listAdapter = new ArrayAdapter<>(AdminActivity.this, android.R.layout.simple_list_item_1, vehicleManager.getVehicleList());
        mListView.setAdapter(listAdapter);

        mManageBtn = (Button) findViewById(R.id.addBtn);
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
