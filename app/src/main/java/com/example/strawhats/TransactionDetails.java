package com.example.strawhats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TransactionDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        Intent intent = getIntent();
        TransactionList Transaction = intent.getParcelableExtra("Transaction Item");

        int id = Transaction.getId();
        String date = Transaction.getDate();
        String amount = Transaction.getAmount();
        String mode = Transaction.getMode();
        String category = Transaction.getCategory();
        String comment = Transaction.getComment();
        String type = Transaction.getType();
        String action = Transaction.getAction();

        TextView tvid = findViewById(R.id.tvTid);
        TextView tvdate = findViewById(R.id.tvTdate);
        TextView tvamount = findViewById(R.id.tvTamount);
        TextView tvtype = findViewById(R.id.tvTtype);
        TextView tvcategory = findViewById(R.id.tvTcategory);

        tvid.setText("Details about transaction "+ Integer.toString(id));
        tvdate.setText("Date : " + date);
        tvamount.setText("Amount : " + amount);
        tvtype.setText("Mode : " + mode );
        tvcategory.setText("Category : " + category);
    }
}
