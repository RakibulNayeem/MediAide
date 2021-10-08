package com.rakibulnayeem.mediaide.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rakibulnayeem.mediaide.Donor.BloodDonorProfile;
import com.rakibulnayeem.mediaide.InternetConnection;
import com.rakibulnayeem.mediaide.Posts.PostEdit;
import com.rakibulnayeem.mediaide.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CallHistoryAdapter extends RecyclerView.Adapter<CallHistoryAdapter.MyViewHolder> {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 100 ;
    private Context context;
    private List<UploadCallHistoryAdapter> adapterList;

    DatabaseReference dRef;
    String current_uid;
    Calendar calendar;
    InternetConnection internetConnection;


    public CallHistoryAdapter(Context context, List<UploadCallHistoryAdapter> adapterList) {
           this.context = context;
           this.adapterList = adapterList;
        }

@NonNull
@Override
public CallHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.call_history_sample_layout,parent,false);
        internetConnection = new InternetConnection(context);

        dRef = FirebaseDatabase.getInstance().getReference("call_history");
        current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        return new MyViewHolder(view);
        }


@Override
public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


       final String name = adapterList.get(position).getName();
       final String number = adapterList.get(position).getPhone_number();
       final String time = adapterList.get(position).getCurrent_time();
       final String type = adapterList.get(position).getType();

       final String key = adapterList.get(position).getKey();



        holder.dateTimeTv.setText(time);
        holder.nameTv.setText(name);
        holder.numberTv.setText(number);
        holder.typeTv.setText(type);

    //handle item click(if clicked on item)
    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + number));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE},MY_PERMISSIONS_REQUEST_CALL_PHONE);
                }
                else
                {
                    context.startActivity(callIntent);
                }
            }
            else
            {
                context.startActivity(callIntent);
            }

            // adding call history
            calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a dd-MM-yyyy");
            String current_time = simpleDateFormat.format(calendar.getTime());

            String key = dRef.push().getKey();
            UploadCallHistoryAdapter uploadCallHistoryAdapter = new UploadCallHistoryAdapter(key, current_uid, name, type, number, current_time);
            dRef.child(current_uid).child(key).setValue(uploadCallHistoryAdapter);


        }
    });

    // handle dots option click
    holder.dotsIV.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete this call history");
            builder.setMessage("Are you sure to delete this call history?");


            //set layout linear layout
            LinearLayout linearLayout = new LinearLayout(context);

            //buttons recover
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //dismiss dialog
                    dialog.dismiss();

                }
            });


            //buttons edit

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //edit dialog
                    if(internetConnection.checkConnection())
                    {
                      dRef.child(current_uid).child(key).removeValue();

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
    });


        }

@Override
public int getItemCount() {

        return adapterList.size();
        }

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView nameTv, numberTv, dateTimeTv,typeTv;
    ImageView dotsIV;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        dateTimeTv = itemView.findViewById(R.id.timeTvId);
        nameTv = itemView.findViewById(R.id.nameTvId);
        numberTv = itemView.findViewById(R.id.numberTvId);
        typeTv = itemView.findViewById(R.id.typeTvId);
        dotsIV = itemView.findViewById(R.id.dotsOptionIvId);


    }
}
}