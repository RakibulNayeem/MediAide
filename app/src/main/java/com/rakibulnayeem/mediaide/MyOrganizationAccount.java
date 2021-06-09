package com.rakibulnayeem.mediaide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MyOrganizationAccount extends AppCompatActivity {


    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String uid;
    String phoneNumber;

    private TextView organizationNameTv,addressTv,zillaTv,phoneNumberTv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_organization_account);

        this.setTitle("Profile");

        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("organizations_info");

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        uid = user.getUid();


        organizationNameTv = findViewById(R.id.organizationNameTvId);
        addressTv = findViewById(R.id.organizationAddressTvId);
        zillaTv = findViewById(R.id.zillaTvId);
        phoneNumberTv = findViewById(R.id.phoneNumberTvId);


        // set info
        Query query = databaseReference.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check until required data get
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    //get data
                    String name = "" + dataSnapshot1.child("name").getValue();
                    String address = "" + dataSnapshot1.child("address").getValue();
                    String zilla = "" + dataSnapshot1.child("zilla").getValue();
                    String phone_number = "" + dataSnapshot1.child("phone_number").getValue();


                    //get phone number
                    phoneNumber = phone_number;
                    //set data
                    organizationNameTv.setText(name);
                    phoneNumberTv.setText(phone_number);
                    addressTv.setText(address);
                    zillaTv.setText(zilla);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(), "Failed to load information ", Toast.LENGTH_LONG).show();
            }
        });



    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

}
