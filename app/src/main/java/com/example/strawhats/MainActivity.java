package com.example.strawhats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomnav = findViewById(R.id.bottom_navigation);
        bottomnav.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selected_fragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_summary:
                            selected_fragment = new summary_fragment();
                            break;
                        case R.id.nav_functions:
                            selected_fragment = new operations_fragment();
                            break;
                        case R.id.nav_settings:
                            selected_fragment = new settings_fragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selected_fragment).commit();
                    return true;
                }


            };
//    private void configureNextButton(){
//
//        Button NewTransaction = (Button)findViewById(R.id.btnTrans);
//        NewTransaction.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               startActivity(new Intent(MainActivity.this , TransactionForm.class));
//            }
//        });
//
//    }
}

