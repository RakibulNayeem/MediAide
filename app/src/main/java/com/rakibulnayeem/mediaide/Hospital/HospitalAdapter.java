package com.rakibulnayeem.mediaide.Hospital;

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

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.MyViewHolder> {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 100;
    private Context context;
    private List<AddHospitalAdapter> adapterList;

    DatabaseReference dRef;
    String current_uid;
    Calendar calendar;


    public HospitalAdapter(Context context, List<AddHospitalAdapter> adapterList) {
        this.context = context;
        this.adapterList = adapterList;
    }

    @NonNull
    @Override
    public HospitalAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.hospital_sample_layout,parent,false);
        dRef = FirebaseDatabase.getInstance().getReference("call_history");
        current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalAdapter.MyViewHolder holder, int position) {

        //using for get data
        final  String name = adapterList.get(position).getName();
        final  String category = adapterList.get(position).getCategory();
        final String address = adapterList.get(position).getAddress();
        final String zilla = adapterList.get(position).getZilla();
        final  String phone_number = adapterList.get(position).getPhone_number();
        final  String hospital_id = adapterList.get(position).getKey();



        //set data
        holder.nameTv.setText(name);
        holder.categoryTv.setText(category);
        holder.addressTv.setText(address);
        holder.zillaTv.setText(zilla);
        holder.phoneNumberTv.setText(phone_number);



        //handle item click(if clicked on item)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {

               Intent intent = new Intent(context, HospitalsDoctors.class);
               intent.putExtra("hospital_id",hospital_id);
               context.startActivity(intent);



           }


        });


        //click on call imageview button

        holder.callHospitalIv.setOnClickListener(new View.OnClickListener() {
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

                // adding call history
                String type = "Hospital";
                calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a dd-MM-yyyy");
                String current_time = simpleDateFormat.format(calendar.getTime());

                String key = dRef.push().getKey();
                UploadCallHistoryAdapter uploadCallHistoryAdapter = new UploadCallHistoryAdapter(key, current_uid, name, type, phone_number, current_time);
                dRef.child(current_uid).child(key).setValue(uploadCallHistoryAdapter);


            }
        });

    }


    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv,addressTv,zillaTv,phoneNumberTv, categoryTv;
        ImageView callHospitalIv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.nameCardTvId);
            categoryTv = itemView.findViewById(R.id.categoryCardTvId);
            addressTv = itemView.findViewById(R.id.addressCardTvId);
            zillaTv = itemView.findViewById(R.id.zillaCardTvId);
            phoneNumberTv = itemView.findViewById(R.id.phoneNumberCardTvId);
            callHospitalIv = itemView.findViewById(R.id.callHospitalBtnId);
        }
    }
}