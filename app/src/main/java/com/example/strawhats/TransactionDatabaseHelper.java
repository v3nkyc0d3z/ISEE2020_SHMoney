package com.example.strawhats;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Date;

public class TransactionDatabaseHelper extends SQLiteOpenHelper {

    public TransactionDatabaseHelper(Context context) {
        super(context, "TransactionDatabase.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE TRANSACTIONS (Tid INTEGER PRIMARY KEY AUTOINCREMENT," +
                       "TransactionDate String," +
                       "Amount FLOAT," +
                       "Mode STRING," +
                       "Category String," +
                       "Comments String," +
                       "Type String)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS TRANSACTIONS");
        onCreate(db);
    }

    public boolean addTransaction(String date, Float amount, String mode, String category, String comments,String type){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("TransactionDate",date);
        contentValues.put("Amount",amount);
        contentValues.put("Mode",mode);
        contentValues.put("Category",category);
        contentValues.put("Comments",comments);
        contentValues.put("Type",type);

        long result =db.insert("TRANSACTIONS",null,contentValues);
        if (result>0){
            return true;
        }
        else{
            return false;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * from TRANSACTIONS";
        Cursor data = db.rawQuery(query,null);
        return data;
    }

    public boolean deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete("TRANSACTIONS", "Tid = ?",new String[] {id});
        if (result>0){
            return true;
        }else{
            return false;
        }
    }
}
