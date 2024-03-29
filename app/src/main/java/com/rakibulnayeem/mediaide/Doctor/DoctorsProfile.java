package com.rakibulnayeem.mediaide.Doctor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rakibulnayeem.mediaide.Fragments.UploadCallHistoryAdapter;
import com.rakibulnayeem.mediaide.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.view.Window;

public class DoctorsProfile extends AppCompatActivity implements View.OnClickListener {


    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 100 ;
    DatabaseReference databaseReference;
    String uid;
    String phoneNumber;

    DatabaseReference dRef;
    String current_uid, Name;
    Calendar calendar;

    TextView nameTv,degreeTv,chamberAddressTv,zillaTv,openTimeTv,ampmTv,closeTimeTv,ampm2Tv,phoneNumberTv,activeDayTv;
    TextView specialityTv, feeTv, hospitalTv, feeText;
    ImageView doctorProfileImage;
    private Button callBtn;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_profile_2_demo);
        this.setTitle("Doctor's Profile");

        toolbar = findViewById(R.id.toolbar); // get the reference of Toolbar
        setSupportActionBar(toolbar); // Setting/replace toolbar as the ActionBar


        databaseReference = FirebaseDatabase.getInstance().getReference("doctors_info");
        dRef = FirebaseDatabase.getInstance().getReference("call_history");
        current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();



        //get clicked user id(uid)
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");

        nameTv = findViewById(R.id.nameProTvId);
        degreeTv = findViewById(R.id.degreeProTvId);
        chamberAddressTv = findViewById(R.id.chamberAddressProTvId);
        hospitalTv = findViewById(R.id.hospitalProTvId);
        zillaTv = findViewById(R.id.zillaProTvId);
        openTimeTv = findViewById(R.id.openTimeProTvId);
        ampmTv = findViewById(R.id.ampmProTvId);
        closeTimeTv = findViewById(R.id.closeTimeProTvId);
        ampm2Tv = findViewById(R.id.ampm2ProTvId);
        phoneNumberTv = findViewById(R.id.phoneNumberProTvId);
        doctorProfileImage = findViewById(R.id.doctorProfileImageToUIvId);
        specialityTv = findViewById(R.id.specialityProTvId);
        feeTv = findViewById(R.id.feeProTvId);
        feeText = findViewById(R.id.feeProTextId);
        activeDayTv = findViewById(R.id.activeDayProTvId);

        //tabLayout and viewPager
        tabLayout = findViewById(R.id.doctorProfileTabId);
        viewPager = findViewById(R.id.doctorProfileVPId);



        callBtn = findViewById(R.id.callProBtnId);

        callBtn.setOnClickListener(this);



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
                    String hospital_name = "" + dataSnapshot1.child("hospital_name").getValue();
                    String zilla = "" + dataSnapshot1.child("zilla").getValue();
                    String open_time = "" + dataSnapshot1.child("open_time").getValue();
                    String ampm = "" + dataSnapshot1.child("ampm").getValue();
                    String close_time = "" + dataSnapshot1.child("close_time").getValue();
                    String ampm2 = "" + dataSnapshot1.child("ampm2").getValue();
                    String phone_number = "" + dataSnapshot1.child("phone_number").getValue();
                    String image_uri = "" + dataSnapshot1.child("imageUri").getValue();
                    String speciality = "" + dataSnapshot1.child("speciality").getValue();
                    String fee = "" + dataSnapshot1.child("fee").getValue();
                    String active_day = "" + dataSnapshot1.child("active_day").getValue();

                    //get phone number
                    phoneNumber = phone_number;

                    //set data
                    nameTv.setText(name);
                    Name = name;
                    degreeTv.setText(degree);
                    phoneNumberTv.setText(phone_number);
                    chamberAddressTv.setText(chamber_address);
                  //  hospitalTv.setText(hospital_name);
                    openTimeTv.setText(open_time);
                    zillaTv.setText(zilla);
                    ampm2Tv.setText(ampm2);
                    ampmTv.setText(ampm);
                    closeTimeTv.setText(close_time);
                   // feeTv.setText(fee);
                    activeDayTv.setText(active_day);

                    if (speciality.isEmpty())
                    {
                        specialityTv.setVisibility(View.GONE);
                    }
                    else {
                        specialityTv.setVisibility(View.VISIBLE);
                        specialityTv.setText(speciality);
                    }


                    if (hospital_name.isEmpty())
                    {
                        hospitalTv.setVisibility(View.GONE);
                    }
                    else {
                        hospitalTv.setVisibility(View.VISIBLE);
                        hospitalTv.setText(hospital_name);
                    }


                    if (fee.isEmpty())
                    {
                        feeTv.setVisibility(View.GONE);
                        feeText.setVisibility(View.GONE);
                    }
                    else {
                        feeTv.setVisibility(View.VISIBLE);
                        feeText.setVisibility(View.VISIBLE);
                        specialityTv.setText(speciality);
                    }


                    try{
                        Picasso.get().load(image_uri)
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



        //viewPager and tabs
        ViewPagerDoctorTabAdapter adapter = new ViewPagerDoctorTabAdapter(getSupportFragmentManager());

        //add fragments

        adapter.AddFragment(new ChamberFragment(),"Chamber");
        adapter.AddFragment(new ExperienceFragment(),"Experience");
        adapter.AddFragment(new AboutDoctorFragment(),"About Doctor");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }




    @Override
    public void onClick(View v) {
        if (v == callBtn) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(DoctorsProfile.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DoctorsProfile.this, new String[]{Manifest.permission.CALL_PHONE},MY_PERMISSIONS_REQUEST_CALL_PHONE);
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
            // adding call history
            String type = "Doctor";
            calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a dd-MM-yyyy");
            String current_time = simpleDateFormat.format(calendar.getTime());

            String key = dRef.push().getKey();
            UploadCallHistoryAdapter uploadCallHistoryAdapter = new UploadCallHistoryAdapter(key, current_uid, Name, type,phoneNumber, current_time);
            dRef.child(current_uid).child(key).setValue(uploadCallHistoryAdapter);

        }

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }

        return super.onOptionsItemSelected(item);

    }
}
