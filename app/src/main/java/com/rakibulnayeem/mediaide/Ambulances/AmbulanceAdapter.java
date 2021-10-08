package com.rakibulnayeem.mediaide.Ambulances;

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

public class AmbulanceAdapter extends RecyclerView.Adapter<AmbulanceAdapter.MyViewHolder> {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 100 ;
    private Context context;
    private List<AddAmbulanceAdapter> adapterList;
    DatabaseReference dRef;
    String current_uid;
    Calendar calendar;

    public AmbulanceAdapter(Context context, List<AddAmbulanceAdapter> adapterList) {
        this.context = context;
        this.adapterList = adapterList;
    }

    @NonNull
    @Override
    public AmbulanceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.ambulance_sample_layout, parent, false);
        dRef = FirebaseDatabase.getInstance().getReference("call_history");
        current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AmbulanceAdapter.MyViewHolder holder, int position) {

        //using for get data
        final String name = adapterList.get(position).getName();
        final String driver_name = adapterList.get(position).getDriver_name();
        final String address = adapterList.get(position).getAddress();
        final String zilla = adapterList.get(position).getZilla();
        final String service_type = adapterList.get(position).getService_type();
        final String vehicle_no = adapterList.get(position).getVehicle_no();
        final String phone_number = adapterList.get(position).getPhone_number();


        //set data
        holder.nameTv.setText(name);
        holder.addressTv.setText(address);
        holder.phoneNumberTv.setText(phone_number);
        holder.zillaTv.setText(zilla);
        holder.vehicleTv.setText(vehicle_no);

        if(!driver_name.equals(""))
        {
            holder.driverNameTitleTv.setVisibility(View.VISIBLE);
            holder.driverNameTv.setVisibility(View.VISIBLE);
            holder.driverNameTv.setText(driver_name);
        }
        else
        {
            holder.driverNameTitleTv.setVisibility(View.GONE);
            holder.driverNameTv.setVisibility(View.GONE);
        }


        if(!service_type.equals(""))
        {
            holder.serviceTypeTv.setVisibility(View.VISIBLE);
            holder.serviceTypeTv.setText(service_type);
        }
        else
        {
            holder.serviceTypeTv.setVisibility(View.GONE);
        }


        //handle item click(if clicked on item)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
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

                String type = "Ambulance";
                calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a dd-MM-yyyy");
                String current_time = simpleDateFormat.format(calendar.getTime());

                String key = dRef.push().getKey();
                UploadCallHistoryAdapter uploadCallHistoryAdapter = new UploadCallHistoryAdapter(key, current_uid, name, type,phone_number, current_time);
                dRef.child(current_uid).child(key).setValue(uploadCallHistoryAdapter);

            }
        });

    }

    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv, addressTv, phoneNumberTv, zillaTv, vehicleTv, serviceTypeTv, driverNameTv, driverNameTitleTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.nameCardTvId);
            driverNameTv = itemView.findViewById(R.id.driverNameCardTvId);
            driverNameTitleTv = itemView.findViewById(R.id.driverNameTitleCardTvId);
            addressTv = itemView.findViewById(R.id.addressCardTvId);
            zillaTv = itemView.findViewById(R.id.zillaCardTvId);
            serviceTypeTv = itemView.findViewById(R.id.serviceTypeCardTvId);
            vehicleTv = itemView.findViewById(R.id.vehicleCardTvId);
            phoneNumberTv = itemView.findViewById(R.id.phoneNumberCardTvId);
        }
    }

}
