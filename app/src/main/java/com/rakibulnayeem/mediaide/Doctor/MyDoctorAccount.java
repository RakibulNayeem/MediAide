package com.rakibulnayeem.mediaide.Doctor;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rakibulnayeem.mediaide.Profile.UpdateProfilePicture;
import com.rakibulnayeem.mediaide.R;
import com.squareup.picasso.Picasso;

public class MyDoctorAccount extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 100 ;
    DatabaseReference databaseReference,dRefDoctorImage;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String uid;
    String phoneNumber;

    private  TextView nameTv,degreeTv,chamberAddressTv,zillaTv,openTimeTv,ampmTv,closeTimeTv,ampm2Tv,phoneNumberTv;
    private TextView specialityTv, activeDayTv, feeTv;
    private Button callBtn;
    private ImageView doctorProfileImage, cameraIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_doctor_account);
        this.setTitle("Profile");

        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("doctors_info");
        dRefDoctorImage = FirebaseDatabase.getInstance().getReference("doctors_profile_image");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        uid = user.getUid();

        doctorProfileImage = findViewById(R.id.myDoctorProfileImageViewId);
        cameraIcon = findViewById(R.id.myDoctorCameraIconViewId);



        nameTv = findViewById(R.id.nameProTvId);
        degreeTv = findViewById(R.id.degreeProTvId);
        chamberAddressTv = findViewById(R.id.chamberAddressProTvId);
        zillaTv = findViewById(R.id.zillaProTvId);
        openTimeTv = findViewById(R.id.openTimeProTvId);
        ampmTv = findViewById(R.id.ampmProTvId);
        closeTimeTv = findViewById(R.id.closeTimeProTvId);
        ampm2Tv = findViewById(R.id.ampm2ProTvId);
        phoneNumberTv = findViewById(R.id.phoneNumberProTvId);
        specialityTv = findViewById(R.id.specialityProTvId);
        activeDayTv = findViewById(R.id.activeDayProTvId);
        feeTv = findViewById(R.id.feeProTvId);




        callBtn = findViewById(R.id.callProBtnId);
        callBtn.setOnClickListener(this);
        cameraIcon.setOnClickListener(this);


        // set info
        Query query = databaseReference.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check until required data get
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    //get data
                    String name = "" + dataSnapshot1.child("name").getValue();
                    String degree = "" + dataSnapshot1.child("degree").getValue();
                    String chamber_address = "" + dataSnapshot1.child("chamber_address").getValue();
                    String zilla = "" + dataSnapshot1.child("zilla").getValue();
                    String open_time = "" + dataSnapshot1.child("open_time").getValue();
                    String ampm = "" + dataSnapshot1.child("ampm").getValue();
                    String close_time = "" + dataSnapshot1.child("close_time").getValue();
                    String ampm2 = "" + dataSnapshot1.child("ampm2").getValue();
                    String phone_number = "" + dataSnapshot1.child("phone_number").getValue();
                    String image_uri = "" + dataSnapshot1.child("imageUri").getValue();
                    String speciality = "" + dataSnapshot1.child("speciality").getValue();
                    String active_day = "" + dataSnapshot1.child("active_day").getValue();
                    String fee = "" + dataSnapshot1.child("fee").getValue();


                    //get phone number
                    phoneNumber = phone_number;

                    //set data
                    nameTv.setText(name);
                    degreeTv.setText(degree);
                    phoneNumberTv.setText(phone_number);
                    chamberAddressTv.setText(chamber_address);
                    openTimeTv.setText(open_time);
                    zillaTv.setText(zilla);
                    ampm2Tv.setText(ampm2);
                    ampmTv.setText(ampm);
                    closeTimeTv.setText(close_time);
                    activeDayTv.setText(active_day);
                    feeTv.setText(fee);

                    if (speciality.isEmpty())
                    {
                        specialityTv.setVisibility(View.GONE);
                    }
                    else {
                        specialityTv.setText(speciality);
                    }


                    //set profile picture
                    Picasso.get()
                            .load(image_uri)
                            .placeholder(R.drawable.doctor_2)
                             .fit()
                            .centerCrop()
                            .into(doctorProfileImage);


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
            if (ActivityCompat.checkSelfPermission(MyDoctorAccount.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MyDoctorAccount.this,
                        android.Manifest.permission.CALL_PHONE)) {
                } else {
                    ActivityCompat.requestPermissions(MyDoctorAccount.this,
                            new String[]{android.Manifest.permission.CALL_PHONE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);
                }
            }

            startActivity(callIntent);
        }

        else if(v == cameraIcon)
        {
            Intent intent = new Intent(getApplicationContext(), UpdateProfilePicture.class);
            intent.putExtra("name","doctor_profile_picture");
            startActivity(intent);
        }
    }
}
