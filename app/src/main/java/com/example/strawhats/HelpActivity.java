package com.example.strawhats;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HelpActivity extends AppCompatActivity {
    TransactionDatabaseHelper mDatabaseHelper = new TransactionDatabaseHelper(this);
    Intent intent;


    TextView textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainhelp);

        textview = (TextView) findViewById(R.id.helpcontent);

        String para = "This application is a product of" + "\n" + "'StrawHats Private Limited'. \n" +
                "We keep forward this product to assist you with your daily life. This application helps to to keep track of your regular transactions. You can use this app to add your income, add your expenses, view the pictorial representations of the summary of transactions. You can edit, delete or repeat the transactions aswell. Click on the \"?\" symbol on right top corner of each page to know more about that particular page. \n" +
                "\n" +
                "If you have any problems, suggestions, appraises or any general feedback, please write to us at" + "\n" + "'strawhats.isee2020@gmail.com'."+ "\n" +" we are always happy to assist you.";

        textview.setText(para);
        textview.setMovementMethod(new ScrollingMovementMethod());
        textview.setTextColor(Color.WHITE);
                textview.setGravity(Gravity.FILL_VERTICAL);
                textview.setGravity(Gravity.CENTER_HORIZONTAL);
                        textview.setTextSize((float) 50d);

    }
}