package com.rakibulnayeem.mediaide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

public class UpdateDonorInfo extends AppCompatActivity implements View.OnClickListener {


    private EditText emailEt,villageEt,upazilaEt,zillaEt,lastDonationDateEt,nameEt,statusEt, phoneNumberEt;
    private TextView idontKnowTv,more4MonthTv,BGTv;
    private ImageView BGDropDownIv;
    private Button buttonSignup;
    String[] bloodGroupNames;
  //  private Spinner bloodGSpinner;
    DatabaseReference databaseReference;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    String uid,bloodGroup;
    private ProgressBar progressBar;
    InternetConnection internetConnection;
    String _lastDonationDate,_name,_email,_village,_upazila,_zilla,_bloodGroup,_status,_LAST_DONATION_DATE, _phone_number;
    String donor_key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_donor_info);
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
      //  bloodGSpinner = findViewById(R.id.bloodGSpinnerId);

        donor_key = getIntent().getStringExtra("donor_key");


        nameEt = findViewById(R.id.nameEtId);
        emailEt = (EditText) findViewById(R.id.emailEtId);
        villageEt = (EditText) findViewById(R.id.villageEtId);
        upazilaEt = (EditText) findViewById(R.id.upazilaEtId);
        zillaEt = (EditText) findViewById(R.id.zillaEtId);
        lastDonationDateEt = findViewById(R.id.lastDDEtId);
        phoneNumberEt = findViewById(R.id.phoneNumberEtId);
        statusEt = findViewById(R.id.statusEtId);
        BGTv = findViewById(R.id.BGTvId);
        BGDropDownIv = findViewById(R.id.BGDropDownIvId);

        idontKnowTv = findViewById(R.id.idontKnowTvId);
        more4MonthTv = findViewById(R.id.more4MonthTvId);


        buttonSignup = (Button) findViewById(R.id.buttonSignupId);
        progressBar = findViewById(R.id.progressbarId);

        internetConnection = new InternetConnection(this);
        //blood group spinner
       // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.blood_group_sample_view,R.id.sampleTvId,bloodGroupNames);
        //bloodGSpinner.setAdapter(adapter);



        //get data from firebase
        getFirebaseData();


        buttonSignup.setOnClickListener(this);
        idontKnowTv.setOnClickListener(this);
        more4MonthTv.setOnClickListener(this);
        BGDropDownIv.setOnClickListener(this);


        //get blood group

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            bloodGroup = bundle.getString("blood_group");
            _bloodGroup = bloodGroup;
            Toast.makeText(getApplicationContext(),"Selected Blood group : "+bloodGroup,Toast.LENGTH_SHORT).show();
           // BGTv.setText(_bloodGroup);
        }

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
        else if (v == BGDropDownIv){
            Intent intent = new Intent(getApplicationContext(),BloodGroupList.class);
            intent.putExtra("name","update_donor");
            intent.putExtra("donor_key", donor_key);
            startActivity(intent);
        }
    }

    private void getFirebaseData() {

        // set info
        Query query = databaseReference.orderByChild("key").equalTo(donor_key);
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
                    String phone_number = ""+dataSnapshot1.child("phone_number").getValue();


                    _LAST_DONATION_DATE = last_donation_date;
                    _name = name;
                    _email = email;
                    _bloodGroup = blood_group;
                    // bloodGroup = blood_group;
                    _upazila = upazila;
                    _zilla = zilla;
                    _village = village;
                    _status = status;
                    _phone_number = phone_number;


                    //set data
                    nameEt.setText(name);
                    emailEt.setText(email);
                    BGTv.setText(_bloodGroup);
                    upazilaEt.setText(upazila);
                    zillaEt.setText(zilla);
                    villageEt.setText(village);
                    statusEt.setText(status);
                    phoneNumberEt.setText(phone_number);
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

        if (bloodGroup!=null)
        {
            BGTv.setText(bloodGroup);
        }
        //last donation date
        _lastDonationDate =  lastDonationDateEt.getText().toString().trim();

        String name = nameEt.getText().toString().trim();
        String email = emailEt.getText().toString().trim();
        String last_donation_date = lastDonationDateEt.getText().toString().trim();
        String village = villageEt.getText().toString().trim();
        String upazila = upazilaEt.getText().toString().trim();
        String zilla = zillaEt.getText().toString().trim();
        String phone_number = phoneNumberEt.getText().toString().trim();
        uid = user.getUid();
        String status = statusEt.getText().toString().trim();
        String blood_group = BGTv.getText().toString().trim();


        //  BGTv.setText(blood_group);

        if(blood_group.isEmpty())
        {
            BGTv.setError("Select blood group");
            BGTv.requestFocus();
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


        if(phone_number.isEmpty())
        {
            phoneNumberEt.setError("Enter Phone Number");
            phoneNumberEt.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        if(internetConnection.checkConnection()) {



            if (isNameChanged() || isEmailChanged() || isVillageChanged() || isUpazilaChanged() || isZillaChanged() || isLDDChanged() || isBGChanged() || isStatusChanged() || isPhoneNumberChanged())
            {
                Toast.makeText(getApplicationContext(),"Data has been updated",Toast.LENGTH_LONG).show();

                finish();
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(getApplicationContext(),MainActivity.class));

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

    private boolean isPhoneNumberChanged() {

        if (!_phone_number.equals(phoneNumberEt.getText().toString().trim())){
            databaseReference.child(donor_key).child("phone_number").setValue(phoneNumberEt.getText().toString().trim());
            _phone_number = phoneNumberEt.getText().toString().trim();
            return true;
        }
        else {
            return false;
        }

    }
    private boolean isStatusChanged() {

        if (!_status.equals(statusEt.getText().toString().trim())){
            databaseReference.child(donor_key).child("status").setValue(statusEt.getText().toString().trim());
            _status = statusEt.getText().toString().trim();
            return true;
        }
        else {
            return false;
        }
    }


    private boolean isNameChanged() {
        if (!_name.equals(nameEt.getText().toString().trim())){
            databaseReference.child(donor_key).child("name").setValue(nameEt.getText().toString().trim());
            _name = nameEt.getText().toString().trim();
            return true;
        }
        else {
            return false;
        }

    }

    private boolean isEmailChanged() {
        if (!_email.equals(emailEt.getText().toString().trim())){
            databaseReference.child(donor_key).child("email").setValue(emailEt.getText().toString().trim());
            _email = emailEt.getText().toString().trim();
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isVillageChanged() {
        if (!_village.equals(villageEt.getText().toString().trim())){
            databaseReference.child(donor_key).child("village").setValue(villageEt.getText().toString().trim());
            _village = villageEt.getText().toString().trim();
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isUpazilaChanged() {
        if (!_upazila.equals(upazilaEt.getText().toString().trim())){
            databaseReference.child(donor_key).child("upazila").setValue(upazilaEt.getText().toString().trim());
            _upazila = upazilaEt.getText().toString().trim();
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isZillaChanged() {
        if (!_zilla.equals(zillaEt.getText().toString().trim())){
            databaseReference.child(donor_key).child("zilla").setValue(zillaEt.getText().toString().trim());
            _zilla = zillaEt.getText().toString().trim();
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isLDDChanged() {
        if (!_LAST_DONATION_DATE.equals(lastDonationDateEt.getText().toString().trim())){
            databaseReference.child(donor_key).child("last_donation_date").setValue(lastDonationDateEt.getText().toString().trim());
            _LAST_DONATION_DATE = lastDonationDateEt.getText().toString().trim();
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isBGChanged() {
        if (!_bloodGroup.equals(BGTv.getText().toString().trim())){
            databaseReference.child(donor_key).child("blood_group").setValue(BGTv.getText().toString().trim());
            _bloodGroup = BGTv.getText().toString().trim();
            return true;
        }
        else {
            return false;
        }
    }



}
