package com.example.speedtester.buildinglistcardview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.speedtester.R;
import com.example.speedtester.building_list.buildingAdapter;
import com.example.speedtester.ssid_list.ssidAdapter;

import java.util.ArrayList;


public class BuildinglistAdapter extends RecyclerView.Adapter<BuildinglistAdapter.MyViewHolder> {
    private RecyclerViewClickListener clickListener;
    static ArrayList<buildinglist.building> buildingArrayList;
    Context context;

    public BuildinglistAdapter(Context c , ArrayList<buildinglist.building> bArrayList,RecyclerViewClickListener listener){
        buildingArrayList = bArrayList;
        context = c;
        clickListener = listener;
    }

    public void setbuildinglistAP(ArrayList<buildinglist.building> bArrayList) {
        buildingArrayList.clear();
        buildingArrayList.addAll(bArrayList);

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.buildingcardviewdetail,parent,false);
        return new MyViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.buildingname.setText( buildingArrayList.get(position).name);
        holder.numofAP.setText("AP: "+ buildingArrayList.get(position).numberofAP);
        holder.warningAP.setText("Warning: "+ buildingArrayList.get(position).warningAP);
        holder.criticalAP.setText("Critical: "+ buildingArrayList.get(position).criticalAP);
        holder.downloadAP.setText(String.valueOf(buildingArrayList.get(position).downloadAP));
        holder.uploadAP.setText(String.valueOf(buildingArrayList.get(position).uploadAP));


    }

    @Override
    public int getItemCount() {
        return buildingArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        TextView buildingname, numofAP, warningAP, criticalAP,downloadAP,uploadAP;

        RecyclerViewClickListener clickListener;

        public MyViewHolder(@NonNull View itemView, RecyclerViewClickListener clickListener) {
            super(itemView);

            buildingname = itemView.findViewById(R.id.buildingtitleTextView);
            numofAP = itemView.findViewById(R.id.numAPcardTextView);
            warningAP = itemView.findViewById(R.id.warningnumcardTextView);
            criticalAP = itemView.findViewById(R.id.criticalNumCardTextView);
            downloadAP = itemView.findViewById(R.id.downloadcardTextView);
            uploadAP = itemView.findViewById(R.id.uploadcardTextView);

            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener{
        void onClick(int position);
    }

}