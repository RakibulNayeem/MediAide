package com.rakibulnayeem.mediaide.Donor;

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


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakibulnayeem.mediaide.MainActivity;
import com.rakibulnayeem.mediaide.R;
import com.rakibulnayeem.mediaide.SignUpLogIn.SignUpAdapter;
import com.rakibulnayeem.mediaide.ZillaList;

import java.util.ArrayList;
import java.util.List;

public class BloodDonorActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private RecyclerView recyclerView;
    BloodDonorAdapter bloodDonorAdapter;
    private List<SignUpAdapter> adapterList;
    DatabaseReference databaseReference,databaseReference2;
    private ProgressBar progressBar;




    private TextView zillaTv;
    private ImageView dropDownIv;
    String Zilla;
    String uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_donor);
        this.setTitle("Blood Donor");

        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        uid = user.getUid();

        recyclerView = findViewById(R.id.usersRecycleViewId);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.recyclerProgressBarId);

        adapterList = new ArrayList<>();

        zillaTv = findViewById(R.id.zillaTvId);
        dropDownIv = findViewById(R.id.dropDownIvId);




        //get zilla name
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            String zillaName = bundle.getString("zillaName");
            Zilla = zillaName;
        }

        zillaTv.setOnClickListener(this);
        dropDownIv.setOnClickListener(this);

        getAllUsers();


    }



    @Override
    public void onClick(View v) {
        if (v == dropDownIv || v == zillaTv )
        {
            Intent intent = new Intent(getApplicationContext(), ZillaList.class);
            intent.putExtra("name","donor_search");
            startActivity(intent);
        }
    }




    private void getAllUsers() {

        //get all users
        if (Zilla==null)
        {
            //get all donors
            Toast.makeText(getApplicationContext(), "Location : "+Zilla, Toast.LENGTH_SHORT).show();

            databaseReference = FirebaseDatabase.getInstance().getReference("persons_info");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    adapterList.clear();
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {
                        SignUpAdapter upInfo = dataSnapshot1.getValue(SignUpAdapter.class);

                        if (!upInfo.getUid().equals(uid) && upInfo.getDonate_switch().toLowerCase().equals("true"))
                        {
                            adapterList.add(upInfo);
                        }

                    }

                    bloodDonorAdapter = new BloodDonorAdapter(BloodDonorActivity.this, adapterList);

                    bloodDonorAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(bloodDonorAdapter);

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
            databaseReference = FirebaseDatabase.getInstance().getReference("persons_info");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    adapterList.clear();
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {
                        SignUpAdapter upInfo = dataSnapshot1.getValue(SignUpAdapter.class);

                        if (!upInfo.getUid().equals(uid) && upInfo.getDonate_switch().equals("true"))
                        {
                            if (upInfo.getZilla().toLowerCase().contains(Zilla.toLowerCase())){

                                   adapterList.add(upInfo);
                            }

                        }


                    }

                    bloodDonorAdapter = new BloodDonorAdapter(BloodDonorActivity.this, adapterList);

                    bloodDonorAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(bloodDonorAdapter);

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

    //option menu
    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater =  getMenuInflater();
        menuInflater.inflate(R.menu.search_activity_menu_layout,menu);

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
                        searchUsers(query);

                    }
                    else {
                        //if search text empty , get all users with zilla
                        getAllUsers();
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
                        searchUsers(newText);

                    }
                    else {
                        //if search text empty , get all users
                        getAllUsers();
                    }

                    return false;
                }
            });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {  finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }


    //search user
    private void searchUsers(final String query) {

        //get all searched users

        if (Zilla!=null)
        {
            zillaTv.setText(Zilla);
            //get all searched users
            databaseReference = FirebaseDatabase.getInstance().getReference("persons_info");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    adapterList.clear();

                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {
                        SignUpAdapter upInfo = dataSnapshot1.getValue(SignUpAdapter.class);

                        if (!upInfo.getUid().equals(uid) && upInfo.getDonate_switch().toLowerCase().equals("true"))
                        {
                            if (upInfo.getZilla().toLowerCase().contains(Zilla.toLowerCase())){


                                if(upInfo.getUpazila().toLowerCase().contains(query.toLowerCase()) ||
                                        upInfo.getVillage().toLowerCase().contains(query.toLowerCase()) ||
                                        upInfo.getBlood_group().toLowerCase().contains(query.toLowerCase()) ||
                                        upInfo.getStatus().toLowerCase().contains(query.toLowerCase()))
                                {
                                    adapterList.add(upInfo);
                                }
                            }
                        }


                    }

                    bloodDonorAdapter = new BloodDonorAdapter(BloodDonorActivity.this, adapterList);

                    //refresh adapter
                    bloodDonorAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(bloodDonorAdapter);

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
            databaseReference = FirebaseDatabase.getInstance().getReference("persons_info");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    adapterList.clear();
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {

                        SignUpAdapter upInfo = dataSnapshot1.getValue(SignUpAdapter.class);

                        if (!upInfo.getUid().equals(uid) && upInfo.getDonate_switch().toLowerCase().equals("true"))
                        {
                             if( upInfo.getUpazila().toLowerCase().contains(query.toLowerCase()) ||
                                    upInfo.getVillage().toLowerCase().contains(query.toLowerCase()) ||
                                    upInfo.getBlood_group().toLowerCase().contains(query.toLowerCase()) ||
                                     upInfo.getStatus().toLowerCase().contains(query.toLowerCase()))
                            {
                                adapterList.add(upInfo);
                            }
                        }

                    }

                    bloodDonorAdapter = new BloodDonorAdapter(BloodDonorActivity.this, adapterList);

                    //refresh adapter
                    bloodDonorAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(bloodDonorAdapter);

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
