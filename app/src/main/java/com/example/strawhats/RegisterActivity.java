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
    UserDatabaseHelper userDatabaseHelper;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username= findViewById(R.id.username);
        password= findViewById(R.id.password);
        dob= findViewById(R.id.dob);
        email= findViewById(R.id.email);
        country= findViewById(R.id.country);
        register= findViewById(R.id.register);
        Currency = (Spinner) findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        list.add("Currency");
        list.add("Euro");
        list.add("Dollar");
        list.add("Rupee");
        list.add("Pound");
        list.add("Yen");
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Currency.setAdapter(arrayAdapter);

        Currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Currency.setSelection(position);


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


                if (usernameValue.length()>1){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("username",usernameValue);
                    contentValues.put("password",passwordValue);
                    contentValues.put("dob",dobValue);
                    contentValues.put("email",emailValue);
                    contentValues.put("country",countryValue);

                    userDatabaseHelper.insertUser(contentValues);
                    Toast.makeText(RegisterActivity.this, "User Registered", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(RegisterActivity.this, "Enter the values", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }
}



