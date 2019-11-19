package com.andolasoft.visitorsmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.andolasoft.visitorsmanagement.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisitorDetails extends AppCompatActivity {
    ImageView camera;
    CircleImageView logo;
    EditText name,number,email,password,reason;
    RadioButton employee,security;
    Button register;
    DataBaseHandler dataBaseHandler;
    String name_val = "",number_val="",email_val="",password_val="";
    AutoCompleteTextView emp_name;
    ArrayList emp_name_list = new ArrayList();
    CommonUtilties commonUtilties = new CommonUtilties();
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitor_details);

        camera = (ImageView)findViewById(R.id.camera);
        logo = (CircleImageView) findViewById(R.id.logo);
        name = (EditText) findViewById(R.id.name);
        number = (EditText) findViewById(R.id.user_email);
        email = (EditText) findViewById(R.id.email);
        register = (Button)findViewById(R.id.signin_button);
        emp_name = (AutoCompleteTextView)findViewById(R.id.emp_name_list);
        reason = (EditText)findViewById(R.id.user_password);
        emp_name_list.add("Gyana Ranjan Mohapatra");
        emp_name_list.add("Sanjib Kumar sahoo");
        emp_name_list.add("Shakti Prasad Mohanty");
        emp_name_list.add("Kalpotary sahoo");
        emp_name_list.add("Rahul Panda");
        emp_name_list.add("Anurag Pattanik");
        emp_name_list.add("JD Sir");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,emp_name_list);
        emp_name.setAdapter(arrayAdapter);
        emp_name.setThreshold(1);
        permission();
        dataBaseHandler = new DataBaseHandler(this);
        dataBaseHandler.getWritableDatabase();
        DataBaseHandler.sqLiteDatabase = VisitorDetails.this.openOrCreateDatabase(DataBaseHandler.DATABASE_NAME,MODE_PRIVATE,null);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,2);
            }
        });
//        emp_name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                emp_name.showDropDown();
//            }
//        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean val = validation();
                if (!val){
                    String image_path = commonUtilties.photo_store_in_local(bitmap,name.getText().toString()+"-"+System.currentTimeMillis());
                    insert_into_db(image_path);
                    Intent intent = new Intent(VisitorDetails.this,BaseActivity.class);
                    startActivity(intent);
                }

            }
        });
    }
    public void permission(){
        ArrayList<String> arrayList = new ArrayList<>();
        int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (camera!= PackageManager.PERMISSION_GRANTED){
            arrayList.add(Manifest.permission.CAMERA);
        }
        if (write!= PackageManager.PERMISSION_GRANTED){
            arrayList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (read!= PackageManager.PERMISSION_GRANTED){
            arrayList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (arrayList.size()!=0){
            ActivityCompat.requestPermissions(this,arrayList.toArray(new String[arrayList.size()]),10);
        }
    }
    public boolean validation(){
        boolean val = false;
        if (!name.getText().toString().trim().equals("") && !number.getText().toString().trim().equals("") && !email.getText().toString().trim().equals("") && !emp_name.getText().toString().trim().equals("") && !reason.getText().toString().trim().equals("") && bitmap!=null){
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
            }if (emp_name.getText().toString().trim().equals("")){
                emp_name.setError("Choose Employee name");
                val = true;
            }if (reason.getText().toString().trim().equals("")){
                reason.setError("Enter Reason");
                val = true;
            }if (bitmap==null){
                Toast.makeText(this, "Profile picture must be Required", Toast.LENGTH_SHORT).show();
            }

        }
        return val;
    }
    public void insert_into_db(String image_path){
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name",name.getText().toString());
        contentValues.put("Number",number.getText().toString());
        contentValues.put("Email",email.getText().toString());
        contentValues.put("Employee_name",emp_name.getText().toString());
        contentValues.put("Reason",reason.getText().toString());
        contentValues.put("status",CommonUtilties.Pending);
        contentValues.put("image",image_path);
        contentValues.put("InTime","");
        contentValues.put("OutTime","");
        DataBaseHandler.sqLiteDatabase.insert(DataBaseHandler.Visitor_table,null, contentValues);

    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
             bitmap = (Bitmap) data.getExtras().get("data");
            logo.setImageBitmap(bitmap);
        }
    }
}
