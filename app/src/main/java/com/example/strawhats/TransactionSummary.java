package com.example.strawhats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class TransactionSummary extends AppCompatActivity {

    private static final String Tag = "TransactionSummary";
    TransactionDatabaseHelper mDatabaseHelper ;
    private ListView mlistView;
    ArrayList<TransactionList> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_summary);

        mlistView = (ListView) findViewById(R.id.lvAllTransactions);
        mDatabaseHelper = new TransactionDatabaseHelper(this);
        
        populateListView();
        mlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TransactionSummary.this,TransactionDetails.class);
                intent.putExtra("Transaction Item",listData.get(position));
                startActivity(intent);
            }
        });


    }

    private void populateListView() {
        Log.d(Tag, "populating List View");
        Cursor data = mDatabaseHelper.getData();
        String content = "";
        String action;
        String amt;
        while(data.moveToNext()){
            int id = data.getInt(0);
            String date = data.getString(1);
            int amount = data.getInt(2);
            amt = Integer.toString(amount);
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
       TransactionListAdapter adapter = new TransactionListAdapter(this,R.layout.adapter_transaction_list,listData);
        mlistView.setAdapter(adapter);
    }
}
