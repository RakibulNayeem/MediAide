package com.rakibulnayeem.mediaide.Hospital;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakibulnayeem.mediaide.MainActivity;
import com.rakibulnayeem.mediaide.R;
import com.rakibulnayeem.mediaide.ZillaList;

import java.util.ArrayList;
import java.util.List;

public class Hospital extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    HospitalAdapter hospitalAdapter;
    private List<AddHospitalAdapter> adapterList;
    DatabaseReference databaseReference;
    private ProgressBar progressBar;


    private TextView zillaTv;
    private ImageView dropDownIv;
    String Zilla=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital);
        this.setTitle("Hospitals");

        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.usersRecycleViewId);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.recyclerProgressBarId);

        dropDownIv = findViewById(R.id.dropDownIvId);
        zillaTv = findViewById(R.id.zillaTvId);

        adapterList = new ArrayList<>();
        //get zilla name
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            String zillaName = bundle.getString("zillaName");
            Zilla = zillaName;
        }

        zillaTv.setOnClickListener(this);
        dropDownIv.setOnClickListener(this);

        getAllHospital();

    }


    @Override
    public void onClick(View v) {

        if (v == dropDownIv || v == zillaTv )
        {
            Intent intent = new Intent(getApplicationContext(), ZillaList.class);
            intent.putExtra("name","hospital");
            startActivity(intent);
        }
    }

    //options menu and searchAction
    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater =  getMenuInflater();
        menuInflater.inflate(R.menu.add_hospital_menu_layout,menu);

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
                    getAllHospital();
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
                    getAllHospital();
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
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

      else if(item.getItemId()==R.id.addHospitalMenuId)
        {
            startActivity(new Intent(getApplicationContext(), AddHospital.class));
        }





        return super.onOptionsItemSelected(item);
    }


    private void getAllHospital() {

        if (Zilla==null)
        {
            //get all hospital

            databaseReference = FirebaseDatabase.getInstance().getReference("hospital_info");


            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {

                        AddHospitalAdapter upInfo = dataSnapshot1.getValue(AddHospitalAdapter.class);

                        adapterList.add(upInfo);
                    }

                    hospitalAdapter = new HospitalAdapter(Hospital.this, adapterList);

                    hospitalAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(hospitalAdapter);

                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(getApplicationContext(),"Error : "+databaseError.getMessage(),Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
        else
        {
            zillaTv.setText(Zilla);
            databaseReference = FirebaseDatabase.getInstance().getReference("hospital_info");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                     adapterList.clear();
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {

                        AddHospitalAdapter upInfo = dataSnapshot1.getValue(AddHospitalAdapter.class);
                          if (upInfo.getZilla().toLowerCase().contains(Zilla.toLowerCase())){
                              adapterList.add(upInfo);
                          }

                    }

                    hospitalAdapter = new HospitalAdapter(Hospital.this, adapterList);

                    hospitalAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(hospitalAdapter);

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




    //search result
    private void searchRequest(final String query) {

        if (Zilla!=null)
        {
            zillaTv.setText(Zilla);
            //get all searched users
            databaseReference = FirebaseDatabase.getInstance().getReference("hospital_info");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    adapterList.clear();
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {

                        AddHospitalAdapter upInfo = dataSnapshot1.getValue(AddHospitalAdapter.class);


                        if (upInfo.getZilla().toLowerCase().contains(Zilla.toLowerCase())){

                            if(upInfo.getName().toLowerCase().contains(query.toLowerCase()) ||
                                    upInfo.getAddress().toLowerCase().contains(query.toLowerCase()))
                            {
                                adapterList.add(upInfo);
                            }
                        }
                    }

                    hospitalAdapter = new HospitalAdapter(Hospital.this, adapterList);

                    //refresh adapter
                    hospitalAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(hospitalAdapter);

                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(getApplicationContext(),"Error : "+databaseError.getMessage(),Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
        else
        {
            databaseReference = FirebaseDatabase.getInstance().getReference("hospital_info");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    adapterList.clear();
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {

                        AddHospitalAdapter upInfo = dataSnapshot1.getValue(AddHospitalAdapter.class);


                            if(upInfo.getName().toLowerCase().contains(query.toLowerCase()) ||
                                    upInfo.getAddress().toLowerCase().contains(query.toLowerCase()))
                            {
                                adapterList.add(upInfo);
                            }
                    }

                    hospitalAdapter = new HospitalAdapter(Hospital.this, adapterList);

                    //refresh adapter
                    hospitalAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(hospitalAdapter);

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



}
