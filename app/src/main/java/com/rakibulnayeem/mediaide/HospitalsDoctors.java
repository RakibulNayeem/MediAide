package com.rakibulnayeem.mediaide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HospitalsDoctors extends AppCompatActivity {


    private RecyclerView recyclerView;
    HospitalsDoctorsAdapter hospitalsDoctorsAdapter;
    private List<AddDoctorForHospitalAdapter> adapterList;
    DatabaseReference databaseReference;
    private ProgressBar progressBar;

    String hospital_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitals_doctors);
        setTitle("Hospitals Doctors");


        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("doctors_info");


        recyclerView = findViewById(R.id.usersRecycleViewId);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.recyclerProgressBarId);

        hospital_id = getIntent().getStringExtra("hospital_id");

        adapterList = new ArrayList<>();

        getAllHospitalsDoctors();


    }




    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater =  getMenuInflater();
        menuInflater.inflate(R.menu.add_doctor_for_hospital_menu_layout,menu);

        MenuItem item = menu.findItem(R.id.searchActionMenuId);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint("Search Here");

        //search listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //called when user press search button from keyboard
                //if search query is not empty then  search
                if (!TextUtils.isEmpty(query.trim()))
                {
                    //search text contains text, search it
                    searchRequest(query);

                }
                else {
                    //if search text empty , get all users
                    getAllHospitalsDoctors();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                //called whenever user press any single letter
                //if search query is not empty then search
                if (!TextUtils.isEmpty(newText.trim()))
                {
                    //search text contains text, search it
                    searchRequest(newText);

                }
                else {
                    //if search text empty , get all users
                    getAllHospitalsDoctors();
                }

                return false;
            }
        });



        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
/*
    else if(item.getItemId()==R.id.addHospitalDoctorMenuId)
        {
            Intent intent = new Intent(getApplicationContext(), AddDoctorForHospital.class);
            intent.putExtra("hospital_id", hospital_id);
            startActivity(intent);
        }

 */

        return super.onOptionsItemSelected(item);
    }



    private void getAllHospitalsDoctors() {
            //get all hospital

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {

                        AddDoctorForHospitalAdapter upInfo = dataSnapshot1.getValue(AddDoctorForHospitalAdapter.class);

                        if (upInfo.getHospital_id().equals(hospital_id))
                        {
                            adapterList.add(upInfo);
                        }


                    }

                    hospitalsDoctorsAdapter = new HospitalsDoctorsAdapter(HospitalsDoctors.this, adapterList);

                    hospitalsDoctorsAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(hospitalsDoctorsAdapter);

                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(getApplicationContext(),"Error : "+databaseError.getMessage(),Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }






    //search result
    private void searchRequest(final String query) {



            databaseReference = FirebaseDatabase.getInstance().getReference("doctors_info");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    adapterList.clear();
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {

                        AddDoctorForHospitalAdapter upInfo = dataSnapshot1.getValue(AddDoctorForHospitalAdapter.class);



                        if( upInfo.getHospital_id().equals(hospital_id) &&
                                ( upInfo.getName().toLowerCase().contains(query.toLowerCase())
                                || upInfo.getChamber_address().toLowerCase().contains(query.toLowerCase())
                                || upInfo.getDegree().toLowerCase().contains(query.toLowerCase())
                                || upInfo.getActive_day().toLowerCase().contains(query.toLowerCase())
                                || upInfo.getSpeciality().toLowerCase().contains(query.toLowerCase())
                                || upInfo.getFee().toLowerCase().contains(query.toLowerCase())
                                ))
                        {
                            adapterList.add(upInfo);
                        }
                    }

                    hospitalsDoctorsAdapter = new HospitalsDoctorsAdapter(HospitalsDoctors.this, adapterList);

                    //refresh adapter
                    hospitalsDoctorsAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(hospitalsDoctorsAdapter);

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