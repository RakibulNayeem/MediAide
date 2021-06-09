package com.rakibulnayeem.mediaide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ApproveDoctorList extends AppCompatActivity {

    private RecyclerView recyclerView;
    ApproveDoctorListAdapter approveDoctorListAdapter;
    private List<AddDoctorAdapter> adapterList;
    DatabaseReference databaseReference;
    private ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_doctor_list);
        this.setTitle("Doctor List to Approve");

        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        recyclerView = findViewById(R.id.usersRecycleViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        uid = user.getUid();

        progressBar = findViewById(R.id.recyclerProgressBarId);

        adapterList = new ArrayList<>();



        getAllDoctors();

    }

    private void getAllDoctors() {
        //get all doctors for approve

        databaseReference = FirebaseDatabase.getInstance().getReference("doctors_info_approve");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapterList.clear();
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {

                    AddDoctorAdapter upInfo = dataSnapshot1.getValue(AddDoctorAdapter.class);

                    adapterList.add(upInfo);
                }

                approveDoctorListAdapter = new ApproveDoctorListAdapter(ApproveDoctorList.this, adapterList);

                approveDoctorListAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(approveDoctorListAdapter);

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(),"Error : "+databaseError.getMessage(),Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }


}
