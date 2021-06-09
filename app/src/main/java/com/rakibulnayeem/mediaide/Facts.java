package com.rakibulnayeem.mediaide;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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

import java.util.ArrayList;
import java.util.List;

public class Facts extends AppCompatActivity {

    private RecyclerView recyclerView;
    FactsAdapter factsAdapter;
    private List<AddFactsAdapter> adapterList;
    DatabaseReference databaseReference;
    private ProgressBar progressBar;
   // private AdView mAdView, adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facts);
        this.setTitle("Facts");

        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        recyclerView = findViewById(R.id.usersRecycleViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.recyclerProgressBarId);

        adapterList = new ArrayList<>();

        getAllFacts();

    }

 /*   @Override
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

        }
     /*   else if (item.getItemId()==R.id.addMenuId)
        {
            startActivity(new Intent(getApplicationContext(),AddFacts.class));
        }


      */


        return super.onOptionsItemSelected(item);
    }


    private void getAllFacts() {

        //get all hospital

        databaseReference = FirebaseDatabase.getInstance().getReference("facts_info");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  adapterList.clear();
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {

                    AddFactsAdapter upInfo = dataSnapshot1.getValue(AddFactsAdapter.class);

                    adapterList.add(upInfo);
                }

               factsAdapter  = new FactsAdapter(Facts.this, adapterList);

                factsAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(factsAdapter);

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
