package com.example.slingkong02;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class FB {
    private static FB instance;
    private FirebaseDatabase database;

    private FB() {
        database = FirebaseDatabase.getInstance();
        setupRecordsListener();
    }

    public static FB getInstance() { //singleton to make sure only one database is created.
        //everytime the app opens a new instance that communicates with the database is created.(cuz it was null a first)
        if (null == instance) {
            instance = new FB();
        }
        return instance;
    }
    //So no matter how many times different parts of the app call FB.getInstance(), they all get back the exact same object — not a new one each time.

    private void setupRecordsListener() {
        Query myQuery = database.getReference("records").orderByChild("score").limitToLast(10); //Query is the thing that "asks" the database for data
        myQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MainActivity.records.clear(); //מנקה בשביל לסדר מחדש ולמנוע שכפולים
                //Loops through each record in the snapshot, converts it to a Record object, and inserts it at position 0 (the top of the list) so the highest scores appear first.
                for(DataSnapshot userSnapshot : snapshot.getChildren()) {
                    Record currentRecord = userSnapshot.getValue(Record.class);
                    if (currentRecord != null) {
                        MainActivity.records.add(0, currentRecord);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    /**
     * Saves the high score to Firebase only if it's better than the existing record.
     * It fetches the user's name from 'users/UID/name' first.
     */
    public void saveHighScoreIfBetter(int newScore) {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) return; //stop running

        // 1. Get user name from database
        database.getReference("records").child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot nameSnapshot) {
                String userName = nameSnapshot.getValue(String.class);
                if (userName == null) userName = "Player";

                final String finalName = userName;
                DatabaseReference recordRef = database.getReference("records").child(uid);

                // 2. Check existing high score
                recordRef.child("score").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot scoreSnapshot) {
                        Integer existingScore = scoreSnapshot.getValue(Integer.class);

                        // 3. Update if new score is better
                        if (existingScore == null || newScore > existingScore) {
                            Record newRecord = new Record(finalName, newScore);
                            recordRef.setValue(newRecord);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public ArrayList<Record> getRecords() {
        return MainActivity.records;
    }
}
