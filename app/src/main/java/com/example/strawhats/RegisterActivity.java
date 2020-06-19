package com.example.strawhats;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    EditText username, password, email,country,dob;
    Button register;
    Spinner Currency;
    ArrayList<CurrencyItem> mCurrencyList;
    UserDatabaseHelper userDatabaseHelper;
    CurrencyAdapter cAdapter;
    CurrencyItem CurrencyPicked;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCurrencyList();
        setContentView(R.layout.activity_register);
        username= findViewById(R.id.username);
        password= findViewById(R.id.password);
        dob= findViewById(R.id.dob);
        email= findViewById(R.id.email);
        country= findViewById(R.id.country);
        register= findViewById(R.id.register);
        Currency = (Spinner) findViewById(R.id.spCurrencyPref);
        cAdapter = new CurrencyAdapter(this,mCurrencyList);
        Currency.setAdapter(cAdapter);

        Currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CurrencyItem clickedItem = (CurrencyItem) parent.getItemAtPosition(position);
                CurrencyPicked = clickedItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        userDatabaseHelper = new UserDatabaseHelper(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameValue=username.getText().toString();
                String passwordValue=password.getText().toString();
                String dobValue=dob.getText().toString();
                String emailValue=email.getText().toString();
                String countryValue=country.getText().toString();
                String currencyValue = CurrencyPicked.getmCurrencyAbbreviation();


                if (usernameValue.length()>1){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("username",usernameValue);
                    contentValues.put("password",passwordValue);
                    contentValues.put("dob",dobValue);
                    contentValues.put("email",emailValue);
                    contentValues.put("country",countryValue);
                    contentValues.put("currency",currencyValue);
                    userDatabaseHelper.insertUser(contentValues);
                    Toast.makeText(RegisterActivity.this, "User Registered", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(RegisterActivity.this, "Enter the values", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void initCurrencyList() {
        mCurrencyList = new ArrayList<>();
        mCurrencyList.add(new CurrencyItem("Rupee", "\u20B9","INR"));
        mCurrencyList.add(new CurrencyItem("Pound", "£","GBP"));
        mCurrencyList.add(new CurrencyItem("Yen", "¥","YEN"));
        mCurrencyList.add(new CurrencyItem("Dollar", "$","USD"));
        mCurrencyList.add(new CurrencyItem("Euro", "€","EUR"));
    }
}



