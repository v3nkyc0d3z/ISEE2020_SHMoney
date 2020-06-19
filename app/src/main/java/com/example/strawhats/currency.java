package com.example.strawhats;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class currency extends AppCompatActivity {
    private ArrayList<CurrencyItem> mCurrencyList;
    private CurrencyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        initList();
        Spinner spinnerCountries = findViewById(R.id.spinner_countries);
        mAdapter = new CurrencyAdapter(this, mCurrencyList);
        spinnerCountries.setAdapter(mAdapter);
        spinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CurrencyItem clickedItem = (CurrencyItem) parent.getItemAtPosition(position);
                String clickedCountryName = clickedItem.getCurrencyName();
                Toast.makeText(currency.this, clickedCountryName + " selected", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initList() {
        mCurrencyList = new ArrayList<>();
        mCurrencyList.add(new CurrencyItem("Rupee", "\u20B9","INR"));
        mCurrencyList.add(new CurrencyItem("Pound", "£","GBP"));
        mCurrencyList.add(new CurrencyItem("Yen", "¥","YEN"));
        mCurrencyList.add(new CurrencyItem("Dollar", "$","USD"));
        mCurrencyList.add(new CurrencyItem("Euro", "€","EUR"));
    }



}
