package com.example.strawhats;
/**pretty complex and delicate code! Do not touch if you have no idea on what you are doing here */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RepeatTransaction extends AppCompatActivity {
    private ArrayList<CategoryItem> mIncomeCategoryList,mExpenseCategoryList;
    private ArrayList<ModeItem> mModeList;
    private ArrayList<CurrencyItem> mCurrencyList;
    private ModeAdapter nAdapter;
    private CurrencyAdapter cAdapter;
    private static final String TAG = "EditTransaction";
    private CategoryAdapter mAdapter;
    private DatePickerDialog.OnDateSetListener DateSetListener;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/dd/MM");
    TransactionDatabaseHelper transactionDatabaseHelper;
    public String ModePicked;
    CurrencyItem CurrencyPicked;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIncomeCategoryList();
        initExpenseCategoryList();
        initModeList();
        initCurrencyList();
        final Intent intent = getIntent();
        final TransactionList transaction = intent.getParcelableExtra("Transaction Object");
        String type = transaction.getType();
        transactionDatabaseHelper = new TransactionDatabaseHelper(this);
        if (type.equals("Income")) {
            setContentView(R.layout.activity_income_form2);
            TextView Title = (TextView) findViewById(R.id.IncomeTitle);
            Title.setText("Edit Income");
            final TextView DisplayDate = (TextView) findViewById(R.id.IncomeDate);
            DisplayDate.setText(transaction.getDate());
            DisplayDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Date tdate = new Date();
                    try {
                        tdate = sdf.parse(transaction.getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar cal = new GregorianCalendar();
                    cal.setTime(tdate);
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog dialog = new DatePickerDialog(
                            RepeatTransaction.this,
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
            final EditText etAmount = (EditText) findViewById(R.id.etIncomeAmount);
            etAmount.setText(transaction.getAmount());
            final TextView catBtn = (TextView) findViewById(R.id.textViewCategory);
            for (int i = 0;i<mIncomeCategoryList.size();i++){
                if (mIncomeCategoryList.get(i).getmCategoryName().equals(transaction.getCategory())){
                    catBtn.setCompoundDrawablesWithIntrinsicBounds(mIncomeCategoryList.get(i).getmCategoryImage(), 0, 0, 0);
                    break;
                } else {
                    catBtn.setCompoundDrawablesWithIntrinsicBounds(mIncomeCategoryList.get(1).getmCategoryImage(), 0, 0, 0);
                }
            }
            Drawable drawables[] = catBtn.getCompoundDrawables();
            drawables[0].setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            catBtn.setText(transaction.getCategory());
            catBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(RepeatTransaction.this);
                    mBuilder.setTitle("Please pick a category");
                    mAdapter = new CategoryAdapter(RepeatTransaction.this, mIncomeCategoryList);

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
//==========================================Currency setting=======================================================
            String transactionCurrency = transaction.getCurrency();
            final TextView Currency = (TextView) findViewById(R.id.textViewCurrency);
            for(int i = 0;i<mCurrencyList.size();i++){
                if(mCurrencyList.get(i).getmCurrencyAbbreviation().equals(transactionCurrency)){
                    Currency.setText(mCurrencyList.get(i).getCurrencySymbol() + " " + mCurrencyList.get(i).getCurrencyName());
                    CurrencyPicked = mCurrencyList.get(i);
                }
            }
            Currency.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder nBuilder = new AlertDialog.Builder(RepeatTransaction.this);
                    cAdapter = new CurrencyAdapter(RepeatTransaction.this, mCurrencyList);
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


            final EditText etComment = (EditText) findViewById(R.id.etIncomeComment);
            String cmt = transaction.getComment();
            etComment.setText(HtmlCompat.fromHtml(cmt,0));
            ImageView Bold,Italic;
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
            Button save = (Button) findViewById(R.id.btnIncomeSave);
            save.setText("Repeat");
            save.setOnClickListener(new View.OnClickListener() {
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
                    } else {
                        String editcomment = HandleNewLine(comment);
                        String currency = CurrencyPicked.getmCurrencyAbbreviation();
                        int id = transaction.getId();
                        addData(date,amt,"NA",category,editcomment,"Income",currency);
                        finish();
                    }
                }
            });
        }
//==================================EXPENSE SECTION================================================================================================
        else
        {

            setContentView(R.layout.activity_expense);
            TextView Title = (TextView) findViewById(R.id.NewExpense);
            Title.setText("Edit Expense");
            final TextView DisplayDate = (TextView) findViewById(R.id.TransDate);
            DisplayDate.setText(transaction.getDate());
            DisplayDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Date tdate = new Date();
                    try {
                        tdate = sdf.parse(transaction.getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar cal = new GregorianCalendar();
                    cal.setTime(tdate);
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog dialog = new DatePickerDialog(
                            RepeatTransaction.this,
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
            final EditText etAmount = (EditText) findViewById(R.id.etAmount);
            etAmount.setText(transaction.getAmount());
            final TextView catBtn = (TextView) findViewById(R.id.textViewCat);
            for (int i = 0;i<mExpenseCategoryList.size();i++){
                if (mExpenseCategoryList.get(i).getmCategoryName().equals(transaction.getCategory())){
                    catBtn.setCompoundDrawablesWithIntrinsicBounds(mExpenseCategoryList.get(i).getmCategoryImage(), 0, 0, 0);
                    break;
                } else {
                    catBtn.setCompoundDrawablesWithIntrinsicBounds(mExpenseCategoryList.get(1).getmCategoryImage(), 0, 0, 0);
                }
            }
            Drawable drawables[] = catBtn.getCompoundDrawables();
            drawables[0].setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            catBtn.setText(transaction.getCategory());
            catBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(RepeatTransaction.this);
                    mBuilder.setTitle("Please pick a category");
                    mAdapter = new CategoryAdapter(RepeatTransaction.this, mExpenseCategoryList);

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
            final TextView modeMenu = findViewById(R.id.textViewMode);
            for (int i = 0;i<mModeList.size();i++){
                if (mModeList.get(i).getmModeName().equals(transaction.getMode())){
                    modeMenu.setCompoundDrawablesWithIntrinsicBounds(mModeList.get(i).getmModeImage(), 0, 0, 0);
                    break;
                } else {
                    modeMenu.setCompoundDrawablesWithIntrinsicBounds(mModeList.get(1).getmModeImage(), 0, 0, 0);
                }
            }
            Drawable drawable[] = modeMenu.getCompoundDrawables();
            drawable[0].setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            modeMenu.setText(transaction.getMode());

            modeMenu.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    AlertDialog.Builder nBuilder = new AlertDialog.Builder(RepeatTransaction.this);
                    nBuilder.setTitle("Please pick a payment mode");
                    nAdapter = new ModeAdapter(RepeatTransaction.this, mModeList);

                    nBuilder.setSingleChoiceItems(nAdapter, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            ListView lw = ((AlertDialog)dialog).getListView();
                            ModeItem checkedItem = (ModeItem) lw.getAdapter().getItem(lw.getCheckedItemPosition());
                            modeMenu.setCompoundDrawablesWithIntrinsicBounds(checkedItem.getmModeImage(), 0, 0, 0);
                            Drawable drawables[] = modeMenu.getCompoundDrawables();

                            drawables[0].setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

                            modeMenu.setText(checkedItem.getmModeName());
                            ModePicked = checkedItem.getmModeName();
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
            String transactionCurrency = transaction.getCurrency();
            final TextView Currency = (TextView) findViewById(R.id.textViewCurrency);
            for(int i = 0;i<mCurrencyList.size();i++){
                if(mCurrencyList.get(i).getmCurrencyAbbreviation().equals(transactionCurrency)){
                    Currency.setText(mCurrencyList.get(i).getCurrencySymbol() + " " + mCurrencyList.get(i).getCurrencyName());
                    CurrencyPicked = mCurrencyList.get(i);
                }
            }
            Currency.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder nBuilder = new AlertDialog.Builder(RepeatTransaction.this);
                    cAdapter = new CurrencyAdapter(RepeatTransaction.this, mCurrencyList);
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


            final EditText etComment = (EditText) findViewById(R.id.etTransactionComment);
            String cmt = transaction.getComment();
            etComment.setText(HtmlCompat.fromHtml(cmt,0));
            ImageView Bold,Italic;
            Bold = (ImageView) findViewById(R.id.fmtBoldExpense);
            Italic = (ImageView) findViewById(R.id.fmtItalicExpense);
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
            Button save = (Button) findViewById(R.id.btnSave);
            save.setText("Repeat");
            save.setOnClickListener(new View.OnClickListener() {
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
                    } else {
                        String editcomment = HandleNewLine(comment);
                        int id = transaction.getId();
                        String currency = CurrencyPicked.getmCurrencyAbbreviation();
                        addData(date,amt,ModePicked,category,editcomment,"Expense",currency);
                        TransactionList updatedTransaction = new TransactionList(id,date,amount,ModePicked,category,editcomment,"Income","you got",currency);
                        setResult(Activity.RESULT_OK,new Intent().putExtra("updatedTransaction",updatedTransaction));
                        finish();
                    }
                }
            });
        }
    }

    private void initIncomeCategoryList(){
        mIncomeCategoryList = new ArrayList<>();
        mIncomeCategoryList.add(new CategoryItem("Wage",R.drawable.ic_work_black_24dp));
        mIncomeCategoryList.add(new CategoryItem("Rental",R.drawable.ic_home_green_24dp));
        mIncomeCategoryList.add(new CategoryItem("Interest",R.drawable.ic_attach_money_black_24dp));
        mIncomeCategoryList.add(new CategoryItem("Other",R.drawable.ic_turned_other_24dp));
    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
    public String HandleNewLine(String str){
        String last2 = str.substring(str.length()-1);
        if (last2.equals("\n")){
            str = str.substring(0,str.length()-1);
        }
        return str;
    }
    public void updateData(int id, String date, Float amount, String mode, String category, String comments,String type,String currency,String profile){
        Boolean res = transactionDatabaseHelper.updateData(id,date,amount,mode,category,comments,type,currency,profile);
    }
    private void initExpenseCategoryList(){
        mExpenseCategoryList = new ArrayList<>();
        mExpenseCategoryList.add(new CategoryItem("Shopping",R.drawable.ic_shopping_category));
        mExpenseCategoryList.add(new CategoryItem("Entertainment",R.drawable.ic_entertainment_category));
        mExpenseCategoryList.add(new CategoryItem("Rental",R.drawable.ic_home_category));
        mExpenseCategoryList.add(new CategoryItem("Hospital",R.drawable.ic_hospital_category));
    }
    private void initModeList(){
        mModeList = new ArrayList<>();
        mModeList.add(new ModeItem("Debit Card",R.drawable.debit_24dp));
        mModeList.add(new ModeItem("Credit Card",R.drawable.ic_credit_card_black_24dp));
        mModeList.add(new ModeItem("Cash",R.drawable.cash_24dp));
        mModeList.add(new ModeItem("PayPal",R.drawable.ic_paypal));
    }
    private void initCurrencyList() {
        mCurrencyList = new ArrayList<>();
        mCurrencyList.add(new CurrencyItem("Rupee", "\u20B9","INR"));
        mCurrencyList.add(new CurrencyItem("Pound", "£","GBP"));
        mCurrencyList.add(new CurrencyItem("Yen", "¥","YEN"));
        mCurrencyList.add(new CurrencyItem("Dollar", "$","USD"));
        mCurrencyList.add(new CurrencyItem("Euro", "€","EUR"));
    }
    public void addData(String date, Float amount, String mode, String category, String comments,String type,String currency){
        boolean insertData = transactionDatabaseHelper.addTransaction(date,amount,mode,category,comments,type,currency,"Default");
        if (insertData){
            toastMessage("Data Inserted!");
        } else {
            toastMessage("Data insertion Failed");
        }
    }
}
