package com.example.speedtester;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
    private RecyclerViewClickListener clickListener;

    ArrayList<String> ssidList;
    ArrayList<JSONObject> lastSpeedtest;
    ArrayList<Integer> statusList;
    Context context;


    public ssidAdapter(Context c, ArrayList<String> ssid, ArrayList<JSONObject> Speedtest, ArrayList<Integer> status, RecyclerViewClickListener listener){
        context = c;
        ssidList = ssid;
        statusList = status;
        lastSpeedtest = Speedtest;
        this.clickListener = listener;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //always the same
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ssid_details,parent,false);
        return new MyViewHolder(view,clickListener);
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
        //setting colour for status indicator
        if(statusList.get(position)==0){
            String text = "Status: Normal";
            SpannableString ss = new SpannableString(text);
            ForegroundColorSpan greenIndicator = new ForegroundColorSpan(context.getResources().getColor(R.color.indicatorGreen));
            ss.setSpan(greenIndicator, 8,14, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            holder.statusTextView.setText(ss);
        }

        else if (statusList.get(position)==1) {
            String text = "Status: Warning";
            SpannableString ss = new SpannableString(text);
            ForegroundColorSpan greenIndicator = new ForegroundColorSpan(context.getResources().getColor(R.color.indicatorOrange));
            ss.setSpan(greenIndicator, 8,15, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            holder.statusTextView.setText(ss);
        }

        else if (statusList.get(position)==2) {
            String text = "Status: Critical";
            SpannableString ss = new SpannableString(text);
            ForegroundColorSpan greenIndicator = new ForegroundColorSpan(context.getResources().getColor(R.color.indicatorRed));
            ss.setSpan(greenIndicator, 8,16, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            holder.statusTextView.setText(ss);
        }

    }

    @Override
    public int getItemCount() {
        return ssidList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        TextView ssidTextView, downloadTextView, uploadTextView, pingTextView,statusTextView;
        ConstraintLayout ssidLayout;
        RecyclerViewClickListener clickListener;

        public MyViewHolder(@NonNull View itemView, RecyclerViewClickListener clickListener) {
            super(itemView);

            ssidTextView = itemView.findViewById(R.id.ssidTextView);
            downloadTextView = itemView.findViewById(R.id.downloadTextView);
            uploadTextView = itemView.findViewById(R.id.uploadTextView);
            pingTextView = itemView.findViewById(R.id.pingTextView);
            statusTextView = itemView.findViewById(R.id.statusAPTextView);
            ssidLayout = itemView.findViewById(R.id.ssidLayout);
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
