package com.example.slingkong02;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button myButton = findViewById(R.id.StartGameX);
        Button LogOutbtn = findViewById(R.id.LogOutbtn);
        Button ScoreBoardbtn = findViewById(R.id.ScoreBoardbtn);
        Button Settingsbtn = findViewById(R.id.Settingsbtn);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BoardGame bg = new BoardGame(MenuActivity.this);
                setContentView(bg);
            }
        });
        LogOutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out the current user from Firebase Authentication
                FirebaseAuth.getInstance().signOut();
                // Create an intent to navigate back to MainActivity (Login/Register screen)
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                // Start the MainActivity
                startActivity(intent);
                // Close the current MenuActivity so the user cannot navigate back to it using the back button
                finish();
            }
        });
        ScoreBoardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent i=new Intent(MenuActivity.this,RecordsActivity.class);
                startActivity(i);
            }
        });





    }

}
