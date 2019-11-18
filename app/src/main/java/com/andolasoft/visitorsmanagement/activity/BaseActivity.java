package com.andolasoft.visitorsmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andolasoft.visitorsmanagement.R;

public class BaseActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView text_pending,text_progress,text_completed;
    View view_progress,view_pending,view_completed;
    DataBaseHandler dataBaseHandler;
    ImageView create_meeting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        init();
        click_function();
        text_pending.performClick();

    }

    private void init(){

        dataBaseHandler = new DataBaseHandler(BaseActivity.this);
        text_progress = findViewById(R.id.text_progress);
        text_pending = findViewById(R.id.text_pending);
        text_completed = findViewById(R.id.text_completed);
        view_pending = findViewById(R.id.view_pending);
        view_progress = findViewById(R.id.view_progress);
        view_completed = findViewById(R.id.view_completed);
        create_meeting = findViewById(R.id.create_meeting);
    }

    private void click_function(){

        text_progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbar(text_progress,view_progress);
                getdata("progress");
            }
        });
        text_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbar(text_pending,view_pending);
                getdata("pending");

            }
        });
        text_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbar(text_completed,view_completed);
                getdata("completed");

            }
        });
        create_meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BaseActivity.this,VisitorDetails.class);
                startActivity(intent);
            }
        });
    }

    private void setdata(){
        recyclerView = findViewById(R.id.recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        HomeListApater adapter = new HomeListApater(BaseActivity.this);
        recyclerView.setAdapter(adapter);
    }

    private void setbar(TextView textView,View view){

        text_pending.setTextColor(getResources().getColor(R.color.text_grey));
        text_completed.setTextColor(getResources().getColor(R.color.text_grey));
        text_progress.setTextColor(getResources().getColor(R.color.text_grey));

        view_pending.setVisibility(View.GONE);
        view_progress.setVisibility(View.GONE);
        view_completed.setVisibility(View.GONE);

        textView.setTextColor(getResources().getColor(R.color.white));
        view.setVisibility(View.VISIBLE);
    }

    private void getdata(String status){

        if(status.equalsIgnoreCase("progess")){

            dataBaseHandler.get_meeting_list(status);
            setdata();
        }else if(status.equalsIgnoreCase("pending")){

            dataBaseHandler.get_meeting_list(status);
            setdata();

        }else if(status.equalsIgnoreCase("completed")){

            dataBaseHandler.get_meeting_list(status);
            setdata();


        }
    }
}
