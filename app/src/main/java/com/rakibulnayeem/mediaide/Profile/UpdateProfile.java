package com.rakibulnayeem.mediaide.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rakibulnayeem.mediaide.InternetConnection;
import com.rakibulnayeem.mediaide.MainActivity;
import com.rakibulnayeem.mediaide.R;

public class UpdateProfile extends AppCompatActivity implements View.OnClickListener {


    private EditText emailEt,villageEt,upazilaEt,lastDonationDateEt,nameEt,statusEt;
    private AutoCompleteTextView zillaEt, bloodGroupTv;
    private TextView idontKnowTv,more4MonthTv;
    //private ImageView BGDropDownIv;
    private Button buttonSignup;
    String[] bloodGroupNames, bd_districts;
    DatabaseReference databaseReference;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    String uid,bloodGroup;
    private ProgressBar progressBar;
    InternetConnection internetConnection;
    String _lastDonationDate,_name,_email,_village,_upazila,_zilla,_bloodGroup,_status,_LAST_DONATION_DATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        //No notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("persons_info");
        bloodGroupNames = getResources().getStringArray(R.array.blood_group_names);
        bd_districts = getResources().getStringArray(R.array.bd_districts);
        uid = user.getUid();



        nameEt = findViewById(R.id.nameEtId);
        emailEt = (EditText) findViewById(R.id.emailEtId);
        villageEt = (EditText) findViewById(R.id.villageEtId);
        upazilaEt = (EditText) findViewById(R.id.upazilaEtId);
        zillaEt = (AutoCompleteTextView) findViewById(R.id.zillaEtId);
        lastDonationDateEt = findViewById(R.id.lastDDEtId);
        statusEt = findViewById(R.id.statusEtId);
        bloodGroupTv = findViewById(R.id.bloodGroupEtId);

        idontKnowTv = findViewById(R.id.idontKnowTvId);
        more4MonthTv = findViewById(R.id.more4MonthTvId);


        buttonSignup = (Button) findViewById(R.id.buttonSignupId);
        progressBar = findViewById(R.id.progressbarId);

