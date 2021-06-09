package com.rakibulnayeem.mediaide;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HospitalsDoctorsAdapter extends RecyclerView.Adapter<HospitalsDoctorsAdapter.MyViewHolder> {

    private Context context;
    private List<AddDoctorForHospitalAdapter> adapterList;


    public HospitalsDoctorsAdapter(Context context, List<AddDoctorForHospitalAdapter> adapterList) {
        this.context = context;
        this.adapterList = adapterList;
    }

    @NonNull
    @Override
    public HospitalsDoctorsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.hospitals_doctors_sample_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //using for get data
        final  String name = adapterList.get(position).getName();
        final  String degree = adapterList.get(position).getDegree();
        final  String speciality = adapterList.get(position).getSpeciality();
        final  String fee = adapterList.get(position).getFee();
        final String address = adapterList.get(position).getChamber_address();
        final String image_uri = adapterList.get(position).getImageUri();

        final  String uid = adapterList.get(position).getUid();


        //set data
        holder.nameTv.setText(name);
        holder.chamberAddressTv.setText(address);
        holder.degreeTv.setText(degree);
        holder.feeTv.setText(fee);

        if (speciality.isEmpty())
        {
            holder.specialityTv.setVisibility(View.GONE);
        }
        else {
            holder.specialityTv.setText(speciality);
        }


        //set profile picture
        try{
            Picasso.get().load(image_uri)
                    .placeholder(R.drawable.doctor_2)
                    .fit()
                    .centerCrop()
                    .into(holder.doctorProfileImage);
        }
        catch (Exception e)
        {
            //if there is any exception then set default;
            Picasso.get().load(R.drawable.doctor_2).into(holder.doctorProfileImage);
        }




        //handle item click(if clicked on item)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,HospitalDoctorsProfile.class);
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

        TextView nameTv,degreeTv,chamberAddressTv,specialityTv,feeTv;
        ImageView doctorProfileImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.nameCardTvId);
            specialityTv = itemView.findViewById(R.id.specialityCardTvId);
            feeTv = itemView.findViewById(R.id.feeCardTvId);
            degreeTv = itemView.findViewById(R.id.degreeCardTvId);
            chamberAddressTv = itemView.findViewById(R.id.chamberAddressCardTvId);
            doctorProfileImage = itemView.findViewById(R.id.doctorProfileImageViewId);


        }

    }
}