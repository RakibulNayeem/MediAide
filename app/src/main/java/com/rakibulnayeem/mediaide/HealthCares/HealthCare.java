package com.rakibulnayeem.mediaide.HealthCares;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakibulnayeem.mediaide.MainActivity;
import com.rakibulnayeem.mediaide.R;

import java.util.ArrayList;
import java.util.List;

public class HealthCare extends AppCompatActivity {

    private RecyclerView recyclerView;
    HealthCareAdapter healthCareAdapter;
    private List<AddHealthCareAdapter> adapterList;
    DatabaseReference databaseReference;
    private ProgressBar progressBar;
//    private AdView mAdView, adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_care);
       this.setTitle("Health Care");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        recyclerView = findViewById(R.id.usersRecycleViewId);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        progressBar = findViewById(R.id.recyclerProgressBarId);

        adapterList = new ArrayList<>();

        getAllHealthTips();

    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_menu_layout,menu);

        return super.onCreateOptionsMenu(menu);
    }


 */


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            this.finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    /*   else if (item.getItemId()==R.id.addMenuId)
        {
            startActivity(new Intent(getApplicationContext(),AddHealthCare.class));
        }


     */


        return super.onOptionsItemSelected(item);
    }


    private void getAllHealthTips() {

        //get all hospital

        databaseReference = FirebaseDatabase.getInstance().getReference("health_care_tips");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  adapterList.clear();
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {

                    AddHealthCareAdapter upInfo = dataSnapshot1.getValue(AddHealthCareAdapter.class);

                    adapterList.add(upInfo);
                }

                healthCareAdapter  = new HealthCareAdapter(HealthCare.this, adapterList);

                healthCareAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(healthCareAdapter);

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(),"Failed to load information"+databaseError.getMessage(),Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });


    }


}
