package com.example.strawhats;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class currency extends AppCompatActivity {
    private ArrayList<CurrencyItem> mCurrencyList;
    private CurrencyAdapter mAdapter;
    CurrencyItem currencyPicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        initList();
        final UserDatabaseHelper userDatabaseHelper = new UserDatabaseHelper(this);
        Cursor userData = userDatabaseHelper.getData();
        String DefaultCurrency = "";
        while(userData.moveToNext()) {
            DefaultCurrency = userData.getString(6);
        }
        Spinner spinnerCountries = findViewById(R.id.spinner_countries);
        mAdapter = new CurrencyAdapter(this, mCurrencyList);
        int index=0;
        for (int i = 0;i<mCurrencyList.size();i++){
            if (mCurrencyList.get(i).getmCurrencyAbbreviation().equals(DefaultCurrency)){
                index = i;
                break;
            }
        }
        spinnerCountries.setAdapter(mAdapter);
        spinnerCountries.setSelection(index);
        spinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CurrencyItem clickedItem = (CurrencyItem) parent.getItemAtPosition(position);
                currencyPicked = clickedItem;

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button change = (Button) findViewById(R.id.btnChangeCurrency);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues userCV = new ContentValues();
                userCV.put("currency",currencyPicked.getmCurrencyAbbreviation());
                userDatabaseHelper.updateUserData(userCV);
                finish();
            }
        });

    }

    private void initList() {
        mCurrencyList = new ArrayList<>();
        mCurrencyList.add(new CurrencyItem("Rupee", "\u20B9","INR",84.84f));
        mCurrencyList.add(new CurrencyItem("Pound", "£","GBP",0.90f));
        mCurrencyList.add(new CurrencyItem("Yen", "¥","YEN",120.27f));
        mCurrencyList.add(new CurrencyItem("Dollar", "$","USD",1.12f));
        mCurrencyList.add(new CurrencyItem("Euro", "€","EUR",1f));
    }
}

