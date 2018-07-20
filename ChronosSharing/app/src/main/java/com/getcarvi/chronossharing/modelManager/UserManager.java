package com.getcarvi.chronossharing.modelManager;


import android.support.annotation.NonNull;

import com.getcarvi.chronossharing.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
//    private final List<User> mUserList;
    private final List<String> mUidList;
    private final DatabaseReference mRef;

    public UserManager() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference("user");
//        mUserList = new ArrayList<User>();
        mUidList = new ArrayList<String>();
//        queryUser();
        queryUid();
    }

    private void queryUid() {
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapShot: dataSnapshot.getChildren()) {
                    String uid = userSnapShot.getKey();
                    if (!mUidList.contains(uid)) {
                        mUidList.add(uid);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public List<String> getUidList () { return mUidList; }

//    private void queryUser () {
//        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot userSnapShot: dataSnapshot.getChildren()) {
//                    User user = new User((String) userSnapShot.child("name").getValue(),
//                            (String) userSnapShot.child("email").getValue(),
//                            ()
//                            );
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//

}
