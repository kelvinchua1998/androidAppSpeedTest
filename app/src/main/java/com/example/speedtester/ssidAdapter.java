package com.example.speedtester;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ssidAdapter extends RecyclerView.Adapter<ssidAdapter.MyViewHolder> {


    ArrayList<String> ssidList;
    ArrayList<JSONObject> lastSpeedtest;
    ArrayList<Integer> statusList;
    Context context;


    public ssidAdapter(Context c, ArrayList<String> ssid, ArrayList<JSONObject> Speedtest, ArrayList<Integer> status){
        context = c;
        ssidList = ssid;
        statusList = status;
        lastSpeedtest = Speedtest;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //always the same
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ssid_details,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.ssidTextView.setText(ssidList.get(position));
        try {
            int download = lastSpeedtest.get(position).getInt("download");
            int upload = lastSpeedtest.get(position).getInt("upload");
            int ping = lastSpeedtest.get(position).getInt("ping");

            holder.downloadTextView.setText("download: "+ download);
            holder.uploadTextView.setText("upload: "+ upload);
            holder.pingTextView.setText("ping: "+ ping);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(statusList.get(position)==0)
            holder.colourIndicator.setImageResource(R.drawable.green_indicator);
        else if (statusList.get(position)==1)
            holder.colourIndicator.setImageResource(R.drawable.yellow_indicator);
        else if (statusList.get(position)==2)
            holder.colourIndicator.setImageResource(R.drawable.red_indicator);


//        holder.ssidLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context,showApDetails.class);
//                intent.putExtra("com.example.speedtester.ssid",data1[position][0]);
//                intent.putExtra("com.example.speedtester.location",data1[position][2]);
//                intent.putExtra("com.example.speedtester.ip",data1[position][7]);
//                intent.putExtra("com.example.speedtester.site",data1[position][1]);
//                context.startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return ssidList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView ssidTextView, downloadTextView, uploadTextView, pingTextView;
        ImageView colourIndicator;
        ConstraintLayout ssidLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ssidTextView = itemView.findViewById(R.id.ssidTextView);
            downloadTextView = itemView.findViewById(R.id.downloadTextView);
            uploadTextView = itemView.findViewById(R.id.uploadTextView);
            pingTextView = itemView.findViewById(R.id.pingTextView);
            colourIndicator = itemView.findViewById(R.id.indicatorImageView);
            ssidLayout = itemView.findViewById(R.id.ssidLayout);
        }
    }
}
