package com.example.strawhats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SecurityQ extends AppCompatActivity {


    EditText SQA;
    Button Submit;
    TextView SQQ;
    UserDatabaseHelper userDatabaseHelper;
    // Boolean isDataAvailable;
    String SQuestion, SAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_q);
        userDatabaseHelper = new UserDatabaseHelper(this);
        SQQ = findViewById(R.id.tvSQ);
        SQA = findViewById(R.id.eSA);
        Submit = findViewById(R.id.submit);
        Cursor data = userDatabaseHelper.getData();
        while (data.moveToNext()) {
            SQuestion = data.getString(5);
            SAnswer = data.getString(4);
        }
        SQQ.setText(SQuestion);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SQ = SQQ.getText().toString();
                String SA = SQA.getText().toString();
                if (SQ.equals(SQuestion) && (SA.equals(SAnswer))) {
                    Toast.makeText(SecurityQ.this, "correct", Toast.LENGTH_SHORT).show();
                    Intent reset = new Intent(getApplicationContext(),ResetPassword.class);
                    startActivity(reset);
                    finish();
                } else {
                    Toast.makeText(SecurityQ.this, "Invalid Answer", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}
