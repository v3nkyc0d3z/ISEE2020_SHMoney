package com.example.strawhats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.nio.file.Watchable;
import java.util.ArrayList;
import java.util.List;

public class TransactionSummary extends AppCompatActivity implements TextWatcher {

    private static final String Tag = "TransactionSummary";
    TransactionDatabaseHelper mDatabaseHelper ;
    private ListView mlistView;
    ArrayList<TransactionList> listData = new ArrayList<>();
    public TransactionListAdapter myAdapter;
    String[] Modes = {"Payment Mode","Cash","Debit Card"};
    Spinner ModeSpinner,CategoriesSpinner,typeSpinner;
    Button reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_summary);
        EditText filter = (EditText) findViewById(R.id.etFilterDescription);

        mlistView = (ListView) findViewById(R.id.lvAllTransactions);
        filter.addTextChangedListener(this);
        mDatabaseHelper = new TransactionDatabaseHelper(this);

        ModeSpinner = (Spinner) findViewById(R.id.spModeFilter);
        ModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    String Mode = parent.getItemAtPosition(position).toString();
                    myAdapter.getFilter().filter(Mode);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        CategoriesSpinner = (Spinner) findViewById(R.id.spCategoryFilter);
        CategoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String Category = parent.getItemAtPosition(position).toString();
                    myAdapter.getFilter().filter(Category);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        typeSpinner = (Spinner) findViewById(R.id.spTypeFilter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0){
                    String type = parent.getItemAtPosition(position).toString();
                    myAdapter.getFilter().filter(type);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        reset = (Button) findViewById(R.id.btnFilterReset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               myAdapter = new TransactionListAdapter(TransactionSummary.this,listData);
               mlistView.setAdapter(myAdapter);
               typeSpinner.setSelection(0);
               CategoriesSpinner.setSelection(0);
               ModeSpinner.setSelection(0);
            }
        });
        populateList();

        myAdapter = new TransactionListAdapter(this,listData);
        mlistView.setAdapter(myAdapter);

        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TransactionSummary.this,TransactionDetails.class);
                intent.putExtra("Transaction Item",myAdapter.Original.get(position));
                startActivity(intent);
            }
        });
    }

    private void populateList() {
        Log.d(Tag, "populating List View");
        Cursor data = mDatabaseHelper.getData();
        String content = "";
        String action;
        String amt;
        while(data.moveToNext()){
            int id = data.getInt(0);
            String date = data.getString(1);
            Float amount = data.getFloat(2);
            amt = Float.toString(amount);
            String mode = data.getString(3);
            String category = data.getString(4);
            String comment = data.getString(5);
            String type = data.getString(6);
            if (type.equals("expense")){
                action = "you spent";
            } else {  action = "you got";}
            TransactionList transaction = new TransactionList(id,date,amt,mode,category,comment,type,action);
            listData.add(transaction);
        }
//       TransactionListAdapter adapter = new TransactionListAdapter(this,R.layout.adapter_transaction_list,listData);
//        mlistView.setAdapter(adapter);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        this.myAdapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        listData.clear();
        populateList();
        myAdapter = new TransactionListAdapter(TransactionSummary.this,listData);
        mlistView.setAdapter(myAdapter);
        typeSpinner.setSelection(0);
        CategoriesSpinner.setSelection(0);
        ModeSpinner.setSelection(0);
    }
}