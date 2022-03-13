package com.rakibulnayeem.mediaide.Donor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rakibulnayeem.mediaide.Fragments.UploadCallHistoryAdapter;
import com.rakibulnayeem.mediaide.R;
import com.rakibulnayeem.mediaide.SignUpLogIn.SignUpAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class BloodDonorAdapter extends RecyclerView.Adapter<BloodDonorAdapter.MyViewHolder> {


    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 100 ;
    private Context context;
    private List<SignUpAdapter> uploadList;

 //   private InterstitialAd mInterstitialAd;
    private int click_counter;
    DatabaseReference clickDataRef;

    DatabaseReference dRef;
    String current_uid;
    Calendar calendar;

    public BloodDonorAdapter(Context context, List<SignUpAdapter> uploadList) {
        this.context = context;
        this.uploadList = uploadList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.users_row_sample_view,parent,false);
        dRef = FirebaseDatabase.getInstance().getReference("call_history");
        current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //using for get data

        final String uid = uploadList.get(position).getUid();
        final String village = uploadList.get(position).getVillage();
      final  String blood_group = uploadList.get(position).getBlood_group();
      final String upazila = uploadList.get(position).getUpazila();
      final String zilla = uploadList.get(position).getZilla();
      final String phone_number = uploadList.get(position).getPhone_number();
      final String donate_switch = uploadList.get(position).getDonate_switch();
      final  String last_donation_date = uploadList.get(position).getLast_donation_date();

        //set data
        holder.bloodGroupTv.setText(blood_group);
        holder.addressTv.setText(new StringBuilder().append(village).append(", ").append(upazila).append(", ").append(zilla).toString());
        holder.lastDDTv.setText(last_donation_date);

        if(donate_switch.equals("true")){
            holder.donate_availabilityTv.setText("Available");
            holder.donate_availabilityTv.setTextColor(Color.parseColor("#047065"));
        }
        else {
            holder.donate_availabilityTv.setText("Not Available");
            holder.donate_availabilityTv.setTextColor(Color.parseColor("#DD4E4E"));
        }



        //handle item click(if clicked on item)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, BloodDonorProfile.class);
                intent.putExtra("uid",uid);
                context.startActivity(intent);


            }
        });

        //handle call button(if clicked on it)
        holder.callDonorBtnIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone_number));
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                            android.Manifest.permission.CALL_PHONE)) {
                    } else {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{android.Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    }
                }

                context.startActivity(callIntent);

                String type = "Blood Donor";
                calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a dd-MM-yyyy");
                String current_time = simpleDateFormat.format(calendar.getTime());

                String key = dRef.push().getKey();
                String name = "Blood Donor";
                UploadCallHistoryAdapter uploadCallHistoryAdapter = new UploadCallHistoryAdapter(key, current_uid, name, type,phone_number, current_time);
                dRef.child(current_uid).child(key).setValue(uploadCallHistoryAdapter);

            }
        });

    }

    @Override
    public int getItemCount() {
        return uploadList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView bloodGroupTv,addressTv, lastDDTv,donate_availabilityTv, donatedTv;
        ImageView callDonorBtnIv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            bloodGroupTv = itemView.findViewById(R.id.bloodGroupCardTvId);
            addressTv = itemView.findViewById(R.id.addressCardTvId);
            donate_availabilityTv = itemView.findViewById(R.id.availabilityCardTvId);
            donatedTv = itemView.findViewById(R.id.donatedCardTvId);
            lastDDTv = itemView.findViewById(R.id.lastDDCardTvId);
            callDonorBtnIv = itemView.findViewById(R.id.callDonorBtnId);

        }
    }
}