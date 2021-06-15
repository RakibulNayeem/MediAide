package com.rakibulnayeem.mediaide.Profile;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rakibulnayeem.mediaide.R;

public class UsersProfile extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 100 ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String uid;
    String phoneNumber;

    private TextView bloogGroupTv, villageTv, lastDonationDateTv, upazilaTv, zillaTv, phoneNumberTv, statusTv,nameTv;
    private Button smsBtn, callBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("persons_info");

        //get clicked user id(uid)
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");


        nameTv = findViewById(R.id.nameTvId);
        villageTv = findViewById(R.id.villageTvId);
        phoneNumberTv = findViewById(R.id.phoneNumberTvId);
        lastDonationDateTv = findViewById(R.id.lastDonationDateTvId);
        bloogGroupTv = findViewById(R.id.bloodGTvId);
        upazilaTv = findViewById(R.id.upazilaTvId);
        zillaTv = findViewById(R.id.zillaTvId);
        statusTv = findViewById(R.id.statusTvId);


        callBtn = findViewById(R.id.callBtnId);
        smsBtn = findViewById(R.id.smsBtnId);

        callBtn.setOnClickListener(this);
        smsBtn.setOnClickListener(this);


        // set info
        Query query = databaseReference.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check until required data get
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    //get data
                    String name = "" + dataSnapshot1.child("name").getValue();
                    String village = "" + dataSnapshot1.child("village").getValue();
                    String phone_number = "" + dataSnapshot1.child("phone_number").getValue();
                    String last_donation_date = "" + dataSnapshot1.child("last_donation_date").getValue();
                    String blood_group = "" + dataSnapshot1.child("blood_group").getValue();
                    String upazil = "" + dataSnapshot1.child("upazila").getValue();
                    String zilla = "" + dataSnapshot1.child("zilla").getValue();
                    String status = "" + dataSnapshot1.child("status").getValue();

                    //get phone number
                    phoneNumber = phone_number;

                    //set data
                    nameTv.setText(name);
                    villageTv.setText(village);
                    lastDonationDateTv.setText(last_donation_date);
                    phoneNumberTv.setText(phone_number);
                    bloogGroupTv.setText(blood_group);
                    upazilaTv.setText(upazil);
                    zillaTv.setText(zilla);
                    statusTv.setText(status);

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
                if (ContextCompat.checkSelfPermission(UsersProfile.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UsersProfile.this, new String[]{Manifest.permission.CALL_PHONE},MY_PERMISSIONS_REQUEST_CALL_PHONE);
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


        else if (v == smsBtn)
        {

            //AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Send Your Message");


            //set layout linear layout
            LinearLayout linearLayout = new LinearLayout(this);
            //views to set in dialog
            final EditText smsEt = new EditText(this);
            smsEt.setHint("Enter your message");
            smsEt.setInputType(InputType.TYPE_CLASS_TEXT);
            smsEt.setMinEms(16);

            linearLayout.addView(smsEt);
            linearLayout.setPadding(10,10,10,10);

            builder.setView(linearLayout);

            //buttons recover
            builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //input email
                        String smsTxt = smsEt.getText().toString().trim();

                        if(smsTxt.isEmpty())
                        {
                            Toast.makeText(getApplicationContext(),"Enter your message",Toast.LENGTH_SHORT).show();

                        }

                        else
                        {
                            //send sms
                            Intent intent = new Intent(Intent.ACTION_SENDTO);
                            intent.setData(Uri.parse("smsto:"+phoneNumber));
                            intent.putExtra("sms_body", smsTxt);
                            startActivity(intent);
                        }

                    }

            });


            //buttons cancel
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //dismiss dialog
                    dialog.dismiss();

                }
            });

            //show dialog
            builder.create().show();

        }
    }

}
