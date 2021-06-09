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

public class HospitalListAdminAdapter extends RecyclerView.Adapter<HospitalListAdminAdapter.MyViewHolder> {

    private Context context;
    private List<AddHospitalAdapter> adapterList;

    public HospitalListAdminAdapter(Context context, List<AddHospitalAdapter> adapterList) {
        this.context = context;
        this.adapterList = adapterList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.hospital_list_sample_layout,parent,false);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final  String name = adapterList.get(position).getName();
        final String address = adapterList.get(position).getAddress();

        holder.nameTv.setText(name);
        holder.addressTv.setText(address);
        final  String hospital_id = adapterList.get(position).getKey();

        //handle item click(if clicked on item)
        holder.itemView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,AddDoctorByAdmin.class);
                intent.putExtra("hospital_name",name);
                intent.putExtra("hospital_id",hospital_id);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv,addressTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.nameCardTvId);
            addressTv = itemView.findViewById(R.id.addressCardTvId);
        }
    }



}
