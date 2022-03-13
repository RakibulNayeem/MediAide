package com.rakibulnayeem.mediaide.Donor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rakibulnayeem.mediaide.DonorYouAddedProfile;
import com.rakibulnayeem.mediaide.R;

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
        final String zilla = uploadList.get(position).getZilla();
        final  String last_donation_date = uploadList.get(position).getLast_donation_date();

        //set data
        holder.bloodGroupTv.setText(blood_group);
        holder.addressTv.setText(new StringBuilder().append(village).append(", ").append(upazila).append(", ").append(zilla).toString());
        holder.lastDDTv.setText(last_donation_date);



        //handle item click(if clicked on item)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DonorYouAddedProfile.class);
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

        TextView bloodGroupTv,addressTv,lastDDTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            bloodGroupTv = itemView.findViewById(R.id.bloodGroupCardTvId);
            addressTv = itemView.findViewById(R.id.addressCardTvId);
            lastDDTv = itemView.findViewById(R.id.lastDDCardTvId);

        }
    }
}