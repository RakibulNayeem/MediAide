package com.rakibulnayeem.mediaide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class PostEdit extends AppCompatActivity implements View.OnClickListener {
    private Button saveBtn, cancelBtn;
    private EditText detailsEt, hospitalNameEt, phoneNumberEt, dateTimeEt;
    private AutoCompleteTextView bloodGroupEt, zillaEt;
    private ProgressBar progressBar;
    DatabaseReference databaseReference;
    String uid, _details, _blood_group, _hospital_name, _zilla, _phone_number, _date_time;
    InternetConnection internetConnection;
    String[] bloodGroupNames, bd_districts;
    String post_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_edit);
        databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        post_key = getIntent().getStringExtra("post_key");
        bloodGroupNames = getResources().getStringArray(R.array.blood_group_names);
        bd_districts = getResources().getStringArray(R.array.bd_districts);

        internetConnection = new InternetConnection(this);

        detailsEt = findViewById(R.id.detailsEtId);
        bloodGroupEt = findViewById(R.id.bloodGroupEtId);
        hospitalNameEt = findViewById(R.id.hospitalNameEtId);
        zillaEt = findViewById(R.id.zillaEtId);
        phoneNumberEt = findViewById(R.id.phoneNumberEtId);
        dateTimeEt = findViewById(R.id.dateTimeEtId);

        saveBtn = findViewById(R.id.savePostBtnId);
        cancelBtn = findViewById(R.id.cancelEditPostBtnId);
        progressBar = findViewById(R.id.progressbarId);

        //blood group
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,bloodGroupNames);
        bloodGroupEt.setThreshold(1);
        bloodGroupEt.setAdapter(adapter);


        //zilla
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,bd_districts);
        zillaEt.setThreshold(1);
        zillaEt.setAdapter(adapter2);

        saveBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);


        getData();


    }

    @Override
    public void onClick(View v) {

        if (v == cancelBtn)
        {
            finish();
            startActivity(new Intent(getApplicationContext(),MyPosts.class));
        }

        else if (v == saveBtn)
        {
            UploadPost();
        }

    }

    private void getData() {

        Query query = databaseReference.orderByChild("key").equalTo(post_key);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    String details = ""+dataSnapshot1.child("details").getValue();
                    String blood_group = ""+dataSnapshot1.child("blood_group").getValue();
                    String hospital_name = ""+dataSnapshot1.child("hospital_name").getValue();
                    String zilla = ""+dataSnapshot1.child("zilla").getValue();
                    String phone_number = ""+dataSnapshot1.child("phone_number").getValue();
                    String date_time = ""+dataSnapshot1.child("date_time").getValue();


                    _details = details;
                    _blood_group = blood_group;
                    _hospital_name = hospital_name;
                    _zilla = zilla;
                    _phone_number = phone_number;
                    _date_time = date_time;

                    detailsEt.setText(details);
                    bloodGroupEt.setText(blood_group);
                    hospitalNameEt.setText(hospital_name);
                    zillaEt.setText(zilla);
                    phoneNumberEt.setText(phone_number);
                    dateTimeEt.setText(date_time);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Failed to load information ",Toast.LENGTH_LONG).show();
            }
        });


    }


    private void UploadPost() {

        final   String blood_group = bloodGroupEt.getText().toString().trim();
        final   String hospital_name = hospitalNameEt.getText().toString().trim();
        final   String phone_number = phoneNumberEt.getText().toString().trim();
        final   String date_time = dateTimeEt.getText().toString().trim();
        final   String details = detailsEt.getText().toString().trim();
        final   String zilla = zillaEt.getText().toString().trim();


        if (blood_group.isEmpty()){
            bloodGroupEt.setError("Please enter blood group");
            bloodGroupEt.requestFocus();
            return;
        }

        else if  (hospital_name.isEmpty()){
            hospitalNameEt.setError("Please enter hospital name");
            hospitalNameEt.requestFocus();
            return;
        }


        else if (zilla.isEmpty()){
            zillaEt.setError("Please enter zilla name");
            zillaEt.requestFocus();
            return;
        }


        else if (phone_number.isEmpty()){
            phoneNumberEt.setError("Please enter phone number");
            phoneNumberEt.requestFocus();
            return;
        }


        else if(phone_number.length()<11)
        {
            phoneNumberEt.setError("Enter a valid phone number");
            phoneNumberEt.requestFocus();
            return;
        }

        else if (date_time.isEmpty()){
            dateTimeEt.setError("Please enter date and time");
            dateTimeEt.requestFocus();
            return;
        }

        else if (details.isEmpty()){
            detailsEt.setError("Please enter details");
            detailsEt.requestFocus();
            return;
        }


        if(internetConnection.checkConnection()) {

            progressBar.setVisibility(View.VISIBLE);

            if (isDetailsChanged() || isBloodGroupChanged()  || isHospitalNameChanged() || isZillaChanged() || isPhoneNumberChanged() || isDateTimeChanged())
            {
                Toast.makeText(getApplicationContext(),"Data has been updated",Toast.LENGTH_LONG).show();

            }
            else {
                Toast.makeText(getApplicationContext(),"Data is same and can not be updated",Toast.LENGTH_LONG).show();

            }
            progressBar.setVisibility(View.GONE);
            finish();
            startActivity(new Intent(getApplicationContext(),MyPosts.class));

        }
        else
        {
            //not available...
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }


    }


    private boolean isDateTimeChanged() {
        if (!_date_time.equals(dateTimeEt.getText().toString().trim())){
            databaseReference.child(post_key).child("date_time").setValue(dateTimeEt.getText().toString().trim());
            _date_time = dateTimeEt.getText().toString().trim();
            return true;
        }
        else {
            return false;
        }

    }

    private boolean isPhoneNumberChanged() {
        if (!_phone_number.equals(phoneNumberEt.getText().toString().trim())){
            databaseReference.child(post_key).child("phone_number").setValue(phoneNumberEt.getText().toString().trim());
            _phone_number = phoneNumberEt.getText().toString().trim();
            return true;
        }
        else {
            return false;
        }

    }

    private boolean isZillaChanged() {
        if (!_zilla.equals(zillaEt.getText().toString().trim())){
            databaseReference.child(post_key).child("zilla").setValue(zillaEt.getText().toString().trim());
            _zilla = zillaEt.getText().toString().trim();
            return true;
        }
        else {
            return false;
        }

    }

    private boolean isHospitalNameChanged() {
        if (!_hospital_name.equals(hospitalNameEt.getText().toString().trim())){
            databaseReference.child(post_key).child("hospital_name").setValue(hospitalNameEt.getText().toString().trim());
            _hospital_name = hospitalNameEt.getText().toString().trim();
            return true;
        }
        else {
            return false;
        }

    }

    private boolean isBloodGroupChanged() {

        if (!_blood_group.equals(bloodGroupEt.getText().toString().trim())){
            databaseReference.child(post_key).child("blood_group").setValue(bloodGroupEt.getText().toString().trim());
            _blood_group = bloodGroupEt.getText().toString().trim();
            return true;
        }
        else {
            return false;
        }

    }




    private boolean isDetailsChanged() {


        if (!_details.equals(detailsEt.getText().toString().trim())){
            databaseReference.child(post_key).child("details").setValue(detailsEt.getText().toString().trim());
            _details = detailsEt.getText().toString().trim();
            return true;
        }
        else {
            return false;
        }

    }



}