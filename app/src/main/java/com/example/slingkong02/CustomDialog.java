package com.example.slingkong02;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

public class CustomDialog extends Dialog implements View.OnClickListener
{
    private Button btnYes, btnNo;
    private BoardGame boardGame;
    private Context context;
    public CustomDialog(@NonNull Context context, BoardGame boardGame) {
        super(context);
        setContentView(R.layout.custom_dialog); // TODO: 2/25/2026 fix

        this.context = context;
        this.boardGame=boardGame; //store reference

        this.btnYes = findViewById(R.id.btnYes);
        btnNo = findViewById(R.id.btnNo);
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) { // TODO: 25/02/2026 add what you need in the future
        // TODO: 3/4/2026 in the future, connect firebase to project and when player chooses to restart,check if it is the highest score, if it is save it.
        //Toast.makeText(getContext(), "onClick", Toast.LENGTH_SHORT).show();
        if(v == btnYes)
        {
            boardGame.restartGame();
            dismiss();
            //here
        }
        if(v == btnNo)
        {
            dismiss(); // TODO: 3/4/2026 add something later like going back to menu
            //here
        }

    }
}
