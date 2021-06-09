package com.rakibulnayeem.mediaide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDoctorForHospital extends AppCompatActivity implements View.OnClickListener {

    private EditText nameEt,degreeEt,chamberAddressEt,phoneNumberEt,specialityEt, feeEt, activeDayEt;
    private AutoCompleteTextView zillaEt;
    private Button addDoctorBtn;
    private ProgressBar progressBar;
    private Spinner timeSpinner,time2Spinner,ampmSpinner,ampm2Spinner;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String uid, hospital_id;

    String[] time, bd_districs;
    String[] ampm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor_for_hospital);
        this.setTitle("Create Account");

        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

      //  uid = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("hospitals_doctors_info");

         hospital_id = getIntent().getStringExtra("hospital_id");
        bd_districs = getResources().getStringArray(R.array.bd_districts);


        nameEt = findViewById(R.id.nameEtId);
        degreeEt = findViewById(R.id.degreeEtId);
        chamberAddressEt = findViewById(R.id.chamberAddressEtId);
        phoneNumberEt = findViewById(R.id.phoneNumberEtId);
        zillaEt = findViewById(R.id.zillaEtId);
        specialityEt = findViewById(R.id.specialityEtId);
        feeEt = findViewById(R.id.feeEtId);
        activeDayEt = findViewById(R.id.activeDayEtId);

        time = getResources().getStringArray(R.array.time);
        ampm = getResources().getStringArray(R.array.am_pm);

        addDoctorBtn = findViewById(R.id.addDoctorBtnId);

        timeSpinner = findViewById(R.id.timeSpinnerId);
        time2Spinner = findViewById(R.id.time2SpinnerId);
        ampmSpinner = findViewById(R.id.ampmSpinnerId);
        ampm2Spinner = findViewById(R.id.ampm2SpinnerId);

        progressBar  = findViewById(R.id.progressbarId);



        // spinners
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this,R.layout.time_spinner_layout,R.id.sampleTvId,time);
        ArrayAdapter<String> time2Adapter = new ArrayAdapter<String>(this,R.layout.time_spinner_layout,R.id.sampleTvId,time);
        ArrayAdapter<String> ampmAdapter = new ArrayAdapter<String>(this,R.layout.time_spinner_layout,R.id.sampleTvId,ampm);
        ArrayAdapter<String> ampm2Adapter = new ArrayAdapter<String>(this,R.layout.time_spinner_layout,R.id.sampleTvId,ampm);

        // districs name
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,bd_districs);
        zillaEt.setThreshold(1);
        zillaEt.setAdapter(adapter);

        timeSpinner.setAdapter(timeAdapter);
        time2Spinner.setAdapter(time2Adapter);
        ampmSpinner.setAdapter(ampmAdapter);
        ampm2Spinner.setAdapter(ampm2Adapter);

        addDoctorBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if (v == addDoctorBtn)
        {
            DoctorAdd();
        }



    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void DoctorAdd() {


        final String name = nameEt.getText().toString().trim();
        final String degree = degreeEt.getText().toString().trim();
        final String chamber_address = chamberAddressEt.getText().toString().trim();
        final String phone_number = phoneNumberEt.getText().toString().trim();
        final String zilla = zillaEt.getText().toString().trim();
        final String speciality = specialityEt.getText().toString().trim();
        final String fee = feeEt.getText().toString().trim();
        final String active_day = activeDayEt.getText().toString().trim();

        final String open_time = timeSpinner.getSelectedItem().toString();
        final String close_time = time2Spinner.getSelectedItem().toString();
        final String ampm = ampmSpinner.getSelectedItem().toString();
        final String ampm2 = ampm2Spinner.getSelectedItem().toString();
        final String imageUri = "noImage";

        if(name.isEmpty())
        {
            nameEt.setError("Enter Name");
            nameEt.requestFocus();
            return;
        }


        if(degree.isEmpty())
        {
            degreeEt.setError("Enter Degrees");
            degreeEt.requestFocus();
            return;
        }

        if(chamber_address.isEmpty())
        {
            chamberAddressEt.setError("Enter Chamber Address");
            chamberAddressEt.requestFocus();
            return;
        }

        if(phone_number.isEmpty())
        {
            phoneNumberEt.setError("Enter Phone Number");
            phoneNumberEt.requestFocus();
            return;
        }

        if(zilla.isEmpty())
        {
            zillaEt.setError("Enter Zilla");
            zillaEt.requestFocus();
            return;
        }

        if(active_day.isEmpty())
        {
            activeDayEt.setError("Enter Active Days");
            activeDayEt.requestFocus();
            return;
        }


        if(fee.isEmpty())
        {
            feeEt.setError("Enter Fee");
            feeEt.requestFocus();
            return;
        }

        if (phone_number.length() < 11)
        {
            phoneNumberEt.setError("Enter Valid Number");
            phoneNumberEt.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);

        String key = databaseReference.push().getKey();

        uid = key;

        AddDoctorForHospitalAdapter adapter = new AddDoctorForHospitalAdapter(uid,key,name,degree,speciality,fee,chamber_address,zilla,phone_number,active_day,hospital_id,open_time,ampm,close_time,ampm2,imageUri);
        databaseReference.child(key).setValue(adapter);

        progressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(),"Your information uploaded.",Toast.LENGTH_LONG).show();
        finish();
        startActivity(new Intent(getApplicationContext(),HospitalsDoctors.class));




    }
}
