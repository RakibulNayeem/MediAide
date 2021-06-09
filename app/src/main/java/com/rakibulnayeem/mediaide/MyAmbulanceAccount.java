package com.rakibulnayeem.mediaide;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MyAmbulanceAccount extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String uid;
    String phoneNumber;
    private SwitchCompat availableSwitch;
    String available_switch_value, key;

    private TextView ambulanceNameTv,locationTv,zillaTv,phoneNumberTv, vehicleNoTv;
  //  private ImageView ambulanceProfileImage, cameraIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ambulance_account);
        this.setTitle("Profile");

        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("ambulance_info");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        uid = user.getUid();

       // ambulanceProfileImage = findViewById(R.id.myAmbulanceProfileImageViewId);
       // cameraIcon = findViewById(R.id.myAmbulanceCameraIconIvId);



        ambulanceNameTv = findViewById(R.id.ambulanceNameTvId);
        locationTv = findViewById(R.id.ambulanceLocationTvId);
        zillaTv = findViewById(R.id.zillaTvId);
        vehicleNoTv = findViewById(R.id.vehicleNoTvId);
        phoneNumberTv = findViewById(R.id.phoneNumberTvId);
        availableSwitch = findViewById(R.id.availableSwitchId);

      //  cameraIcon.setOnClickListener(this);

        getAvailableSwitch();
        getAllData();




    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

      /*  if(v == cameraIcon)
        {
            Intent intent = new Intent(getApplicationContext(),UpdateProfilePicture.class);
            intent.putExtra("name","ambulance_profile_picture");
            startActivity(intent);
        }

       */
    }



    private void getAllData() {
        // set info
        Query query = databaseReference.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check until required data get
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    //get data
                    String name = "" + dataSnapshot1.child("name").getValue();
                    String location = "" + dataSnapshot1.child("address").getValue();
                    String zilla = "" + dataSnapshot1.child("zilla").getValue();
                    String vehicle_no = "" + dataSnapshot1.child("vehicle_no").getValue();
                    String phone_number = "" + dataSnapshot1.child("phone_number").getValue();
                 //   String image_uri = "" + dataSnapshot1.child("imageUri").getValue();
                    String available_switch = "" + dataSnapshot1.child("available_switch_value").getValue();

                    key = "" + dataSnapshot1.child("key").getValue();


                    //get phone number
                    phoneNumber = phone_number;
                    //set data
                    ambulanceNameTv.setText(name);
                    phoneNumberTv.setText(phone_number);
                    locationTv.setText(location);
                    zillaTv.setText(zilla);
                    vehicleNoTv.setText(vehicle_no);

                    if(available_switch.equals("true"))
                    {
                        availableSwitch.setChecked(true);
                    } else
                    {
                        availableSwitch.setChecked(false);
                    }

                    //set profile picture
               /*     Picasso.get()
                            .load(image_uri)
                            .placeholder(R.drawable.user_icon)
                            .fit()
                            .centerCrop()
                            .into(ambulanceProfileImage);

                */



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "Failed to load information ", Toast.LENGTH_LONG).show();
            }
        });


    }


    private void getAvailableSwitch() {

        availableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    available_switch_value= "true";

                }
                else {
                    available_switch_value = "false";

                }

                databaseReference.child(key).child("available_switch_value").setValue(available_switch_value);

            /*    Query query = databaseReference.orderByChild("uid").equalTo(uid);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren())
                        {
                            String donate_switch = ""+ ds.child("available_switch_value").getValue();

                            if(donate_switch.equals("true"))
                            {
                                availableSwitch.setChecked(true);
                            } else
                            {
                                availableSwitch.setChecked(false);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(),"Failed to load information ",Toast.LENGTH_LONG).show();
                    }
                });  */

            }
        });

    }

}
