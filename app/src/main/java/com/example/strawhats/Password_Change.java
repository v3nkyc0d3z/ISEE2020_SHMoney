package com.example.strawhats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Password_Change extends AppCompatActivity {
    EditText  current_password, new_password, confirmPass;
    Button change_password;
    UserDatabaseHelper userDatabaseHelper;
    String pass, email, country, currency,dob, username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password__change);

        current_password= findViewById(R.id.et_current_password);
        new_password= findViewById(R.id.et_new_password);
        confirmPass = findViewById(R.id.et_confirm_password);

        change_password= findViewById(R.id.btn_change_password);

        userDatabaseHelper = new UserDatabaseHelper(this);

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPasswordValue = current_password.getText().toString();
                String NewPasswordValue = new_password.getText().toString();
                String confirmPassValue = confirmPass.getText().toString();
                Cursor data = userDatabaseHelper.getData();
                while(data.moveToNext()){

                            username = data.getString(1);
                            dob = data.getString(3);
                            email = data.getString(4);
                            country = data.getString(5);
                            currency = data.getString(6);

                    pass = data.getString(2);
                }

                if (currentPasswordValue.equals(pass) && NewPasswordValue.equals(confirmPassValue) ){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("username",username);
                    contentValues.put("password",NewPasswordValue);
                    contentValues.put("dob",dob);
                    contentValues.put("email",email);
                    contentValues.put("country",country);
                    contentValues.put("currency",currency);
                    userDatabaseHelper.insertUser(contentValues);


                    Toast.makeText(Password_Change.this, "Password has been changed", Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(Password_Change.this, "Enter correct values", Toast.LENGTH_SHORT).show();
                }
            }
        });}
}
