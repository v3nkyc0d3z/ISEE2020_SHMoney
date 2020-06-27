package com.example.strawhats;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.text.HtmlCompat;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;

public class ImportExport extends AppCompatActivity {
    Intent mFileManager;
    TextView filePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_export);
        TransactionDatabaseHelper TDBHelper = new TransactionDatabaseHelper(this);
        Button ExportData = (Button) findViewById(R.id.btnExportData);
        final Cursor Transactions = TDBHelper.getData();
        ExportData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder data = new StringBuilder();
                data.append("Date,Amount,Mode,Category,Comments,Type,Currency");
                while (Transactions.moveToNext()){
                    String date = Transactions.getString(1);
                    String amount = Float.toString(Transactions.getFloat(2));
                    String mode = Transactions.getString(3);
                    String category = Transactions.getString(4);
                    String comment = Transactions.getString(5);
                    comment = HtmlCompat.fromHtml(comment,0).toString().trim();
                    String type = Transactions.getString(6);
                    String currency = Transactions.getString(7);
                    data.append("\n"+date+","+amount+","+mode+","+category+","+comment+","+type+","+currency);
                }

                try {
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    String snow = now.toString();
                    FileOutputStream out = openFileOutput("data_"+snow+".csv", Context.MODE_PRIVATE);
                    out.write((data.toString()).getBytes());
                    out.close();

                    Context context = getApplicationContext();


                    File filelocation = new File(getFilesDir(),"data_"+snow+".csv");
                    String paths = filelocation.getPath();
                    toastMessage("File " + filelocation.getPath() + " written");

                    Uri path = FileProvider.getUriForFile(context,"com.example.strawhats.fileprovider",filelocation);
                    Intent fileIntent = new Intent(Intent.ACTION_SEND);
                    fileIntent.setType("text/csv");
                    fileIntent.putExtra(Intent.EXTRA_SUBJECT,"Data");
                    fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    fileIntent.putExtra(Intent.EXTRA_STREAM,path);

                    startActivity(Intent.createChooser(fileIntent,"Send Mail"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        filePicker =   (TextView) findViewById(R.id.tvChooseCSV);
        filePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFileManager = new Intent(Intent.ACTION_GET_CONTENT);
                mFileManager.setType("*/*");
                startActivityForResult(mFileManager,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Uri uri = data.getData();
            File file = new File(uri.getPath());
            String name = file.getName();
            String path = uri.getPath().toString();
            filePicker.setText(path);
        }
    }
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}
