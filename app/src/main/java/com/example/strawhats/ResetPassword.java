package com.example.strawhats;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResetPassword extends AppCompatActivity {
    EditText current_password, new_password, confirmPass;
    Button change_password;
    UserDatabaseHelper userDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password__change);

        current_password= findViewById(R.id.et_current_password);
        new_password= findViewById(R.id.et_new_password);
        confirmPass = findViewById(R.id.et_confirm_password);
        change_password= findViewById(R.id.btn_change_password);
        userDatabaseHelper = new UserDatabaseHelper(this);

        current_password.setVisibility(View.INVISIBLE);
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NewPasswordValue = new_password.getText().toString();
                String confirmPassValue = confirmPass.getText().toString();

                if(NewPasswordValue.equals(confirmPassValue)){
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("password",NewPasswordValue);
                    userDatabaseHelper.updateUserData(contentValues);
                    Toast.makeText(ResetPassword.this, "Password has been reset", Toast.LENGTH_SHORT).show();
                    Intent logout = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(logout);
                    finish();
                } else{
                    Toast.makeText(ResetPassword.this,"Passwords dont match",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}