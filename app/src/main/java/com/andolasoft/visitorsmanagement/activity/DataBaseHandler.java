package com.andolasoft.visitorsmanagement.activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHandler extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "my_db";
    public static String Register_table = "register_table";
    public static SQLiteDatabase sqLiteDatabase;
    public DataBaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(" CREATE TABLE IF NOT EXISTS " + Register_table + "(_id integer primary key, Name text, Number text,Email text, Password text,Type text,status text,image text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
