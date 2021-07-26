package com.rakibulnayeem.mediaide.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakibulnayeem.mediaide.AboutUs;
import com.rakibulnayeem.mediaide.Ambulances.Ambulance;
import com.rakibulnayeem.mediaide.BloodBanks.BloodBank;
import com.rakibulnayeem.mediaide.Doctor.Doctors;
import com.rakibulnayeem.mediaide.Donor.AddDonor;
import com.rakibulnayeem.mediaide.Donor.SearchActivity;
import com.rakibulnayeem.mediaide.Fact.Facts;
import com.rakibulnayeem.mediaide.HealthCares.HealthCare;
import com.rakibulnayeem.mediaide.Hospital.Hospital;
import com.rakibulnayeem.mediaide.Hospital.HospitalList;
import com.rakibulnayeem.mediaide.Organization.Organizations;
import com.rakibulnayeem.mediaide.Posts.Post;
import com.rakibulnayeem.mediaide.R;
import com.rakibulnayeem.mediaide.UserFeedbacks.UserFeedback;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment implements View.OnClickListener {


    private CardView searchCardView,doctorsCardView, postCardView,addDonorCardView,aboutUsCardView,hospitalCardView;
    private CardView bloodBankCardView,ambulanceCardView,factsCardView,healthCareCardView,organizationCardView,userFeedbackCardView;
    private TextView shareAppTv,rateAppTv;


    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    String appLink, Zilla;
    FusedLocationProviderClient fusedLocationProviderClient;

    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

            mAuth = FirebaseAuth.getInstance();

         //Initialize fusedLocationProviderClient
         fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

            //init views
            searchCardView = view.findViewById(R.id.searchCardViewId);
            addDonorCardView = view.findViewById(R.id.addDonorCardViewId);
            bloodBankCardView = view.findViewById(R.id.bloodBankCardViewId);
            hospitalCardView = view.findViewById(R.id.hospitalCardViewId);
            doctorsCardView = view.findViewById(R.id.doctorsCardViewId);
            ambulanceCardView = view.findViewById(R.id.ambulanceCardViewId);
            organizationCardView = view.findViewById(R.id.organizationsCardViewId);
            aboutUsCardView = view.findViewById(R.id.aboutAppCardViewId);
            userFeedbackCardView = view.findViewById(R.id.userFeedbackCardViewId);
            postCardView = view.findViewById(R.id.postCardViewId);
            factsCardView = view.findViewById(R.id.factsCardViewId);
            healthCareCardView = view.findViewById(R.id.healthCareCardViewId);

            shareAppTv = view.findViewById(R.id.shareAppTvId);
            rateAppTv = view.findViewById(R.id.rateAppTvId);





            searchCardView.setOnClickListener(this);
            addDonorCardView.setOnClickListener(this);
            bloodBankCardView.setOnClickListener(this);
            hospitalCardView.setOnClickListener(this);
            doctorsCardView.setOnClickListener(this);
            ambulanceCardView.setOnClickListener(this);
            organizationCardView.setOnClickListener(this);
            aboutUsCardView.setOnClickListener(this);
            userFeedbackCardView.setOnClickListener(this);
            postCardView.setOnClickListener(this);
            factsCardView.setOnClickListener(this);
            healthCareCardView.setOnClickListener(this);

            shareAppTv.setOnClickListener(this);
            rateAppTv.setOnClickListener(this);

         //   getLocation();




            ///app link for share the app
            databaseReference = FirebaseDatabase.getInstance().getReference("1_app_link");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String app_link= ""+dataSnapshot.child("app_link_child").getValue();

                    //set value
                    appLink = app_link;

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(),"Failed to load info",Toast.LENGTH_SHORT).show();
                }
            });



            return  view;
    }



    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.searchCardViewId :
                Intent intent = new Intent(getContext(), SearchActivity.class);
                intent.putExtra("zillaName", Zilla);
                startActivity(intent);
                break;


            case R.id.addDonorCardViewId :
                startActivity(new Intent(getContext(), AddDonor.class));
                break;

            case R.id.bloodBankCardViewId :
                Intent intent2 = new Intent(getContext(), BloodBank.class);
                intent2.putExtra("zillaName", Zilla);
                startActivity(intent2);
                break;

            case R.id.hospitalCardViewId :
                Intent intent3 = new Intent(getContext(), Hospital.class);
                intent3.putExtra("zillaName", Zilla);
                startActivity(intent3);
                break;

            case R.id.doctorsCardViewId :
                Intent intent4 = new Intent(getContext(), Doctors.class);
                intent4.putExtra("zillaName", Zilla);
                startActivity(intent4);
                break;

            case R.id.ambulanceCardViewId :
                Intent intent5 = new Intent(getContext(), Ambulance.class);
                intent5.putExtra("zillaName", Zilla);
                startActivity(intent5);
                break;

            case R.id.organizationsCardViewId :
                Intent intent6 = new Intent(getContext(), Organizations.class);
                intent6.putExtra("zillaName", Zilla);
                startActivity(intent6);
                break;

            case R.id.aboutAppCardViewId :
                startActivity(new Intent(getContext(), AboutUs.class));
                break;


            case R.id.userFeedbackCardViewId :
                startActivity(new Intent(getContext(), UserFeedback.class));
                break;

            case R.id.postCardViewId :
                startActivity(new Intent(getContext(), Post.class));
                break;

            case R.id.factsCardViewId :
                startActivity(new Intent(getContext(), Facts.class));
                break;

            case R.id.healthCareCardViewId :
                startActivity(new Intent(getContext(), HealthCare.class));
                break;

            case R.id.shareAppTvId :
                Intent intent7 = new Intent(Intent.ACTION_SEND);
                intent7.setType("text/plain");
                String subject = "BloodPlus";
                String body = "We are helping you to collect blood and save life.\n"+appLink;

                intent7.putExtra(Intent.EXTRA_SUBJECT,subject);
                intent7.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(Intent.createChooser(intent7,"share with"));

                break;

            case R.id.rateAppTvId :
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + "com.rakibulnayeem.mediaide")));


        }


    }

    private void getLocation() {
        //check permission
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            //when permission granted
            fusedLocationProviderClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            //Initialize location
                            Location location = task.getResult();
                            if (location!= null)
                            {
                                try {
                                    //Initialize geoCoder
                                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                                    //Initialize address list
                                    List<Address> addresses = geocoder.getFromLocation(
                                            location.getLatitude(), location.getLongitude(), 1
                                    );

                                    //set location on zillaTextView
                                    Zilla =  addresses.get(0).getLocality();
                                  //  Toast.makeText(getContext(),"Zilla : "+Zilla,Toast.LENGTH_SHORT).show();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });


        }
        else
        {
            //when permission denied
            ActivityCompat.requestPermissions((Activity) getContext(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

    }


}
