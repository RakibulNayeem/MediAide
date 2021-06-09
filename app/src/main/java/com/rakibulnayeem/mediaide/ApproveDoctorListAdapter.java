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

public class ApproveDoctorListAdapter extends RecyclerView.Adapter<ApproveDoctorListAdapter.MyViewHolder> {

    private Context context;
    private List<AddDoctorAdapter> adapterList;

    public ApproveDoctorListAdapter(Context context, List<AddDoctorAdapter> adapterList) {
        this.context = context;
        this.adapterList = adapterList;
    }

    @NonNull
    @Override
    public ApproveDoctorListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.approve_doctor_sample_layout,parent,false);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final  String name = adapterList.get(position).getName();
        final  String uid = adapterList.get(position).getUid();


        //set data
        holder.nameTv.setText(name);

        //handle item click(if clicked on item)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DoctorApprove.class);
                intent.putExtra("uid",uid);
                context.startActivity(intent);


            }
        });



    }


    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.nameCardTvId);

        }
    }
}