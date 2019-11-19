package com.andolasoft.visitorsmanagement.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.andolasoft.visitorsmanagement.R;
import com.bigkoo.pickerview.MyOptionsPickerView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeListApater extends RecyclerView.Adapter<HomeListApater.ViewHolder> {


    Context context;
    ArrayList arrayList;
    String status;
    DataBaseHandler dataBaseHandler;
    public HomeListApater(Context context,ArrayList arrayList,String status) {

        this.context = context;
        this.arrayList = arrayList;
        this.status = status;
        dataBaseHandler = new DataBaseHandler(context);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        SelectedMenuModel selectedMenuModel = (SelectedMenuModel) arrayList.get(i);
        viewHolder.text_name.setText("Name : "+selectedMenuModel.getName());
        viewHolder.text_visitee.setText("To Visit : "+selectedMenuModel.getEmployee_name());
        viewHolder.text_reason.setText("Reason : "+selectedMenuModel.getReason());
        if (selectedMenuModel.getIn_time()!=null && !selectedMenuModel.getIn_time().equals("")){
            viewHolder.text_time.setText("In time : "+selectedMenuModel.getIn_time());

        }else {
            viewHolder.text_time.setText("In time : "+"Not Added");

        }
        if (selectedMenuModel.getOut_time()!=null && !selectedMenuModel.getOut_time().equals("")){
            viewHolder.out_time.setText("Out time : "+selectedMenuModel.getOut_time());

        }else {
            viewHolder.out_time.setText("Out time : "+"Not Added");

        }

        try {
            Bitmap bitmap = BitmapFactory.decodeFile(selectedMenuModel.getImage_path());
            viewHolder.prof_pic.setImageBitmap(bitmap);
        }catch (Exception e){e.printStackTrace();
        }
        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (status.equalsIgnoreCase(CommonUtilties.Pending)) {
                    show_pisker("Enter In Time : ",status,i);

                }else if(status.equalsIgnoreCase(CommonUtilties.InProgress)){
                    show_pisker("Enter Out Time : ",status,i);
                }

            }
        });
    }

    @Override
    public int getItemCount() {

        return arrayList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout relativeLayout;
        TextView text_name,text_visitee,text_reason,text_time,out_time;
        CircleImageView prof_pic;

        public ViewHolder(View itemView) {
            super(itemView);

            relativeLayout = itemView.findViewById(R.id.rel_main);
            text_name = itemView.findViewById(R.id.text_name);
            text_visitee = itemView.findViewById(R.id.text_visitee);
            text_reason = itemView.findViewById(R.id.text_reason);
            text_time = itemView.findViewById(R.id.text_time);
            out_time = itemView.findViewById(R.id.out_time);
            prof_pic = itemView.findViewById(R.id.prof_pic);
        }

    }

    private void show_pisker(String title, final String status,final int j){

        MyOptionsPickerView threePicker = new MyOptionsPickerView(context);
        final ArrayList<String> threeItemsOptions1 = new ArrayList<String>();

        for(int i=1;i<=12;i++){

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
        threePicker.setTitle(title);
        threePicker.setCyclic(false, false, false);
        threePicker.setSelectOptions(0, 0, 0);
        threePicker.setOnoptionsSelectListener(new MyOptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {

                String hour = threeItemsOptions1.get(options1);
                String minute = threeItemsOptions2.get(option2);
                String ampm = threeItemsOptions3.get(options3);

                if(status.equalsIgnoreCase(CommonUtilties.Pending)){

                    if(dataBaseHandler!=null){
                        SelectedMenuModel selectedMenuModel = (SelectedMenuModel) arrayList.get(j);
                        dataBaseHandler.update_meeting_list(selectedMenuModel.getId(),CommonUtilties.InProgress);

                        arrayList.remove(j);
                        notifyDataSetChanged();
                    }

                }else if(status.equalsIgnoreCase(CommonUtilties.InProgress)){

                    if(dataBaseHandler!=null){
                        SelectedMenuModel selectedMenuModel = (SelectedMenuModel) arrayList.get(j);
                        dataBaseHandler.update_meeting_list(selectedMenuModel.getId(),CommonUtilties.Completed);

                        arrayList.remove(j);
                        notifyDataSetChanged();
                    }

                }
            }
        });
        threePicker.show();
    }



}