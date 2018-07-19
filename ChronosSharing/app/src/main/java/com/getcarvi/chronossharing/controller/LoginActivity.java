package com.getcarvi.chronossharing.controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.getcarvi.chronossharing.R;
import com.getcarvi.chronossharing.model.Building;
import com.getcarvi.chronossharing.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mLoginBtn;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static List<String> buildingList;

    private String TAG ="LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("user");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() !=null) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        };

        BuildingManager buildingManager = new BuildingManager();
        buildingList = buildingManager.getBuildingList();

        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);
        mLoginBtn = (Button) findViewById(R.id.loginBtn);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignIn();
            }
        });

    }

    private void startSignIn() {
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Fields are empty", Toast.LENGTH_LONG).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Sign in problem", Toast.LENGTH_LONG).show();
                    } else {
                        String uid = mAuth.getCurrentUser().getUid();
                        mRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String uid = mAuth.getCurrentUser().getUid();
                                if (((boolean) dataSnapshot.child("isAdmin").getValue()) && (dataSnapshot.child("building").getValue() == null)) {
                                    Intent intent = new Intent(LoginActivity.this, AdminAddBuilding.class);
                                    intent.putExtra("uid", uid);
                                    startActivity(intent);
                                } else if ((boolean) dataSnapshot.child("isAdmin").getValue()) {
                                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                    intent.putExtra("building", (String) dataSnapshot.child("building").getValue());
                                    startActivity(intent);
                                } else if (dataSnapshot.child("building").getValue() == null){
                                    Intent intent = new Intent(LoginActivity.this, UserSetUpActivity.class);
                                    intent.putExtra("uid", uid);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("uid", uid);
                                    intent.putExtra("building", (String) dataSnapshot.child("building").getValue());
                                    startActivity(intent);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

            });
        }

    }
}