        internetConnection = new InternetConnection(this);
        //blood group
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,bloodGroupNames);
        bloodGroupTv.setThreshold(1);
        bloodGroupTv.setAdapter(adapter);

        //zilla
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,bd_districts);
        zillaEt.setThreshold(1);
        zillaEt.setAdapter(adapter2);



        //get data from firebase
        getFirebaseData();


        buttonSignup.setOnClickListener(this);
        idontKnowTv.setOnClickListener(this);
        more4MonthTv.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {

        if(v==buttonSignup)
        {
            userRegister();
        }

        else if(v == idontKnowTv){
            _lastDonationDate = "I Don't Know";
            lastDonationDateEt.setText(_lastDonationDate);
        }
        else if (v == more4MonthTv)
        {
            _lastDonationDate = "More than 4 months";
            lastDonationDateEt.setText(_lastDonationDate);
        }

    }

    private void getFirebaseData() {

        // set info
        uid = user.getUid();
        Query query = databaseReference.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //check until required data get
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    //get data
                    String name = ""+dataSnapshot1.child("name").getValue();
                    String email = ""+dataSnapshot1.child("email").getValue();
                    String blood_group = ""+dataSnapshot1.child("blood_group").getValue();
                    String upazila = ""+dataSnapshot1.child("upazila").getValue();
                    String zilla = ""+dataSnapshot1.child("zilla").getValue();
                    String village = ""+dataSnapshot1.child("village").getValue();
                    String status = ""+dataSnapshot1.child("status").getValue();
                    String last_donation_date = ""+dataSnapshot1.child("last_donation_date").getValue();


                    _LAST_DONATION_DATE = last_donation_date;
                  _name = name;
                  _email = email;
                  _bloodGroup = blood_group;
                  _upazila = upazila;
                  _zilla = zilla;
                  _village = village;
                  _status = status;


                    //set data
                    nameEt.setText(name);
                    emailEt.setText(email);
                    bloodGroupTv.setText(blood_group);
                    upazilaEt.setText(upazila);
                    zillaEt.setText(zilla);
                    villageEt.setText(village);
                    statusEt.setText(status);
                    lastDonationDateEt.setText(last_donation_date);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(),"Failed to load information ",Toast.LENGTH_LONG).show();
            }
        });


    }




    private void userRegister() {

        //last donation date
        _lastDonationDate =  lastDonationDateEt.getText().toString().trim();

        String name = nameEt.getText().toString().trim();
        String email = emailEt.getText().toString().trim();
        String last_donation_date = lastDonationDateEt.getText().toString().trim();
        String village = villageEt.getText().toString().trim();
        String upazila = upazilaEt.getText().toString().trim();
        String zilla = zillaEt.getText().toString().trim();
        String phone_number = user.getPhoneNumber();
         uid = user.getUid();
        String status = statusEt.getText().toString().trim();
        String blood_group = bloodGroupTv.getText().toString().trim();


        if(blood_group.isEmpty())
        {
            bloodGroupTv.setError("Enter blood group");
            bloodGroupTv.requestFocus();
            return;
        }

        if(name.isEmpty())
        {
            nameEt.setError("Enter name");
            nameEt.requestFocus();
            return;
        }

        if(last_donation_date.isEmpty())
        {
            lastDonationDateEt.setError("Enter last donation date");
            lastDonationDateEt.requestFocus();
            return;
        }

        //checking the validity of email


       else if(village.isEmpty())
        {
            villageEt.setError("Enter village name");
            villageEt.requestFocus();
            return;
        }


        if(upazila.isEmpty())
        {
            upazilaEt.setError("Enter upazila name");
            upazilaEt.requestFocus();
            return;
        }

        if(zilla.isEmpty())
        {
            zillaEt.setError("Enter zilla name");
            zillaEt.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        if(internetConnection.checkConnection()) {



            if (isNameChanged() || isEmailChanged() || isVillageChanged() || isUpazilaChanged() || isZillaChanged() || isLDDChanged() || isBGChanged() || isStatusChanged())
            {
                Toast.makeText(getApplicationContext(),"Data has been updated",Toast.LENGTH_LONG).show();

                finish();
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
            else {
                Toast.makeText(getApplicationContext(),"Data is same and can not be updated",Toast.LENGTH_LONG).show();

                progressBar.setVisibility(View.GONE);
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }

        }
        else
        {
            //not available...
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }



    }




///check isChanged method

    private boolean isStatusChanged() {

        if (!_status.equals(statusEt.getText().toString().trim())){
            databaseReference.child(uid).child("status").setValue(statusEt.getText().toString().trim());
            _status = statusEt.getText().toString().trim();
            return true;
        }
        else {
            return false;
        }
    }


    private boolean isNameChanged() {
        if (!_name.equals(nameEt.getText().toString().trim())){
            databaseReference.child(uid).child("name").setValue(nameEt.getText().toString().trim());
            _name = nameEt.getText().toString().trim();
            return true;
        }
        else {
            return false;
        }

    }

    private boolean isEmailChanged() {
        if (!_email.equals(emailEt.getText().toString().trim())){
            databaseReference.child(uid).child("email").setValue(emailEt.getText().toString().trim());
            _email = emailEt.getText().toString().trim();
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isVillageChanged() {
        if (!_village.equals(villageEt.getText().toString().trim())){
            databaseReference.child(uid).child("village").setValue(villageEt.getText().toString().trim());
            _village = villageEt.getText().toString().trim();
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isUpazilaChanged() {
        if (!_upazila.equals(upazilaEt.getText().toString().trim())){
            databaseReference.child(uid).child("upazila").setValue(upazilaEt.getText().toString().trim());
            _upazila = upazilaEt.getText().toString().trim();
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isZillaChanged() {
        if (!_zilla.equals(zillaEt.getText().toString().trim())){
            databaseReference.child(uid).child("zilla").setValue(zillaEt.getText().toString().trim());
            _zilla = zillaEt.getText().toString().trim();
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isLDDChanged() {
        if (!_LAST_DONATION_DATE.equals(lastDonationDateEt.getText().toString().trim())){
            databaseReference.child(uid).child("last_donation_date").setValue(lastDonationDateEt.getText().toString().trim());
            _LAST_DONATION_DATE = lastDonationDateEt.getText().toString().trim();
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isBGChanged() {
        if (!_bloodGroup.equals(bloodGroupTv.getText().toString().trim())){
            databaseReference.child(uid).child("blood_group").setValue(bloodGroupTv.getText().toString().trim());
            _bloodGroup = bloodGroupTv.getText().toString().trim();
            return true;
        }
        else {
            return false;
        }
    }



}
