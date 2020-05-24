package com.example.strawhats;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class IncomeForm extends AppCompatActivity {
    private static final String TAG = "IncomeForm";
    private static final String NA = "NA";
    private CategoryAdapter mAdapter;
    private TextView DisplayDate;
    private DatePickerDialog.OnDateSetListener DateSetListener;
    public EditText etAmount;
    public EditText etComment;
    TransactionDatabaseHelper mDatabaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_form);

//---------------------Date picker section---------------------------------------------------
        DisplayDate = (TextView) findViewById(R.id.TransDate);
        DisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        IncomeForm.this,
                        android.R.style.Theme_Holo_Light_Dialog,
                        DateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        DateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String Date = year + "/" + dayOfMonth + "/" + month;
                Log.d(TAG,"ondate set DATE="+year +"/"+month+"/" +dayOfMonth);
                DisplayDate.setText(Date);

            }
        };

//-------------------------Save Button Action------------------------------------------------
        Button SaveButton = (Button) findViewById(R.id.btnSave);
        mDatabaseHelper = new TransactionDatabaseHelper(this);
        etAmount = (EditText) findViewById(R.id.etAmount);
        final Spinner ModeSelect = (Spinner) findViewById(R.id.PaymentType);
        etComment = (EditText) findViewById(R.id.etTransactionComment);

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = DisplayDate.getText().toString();
                String amount = etAmount.getText().toString();
                Float amt = Float.parseFloat(amount);
                String comment = etComment.getText().toString();
                if(amount.length() == 0){
                    toastMessage("Amount should not be empty");
                } else if(comment.length() == 0){
                    toastMessage("comment cannot be empty");
                } else{
                    addData(date,amt,"NA","NA",comment);
                }
                finish();
            }
        });

    }
       public void addData(String date, Float amount, String mode, String category, String comments){
        boolean insertData = mDatabaseHelper.addTransaction(date,amount,"NA","NA",comments,"Income");
        if (insertData){
            toastMessage("Data Inserted");
        } else {
            toastMessage("Data insertion Failed");
        }
    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
