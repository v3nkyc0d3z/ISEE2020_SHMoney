package com.example.strawhats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.tooltip.Tooltip;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TransactionForm extends AppCompatActivity {
    private static final String TAG = "TransactionForm";
    private ArrayList<CategoryItem> mCategoryList;
    private ArrayList<ModeItem> mModeList;
    private ArrayList<CurrencyItem> mCurrencyList;
    private CategoryAdapter mAdapter;
    private ModeAdapter nAdapter;
    private CurrencyAdapter cAdapter;
    private TextView DisplayDate;
    private DatePickerDialog.OnDateSetListener DateSetListener;
    public String CategoryPicked;
    public String ModePicked;
    public EditText etAmount;
    public EditText etComment;
    TransactionDatabaseHelper mDatabaseHelper;
    TextView catMenu;
    TextView modeMenu;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/dd/MM");
    ImageView Bold,Italic;
    CurrencyItem CurrencyPicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        final ImageButton Help = (ImageButton) findViewById(R.id.expensescreenhelp);
        Help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tooltip tooltip = new Tooltip.Builder(Help).setText("This is the 'add expense' screen \nUse this page to add your expenses\nClick on this message to make it disappear")
                        .setTextColor(Color.WHITE).setGravity(Gravity.BOTTOM)
                        .setCornerRadius(8f)
                        .setDismissOnClick(true)
                        .show();
            }
        });
//Initialize the available categories
        initCategoryList();
        initModeList();
        initCurrencyList();
//Get current date
        Calendar calendar = Calendar.getInstance();
        Date currDate = calendar.getTime();
        String scurrDate = sdf.format(currDate);
//        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
//
        TextView textViewDate = findViewById(R.id.TransDate);
        textViewDate.setText(scurrDate);


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
                        android.R.style.Theme_Holo_Dialog,
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
//--------------------------------------------------------------------------------------------------
//-----------------------------------Category Popup Menu--------------------------------------------

        catMenu = findViewById(R.id.textViewCat);
        catMenu.setCompoundDrawablesWithIntrinsicBounds(mCategoryList.get(1).getmCategoryImage(), 0, 0, 0);
        Drawable drawables[] = catMenu.getCompoundDrawables();
        drawables[0].setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        catMenu.setText(mCategoryList.get(1).getmCategoryName());
        CategoryPicked = mCategoryList.get(1).getmCategoryName();
        catMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(TransactionForm.this);
                mBuilder.setTitle("Please pick a category");
                mAdapter = new CategoryAdapter(TransactionForm.this, mCategoryList);

                mBuilder.setSingleChoiceItems(mAdapter, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        ListView lw = ((AlertDialog)dialog).getListView();
                        CategoryItem checkedItem = (CategoryItem) lw.getAdapter().getItem(lw.getCheckedItemPosition());
                        catMenu.setCompoundDrawablesWithIntrinsicBounds(checkedItem.getmCategoryImage(), 0, 0, 0);
                        Drawable drawables[] = catMenu.getCompoundDrawables();
                        drawables[0].setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                        catMenu.setText(checkedItem.getmCategoryName());
//                        catMenu.setText("  " + checkedItem.getmCategoryName());
                        CategoryPicked = checkedItem.getmCategoryName();
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

//--------------------------------------------------------------------------------------------------
//-----------------------------------Payment Popup Menu--------------------------------------------

        modeMenu = findViewById(R.id.textViewMode);
        modeMenu.setCompoundDrawablesWithIntrinsicBounds(mModeList.get(1).getmModeImage(), 0, 0, 0);
        Drawable drawable[] = modeMenu.getCompoundDrawables();
        drawable[0].setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        modeMenu.setText(mModeList.get(1).getmModeName());
        ModePicked = mModeList.get(1).getmModeName();
        modeMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder nBuilder = new AlertDialog.Builder(TransactionForm.this);
                nBuilder.setTitle("Please pick a payment mode");
                nAdapter = new ModeAdapter(TransactionForm.this, mModeList);

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


//--------------------------------------------------------------------------------------------------
        String preferedCurrency = "EUR";
        etAmount = (EditText) findViewById(R.id.etAmount);
        UserDatabaseHelper userDB = new UserDatabaseHelper(this);
        Cursor cursor = userDB.getData();
        while(cursor.moveToNext()){
            preferedCurrency = cursor.getString(6);
        }
        etAmount.setHint("0.00"+" " +preferedCurrency);
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
                AlertDialog.Builder nBuilder = new AlertDialog.Builder(TransactionForm.this);
                cAdapter = new CurrencyAdapter(TransactionForm.this, mCurrencyList);
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

//-------------------------Save Button Action-------------------------------------------------------
        Button SaveButton = (Button) findViewById(R.id.btnSave);
        mDatabaseHelper = new TransactionDatabaseHelper(this);

        //final Spinner ModeSelect = (Spinner) findViewById(R.id.PaymentType);
        etComment = (EditText) findViewById(R.id.etTransactionComment);
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

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = DisplayDate.getText().toString();
                String amount = etAmount.getText().toString();
                Float amt = 0f;
                if (amount.length()!= 0) {
                    amt = Float.parseFloat(amount);
                }
                //String mode = ModeSelect.getSelectedItem().toString();
                String comment = HtmlCompat.toHtml(etComment.getText(),HtmlCompat.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE);
                if (comment.length()==0){
                    comment = " ";
                }
                if(amount.length() == 0){
                    toastMessage("Amount should not be empty");
                } else {
                        String editcomment = HandleNewLine(comment);
                        String Currency = CurrencyPicked.getmCurrencyAbbreviation();
                        amt = amt/CurrencyPicked.getCurrencyExchange();
                        addData(date,amt,ModePicked,CategoryPicked,editcomment,Currency);
                        finish();
                    }

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
    private void initModeList(){
        mModeList = new ArrayList<>();
        mModeList.add(new ModeItem("Debit Card",R.drawable.debit_24dp));
        mModeList.add(new ModeItem("Credit Card",R.drawable.ic_credit_card_black_24dp));
        mModeList.add(new ModeItem("Cash",R.drawable.cash_24dp));
        mModeList.add(new ModeItem("PayPal",R.drawable.ic_paypal));
    }
    private void initCurrencyList() {
        mCurrencyList = new ArrayList<>();
        mCurrencyList.add(new CurrencyItem("Rupee", "\u20B9","INR",84.84f));
        mCurrencyList.add(new CurrencyItem("Pound", "£","GBP",0.90f));
        mCurrencyList.add(new CurrencyItem("Yen", "¥","YEN",120.27f));
        mCurrencyList.add(new CurrencyItem("Dollar", "$","USD",1.12f));
        mCurrencyList.add(new CurrencyItem("Euro", "€","EUR",1f));
    }
    public void addData(String date, Float amount, String mode, String category, String comments,String currency){
        boolean insertData = mDatabaseHelper.addTransaction(date,amount,mode,category,comments,"Expense",currency,"Default");
        if (insertData){
            toastMessage("Data Inserted!");
        } else {
            toastMessage("Data insertion Failed");
        }
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
}
