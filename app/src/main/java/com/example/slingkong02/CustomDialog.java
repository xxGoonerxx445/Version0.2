package com.example.slingkong02;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

public class CustomDialog extends Dialog implements View.OnClickListener
{
    private Button btnYes, btnNo;
    private Context context;
    public CustomDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.custom_dialog);



        this.context = context;

        this.btnYes = findViewById(R.id.btnYes);
        btnNo = findViewById(R.id.btnNo);
        btnYes.setOnClickListener(this);
        btnNo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) { // TODO: 25/02/2026 add what you need in the future
        //Toast.makeText(getContext(), "onClick", Toast.LENGTH_SHORT).show();
        if(v == btnYes)
        {
            dismiss();
            //here
        }
        if(v == btnNo)
        {
            //here
        }

    }
}
