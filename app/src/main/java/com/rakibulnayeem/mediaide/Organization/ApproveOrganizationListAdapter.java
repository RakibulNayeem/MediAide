package com.rakibulnayeem.mediaide.Organization;

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
import com.rakibulnayeem.mediaide.InternetConnection;
import com.rakibulnayeem.mediaide.R;

import java.util.List;

public class ApproveOrganizationListAdapter extends RecyclerView.Adapter<ApproveOrganizationListAdapter.MyViewHolder> {


    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 100;
    private Context context;
    private List<AddOrganizationsAdapter> adapterList;
    InternetConnection internetConnection;
    DatabaseReference dataRef, dataRefApprove;

    String _uid, _key,  _name,_open, _zilla, _phone_number, _address;

    public ApproveOrganizationListAdapter(Context context, List<AddOrganizationsAdapter> adapterList) {
        this.context = context;
        this.adapterList = adapterList;
    }

    @NonNull
    @Override
    public ApproveOrganizationListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.organizations_sample_layout, parent, false);
        internetConnection = new InternetConnection(context);
        dataRef = FirebaseDatabase.getInstance().getReference("organizations_info");
        dataRefApprove = FirebaseDatabase.getInstance().getReference("organizations_info_approve");


        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //using for get data
        final String name = adapterList.get(position).getName();
        final String address = adapterList.get(position).getAddress();
        final String phone_number = adapterList.get(position).getPhone_number();
        final String zilla = adapterList.get(position).getZilla();
        final String open = adapterList.get(position).getOpen();
        final String uid = adapterList.get(position).getUid();
        final String key = adapterList.get(position).getUid();

        _name = name;
        _address = address;
        _phone_number = phone_number;
        _zilla = zilla;
        _open = open;
        _uid = uid;
        _key = key;



        //set data
        holder.nameTv.setText(name);
        holder.addressTv.setText(address);
        holder.zillaTv.setText(zilla);
        holder.openTv.setText(open);
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


        //handle long click
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                ApproveInfo();

                return false;
            }
        });



    }


    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv, addressTv, phoneNumberTv, openTv, zillaTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.nameCardTvId);
            zillaTv = itemView.findViewById(R.id.zillaCardTvId);
            openTv = itemView.findViewById(R.id.openCardTvId);
            addressTv = itemView.findViewById(R.id.addressCardTvId);
            phoneNumberTv = itemView.findViewById(R.id.phoneNumberCardTvId);
        }
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


    private void Approved() {


        AddOrganizationsAdapter adapter = new AddOrganizationsAdapter(_uid,_key,_name,_address,_open, _zilla,_phone_number);
        dataRef.child(_uid).setValue(adapter);

        Toast.makeText(context,"Information uploaded",Toast.LENGTH_LONG).show();

        DeleteInfo();

    }

    private void DeleteInfo() {

        dataRefApprove.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(_uid)){
                    dataRefApprove.child(_uid).removeValue();
                    Toast.makeText(context,"Information  deleted successfully from old position. ",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}
