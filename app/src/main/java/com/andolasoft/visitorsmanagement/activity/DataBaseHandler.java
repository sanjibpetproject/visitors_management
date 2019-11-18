package com.andolasoft.visitorsmanagement.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHandler extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "my_db";
    public static String Register_table = "register_table";
    public static String Visitor_table = "visitor_table";
    public static SQLiteDatabase sqLiteDatabase;
    public DataBaseHandler(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(" CREATE TABLE IF NOT EXISTS " + Register_table + "(_id integer primary key, Name text, Number text,Email text, Password text,Type text,status text,image text)");
        sqLiteDatabase.execSQL(" CREATE TABLE IF NOT EXISTS " + Visitor_table + "(_id integer primary key, Name text, Number text,Email text,Employee_name text,Reason TEXT,status text,image text,InTime text,OutTime text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS register_table");
        onCreate(db);
    }

    public boolean update_meeting_list(int id,String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        db.update(Visitor_table, contentValues, "status" + " = ?" , new String[] { Integer.toString(id)} );
        return true;
    }

    public void get_meeting_list(String status) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + Visitor_table + " WHERE status=?", new String[]{status});

        res.moveToFirst();

        while(res.isAfterLast() == false){

            /*SelectedMenuModel selectedMenuModel = new SelectedMenuModel();
            selectedMenuModel.setMenuId(res.getInt(0));
            selectedMenuModel.setTimestamp(res.getLong(1));
            selectedMenuModel.setMenuname(res.getString(2));
            selectedMenuModel.setMenu_item(res.getString(3));
            selectedMenuModel.setRate_per_menu(res.getInt(5));
            selectedMenuModel.setNumber_of_menu(res.getInt(4));
            selectedMenuModel.setDate(res.getString(6));

            array_list.add(selectedMenuModel);*/

            res.moveToNext();
        }
    }
}
