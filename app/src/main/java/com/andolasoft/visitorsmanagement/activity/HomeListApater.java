package com.andolasoft.visitorsmanagement.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.andolasoft.visitorsmanagement.R;

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

    }

    @Override
    public int getItemCount() {

        return 20;
    }
    class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(View itemView) {
            super(itemView);


        }

    }



}