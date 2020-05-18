package com.example.speedtester;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ssidAdapter extends RecyclerView.Adapter<ssidAdapter.MyViewHolder> {

    String data1[][];
    Context context;

    public ssidAdapter(Context c, String[][] s1 ){
        context = c;
        data1 = s1;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ssid_details,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.text1.setText(data1[position][0]);
        holder.text2.setText(data1[position][2]);
        holder.text3.setText(data1[position][7]);

        holder.ssidLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,showApDetails.class);
                intent.putExtra("com.example.speedtester.ssid",data1[position][0]);
                intent.putExtra("com.example.speedtester.location",data1[position][2]);
                intent.putExtra("com.example.speedtester.ip",data1[position][7]);
                intent.putExtra("com.example.speedtester.site",data1[position][1]);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data1.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView text1, text2, text3;
        ConstraintLayout ssidLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.ssidTextView);
            text2 = itemView.findViewById(R.id.locationTextView);
            text3 = itemView.findViewById(R.id.ipTextView);
            ssidLayout = itemView.findViewById(R.id.ssidLayout);
        }
    }
}
