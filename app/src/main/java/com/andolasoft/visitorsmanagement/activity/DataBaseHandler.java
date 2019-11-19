package com.andolasoft.visitorsmanagement.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

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

    public boolean update_meeting_list(int id,String status,String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        //db.update(Visitor_table, contentValues, "_id" + " = ?" , new String[] { Integer.toString(id)} );
        if (status.equals(CommonUtilties.InProgress)){
            DataBaseHandler.sqLiteDatabase.execSQL(" UPDATE " + DataBaseHandler.Visitor_table + " SET status = '" + status + "' ,InTime = '" + time + "' WHERE _id = '" + id + "'");
        }if (status.equals(CommonUtilties.Completed)){
            DataBaseHandler.sqLiteDatabase.execSQL(" UPDATE " + DataBaseHandler.Visitor_table + " SET status = '" + status + "' ,OutTime = '" + time + "' WHERE _id = '" + id + "'");
        }
        return true;
    }

    public ArrayList get_meeting_list(String status) {
        ArrayList arrayList1 = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + Visitor_table + " WHERE status=?", new String[]{status});

        res.moveToFirst();

        while(res.isAfterLast() == false){

            SelectedMenuModel selectedMenuModel = new SelectedMenuModel();
            selectedMenuModel.setId(res.getInt(0));
            selectedMenuModel.setName(res.getString(1));
            selectedMenuModel.setNumber(res.getString(2));
            selectedMenuModel.setEmployee_name(res.getString(4));
            selectedMenuModel.setIn_time(res.getString(8));
            selectedMenuModel.setOut_time(res.getString(9));
            selectedMenuModel.setReason(res.getString(5));
            selectedMenuModel.setEmail(res.getString(3));
            selectedMenuModel.setStatus(res.getString(6));
            selectedMenuModel.setImage_path(res.getString(7));
            arrayList1.add(selectedMenuModel);

            res.moveToNext();
        }
        return arrayList1;
    }
}
