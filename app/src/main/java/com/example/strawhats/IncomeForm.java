package com.example.strawhats;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import com.tooltip.Tooltip;

public class IncomeForm extends AppCompatActivity {
    private static final String TAG = "IncomeForm";
    private ArrayList<CategoryItem> mCategoryList;
    private ArrayList<CurrencyItem> mCurrencyList;
    private static final String NA = "NA";
    private CategoryAdapter mAdapter;
    private CurrencyAdapter cAdapter;
    private TextView DisplayDate;
    private DatePickerDialog.OnDateSetListener DateSetListener;
    public EditText etAmount;
    public EditText etComment;
    TransactionDatabaseHelper mDatabaseHelper;
    TextView catBtn;
    ImageView Bold,Italic;
    String comments;
    CurrencyItem CurrencyPicked;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_form2);
        initCategoryList();
        initCurrencyList();
        //Get current date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/dd/MM");
        String currentDate = sdf.format(new Date());

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
        catBtn.setText(mCategoryList.get(1).getmCategoryName());
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
                        catBtn.setText(checkedItem.getmCategoryName());
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
        final ImageButton Help = (ImageButton) findViewById(R.id.incomescreenhelp);
        Help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tooltip tooltip = new Tooltip.Builder(Help).setText("This is the 'add income' screen \nUse this page to add your new income\nClick on this message to make it disappear")
                        .setTextColor(Color.WHITE).setGravity(Gravity.BOTTOM)
                        .setCornerRadius(8f)
                        .setDismissOnClick(true)
                        .show();
            }
        });
//---------------------------------------------------------------------------------------
        String preferedCurrency = "EUR";
        UserDatabaseHelper userDB = new UserDatabaseHelper(this);
        Cursor cursor = userDB.getData();
        while(cursor.moveToNext()){
            preferedCurrency = cursor.getString(6);
        }
        final TextView Currency = (TextView) findViewById(R.id.textViewCurrency);
        for(int i = 0;i<mCurrencyList.size();i++){
            if(mCurrencyList.get(i).getmCurrencyAbbreviation().equals(preferedCurrency)){
                Currency.setText(mCurrencyList.get(i).getCurrencySymbol() + " " + mCurrencyList.get(i).getCurrencyName());
                CurrencyPicked = mCurrencyList.get(i);
            }
        }
        Currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder nBuilder = new AlertDialog.Builder(IncomeForm.this);
                cAdapter = new CurrencyAdapter(IncomeForm.this, mCurrencyList);
                nBuilder.setSingleChoiceItems(cAdapter, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView lw = ((AlertDialog) dialog).getListView();
                        CurrencyItem checked = (CurrencyItem) lw.getAdapter().getItem(lw.getCheckedItemPosition());
                        Currency.setText(checked.getCurrencySymbol() + " " + checked.getCurrencyName());
                        etAmount.setHint("0.00 " + checked.getmCurrencyAbbreviation());
                        CurrencyPicked = checked;
                        dialog.dismiss();
                    }
                });
                nBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog nDialog = nBuilder.create();
                nDialog.show();
            }
        });



//-------------------------Save Button Action------------------------------------------------
        Button SaveButton = (Button) findViewById(R.id.btnIncomeSave);
        mDatabaseHelper = new TransactionDatabaseHelper(this);
        etAmount = (EditText) findViewById(R.id.etIncomeAmount);
        etComment = (EditText) findViewById(R.id.etIncomeComment);
        Bold = (ImageView) findViewById(R.id.fmtBoldIncome);
        Italic = (ImageView) findViewById(R.id.fmtItalicIncome);
        Bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int startSelection = etComment.getSelectionStart();
                int endSelection = etComment.getSelectionEnd();

                if (startSelection>endSelection){
                    int temp = endSelection;
                    endSelection = startSelection;
                    startSelection = temp;
                }

                Spannable s = etComment.getText();
                s.setSpan(new StyleSpan(Typeface.BOLD),startSelection,endSelection,0);
                System.out.println(s);
                String html = HtmlCompat.toHtml(s,HtmlCompat.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL);
                etComment.setText(HtmlCompat.fromHtml(html,0));
            }
        });
        Italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int startSelection = etComment.getSelectionStart();
                int endSelection = etComment.getSelectionEnd();
                if (startSelection>endSelection){
                    int temp = endSelection;
                    endSelection = startSelection;
                    startSelection = temp;
                }

                Spannable s = etComment.getText();
                s.setSpan(new StyleSpan(Typeface.ITALIC),startSelection,endSelection,0);
                System.out.println(s);
                String html = HtmlCompat.toHtml(s,HtmlCompat.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL);
                etComment.setText(HtmlCompat.fromHtml(html,0));
            }
        });
        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = DisplayDate.getText().toString();
                String amount = etAmount.getText().toString();
                Float amt = 0f;
                if (amount.length() != 0) {
                    amt = Float.parseFloat(amount);
                }
                String category = catBtn.getText().toString();
                String comment = HtmlCompat.toHtml(etComment.getText(),HtmlCompat.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE);
                if (comment.length()==0){
                    comment = " ";
                }
                if(amount.length() == 0){
                    toastMessage("Amount should not be empty");
                }else{
                    String editcomment = HandleNewLine(comment);
                    String currency = CurrencyPicked.getmCurrencyAbbreviation();
                    amt = amt/CurrencyPicked.getCurrencyExchange();
                    addData(date,amt,"NA",category,editcomment,currency);
                    finish();
                }
            }
        });

    }
    public void addData(String date, Float amount, String mode, String category, String comments,String currency){
        boolean insertData = mDatabaseHelper.addTransaction(date,amount,"NA",category,comments,"Income",currency,"Default");
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
    private void initCurrencyList() {
        mCurrencyList = new ArrayList<>();
        mCurrencyList.add(new CurrencyItem("Rupee", "\u20B9","INR",84.84f));
        mCurrencyList.add(new CurrencyItem("Pound", "£","GBP",0.90f));
        mCurrencyList.add(new CurrencyItem("Yen", "¥","YEN",120.27f));
        mCurrencyList.add(new CurrencyItem("Dollar", "$","USD",1.12f));
        mCurrencyList.add(new CurrencyItem("Euro", "€","EUR",1f));
    }
    public String HandleNewLine(String str){
        String last2 = str.substring(str.length()-1);
        if (last2.equals("\n")){
            str = str.substring(0,str.length()-1);
        }
        return str;
    }
}
