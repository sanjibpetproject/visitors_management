package com.andolasoft.visitorsmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.andolasoft.visitorsmanagement.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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
    ArrayList arrayList_name,arrayList_devicetoken;
    String device_token = "";
    JSONObject jsonObject = new JSONObject();
    String emailPattern  = "^[A-Za-z0-9,!#\\$%&'\\*\\+/=\\?\\^_`\\{\\|}~-]+(\\.[A-Za-z0-9,!#\\$%&'\\*\\+/=\\?\\^_`\\{\\|}~-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.([A-Za-z]{2,})$";



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
        getData();

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
                    device_token = (String) arrayList_devicetoken.get(arrayList_name.indexOf(emp_name.getText().toString()));
                    String image_path = commonUtilties.photo_store_in_local(bitmap,name.getText().toString()+"-"+System.currentTimeMillis());
                    jsonObject = new JSONObject();
                    JSONObject jsonObject2 = new JSONObject();
                    try {
                        jsonObject.put("name",name.getText().toString());
                        jsonObject.put("number",number.getText().toString());
                        jsonObject.put("email",email.getText().toString());
                        jsonObject.put("emp_name",emp_name.getText().toString());
                        jsonObject.put("reason",reason.getText().toString());


                        jsonObject2 = new JSONObject();

                        jsonObject2.put("data",jsonObject);
                        jsonObject2.put("priority","high");
                        jsonObject2.put("to",device_token);

                    }catch (Exception e){

                    }
                    call(jsonObject2);
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
            }if (emp_name.getText().toString().trim().equals("")){
                emp_name.setError("Choose Employee name");
                val = true;
            }if (reason.getText().toString().trim().equals("")){
                reason.setError("Enter Reason");
                val = true;
            }if (bitmap==null){
                val = true;
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


    private void getData(){

        arrayList_name = new ArrayList<>();
        arrayList_devicetoken = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            User user = snapshot.getValue(User.class);

                            arrayList_name.add(user.getName());
                            arrayList_devicetoken.add(user.getDevicetoken());
                        }

                        ArrayAdapter arrayAdapter = new ArrayAdapter(VisitorDetails.this,R.layout.support_simple_spinner_dropdown_item,arrayList_name);
                        emp_name.setAdapter(arrayAdapter);
                        emp_name.setThreshold(1);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

    }
    public void call(final JSONObject data){


        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                String response = "";


                try {
                    response = makePostRequest1("https://fcm.googleapis.com/fcm/send",data);
                }catch (Exception e){
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                try {
                    if (response != null) {

                        String val = response;
                    }
                }catch (Exception e){

                    e.printStackTrace();
                }
            }
        }.execute();


    }
    public String makePostRequest1(String reqUrl, JSONObject params) {
        String response = null;
        System.out.println("something" + params);
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);

            connection.setRequestProperty ("Authorization", "key=AIzaSyBUTP59EKALo_dUKDfnXacbE9mmkt0TDMs");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            connection.setUseCaches (false);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

            wr.writeBytes(params.toString());
            wr.flush();
            wr.close();

            Log.d("status code" , connection.getResponseCode() + "");
            Log.d("response message" , connection.getResponseMessage() + "");

            if (connection.getResponseCode() == 401) response = "unauthorized";

            InputStream in = new BufferedInputStream(connection.getInputStream());
            response = convertStreamToString(in);
            if (connection.getResponseCode() == 201) response += "success";
        } catch (MalformedURLException e) {
            //Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            // Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            //  Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            //   Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        String sb1="";
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
                //sb1 = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

}
