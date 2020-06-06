package com.example.strawhats;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class IncomeForm extends AppCompatActivity {
    private static final String TAG = "IncomeForm";
    private ArrayList<CategoryItem> mCategoryList;
    private static final String NA = "NA";
    private CategoryAdapter mAdapter;
    private TextView DisplayDate;
    private DatePickerDialog.OnDateSetListener DateSetListener;
    public EditText etAmount;
    public EditText etComment;
    TransactionDatabaseHelper mDatabaseHelper;
    TextView catBtn;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_form2);
        initCategoryList();
        //Get current date
        Calendar calendar = Calendar.getInstance();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        String currentDate = dtf.format(now);

//---------------------Date picker section---------------------------------------------------
        DisplayDate = (TextView) findViewById(R.id.IncomeDate);
        DisplayDate.setText(currentDate);
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

        catBtn = findViewById(R.id.textViewCategory);
        catBtn.setCompoundDrawablesWithIntrinsicBounds(mCategoryList.get(1).getmCategoryImage(), 0, 0, 0);
        Drawable drawables[] = catBtn.getCompoundDrawables();
        drawables[0].setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        catBtn.setText("  " + mCategoryList.get(1).getmCategoryName());
        catBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(IncomeForm.this);
                mBuilder.setTitle("Please pick a category");
                mAdapter = new CategoryAdapter(IncomeForm.this, mCategoryList);

                mBuilder.setSingleChoiceItems(mAdapter, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        ListView lw = ((AlertDialog)dialog).getListView();
                        CategoryItem checkedItem = (CategoryItem) lw.getAdapter().getItem(lw.getCheckedItemPosition());
                        catBtn.setCompoundDrawablesWithIntrinsicBounds(checkedItem.getmCategoryImage(), 0, 0, 0);
                        Drawable drawables[] = catBtn.getCompoundDrawables();
                        drawables[0].setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                        catBtn.setText("  " + checkedItem.getmCategoryName());
                        dialog.dismiss();
                }
            });
                mBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }

        });


//-------------------------Save Button Action------------------------------------------------
        Button SaveButton = (Button) findViewById(R.id.btnIncomeSave);
        mDatabaseHelper = new TransactionDatabaseHelper(this);
        etAmount = (EditText) findViewById(R.id.etIncomeAmount);
        etComment = (EditText) findViewById(R.id.etIncomeComment);

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = DisplayDate.getText().toString();
                String amount = etAmount.getText().toString();
                Float amt = Float.parseFloat(amount);
                String category = catBtn.getText().toString();
                String comment = etComment.getText().toString();
                if(amount.length() == 0){
                    toastMessage("Amount should not be empty");
                } else if(comment.length() == 0){
                    toastMessage("comment cannot be empty");
                } else{
                    addData(date,amt,"NA",category,comment);
                }
                finish();
            }
        });

    }
       public void addData(String date, Float amount, String mode, String category, String comments){
        boolean insertData = mDatabaseHelper.addTransaction(date,amount,"NA",category,comments,"Income");
        if (insertData){
            toastMessage("Data Inserted");
        } else {
            toastMessage("Data insertion Failed");
        }
    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    private void initCategoryList(){
        mCategoryList = new ArrayList<>();
        mCategoryList.add(new CategoryItem("Wage",R.drawable.ic_work_black_24dp));
        mCategoryList.add(new CategoryItem("Rental",R.drawable.ic_home_green_24dp));
        mCategoryList.add(new CategoryItem("Interest",R.drawable.ic_attach_money_black_24dp));
        mCategoryList.add(new CategoryItem("Other",R.drawable.ic_turned_other_24dp));
    }
}
