package com.rakibulnayeem.mediaide.BloodBanks;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.rakibulnayeem.mediaide.ZillaList;

import java.util.ArrayList;
import java.util.List;

public class BloodBank extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    BloodBankAdapter bloodBankAdapter;
    private List<AddBBankAdapter> adapterList;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private ProgressBar progressBar;
    private Button callBtn;


    private TextView zillaTv;
    private ImageView dropDownIv;
    String Zilla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);
        this.setTitle("Blood Banks");

        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = findViewById(R.id.usersRecycleViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.recyclerProgressBarId);
        dropDownIv = findViewById(R.id.dropDownIvId);
        zillaTv = findViewById(R.id.zillaTvId);

        adapterList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            String zillaName = bundle.getString("zillaName");
            Zilla = zillaName;
        }

        zillaTv.setOnClickListener(this);
        dropDownIv.setOnClickListener(this);

        getAllBBank();


    }

    @Override
    public void onClick(View v) {

        if (v == dropDownIv || v == zillaTv )
        {
            Intent intent = new Intent(getApplicationContext(), ZillaList.class);
            intent.putExtra("name","blood_bank");
            startActivity(intent);
        }
    }

    //options menu and searchAction
    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater =  getMenuInflater();
        menuInflater.inflate(R.menu.add_b_bank_menu_layout,menu);

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
                    getAllBBank();
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
                    getAllBBank();
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
    /*    else if (item.getItemId()==R.id.addBBankMenuId)
        {
            startActivity(new Intent(getApplicationContext(), AddBloodBank.class));
        }


     */

        return super.onOptionsItemSelected(item);
    }



    private void getAllBBank() {

        //get all users

        if (Zilla==null)
        {
            //get all hospital

            databaseReference = FirebaseDatabase.getInstance().getReference("blood_bank");


            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                         adapterList.clear();
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {

                        AddBBankAdapter upInfo = dataSnapshot1.getValue(AddBBankAdapter.class);

                        adapterList.add(upInfo);
                    }

                    bloodBankAdapter = new BloodBankAdapter(BloodBank.this, adapterList);

                    bloodBankAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(bloodBankAdapter);

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
            databaseReference = FirebaseDatabase.getInstance().getReference("blood_bank");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    adapterList.clear();
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {

                        AddBBankAdapter upInfo = dataSnapshot1.getValue(AddBBankAdapter.class);

                        if (upInfo.getZilla_bb().toLowerCase().contains(Zilla.toLowerCase())){
                            adapterList.add(upInfo);
                        }

                    }

                    bloodBankAdapter = new BloodBankAdapter(BloodBank.this, adapterList);

                    bloodBankAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(bloodBankAdapter);

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






    //search user
    private void searchRequest(final String query) {

        //get all searched users

        if (Zilla!=null)
        {
            zillaTv.setText(Zilla);
            //get all searched users
            databaseReference = FirebaseDatabase.getInstance().getReference("blood_bank");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    adapterList.clear();
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {

                        AddBBankAdapter upInfo = dataSnapshot1.getValue(AddBBankAdapter.class);


                        if (upInfo.getZilla_bb().toLowerCase().contains(Zilla.toLowerCase())){

                            if(upInfo.getName_bb().toLowerCase().contains(query.toLowerCase())
                                    || upInfo.getAddress_bb().toLowerCase().contains(query.toLowerCase())
                                    || upInfo.getOpen_bb().toLowerCase().contains(query.toLowerCase())
                                    || upInfo.getPhone_number_bb().toLowerCase().contains(query.toLowerCase()))
                            {
                                adapterList.add(upInfo);
                            }
                        }
                    }

                    bloodBankAdapter = new BloodBankAdapter(BloodBank.this, adapterList);

                    //refresh adapter
                    bloodBankAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(bloodBankAdapter);

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
            databaseReference = FirebaseDatabase.getInstance().getReference("blood_bank");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    adapterList.clear();
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {

                        AddBBankAdapter upInfo = dataSnapshot1.getValue(AddBBankAdapter.class);


                        if(upInfo.getName_bb().toLowerCase().contains(query.toLowerCase())
                                || upInfo.getAddress_bb().toLowerCase().contains(query.toLowerCase())
                                || upInfo.getZilla_bb().toLowerCase().contains(query.toLowerCase())
                                || upInfo.getOpen_bb().toLowerCase().contains(query.toLowerCase())
                                || upInfo.getPhone_number_bb().toLowerCase().contains(query.toLowerCase()))
                        {
                            adapterList.add(upInfo);
                        }
                    }

                    bloodBankAdapter = new BloodBankAdapter(BloodBank.this, adapterList);

                    //refresh adapter
                    bloodBankAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(bloodBankAdapter);
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
