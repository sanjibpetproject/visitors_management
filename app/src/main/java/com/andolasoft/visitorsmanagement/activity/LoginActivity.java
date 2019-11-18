package com.andolasoft.visitorsmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andolasoft.visitorsmanagement.R;

public class LoginActivity extends AppCompatActivity {

    Button signin_button,signup;
    DataBaseHandler dataBaseHandler;
    EditText user_email,user_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        dataBaseHandler = new DataBaseHandler(this);
        dataBaseHandler.getWritableDatabase();
        DataBaseHandler.sqLiteDatabase = LoginActivity.this.openOrCreateDatabase(DataBaseHandler.DATABASE_NAME,MODE_PRIVATE,null);
        user_email = (EditText)findViewById(R.id.user_email);
        user_password = (EditText)findViewById(R.id.user_password);
        signin_button = findViewById(R.id.signin_button);
        signup = (Button)findViewById(R.id.signup);
        signin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (true){
                    Intent intent = new Intent(LoginActivity.this, BaseActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this, "Incorrect email and password", Toast.LENGTH_SHORT).show();
                }


            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, Register.class);
                startActivity(intent);
                finish();
            }
        });

    }
    public boolean validation(){
        boolean val = true;
        Cursor cursor = DataBaseHandler.sqLiteDatabase.rawQuery(" SELECT * FROM " + DataBaseHandler.Register_table + " WHERE Email = '" + user_email.getText().toString().trim() + "' AND Password = '" + user_password.getText().toString().trim() + "'", null);
        if (cursor.getCount()>0){
            val = false;
        }
        return val;
    }
}
