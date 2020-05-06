package com.example.strawhats;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TransactionForm extends AppCompatActivity {
    private static final String TAG = "TransactionForm";
    private ArrayList<CategoryItem> mCategoryList;
    private CategoryAdapter mAdapter;
    private TextView DisplayDate;
    private DatePickerDialog.OnDateSetListener DateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_form);
        initCategoryList();
        DisplayDate = (TextView) findViewById(R.id.TransDate);

        DisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
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
                String Date = year + "/" + dayOfMonth + "/" + month;
                Log.d(TAG,"ondate set DATE="+year +"/"+month+"/" +dayOfMonth);
                DisplayDate.setText(Date);

            }
        };
        Spinner spinnerCategories = findViewById(R.id.Spinner_Category);
        mAdapter = new CategoryAdapter(this,mCategoryList);
        spinnerCategories.setAdapter(mAdapter);

        spinnerCategories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CategoryItem ClickedItem = (CategoryItem) parent.getItemAtPosition(position);
                String ClickedCategoryName = ClickedItem.getmCategoryName();
                Toast.makeText(TransactionForm.this,ClickedCategoryName+" Selected",Toast.LENGTH_SHORT);
                }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void initCategoryList(){
        mCategoryList = new ArrayList<>();
        mCategoryList.add(new CategoryItem("Shopping",R.drawable.ic_shopping_category));
        mCategoryList.add(new CategoryItem("Entertainment",R.drawable.ic_entertainment_category));
    }
}
