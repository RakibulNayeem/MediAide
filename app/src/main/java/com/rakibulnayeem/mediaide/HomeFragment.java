package com.rakibulnayeem.mediaide;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment implements View.OnClickListener {


    private CardView searchCardView,doctorsCardView, postCardView,addDonorCardView,aboutUsCardView,hospitalCardView;
    private CardView bloodBankCardView,ambulanceCardView,factsCardView,healthCareCardView,organizationCardView,userFeedbackCardView;
    private TextView shareAppTv,rateAppTv;


    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    String appLink;


    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

            mAuth = FirebaseAuth.getInstance();



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
                startActivity(new Intent(getContext(),SearchActivity.class));
                break;


            case R.id.addDonorCardViewId :
                startActivity(new Intent(getContext(),AddDonor.class));
                break;

            case R.id.bloodBankCardViewId :
                startActivity(new Intent(getContext(),BloodBank.class));
                break;

            case R.id.hospitalCardViewId :
                startActivity(new Intent(getContext(),Hospital.class));
                break;

            case R.id.doctorsCardViewId :
                startActivity(new Intent(getContext(),Doctors.class));
                break;

            case R.id.ambulanceCardViewId :
                startActivity(new Intent(getContext(),Ambulance.class));
                break;

            case R.id.organizationsCardViewId :
                startActivity(new Intent(getContext(),Organizations.class));
                break;

            case R.id.aboutAppCardViewId :
                startActivity(new Intent(getContext(),AboutUs.class));
                break;


            case R.id.userFeedbackCardViewId :
                startActivity(new Intent(getContext(),UserFeedback.class));
                break;

            case R.id.postCardViewId :
                startActivity(new Intent(getContext(),Post.class));
                break;

            case R.id.factsCardViewId :
                startActivity(new Intent(getContext(),Facts.class));
                break;

            case R.id.healthCareCardViewId :
                startActivity(new Intent(getContext(),HealthCare.class));
                break;

            case R.id.shareAppTvId :
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String subject = "BloodPlus";
                String body = "We are helping you to collect blood and save life.\n"+appLink;

                intent.putExtra(Intent.EXTRA_SUBJECT,subject);
                intent.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(Intent.createChooser(intent,"share with"));

                break;

            case R.id.rateAppTvId :
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + "com.rakibulnayeem.mediaide")));


        }


    }




}
