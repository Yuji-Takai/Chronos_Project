package com.getcarvi.chronossharing.controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.getcarvi.chronossharing.R;
import com.getcarvi.chronossharing.model.User;
import com.getcarvi.chronossharing.model.Vehicle;
import com.getcarvi.chronossharing.modelManager.UserManager;
import com.getcarvi.chronossharing.modelManager.VehicleManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class WelcomeActivity extends AppCompatActivity {
    // Firebase authentication
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    // Firebase Database
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;

    // Google sign in
    private static final int RC_SIGN_IN = 21;
    private GoogleSignInClient mGoogleSignInClient;

    // Facebook sign in
    CallbackManager mCallbackManager;

    // Others
    private static final String TAG = "WelcomeActivity";
    public static List<String> uidList;
    private static final int FAILURE = 0;
    private static final int SIGN_UP =1;
    private static final int MAIN = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("user");

        UserManager userManager = new UserManager();
        uidList = userManager.getUidList();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestProfile()
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(WelcomeActivity.this, gso);
        SignInButton mGoogleBtn = (SignInButton) findViewById(R.id.googleSigninBtn);
        mGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        // Facebook Login
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton mFacebookBtn = (LoginButton) findViewById(R.id.facebookSigninBtn);
        mFacebookBtn.setReadPermissions("email", "public_profile");
        mFacebookBtn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess");
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError");
            }
        });

        // Log in button
        Button mLoginBtn = findViewById(R.id.loginBtn);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            }
        });

        // Sign up button
        Button mSignUpBtn = findViewById(R.id.signupBtn);
        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this, SignUpActivity.class));
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                updateUI(FAILURE);
            }
        } else {
            // Pass the activity result back to the Facebook SDK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        Log.d(TAG, "handleFacebookAccessToken:" + accessToken);
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userID = user.getUid();
                            if (uidList.contains(userID)) {
                                updateUI(MAIN);
                            } else {
                                String name = user.getDisplayName();
                                String email = user.getEmail();
                                mRef.child(userID).setValue(new User(name, email, false));
                                updateUI(SIGN_UP);
                            }
                        } else {
                            // If sign in fails, display a message to the user
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Could not register to firebase", Toast.LENGTH_LONG).show();
                            updateUI(FAILURE);
                        }
                    }
                });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userID = user.getUid();
                            if (uidList.contains(userID)) {
                                updateUI(MAIN);
                            } else {
                                String name = user.getDisplayName();
                                String email = user.getEmail();
                                mRef.child(userID).setValue(new User(name, email, false));
                                updateUI(SIGN_UP);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            updateUI(FAILURE);
                        }
                    }
                });
    }

    private void updateUI(int activity) {
        if (activity == MAIN) {
            String userID = mAuth.getCurrentUser().getUid();
            mRef.child(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String userID = mAuth.getCurrentUser().getUid();
                    String building = (String) dataSnapshot.child("building").getValue();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("uid", userID);
                    intent.putExtra("building", building);
                    startActivity(intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else if (activity == SIGN_UP) {
            Intent intent = new Intent(this, UserSetUpActivity.class);
            FirebaseUser user = mAuth.getCurrentUser();
            String userID = user.getUid();
            intent.putExtra("uid", userID);
            startActivity(intent);
        } else {
            Toast.makeText(WelcomeActivity.this, "Authentication Failed. Try Again.", Toast.LENGTH_LONG).show();
        }
    }

}

