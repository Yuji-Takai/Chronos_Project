package com.getcarvi.chronossharing.controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.getcarvi.chronossharing.R;
import com.getcarvi.chronossharing.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private EditText mNameField;
    private EditText mEmailField;
    private EditText mPasswordField;
    private RadioGroup mAdminField;
    private Button mSignUpBtn;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("user");

        mNameField = (EditText) findViewById(R.id.nameField);
        mEmailField = (EditText) findViewById(R.id.emailField);
        mPasswordField = (EditText) findViewById(R.id.passwordField);
        mAdminField = (RadioGroup) findViewById(R.id.adminField);
        mSignUpBtn = (Button) findViewById(R.id.signupBtn);
        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

    }

    private void signUp() {
        String email = mEmailField.getText().toString();
        String password = mPasswordField.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this,
                                    "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            String userID = mAuth.getCurrentUser().getUid();
                            String choice = ((RadioButton) findViewById(mAdminField.getCheckedRadioButtonId())).getText().toString();
                            boolean isAdmin = "YES".equals(choice);
                            mRef.child(userID).setValue(new User(mNameField.getText().toString(),
                                    mEmailField.getText().toString(), isAdmin));
                            Toast.makeText(SignUpActivity.this,
                                    "New account created successfully.", Toast.LENGTH_SHORT).show();
                            Intent intent = isAdmin ?
                                    new Intent(SignUpActivity.this, AdminAddBuilding.class)
                                    : new Intent(SignUpActivity.this, UserSetUpActivity.class);

                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}
