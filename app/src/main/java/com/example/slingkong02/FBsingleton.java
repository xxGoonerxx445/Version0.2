package com.example.slingkong02;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * מחלקת Singleton לניהול הקשר עם Firebase Database.
 * משמשת לריכוז פעולות הכתיבה והקריאה מהמסד נתונים.
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
    public void setName(String name) {
        // בדיקה שיש משתמש מחובר לפני ניסיון כתיבה
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            // יצירת נתיב בתוך ה-Database תחת users -> UID -> name
            DatabaseReference myRef = database.getReference("users/" + FirebaseAuth.getInstance().getUid() + "/name");
            // שמירת הערך
            myRef.setValue(name);
        }
    }
}
