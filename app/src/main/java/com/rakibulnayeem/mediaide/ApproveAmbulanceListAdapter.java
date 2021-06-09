package com.rakibulnayeem.mediaide;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ApproveAmbulanceListAdapter extends RecyclerView.Adapter<ApproveAmbulanceListAdapter.MyViewHolder> {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 100 ;
    private Context context;
    private List<AddAmbulanceAdapter> adapterList;
    InternetConnection internetConnection;
    DatabaseReference dataRef, dataRefApprove;

    String _name, _driver_name, _address, _phone_number,_vehicle_no, _service_type, _available_switch_value,  _uid,_key,  _zilla;

    public ApproveAmbulanceListAdapter(Context context, List<AddAmbulanceAdapter> adapterList) {
        this.context = context;
        this.adapterList = adapterList;
    }

    @NonNull
    @Override
    public ApproveAmbulanceListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.ambulance_sample_layout, parent, false);
        dataRef = FirebaseDatabase.getInstance().getReference("ambulance_info");
        dataRefApprove = FirebaseDatabase.getInstance().getReference("ambulance_info_approve");

        internetConnection = new InternetConnection(context);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //using for get data
        final String name = adapterList.get(position).getName();
        final String driver_name = adapterList.get(position).getDriver_name();
        final String address = adapterList.get(position).getAddress();
        final String zilla = adapterList.get(position).getZilla();
        final String service_type = adapterList.get(position).getService_type();
        final String vehicle_no = adapterList.get(position).getVehicle_no();
        final String phone_number = adapterList.get(position).getPhone_number();

        _uid = adapterList.get(position).getUid();
        _zilla = zilla;
        _key = adapterList.get(position).getKey();
        _available_switch_value = adapterList.get(position).getAvailable_switch_value();
        _name = name;
        _driver_name = driver_name;
        _address = address;
        _vehicle_no = vehicle_no;
        _service_type = service_type;
        _phone_number = phone_number;


        //set data
        holder.nameTv.setText(name);
        holder.driverNameTv.setText(driver_name);
        holder.serviceTypeTv.setText(service_type);
        holder.driverNameTv.setText(driver_name);
        holder.addressTv.setText(address);
        holder.zillaTv.setText(zilla);
        holder.vechicleTv.setText(vehicle_no);
        holder.phoneNumberTv.setText(phone_number);


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

            }
        });

        // handle long click

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                ApproveInfo();

                return false;
            }
        });


    }

    private void ApproveInfo() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure to approve?");


        //set layout linear layout
        LinearLayout linearLayout = new LinearLayout(context);

        //buttons recover
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(internetConnection.checkConnection())
                {

                    Approved();

                }
                else
                {
                    // Not Available...
                    Toast.makeText(context,"No Internet Connection",Toast.LENGTH_LONG).show();
                }

            }
        });


        //buttons edit

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //edit dialog
                if(internetConnection.checkConnection())
                {
                    dialog.dismiss();
                }
                else
                {
                    // Not Available...
                    Toast.makeText(context,"No Internet Connection",Toast.LENGTH_LONG).show();
                }

            }
        });

        //show dialog
        builder.create().show();

    }



    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv, driverNameTv, addressTv, phoneNumberTv, zillaTv, vechicleTv, serviceTypeTv, driverNameTitleTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.nameCardTvId);
            driverNameTv = itemView.findViewById(R.id.driverNameCardTvId);
            driverNameTitleTv = itemView.findViewById(R.id.driverNameTitleCardTvId);
            addressTv = itemView.findViewById(R.id.addressCardTvId);
            zillaTv = itemView.findViewById(R.id.zillaCardTvId);
            serviceTypeTv = itemView.findViewById(R.id.serviceTypeCardTvId);
            vechicleTv = itemView.findViewById(R.id.vehicleCardTvId);
            phoneNumberTv = itemView.findViewById(R.id.phoneNumberCardTvId);
        }
    }




    private void Approved() {


        AddAmbulanceAdapter adapter = new AddAmbulanceAdapter(_uid, _key, _name, _driver_name, _address,_zilla, _vehicle_no, _service_type, _available_switch_value,_phone_number);
        dataRef.child(_key).setValue(adapter);

        Toast.makeText(context,"Information uploaded successfully!",Toast.LENGTH_LONG).show();

        DeleteInfo();

    }

    private void DeleteInfo() {

        dataRefApprove.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(_key)){
                    dataRefApprove.child(_key).removeValue();
                    Toast.makeText(context,"Information  deleted successfully from old position.",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
