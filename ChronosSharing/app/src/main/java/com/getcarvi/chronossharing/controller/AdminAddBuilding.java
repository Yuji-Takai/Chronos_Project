package com.getcarvi.chronossharing.controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.getcarvi.chronossharing.R;
import com.getcarvi.chronossharing.model.Building;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class AdminAddBuilding extends AppCompatActivity {
    private EditText mNameField;
    private EditText mAddressField;
    private Button mSubmitBtn;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUserRef;
    private DatabaseReference mBuildingRef;
    private String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_building);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mUserRef = mDatabase.getReference("user");
        mBuildingRef = mDatabase.getReference("building");

        uid = getIntent().getStringExtra("uid");

        mNameField = (EditText) findViewById(R.id.nameField);
        mAddressField = (EditText) findViewById(R.id.addressField);
        mSubmitBtn = (Button) findViewById(R.id.submitBtn);
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mNameField.getText().toString();
                String address = mAddressField.getText().toString();

                Building building = new Building(name, address);
                mBuildingRef.child(name).setValue(building);

                Map<String, Object> userUpdate = new HashMap<>();
                userUpdate.put("building", name);
                mUserRef.child(uid).updateChildren(userUpdate);
                Intent intent = new Intent(AdminAddBuilding.this, AdminActivity.class);
                intent.putExtra("building", name);
                startActivity(intent);
            }
        });


    }
}
