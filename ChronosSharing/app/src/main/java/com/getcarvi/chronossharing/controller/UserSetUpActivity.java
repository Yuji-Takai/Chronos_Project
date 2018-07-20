package com.getcarvi.chronossharing.controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.getcarvi.chronossharing.R;
import com.getcarvi.chronossharing.model.Building;
import com.getcarvi.chronossharing.model.Vehicle;
import com.getcarvi.chronossharing.modelManager.VehicleManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;



public class UserSetUpActivity extends AppCompatActivity {
    private Spinner mBuildingField;
    private List<String> mBuildingList;
    private Button mFinishBtn;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserRef;
    private DatabaseReference mBuildingRef;
    private String uid;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_set_up);

        mDatabase = FirebaseDatabase.getInstance();
        mUserRef = mDatabase.getReference("user");
        mAuth = FirebaseAuth.getInstance();

        mBuildingField = (Spinner) findViewById(R.id.buildingField);
        mBuildingList = new ArrayList<>();
        mBuildingRef = mDatabase.getReference("building");
        mBuildingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mBuildingList.clear();
                for (DataSnapshot buildingSnapShot: dataSnapshot.getChildren()) {
                    mBuildingList.add(buildingSnapShot.getValue(Building.class).getName());
                }
                arrayAdapter = new ArrayAdapter<> (getApplicationContext(), android.R.layout.simple_list_item_1, mBuildingList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mBuildingField.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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


        uid = mAuth.getCurrentUser().getUid();
        mFinishBtn = (Button) findViewById(R.id.finishBtn);
        mFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String building = (String) mBuildingField.getSelectedItem();
                mUserRef.child(uid).child("building").setValue(building);
                Intent intent = new Intent(UserSetUpActivity.this, MainActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("building", building);
                startActivity(intent);
            }
        });
    }
}
