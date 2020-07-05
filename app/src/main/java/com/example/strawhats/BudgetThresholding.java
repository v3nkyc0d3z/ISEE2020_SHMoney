package com.example.strawhats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.tooltip.Tooltip;

public class BudgetThresholding extends AppCompatActivity {
    EditText etAmount;
    Button btnSet;
    UserDatabaseHelper userDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_thresholding);
        userDatabaseHelper = new UserDatabaseHelper(this);
        etAmount = (EditText) findViewById(R.id.etThreshold);
        btnSet = (Button) findViewById(R.id.btnSetThreshold);
        Float constraint = 0f;
        Cursor data = userDatabaseHelper.getData();
        while(data.moveToNext()){
            constraint = data.getFloat(7);
        }
        etAmount.setText(constraint.toString());

        final ImageButton Help = (ImageButton) findViewById(R.id.thresholdhelp);
        Help.setColorFilter(ContextCompat.getColor(BudgetThresholding.this,R.color.colorWhiteBackgroundDark));
        Help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tooltip tooltip = new Tooltip.Builder(Help).setText("Use this page to set your monthly threshold limit \nSet the value to zero (0) to turn off the threshold \nClick on this message to make it disappear ")
                        .setTextColor(Color.WHITE).setGravity(Gravity.BOTTOM)
                        .setCornerRadius(8f)
                        .setDismissOnClick(true)
                        .show();
            }
        });
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = etAmount.getText().toString();
                Float Famount = Float.parseFloat(amount);
                ContentValues cv = new ContentValues();
                cv.put("constraint",Famount);
//                userDatabaseHelper.updateUserData(cv);
                userDatabaseHelper.rawQuery("UPDATE user SET \'constraint\' = " + amount + " WHERE ID = 1");
                finish();
            }
        });

    }
}
