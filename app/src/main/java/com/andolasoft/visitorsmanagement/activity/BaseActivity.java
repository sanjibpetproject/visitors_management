package com.andolasoft.visitorsmanagement.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.andolasoft.visitorsmanagement.R;

public class BaseActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView text_pending,text_progress,text_completed;
    View view_progress,view_pending,view_completed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        init();
        click_function();
        setdata();

    }

    private void init(){

        text_progress = findViewById(R.id.text_progress);
        text_pending = findViewById(R.id.text_pending);
        text_completed = findViewById(R.id.text_completed);
        view_pending = findViewById(R.id.view_pending);
        view_progress = findViewById(R.id.view_progress);
        view_completed = findViewById(R.id.view_completed);
    }

    private void click_function(){

        text_progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbar(text_progress,view_progress);
            }
        });
        text_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbar(text_pending,view_pending);

            }
        });
        text_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbar(text_completed,view_completed);

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
}
