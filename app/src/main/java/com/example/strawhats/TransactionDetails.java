package com.example.strawhats;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TransactionDetails extends AppCompatActivity {
    TransactionDatabaseHelper mDatabaseHelper = new TransactionDatabaseHelper(this);
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.6),(int)(height*.55));

        intent = getIntent();
        final TransactionList Transaction = intent.getParcelableExtra("Transaction Item");
        setupEverything(Transaction);
        int id = Transaction.getId();
        final String id_str = Integer.toString(id);
        ImageView Delete = (ImageView) findViewById(R.id.icDelete);
//        Button Delete = (Button) findViewById(R.id.btnTdelete);
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData(id_str);
                finish();
            }
        });

        ImageView Edit = (ImageView)findViewById(R.id.icEdit);
        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionDetails.this,EditTransaction.class);
                intent.putExtra("Transaction Object",Transaction);
                startActivityForResult(intent,0);
            }
        });
        ImageView Repeat =(ImageView)findViewById(R.id.icRepeat);
        Repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TransactionDetails.this,RepeatTransaction.class);
                intent.putExtra("Transaction Object",Transaction);
                startActivityForResult(intent,0);
            }
        });

    }
    public void deleteData(String id){
        boolean deletedata = mDatabaseHelper.deleteData(id);
        if(deletedata){
            toastMessage("Data Deleted");
        } else{
            toastMessage("Data Not Deleted");
        }
    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
    public void setupEverything(final TransactionList Transaction){
        int id = Transaction.getId();
        final String id_str = Integer.toString(id);
        String date = Transaction.getDate();
        String amount = Transaction.getAmount();
        String mode = Transaction.getMode();
        String category = Transaction.getCategory();
        String comment = Transaction.getComment();
        String type = Transaction.getType();
        String action = Transaction.getAction();

        TextView tvid = findViewById(R.id.tvTid);
        TextView tv1 = findViewById(R.id.tvT1);
        TextView tv2 = findViewById(R.id.tvT2);
        TextView tv3 = findViewById(R.id.tvT3);
        TextView tv4 = findViewById(R.id.tvT4);
        TextView tv5 = findViewById(R.id.tvT5);
        if (type.equals("Expense")) {
            tvid.setText("Transaction : " + Integer.toString(id));
            tv1.setText("Date : " + date);
            tv2.setText("Type : " + type);
            tv3.setText("Amount :  " + amount);
            tv4.setText("Mode     : " + mode);
            tv5.setText("Category : " + category);
        } else {
            tvid.setText("Transaction : " + Integer.toString(id));
            tv1.setText("Date     : " + date);
            tv2.setText("Type     : " + type);
            tv3.setText("Amount   : " + amount);
            tv4.setText("Category   : " + category);
            tv5.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK){
            TransactionList updatedTransaction = data.getParcelableExtra("updatedTransaction");
            setupEverything(updatedTransaction);
        }
    }
}
