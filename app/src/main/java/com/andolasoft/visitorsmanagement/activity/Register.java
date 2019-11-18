package com.andolasoft.visitorsmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.andolasoft.visitorsmanagement.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity {

    ImageView camera;
    CircleImageView logo;
    EditText name,number,email,password;
    RadioButton employee,security;
    Button register;
    DataBaseHandler dataBaseHandler;
    String name_val = "",number_val="",email_val="",password_val="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        camera = (ImageView)findViewById(R.id.camera);
        logo = (CircleImageView) findViewById(R.id.logo);
        name = (EditText) findViewById(R.id.name);
        number = (EditText) findViewById(R.id.user_email);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.user_password);
        employee = (RadioButton) findViewById(R.id.emp);
        security = (RadioButton) findViewById(R.id.sec);
        register = (Button)findViewById(R.id.signin_button);
        permission();
         dataBaseHandler = new DataBaseHandler(this);
        dataBaseHandler.getWritableDatabase();
        DataBaseHandler.sqLiteDatabase = Register.this.openOrCreateDatabase(DataBaseHandler.DATABASE_NAME,MODE_PRIVATE,null);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,2);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean val = validation();
                if (!val){
                   Insert_into_db();
                   Intent intent = new Intent(Register.this,LoginActivity.class);
                   startActivity(intent);
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            logo.setImageBitmap(bitmap);
        }
    }
    public void permission(){
        ArrayList<String> arrayList = new ArrayList<>();
        int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (camera!= PackageManager.PERMISSION_GRANTED){
            arrayList.add(Manifest.permission.CAMERA);
        }
        if (arrayList.size()!=0){
            ActivityCompat.requestPermissions(this,arrayList.toArray(new String[arrayList.size()]),10);
        }
    }
    public boolean validation(){
        boolean val = false;
        if (!name.getText().toString().trim().equals("") && !number.getText().toString().trim().equals("") && !email.getText().toString().trim().equals("") && !password.getText().toString().trim().equals("") && (employee.isChecked() || security.isChecked())){
            val = false;
        }else {
            if (name.getText().toString().trim().equals("")){
                name.setError("Enter Name");
                val = true;
            }if (email.getText().toString().trim().equals("")){
                email.setError("Enter Email");
                val = true;
            }if (number.getText().toString().trim().equals("")){
                number.setError("Enter Number");
                val = true;
            }if (password.getText().toString().trim().equals("")){
                password.setError("Enter Password");
                val = true;
            }if (employee.isChecked() && security.isChecked()){
                val = true;
            }
        }
        return val;
    }
    public void Insert_into_db(){
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name",name.getText().toString());
        contentValues.put("Number",number.getText().toString());
        contentValues.put("Email",email.getText().toString());
        contentValues.put("Password",password.getText().toString());
        if (employee.isChecked()){
            contentValues.put("Type","1");
        }else {
            contentValues.put("Type","2");
        }
       contentValues.put("status","");
        contentValues.put("image","");
        long l = DataBaseHandler.sqLiteDatabase.insert(DataBaseHandler.Register_table, null, contentValues);
    }
}
