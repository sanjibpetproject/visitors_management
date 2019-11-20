package com.andolasoft.visitorsmanagement.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.andolasoft.visitorsmanagement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

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
    CommonUtilties commonUtilties = new CommonUtilties();
    Bitmap bitmap;
    private FirebaseAuth mAuth;
    ProgressDialog dialog;
    String emailPattern  = "^[A-Za-z0-9,!#\\$%&'\\*\\+/=\\?\\^_`\\{\\|}~-]+(\\.[A-Za-z0-9,!#\\$%&'\\*\\+/=\\?\\^_`\\{\\|}~-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.([A-Za-z]{2,})$";

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
        dialog = new ProgressDialog(Register.this);
        mAuth = FirebaseAuth.getInstance();
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

                    dialog.show();
                    create_acc_firebase(email.getText().toString(),password.getText().toString());

                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
             bitmap = (Bitmap) data.getExtras().get("data");
            logo.setImageBitmap(bitmap);
        }
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
        if (!name.getText().toString().trim().equals("") && !number.getText().toString().trim().equals("") && !email.getText().toString().trim().equals("") && !password.getText().toString().trim().equals("") && (employee.isChecked() || security.isChecked()) && bitmap!=null){
            val = false;
            if (number.getText().toString().trim().length()!=10){
                val = true;
                email.setError("please enter 10 digit number");
            }
            if (!email.getText().toString().matches(emailPattern)){
                val = true;
                email.setError("Enter valid Email");
            }
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
            if (bitmap==null){
                val = true;
                Toast.makeText(this, "Profile picture must be Required", Toast.LENGTH_SHORT).show();
            }
        }
        return val;
    }
    public void Insert_into_db(String image_path){
        ContentValues contentValues = new ContentValues();
        contentValues.put("Name",name.getText().toString());
        contentValues.put("Number",number.getText().toString());
        contentValues.put("Email",email.getText().toString());
        contentValues.put("Password",password.getText().toString());
        if (employee.isChecked()){
            contentValues.put("Type",CommonUtilties.Employeee);
        }else {
            contentValues.put("Type",CommonUtilties.Security);
        }
       contentValues.put("status","");
        contentValues.put("image",image_path);
        long l = DataBaseHandler.sqLiteDatabase.insert(DataBaseHandler.Register_table, null, contentValues);
    }

    private void create_acc_firebase(String email,String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();

                            writedata_to_firebase();

                        } else {
                            dialog.dismiss();
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }


    private void writedata_to_firebase(){

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {

                        String token = task.getResult().getToken();
                        User user = new User();
                        user.setName(name.getText().toString());
                        user.setDevicetoken(token);

                        FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                dialog.dismiss();
                                String image_path = commonUtilties.photo_store_in_local(bitmap,name.getText().toString()+"-"+System.currentTimeMillis());
                                Insert_into_db(image_path);
                                Intent intent = new Intent(Register.this,LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                });

    }
}
