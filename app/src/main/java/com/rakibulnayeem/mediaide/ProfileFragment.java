package com.rakibulnayeem.mediaide;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment implements View.OnClickListener {


    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,databaseReference2,databaseReference3;
    FirebaseUser user;
    String uid;


    private TextView emailTv,bloodGroupTv,villageTv,upazilaTv,zillaTv,phoneNumberTv;
    private TextView nameTv,statusTv,lastDonationDateTv;
    private SwitchCompat donateSwitch;
    private Button editProfileBtn, myPostBtn;
    private ImageView profileImageView,cameraIcon;
    private Uri profileImageUri;

    String switch_value;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference =  firebaseDatabase.getReference("persons_info");
       // databaseReference2 =  firebaseDatabase.getReference("profile_image");
        databaseReference3 =  firebaseDatabase.getReference("donate_switch_info");
        uid = user.getUid();



        profileImageView = view.findViewById(R.id.profileImageViewId);
       // cameraIcon = view.findViewById(R.id.cameraIconViewId);

        nameTv = view.findViewById(R.id.nameTvId);
        phoneNumberTv = view.findViewById(R.id.phoneNumberTvId);
        emailTv = view.findViewById(R.id.emailTvId);
        bloodGroupTv = view.findViewById(R.id.bloodGTvId);
        upazilaTv = view.findViewById(R.id.upazilaTvId);
        zillaTv = view.findViewById(R.id.zillaTvId);
        villageTv  = view.findViewById(R.id.villageTvId);
        statusTv = view.findViewById(R.id.statusTvId);
        lastDonationDateTv = view.findViewById(R.id.lastDonationDateTvId);

        editProfileBtn = view.findViewById(R.id.editProfileBtnId);
        myPostBtn = view.findViewById(R.id.myPostBtnId);

        donateSwitch = view.findViewById(R.id.donateSwitchId);


       donateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    switch_value = "true";

                }
                else {
                    switch_value = "false";

                }

                databaseReference.child(uid).child("donate_switch").setValue(switch_value);

                Query query = databaseReference.orderByChild("uid").equalTo(user.getUid());
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren())
                        {
                            String donate_switch = ""+ ds.child("donate_switch").getValue();

                            if(donate_switch.equals("true"))
                            {
                                donateSwitch.setChecked(true);
                            } else
                            {
                                donateSwitch.setChecked(false);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(),"Failed to load information ",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });





        // set info
        Query query = databaseReference.orderByChild("uid").equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check until required data get
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    //get data
                    String name = ""+dataSnapshot1.child("name").getValue();
                    String email = ""+dataSnapshot1.child("email").getValue();
                    String blood_group = ""+dataSnapshot1.child("blood_group").getValue();
                    String upazila = ""+dataSnapshot1.child("upazila").getValue();
                    String zilla = ""+dataSnapshot1.child("zilla").getValue();
                    String village = ""+dataSnapshot1.child("village").getValue();
                    String status = ""+dataSnapshot1.child("status").getValue();
                    String last_donation_date = ""+dataSnapshot1.child("last_donation_date").getValue();
                    String donate_switch = ""+ dataSnapshot1.child("donate_switch").getValue();
                    String phone_number = user.getPhoneNumber();


                    //set data
                    nameTv.setText(name);
                    emailTv.setText(email);
                    phoneNumberTv.setText(phone_number);
                    bloodGroupTv.setText(blood_group);
                    upazilaTv.setText(upazila);
                    zillaTv.setText(zilla);
                    villageTv.setText(village);
                    statusTv.setText(status);
                    lastDonationDateTv.setText(last_donation_date);

                    if(donate_switch.equals("true"))
                    {
                        donateSwitch.setChecked(true);
                    } else
                    {
                        donateSwitch.setChecked(false);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getContext(),"Failed to load information ",Toast.LENGTH_LONG).show();
            }
        });



    /*  // set profile picture
        Query query2 = databaseReference2.orderByChild("uid").equalTo(user.getUid());
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check until required data get
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    //get data
                    String image_uri = ""+dataSnapshot1.child("imageUri").getValue();

                    //set data
                 try{
                     Picasso.get().load(image_uri).into(profileImageView);
                 }
                 catch (Exception e)
                 {
                     //if there is any exception then set default;
                     Picasso.get().load(R.drawable.user_icon).into(profileImageView);
                 }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getContext(),"Failed to load information ",Toast.LENGTH_LONG).show();
            }
        });

     */


        //edit profile
        editProfileBtn.setOnClickListener(this);
      //  cameraIcon.setOnClickListener(this);
        myPostBtn.setOnClickListener(this);

        return view;

    }

    @Override
    public void onClick(View v) {


        if (v == editProfileBtn)
        {
            startActivity(new Intent(getContext(), UpdateProfile.class));
        }
     /*   else if(v == cameraIcon)
        {
            Intent intent = new Intent(getContext(),UpdateProfilePicture.class);
                intent.putExtra("name","my_profile_picture");
                startActivity(intent);
        }

      */

        else if (v == myPostBtn)
        {
            startActivity(new Intent(getContext(), MyPosts.class));
        }
    }


}
