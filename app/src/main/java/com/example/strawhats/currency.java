package com.example.strawhats;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class currency extends AppCompatActivity {
    private ArrayList<CountryItem> mCountryList;
    private CurrencyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        initList();
        Spinner spinnerCountries = findViewById(R.id.spinner_countries);
        mAdapter = new CurrencyAdapter(this, mCountryList);
        spinnerCountries.setAdapter(mAdapter);
        spinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryItem clickedItem = (CountryItem) parent.getItemAtPosition(position);
                String clickedCountryName = clickedItem.getCountryName();
                Toast.makeText(currency.this, clickedCountryName + " selected", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    private void initList() {
        mCountryList = new ArrayList<>();
        mCountryList.add(new CountryItem("Rupee", "\u20B9"));
        mCountryList.add(new CountryItem("Pound", "£"));
        mCountryList.add(new CountryItem("Yen", "¥"));
        mCountryList.add(new CountryItem("Dollar", "$"));
        mCountryList.add(new CountryItem("Euro", "€"));
    }



}
