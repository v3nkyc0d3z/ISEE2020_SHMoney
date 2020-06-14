package com.example.strawhats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    Button Login;
    TextView Register;
    DatabaseHelper databaseHelper;

    String uname,pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username= findViewById(R.id.etusr);
        password= findViewById(R.id.etpass);
        Login = findViewById(R.id.btlogin);
        Register=findViewById(R.id.tvreg);

        databaseHelper = new DatabaseHelper(this);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userentry = username.getText().toString();
                String passentry = password.getText().toString();


                Cursor data = databaseHelper.getData();
                while(data.moveToNext()){
                    uname = data.getString(1);
                    pass = data.getString(2);
                }
                if(userentry.equals(uname) && (passentry.equals(pass))){
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this,"Login Successful",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }

            }
        });
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
}
