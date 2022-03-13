package com.rakibulnayeem.mediaide.BloodBanks;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rakibulnayeem.mediaide.Fragments.UploadCallHistoryAdapter;
import com.rakibulnayeem.mediaide.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class BloodBankAdapter extends RecyclerView.Adapter<BloodBankAdapter.MyViewHolder> {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 100 ;
    private Context context;
    private List<AddBBankAdapter> bBankAdapterList;

    DatabaseReference dRef;
    String current_uid, Name;
    Calendar calendar;

    public BloodBankAdapter(Context context, List<AddBBankAdapter> bBankAdapterList) {
        this.context = context;
        this.bBankAdapterList = bBankAdapterList;
    }

    @NonNull
    @Override
    public BloodBankAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.blood_bank_sample_layout, parent, false);

        dRef = FirebaseDatabase.getInstance().getReference("call_history");
        current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BloodBankAdapter.MyViewHolder holder, int position) {

        //using for get data
        final String name_bb = bBankAdapterList.get(position).getName_bb();
        final String address_bb = bBankAdapterList.get(position).getAddress_bb();
        final String open_bb = bBankAdapterList.get(position).getOpen_bb();
        final String zilla_bb = bBankAdapterList.get(position).getZilla_bb();
        final String phone_number_bb = bBankAdapterList.get(position).getPhone_number_bb();

        Name = name_bb;


        //set data
        holder.nameBBTv.setText(name_bb);
        holder.addressBBTv.setText(address_bb);
        holder.openBBTv.setText(open_bb);
//        holder.zillaBBTv.setText(zilla_bb);
        holder.phoneNumberBBTv.setText(phone_number_bb);


        //handle item click(if clicked on item)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone_number_bb));

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                            Manifest.permission.CALL_PHONE)) {
                    } else {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_CALL_PHONE);
                    }


                    return;
                }
                context.startActivity(callIntent);

                // adding call history
                String type = "Blood Bank";
                calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a dd-MM-yyyy");
                String current_time = simpleDateFormat.format(calendar.getTime());

                String key = dRef.push().getKey();
                UploadCallHistoryAdapter uploadCallHistoryAdapter = new UploadCallHistoryAdapter(key, current_uid, Name, type,phone_number_bb, current_time);
                dRef.child(current_uid).child(key).setValue(uploadCallHistoryAdapter);


            }
        });

    }

    @Override
    public int getItemCount() {
        return bBankAdapterList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameBBTv,addressBBTv,zillaBBTv,phoneNumberBBTv, openBBTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameBBTv = itemView.findViewById(R.id.nameBBCardTvId);
            addressBBTv = itemView.findViewById(R.id.addressCardTvId);
            openBBTv = itemView.findViewById(R.id.openCardTvId);
           // zillaBBTv = itemView.findViewById(R.id.zillaCardTvId);
            phoneNumberBBTv = itemView.findViewById(R.id.phoneNumberCardTvId);
        }
    }
}