package com.example.slingkong02;

import android.icu.text.Transliterator;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * מחלקת Singleton לניהול הקשר עם Firebase Database.
 * משמשת לריכוז פעולות הכתיבה והקריאה מהמסד נתונים.
 * אחראית על הגדרות ראשוניות של המשתמש
 */
public class FBsingleton {
    private static FBsingleton instance;
    private FirebaseDatabase database;

    private FBsingleton() {
        // אתחול המופע של ה-Database
        database = FirebaseDatabase.getInstance();
    }

    /**
     * מחזירה את המופע היחיד של המחלקה (Singleton).
     */
    public static FBsingleton getInstance() {
        if (null == instance) {
            instance = new FBsingleton();
        }
        return instance;
    }

    /**
     * שמירת שם המשתמש ב-Firebase Realtime Database.
     * @param name השם שהוזן על ידי המשתמש בהרשמה.
     */
    public void setName(String name) { //נקראת רק במעמד ההרשמה של משתמש חדש.
        // בדיקה שיש משתמש מחובר לפני ניסיון כתיבה
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DatabaseReference myRef = database.getReference("records/" + FirebaseAuth.getInstance().getUid()); //creates the path if it doesn't exist yet (all ready exists)
            Record record = new Record(name, 0);
            myRef.setValue(record);



        }
    }
}
