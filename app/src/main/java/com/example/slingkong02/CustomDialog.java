package com.example.slingkong02;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomDialog extends Dialog implements View.OnClickListener {
    private Button btnYes, btnNo;
    private BoardGame boardGame;
    private Context context;



    public CustomDialog(@NonNull Context context, BoardGame boardGame) {
        super(context);
        setContentView(R.layout.custom_dialog);


        this.context = context;
        this.boardGame = boardGame;

        this.btnYes = findViewById(R.id.btnYes);
        this.btnNo = findViewById(R.id.btnNo);
        
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getWindow() != null) {
            getWindow().setLayout(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            getWindow().setGravity(Gravity.CENTER);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }



    @Override
    public void onClick(View v) {
        if (v == btnYes) {
            boardGame.restartGame();
            dismiss();
        }
        
        if (v == btnNo) {
            // Save score to Firebase only if it's the highest
            int finalScore = boardGame.getScore();
            FB.getInstance().saveHighScoreIfBetter(finalScore);

            dismiss();
            
            // Navigate back to Menu
            Intent intent = new Intent(context, MenuActivity.class);
            context.startActivity(intent);

            // Close the Game Activity
            if (context instanceof android.app.Activity) {
                ((android.app.Activity) context).finish();
            }
        }
    }





}
