package com.rakibulnayeem.mediaide.Organization;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rakibulnayeem.mediaide.MainActivity;
import com.rakibulnayeem.mediaide.R;
import com.rakibulnayeem.mediaide.ZillaList;

import java.util.ArrayList;
import java.util.List;

public class Organizations extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
   OrganizationsAdapter organizationsAdapter;
    private List<AddOrganizationsAdapter> adapterList;
    DatabaseReference databaseReference,dataRefToCheck;
    private ProgressBar progressBar;

    private TextView zillaTv;
    private ImageView dropDownIv;
    String Zilla=null, uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizations);
        this.setTitle("Organizations");

        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        dataRefToCheck = FirebaseDatabase.getInstance().getReference("organizations_info_approve");
        databaseReference = FirebaseDatabase.getInstance().getReference("organizations_info");

        recyclerView = findViewById(R.id.usersRecycleViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        progressBar = findViewById(R.id.recyclerProgressBarId);

        adapterList = new ArrayList<>();

        dropDownIv = findViewById(R.id.dropDownIvId);
        zillaTv = findViewById(R.id.zillaTvId);

        //get zilla name
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            String zillaName = bundle.getString("zillaName");
            Zilla = zillaName;
        }

        dropDownIv.setOnClickListener(this);
        zillaTv.setOnClickListener(this);

        getAllOrganizations();

    }

    @Override
    public void onClick(View v) {

        if (v == dropDownIv || v == zillaTv )
        {
            Intent intent = new Intent(getApplicationContext(), ZillaList.class);
            intent.putExtra("name","organizations");
            startActivity(intent);
        }
    }

    private void getAllOrganizations() {

        //get all organizations
        if (Zilla==null)
        {
            //get all hospital

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    adapterList.clear();
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {

                        AddOrganizationsAdapter upInfo = dataSnapshot1.getValue(AddOrganizationsAdapter.class);

                        adapterList.add(upInfo);
                    }

                    organizationsAdapter = new OrganizationsAdapter(Organizations.this, adapterList);

                    organizationsAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(organizationsAdapter);

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
            databaseReference = FirebaseDatabase.getInstance().getReference("organizations_info");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    adapterList.clear();
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {
                        AddOrganizationsAdapter upInfo = dataSnapshot1.getValue(AddOrganizationsAdapter.class);

                        if (upInfo.getZilla().toLowerCase().contains(Zilla.toLowerCase())){
                            adapterList.add(upInfo);
                        }

                    }

                    organizationsAdapter = new OrganizationsAdapter(Organizations.this, adapterList);

                    organizationsAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(organizationsAdapter);

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


    //options menu and searchAction
    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater =  getMenuInflater();
        menuInflater.inflate(R.menu.add_organizations_menu_layout,menu);

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
                    getAllOrganizations();
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
                    getAllOrganizations();
                }

                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.addOrganizationsMenuId)
        {
            Query query = databaseReference.orderByChild("uid").equalTo(uid);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //check until required data get
                    if (dataSnapshot.exists()) {
                        //add the username
                        Toast.makeText(Organizations.this, "You already have an account.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MyOrganizationAccount.class);
                        startActivity(intent);

                    }
                    else
                    {
                        Query query1 = dataRefToCheck.orderByChild("uid").equalTo(uid);
                        query1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists())
                                {
                                    Toast.makeText(Organizations.this, "Your account in pending. It make take few more days!.", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Intent intent = new Intent(getApplicationContext(), AddOrganizations.class);
                                    intent.putExtra("from","from_user");
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(getApplicationContext(), "Failed to load information ", Toast.LENGTH_LONG).show();
                }
            });
        }

       else if(item.getItemId()==android.R.id.home)
        {
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
  /*
        else if(item.getItemId()==R.id.addOrganizationByAdminMenuId)
        {
         Intent intent = new Intent(getApplicationContext(),AddOrganizations.class);
         intent.putExtra("from","from_admin");
         startActivity(intent);
        }

   */

        else if(item.getItemId()==R.id.showOrganizationAccountMenuId)
        {
            Query query = databaseReference.orderByChild("uid").equalTo(uid);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //check until required data get
                    if (dataSnapshot.exists()) {
                        //add the username
                        Intent intent = new Intent(getApplicationContext(), MyOrganizationAccount.class);
                        startActivity(intent);

                    }
                    else
                    {
                        Query query2 = dataRefToCheck.orderByChild("uid").equalTo(uid);
                        query2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //check pending account
                                if (dataSnapshot.exists())
                                {
                                    Toast.makeText(Organizations.this, "Your organization account is in pending, it may take few more days.", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(Organizations.this, "You don't have any account.First add an organization.", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),AddOrganizations.class));
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(getApplicationContext(), "Failed to load information ", Toast.LENGTH_LONG).show();
                }
            });
        }

/*
        else if(item.getItemId()==R.id.approveOrganizationMenuId)
        {
            startActivity(new Intent(getApplicationContext(),ApproveOrganizationList.class));
        }

 */



        return super.onOptionsItemSelected(item);
    }



    //search result
    private void searchRequest(final String query) {

        //get all searched organizations

        if (Zilla!=null)
        {
            zillaTv.setText(Zilla);
            //get all searched users
            databaseReference = FirebaseDatabase.getInstance().getReference("organizations_info");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    adapterList.clear();
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {

                        AddOrganizationsAdapter upInfo = dataSnapshot1.getValue(AddOrganizationsAdapter.class);


                        if (upInfo.getZilla().toLowerCase().contains(Zilla.toLowerCase())){

                            if(upInfo.getName().toLowerCase().contains(query.toLowerCase())
                                    ||upInfo.getAddress().toLowerCase().contains(query.toLowerCase())
                                    ||upInfo.getPhone_number().toLowerCase().contains(query.toLowerCase()))
                            {
                                adapterList.add(upInfo);
                            }
                        }
                    }

                    organizationsAdapter = new OrganizationsAdapter(Organizations.this, adapterList);

                    //refresh adapter
                    organizationsAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(organizationsAdapter);

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
            databaseReference = FirebaseDatabase.getInstance().getReference("organizations_info");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    adapterList.clear();
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                    {

                        AddOrganizationsAdapter upInfo = dataSnapshot1.getValue(AddOrganizationsAdapter.class);


                        if(upInfo.getName().toLowerCase().contains(query.toLowerCase())
                                ||upInfo.getAddress().toLowerCase().contains(query.toLowerCase())
                                ||upInfo.getZilla().toLowerCase().contains(query.toLowerCase())
                                ||upInfo.getPhone_number().toLowerCase().contains(query.toLowerCase()))
                        {
                            adapterList.add(upInfo);
                        }
                    }

                    organizationsAdapter = new OrganizationsAdapter(Organizations.this, adapterList);

                    //refresh adapter
                    organizationsAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(organizationsAdapter);

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
