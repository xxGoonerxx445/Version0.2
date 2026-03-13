package com.example.slingkong02;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // שימוש ב-Layout הרגיל ללא EdgeToEdge בשלב זה למניעת מסך שחור
        setContentView(R.layout.activity_main);

        frameLayout = findViewById(R.id.frameLayout);
        tabLayout = findViewById(R.id.tabLayout);

        // טעינת הפראגמנט הראשון
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout, new LoginFragment())
                    .commit();
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0: fragment = new LoginFragment(); break;
                    case 1: fragment = new RegistrationFragment(); break;
                }
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, fragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // בדיקה אם המשתמש מחובר
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            moveToMenu();
        }
    }

    private void moveToMenu() {
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
