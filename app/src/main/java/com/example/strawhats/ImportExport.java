package com.example.strawhats;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.text.HtmlCompat;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ImportExport extends AppCompatActivity {
    Intent mFileManager;
    TextView filePicker;
    TransactionDatabaseHelper TDBHelper;
    String FileLocation;
    File importFile;
    Uri importUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_export);
        TDBHelper = new TransactionDatabaseHelper(this);
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
        Button importData = (Button) findViewById(R.id.btnImportData);
        TDBHelper = new TransactionDatabaseHelper(this);
        importData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<TransactionList> Transactions = new ArrayList<>();
                if (importUri != null) {
                    String line = "";
                    ContentValues cv;
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(importUri);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                        while ((line = reader.readLine()) != null) {
                            String[] tokens = line.split(",");
                            if (!tokens[0].equals("Date")) {
                                Boolean res = TDBHelper.addTransaction(tokens[0], Float.parseFloat(tokens[1]), tokens[2],
                                        tokens[3], tokens[4], tokens[5], tokens[6], "Default");
                                if (res) {
                                    Log.d("line parsed", "import successful");
                                } else {
                                    Log.d("problem", "insert failed");
                                }
                            }
                        }
                    } catch (FileNotFoundException e) {
                        Log.wtf("Import Failed", "No File found" + FileLocation, e);
                        e.printStackTrace();
                    } catch (IOException e) {
                        Log.wtf("Read Failed", "Error reading line found" + line, e);
                        e.printStackTrace();
                    }
                    finish();
                }else{
                    toastMessage("Choose a file to import");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            importUri = data.getData();
            importFile = new File(importUri.getPath());
            String name = importFile.getName();
            FileLocation = importUri.getPath().toString();
            filePicker.setText(FileLocation);
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}
