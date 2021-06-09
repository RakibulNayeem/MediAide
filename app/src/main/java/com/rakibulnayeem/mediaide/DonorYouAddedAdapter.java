package com.rakibulnayeem.mediaide;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DonorYouAddedAdapter extends RecyclerView.Adapter<DonorYouAddedAdapter.MyViewHolder> {


    private Context context;
    private List<AddDonorAdapter> uploadList;

    public DonorYouAddedAdapter(Context context, List<AddDonorAdapter> uploadList) {
        this.context = context;
        this.uploadList = uploadList;
    }

    @NonNull
    @Override
    public DonorYouAddedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.users_row_sample_view,parent,false);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //using for get data

        final String uid = uploadList.get(position).getUid();
        final String donor_key = uploadList.get(position).getKey();

        final String village = uploadList.get(position).getVillage();
        final  String blood_group = uploadList.get(position).getBlood_group();
        final String upazila = uploadList.get(position).getUpazila();
        final  String last_donation_date = uploadList.get(position).getLast_donation_date();

        //set data
        holder.bloodGroupTv.setText(blood_group);
        holder.upazilaTv.setText(upazila);
        holder.villageTv.setText(village);
        holder.lastDDTv.setText(last_donation_date);



        //handle item click(if clicked on item)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DonorYouAddedProfile.class);
                intent.putExtra("donor_key",donor_key);
                context.startActivity(intent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return uploadList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView bloodGroupTv,upazilaTv,villageTv,lastDDTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            bloodGroupTv = itemView.findViewById(R.id.bloodGroupCardTvId);
            upazilaTv = itemView.findViewById(R.id.upazilaCardTvId);
            villageTv = itemView.findViewById(R.id.villageCardTvId);
            lastDDTv = itemView.findViewById(R.id.lastDDCardTvId);

        }
    }
}