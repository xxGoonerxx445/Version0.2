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
    private ArrayList<Record> recordsList = new ArrayList<>();

    private FB() {
        database = FirebaseDatabase.getInstance();
        setupRecordsListener();
    }

    public static FB getInstance() {
        if (null == instance) {
            instance = new FB();
        }
        return instance;
    }

    private void setupRecordsListener() {
        Query myQuery = database.getReference("records").orderByChild("score").limitToLast(10);
        myQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                recordsList.clear();
                for(DataSnapshot userSnapshot : snapshot.getChildren()) {
                    Record currentRecord = userSnapshot.getValue(Record.class);
                    if (currentRecord != null) {
                        recordsList.add(0, currentRecord);
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
        if (uid == null) return;

        // 1. Get user name from database
        database.getReference("users").child(uid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
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
        return recordsList;
    }
}
