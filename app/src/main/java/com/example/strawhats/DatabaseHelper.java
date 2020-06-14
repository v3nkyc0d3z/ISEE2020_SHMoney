package com.example.strawhats;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    static String name = "database";
    static int version = 1;

    String createTableUser= "CREATE TABLE  if not exists \"user\" (\n" +
            "\t\"ID\"\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t\"username\"\tTEXT,\n" +
            "\t\"password\"\tTEXT,\n" +
            "\t\"dob\"\tTEXT,\n" +
            "\t\"email\"\tTEXT,\n" +
            "\t\"country\"\tTEXT)";

    public DatabaseHelper(@Nullable Context context) {
        super(context, name, null, version);
        getWritableDatabase().execSQL(createTableUser);
    }


    public void insertUser(ContentValues contentValues){
        getWritableDatabase().insert("user", "",contentValues);

    }




    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTableUser);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }



    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "Select * from user";
        Cursor data = db.rawQuery(query,null);
        return data;
    }


}