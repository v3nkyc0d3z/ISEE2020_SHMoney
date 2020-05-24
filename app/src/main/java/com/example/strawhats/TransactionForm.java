package com.example.strawhats;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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

public class TransactionForm extends AppCompatActivity {
    private static final String TAG = "TransactionForm";
    private ArrayList<CategoryItem> mCategoryList;
    private CategoryAdapter mAdapter;
    private TextView DisplayDate;
    private DatePickerDialog.OnDateSetListener DateSetListener;
    public String CategoryPicked;
    public EditText etAmount;
    public EditText etComment;
    TransactionDatabaseHelper mDatabaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_form);
//Initialize the available categories
        initCategoryList();
//---------------------Date picker section---------------------------------------------------
        DisplayDate = (TextView) findViewById(R.id.TransDate);
        DisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog;
                dialog = new DatePickerDialog(
                        TransactionForm.this,
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
                //Date format
                String Date = year + "/" + dayOfMonth + "/" + month;
                Log.d(TAG,"ondate set DATE="+year +"/"+month+"/" +dayOfMonth);
                DisplayDate.setText(Date);

            }
        };
//-------------------------------------------------------------------------------------
//-------------------------Category Spinner---------------------------------------------
        Spinner spinnerCategories = findViewById(R.id.Spinner_Category);
        mAdapter = new CategoryAdapter(this,mCategoryList);
        spinnerCategories.setAdapter(mAdapter);

        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CategoryItem ClickedItem = (CategoryItem) parent.getItemAtPosition(position);
                String ClickedCategoryName = ClickedItem.getmCategoryName();
                CategoryPicked = ClickedCategoryName;
                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//-------------------------------------------------------------------------------------------
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
                String mode = ModeSelect.getSelectedItem().toString();
                String comment = etComment.getText().toString();
                if(amount.length() == 0){
                    toastMessage("Amount should not be empty");
                } else if(comment.length() == 0){
                    toastMessage("comment cannot be empty");
                } else if(mode.equals("None")){
                    toastMessage("Enter Mode of Payment");
                }else {
                        addData(date,amt,mode,CategoryPicked,comment);
                    }
                finish();
            }
        });

    }
    private void initCategoryList(){
        mCategoryList = new ArrayList<>();
        mCategoryList.add(new CategoryItem("Shopping",R.drawable.ic_shopping_category));
        mCategoryList.add(new CategoryItem("Entertainment",R.drawable.ic_entertainment_category));
        mCategoryList.add(new CategoryItem("Rental",R.drawable.ic_home_category));
        mCategoryList.add(new CategoryItem("Hospital",R.drawable.ic_hospital_category));
    }
    public void addData(String date, Float amount, String mode, String category, String comments){
        boolean insertData = mDatabaseHelper.addTransaction(date,amount,mode,category,comments,"expense");
        if (insertData){
            toastMessage("Data Inserted!");
        } else {
            toastMessage("Data insertion Failed");
        }
    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
