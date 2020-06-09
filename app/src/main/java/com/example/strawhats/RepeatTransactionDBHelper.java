package com.example.strawhats;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RepeatTransactionDBHelper extends SQLiteOpenHelper {

    public RepeatTransactionDBHelper(Context context){
        super (context,"RepeatTransactionDBHelper",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE REPEATTRANSACTION(Rid INTEGER PRIMARY KEY AUTOINCREMENT,"+
                                                        "Name STRING,"+
                                                        "Amount FLOAT," +
                                                        "Mode STRING," +
                                                        "Category String," +
                                                        "Comments String," +
                                                        "Type String,"+
                                                        "Currency String,"+
                                                        "Recurrence Boolean,"+
                                                        "Profile String)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS REPEATTRANSACTION");
        onCreate(db);
    }
    public boolean addTranscation(String name,Float amount,String mode,String category,String comment, String type,String currency,Boolean Recurrence,String profile){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name",name);
        contentValues.put("Amount",name);
        contentValues.put("Mode",name);
        contentValues.put("Category",name);
        contentValues.put("Comments",name);
        contentValues.put("Type",name);
        contentValues.put("Currency",name);
        contentValues.put("Recurrence",name);
        contentValues.put("Profile",name);

        long result = db.insert("REPEATTRANSACTION",null,contentValues);
        if (result>0){
            return true;
        }else{
            return false;
        }
    }
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM REPEATTRANSACTION";
        Cursor data = db.rawQuery(query,null);
        return data;
    }
}
