package com.rakibulnayeem.mediaide;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {

    private Context context;
    private List<PostAdapter> adapterList;

    public RequestAdapter(Context context, List<PostAdapter> adapterList) {
        this.context = context;
        this.adapterList = adapterList;
    }

    @NonNull
    @Override
    public RequestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.blood_request_sample_layout,parent,false);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        final String blood_group = adapterList.get(position).getBlood_group();
        final String hospital_name = adapterList.get(position).getHospital_name();
        final String details = adapterList.get(position).getDetails();
        final String zilla = adapterList.get(position).getZilla();
        final String phone_number = adapterList.get(position).getPhone_number();
        final String date_time = adapterList.get(position).getDate_time();
        final String current_time = adapterList.get(position).getCurrent_time();


        holder.bloodGroupTv.setText(blood_group);
        holder.hospitalNameTv.setText(hospital_name);
        holder.detailsTv.setText(details);
        holder.zillaTv.setText(zilla);
        holder.phoneNumberTv.setText(phone_number);
        holder.dateTimeTv.setText(date_time);
        holder.currenTimeTv.setText(current_time);


    }

    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView hospitalNameTv,detailsTv,zillaTv,phoneNumberTv,dateTimeTv,bloodGroupTv, currenTimeTv;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            bloodGroupTv = itemView.findViewById(R.id.bloodGroupCardTvId);
            hospitalNameTv = itemView.findViewById(R.id.hospitalNameCardTvId);
            detailsTv = itemView.findViewById(R.id.detailsCardTvId);
            zillaTv = itemView.findViewById(R.id.zillaCardTvId);
            phoneNumberTv = itemView.findViewById(R.id.phoneNumberCardTvId);
            dateTimeTv = itemView.findViewById(R.id.dateTimeCardTvId);
            currenTimeTv = itemView.findViewById(R.id.currentTimeCardTvId);
        }
    }
}