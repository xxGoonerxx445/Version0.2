package com.example.slingkong02;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

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
