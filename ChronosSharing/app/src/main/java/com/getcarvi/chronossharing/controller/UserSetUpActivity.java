package com.getcarvi.chronossharing.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.getcarvi.chronossharing.R;
import com.getcarvi.chronossharing.model.Vehicle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class UserSetUpActivity extends AppCompatActivity {
    private Spinner mBuildingField;
    private Button mFinishBtn;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserRef;
    private String uid;
    public static List<Vehicle> vehicleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_set_up);


        mDatabase = FirebaseDatabase.getInstance();
        mUserRef = mDatabase.getReference("user");
        uid = getIntent().getStringExtra("uid");


        mBuildingField = (Spinner) findViewById(R.id.buildingField);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, LoginActivity.buildingList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBuildingField.setAdapter(adapter);
        mBuildingField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = (String) adapterView.getItemAtPosition(i);
                Toast.makeText(UserSetUpActivity.this, "selected item:" + selected, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mFinishBtn = (Button) findViewById(R.id.finishBtn);
        mFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String building = (String) mBuildingField.getSelectedItem();
                mUserRef.child(uid).child("building").setValue(building);
                VehicleManager vehicleManager = new VehicleManager(building);
                vehicleList = vehicleManager.getVehicleList();
                Intent intent = new Intent(UserSetUpActivity.this, MainActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("building", building);
                startActivity(intent);
            }
        });
    }
}
