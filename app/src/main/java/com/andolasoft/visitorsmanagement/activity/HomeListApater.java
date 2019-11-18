package com.andolasoft.visitorsmanagement.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.andolasoft.visitorsmanagement.R;
import com.bigkoo.pickerview.MyOptionsPickerView;

import java.util.ArrayList;

public class HomeListApater extends RecyclerView.Adapter<HomeListApater.ViewHolder> {


    Context context;

    public HomeListApater(Context context) {

        this.context = context;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder companyViewHolder, int i) {

        companyViewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_pisker();
            }
        });
    }

    @Override
    public int getItemCount() {

        return 20;
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            relativeLayout = itemView.findViewById(R.id.rel_main);
        }

    }

    private void show_pisker(){

        MyOptionsPickerView threePicker = new MyOptionsPickerView(context);
        final ArrayList<String> threeItemsOptions1 = new ArrayList<String>();

        for(int i=0;i<=12;i++){

            if(i<10){
                threeItemsOptions1.add("0"+Integer.toString(i));
            }else{
                threeItemsOptions1.add(Integer.toString(i)) ;
            }

        }

        final ArrayList<String> threeItemsOptions2 = new ArrayList<String>();
        for(int i=0;i<60;i++){

            if(i<10){
                threeItemsOptions2.add("0"+Integer.toString(i));
            }else{
                threeItemsOptions2.add(Integer.toString(i)) ;
            }

        }

        final ArrayList<String> threeItemsOptions3 = new ArrayList<String>();
        threeItemsOptions3.add("AM");
        threeItemsOptions3.add("PM");


        threePicker.setPicker(threeItemsOptions1, threeItemsOptions2, threeItemsOptions3, false);
        threePicker.setTitle("Enter In Time : ");
        threePicker.setCyclic(false, false, false);
        threePicker.setSelectOptions(0, 0, 0);
        threePicker.setOnoptionsSelectListener(new MyOptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {

            }
        });
        threePicker.show();
    }



}