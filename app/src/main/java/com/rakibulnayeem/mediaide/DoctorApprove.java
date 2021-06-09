package com.rakibulnayeem.mediaide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DoctorApprove extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 100 ;
    DatabaseReference databaseReference, databaseReference2;
    String uid;
    String phoneNumber;

    TextView nameTv,degreeTv,chamberAddressTv,zillaTv,openTimeTv,ampmTv,closeTimeTv,ampm2Tv,phoneNumberTv,specialityTv,feeTv;
    TextView activeDayTv, hospitalTv;
    ImageView doctorProfileImage;
    private Button callBtn;
    private Button approveDoctorBtn;
    InternetConnection internetConnection;
    private ProgressBar progressBar;

    String name, degree, chamber_address,hospital_name,hospital_id, zilla,speciality,fee, active_day, open_time, close_time, ampm, ampm2, phone_number, imageUri = "noImage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_approve);
        this.setTitle("Approve Doctor");

        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("doctors_info_approve");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("doctors_info");
        internetConnection = new InternetConnection(this);


        //get  user id(uid)
        uid = getIntent().getStringExtra("uid");


        nameTv = findViewById(R.id.nameProTvId);
        degreeTv = findViewById(R.id.degreeProTvId);
        chamberAddressTv = findViewById(R.id.chamberAddressProTvId);
        hospitalTv = findViewById(R.id.hospitalProTvId);
        zillaTv = findViewById(R.id.zillaProTvId);
        specialityTv = findViewById(R.id.specialityProTvId);
        feeTv = findViewById(R.id.feeProTvId);
        openTimeTv = findViewById(R.id.openTimeProTvId);
        ampmTv = findViewById(R.id.ampmProTvId);
        closeTimeTv = findViewById(R.id.closeTimeProTvId);
        ampm2Tv = findViewById(R.id.ampm2ProTvId);
        phoneNumberTv = findViewById(R.id.phoneNumberProTvId);
        activeDayTv = findViewById(R.id.activeDayProTvId);

        doctorProfileImage = findViewById(R.id.doctorProfileImageToUIvId);
        approveDoctorBtn = findViewById(R.id.approveDoctorBtnId);
        progressBar = findViewById(R.id.progressbarId);





        callBtn = findViewById(R.id.callProBtnId);

        callBtn.setOnClickListener(this);
        approveDoctorBtn.setOnClickListener(this);



        // set info
        Query query = databaseReference.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check until required data get
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    //get data
                    String Name = "" + dataSnapshot1.child("name").getValue();
                    String Degree = "" + dataSnapshot1.child("degree").getValue();
                    String Chamber_address = "" + dataSnapshot1.child("chamber_address").getValue();
                    String Hospital_name = "" + dataSnapshot1.child("hospital_name").getValue();
                    String Zilla = "" + dataSnapshot1.child("zilla").getValue();
                    String Open_time = "" + dataSnapshot1.child("open_time").getValue();
                    String Ampm = "" + dataSnapshot1.child("ampm").getValue();
                    String Close_time = "" + dataSnapshot1.child("close_time").getValue();
                    String Ampm2 = "" + dataSnapshot1.child("ampm2").getValue();
                    String Phone_number = "" + dataSnapshot1.child("phone_number").getValue();
                    String Image_uri = "" + dataSnapshot1.child("imageUri").getValue();
                    String Speciality = ""+dataSnapshot1.child("speciality").getValue();
                    String Fee = ""+dataSnapshot1.child("fee").getValue();
                    String Active_day = ""+dataSnapshot1.child("active_day").getValue();
                     hospital_id = ""+dataSnapshot1.child("hospital_id").getValue();

                    //get phone number
                    phoneNumber = Phone_number;

                    //set data
                    nameTv.setText(Name);
                    degreeTv.setText(Degree);
                    phoneNumberTv.setText(Phone_number);
                    chamberAddressTv.setText(Chamber_address);
                    hospitalTv.setText(Hospital_name);
                    openTimeTv.setText(Open_time);
                    zillaTv.setText(Zilla);
                    ampm2Tv.setText(Ampm2);
                    ampmTv.setText(Ampm);
                    closeTimeTv.setText(Close_time);
                    specialityTv.setText(Speciality);
                    feeTv.setText(Fee);
                    activeDayTv.setText(Active_day);

                    // set data to string
                    name = Name;
                    degree = Degree;
                    speciality = Speciality;
                    fee = Fee;
                    phone_number = Phone_number;
                    chamber_address = Chamber_address;
                    hospital_name = Hospital_name;
                    open_time = Open_time;
                    ampm = Ampm;
                    close_time = Close_time;
                    ampm2 = Ampm2;
                    zilla = Zilla;
                    active_day = Active_day;


                    try{
                        Picasso.get()
                                .load(Image_uri)
                                .placeholder(R.drawable.doctor_2)
                                .fit()
                                .centerCrop()
                                .into(doctorProfileImage);
                    }
                    catch (Exception e)
                    {
                        //if there is any exception then set default;
                        Picasso.get().load(R.drawable.doctor_2).into(doctorProfileImage);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "Failed to load information ", Toast.LENGTH_LONG).show();
            }
        });



    }




    @Override
    public void onClick(View v) {
        if (v == callBtn) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(DoctorApprove.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DoctorApprove.this, new String[]{Manifest.permission.CALL_PHONE},MY_PERMISSIONS_REQUEST_CALL_PHONE);
                }
                else
                {
                    startActivity(callIntent);
                }
            }
            else
            {
                startActivity(callIntent);
            }
        }


        else if (v == approveDoctorBtn)
        {
            // ApproveDoctorAccount();
            Approved();
        }

    }

    private void ApproveDoctorAccount() {

        //AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure to approve doctor's account?");


        //buttons
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Approved();

            }
        });

        //buttons edit

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        //show dialog
        builder.create().show();

    }

    private void Approved() {


        progressBar.setVisibility(View.VISIBLE);
        String key = databaseReference2.push().getKey();

        DoctorApproveAdapter adapter = new DoctorApproveAdapter(uid,key,name,hospital_name,hospital_id,degree,speciality,fee,chamber_address,zilla,phone_number,active_day,open_time,ampm,close_time,ampm2,imageUri);
        databaseReference2.child(key).setValue(adapter);

        progressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(),"Information uploaded",Toast.LENGTH_LONG).show();

        DeleteInfo();
        finish();
        startActivity(new Intent(getApplicationContext(),ApproveDoctorList.class));

    }

    private void DeleteInfo() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(uid)){
                    databaseReference.child(uid).removeValue();
                    Toast.makeText(getApplicationContext(),"Information  deleted successfully from old position. ",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
