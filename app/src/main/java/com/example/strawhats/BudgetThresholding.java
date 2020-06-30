package com.example.strawhats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
