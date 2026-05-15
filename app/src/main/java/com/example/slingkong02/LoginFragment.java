package com.example.slingkong02;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Fragment האחראי על מסך ההתחברות (Login).
 * משתמש ב-Firebase Auth לצורך אימות משתמשים קיימים.
 */
public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth; // משתנה לניהול האימות מול Firebase
    private EditText etEmail, etPassword; // שדות קלט למייל וסיסמה
    private Button btnLogin; // כפתור התחברות

    public LoginFragment() {
        // בנאי ריק נדרש עבור Fragments
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // טעינת ה-Layout של ה-Fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. אתחול מופע ה-FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // 2. קישור רכיבי הממשק (UI) מה-XML לקוד ה-Java
        etEmail = view.findViewById(R.id.etEmailAddress);
        etPassword = view.findViewById(R.id.etNumberPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        // 3. הגדרת מאזין ללחיצה על כפתור ההתחברות
        if (btnLogin != null) {
            btnLogin.setOnClickListener(v -> performLogin());
        }
    }

    /**
     * פונקציה המבצעת את תהליך ההתחברות מול Firebase.
     */
    private void performLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // בדיקה בסיסית שכל השדות מולאו
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "אנא הזן מייל וסיסמה", Toast.LENGTH_SHORT).show();
            return;
        }

        // ביצוע התחברות באמצעות אימייל וסיסמה
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        // התחברות הצליחה! מעבר למסך התפריט הראשי
                        Intent intent = new Intent(getActivity(), MenuActivity.class);
                        startActivity(intent);
                        // סגירת ה-Activity הנוכחית כדי למנוע חזרה למסך ההתחברות
                        requireActivity().finish();
                    } else {
                        // התחברות נכשלה - הצגת הודעת שגיאה מהשרת
                        String error = task.getException() != null ? task.getException().getMessage() : "שגיאה לא ידועה";
                        Toast.makeText(getContext(), "התחברות נכשלה: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
