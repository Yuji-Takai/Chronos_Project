package com.getcarvi.chronossharing.controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.getcarvi.chronossharing.R;
import com.getcarvi.chronossharing.model.Vehicle;
import com.getcarvi.chronossharing.modelManager.VehicleManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class AddVehicleActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mVehicleRef;

    private EditText mMakeField;
    private EditText mModelField;
    private EditText mYearField;
    private EditText mVINField;
    private Button mCompleteBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mVehicleRef = mDatabase.getReference("vehicle");

        mMakeField = (EditText) findViewById(R.id.makeField);
        mModelField = (EditText) findViewById(R.id.modelField);
        mYearField = (EditText) findViewById(R.id.yearField);
        mVINField = (EditText) findViewById(R.id.vinField);
        mCompleteBtn = (Button) findViewById(R.id.completeBtn);
        mCompleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String buildingName = getIntent().getStringExtra("building");
                String make = mMakeField.getText().toString();
                String model = mModelField.getText().toString();
                int year = Integer.parseInt(mYearField.getText().toString());
                String vin = mVINField.getText().toString();
                Vehicle vehicle = new Vehicle(make, model, year, vin, buildingName);
                Map<String, Object> updateVehicle = new HashMap<>();
                updateVehicle.put(vin, vehicle);
                mVehicleRef.updateChildren(updateVehicle);
                Intent intent = new Intent(AddVehicleActivity.this, AdminActivity.class);
                intent.putExtra("building", buildingName);
                startActivity(intent);
            }
        });


    }
}
