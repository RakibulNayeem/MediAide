package com.rakibulnayeem.mediaide.Doctor;

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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rakibulnayeem.mediaide.Hospital.HospitalListAdmin;
import com.rakibulnayeem.mediaide.R;

public class AddDoctorByAdmin extends AppCompatActivity implements View.OnClickListener {


    private EditText nameEt,degreeEt,chamberAddressEt,phoneNumberEt,specialityEt, feeEt,activeDayEt;
    private TextView hospitalTv;
    private ImageView dropDownIv;
    private AutoCompleteTextView zillaEt;
    private Button addDoctorBtn;
    private ProgressBar progressBar;
    private Spinner timeSpinner,time2Spinner,ampmSpinner,ampm2Spinner;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String uid= null;
    private String[] bd_districts;

    String hospital_name = null, hospital_id = null;

    String[] time;
    String[] ampm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor_by_admin);

        this.setTitle("Create Account");

        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        databaseReference = FirebaseDatabase.getInstance().getReference("doctors_info");

        bd_districts = getResources().getStringArray(R.array.bd_districts);

        nameEt = findViewById(R.id.nameEtId);
        degreeEt = findViewById(R.id.degreeEtId);
        chamberAddressEt = findViewById(R.id.chamberAddressEtId);
        phoneNumberEt = findViewById(R.id.phoneNumberEtId);
        //autocomplete edit text
        zillaEt = findViewById(R.id.zillaEtId);

        specialityEt = findViewById(R.id.specialityEtId);
        feeEt = findViewById(R.id.feeEtId);
        activeDayEt = findViewById(R.id.activeDayEtId);
        dropDownIv = findViewById(R.id.dropDownIvId);
        hospitalTv = findViewById(R.id.selectHospitalTvId);

        time = getResources().getStringArray(R.array.time);
        ampm = getResources().getStringArray(R.array.am_pm);

        addDoctorBtn = findViewById(R.id.addDoctorBtnId);

        timeSpinner = findViewById(R.id.timeSpinnerId);
        time2Spinner = findViewById(R.id.time2SpinnerId);
        ampmSpinner = findViewById(R.id.ampmSpinnerId);
        ampm2Spinner = findViewById(R.id.ampm2SpinnerId);

        progressBar  = findViewById(R.id.progressbarId);



        //get hospital name
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String  hospital_name1 = bundle.getString("hospital_name");
            hospital_id = bundle.getString("hospital_id");
            hospital_name = hospital_name1;

            if (hospital_name != null)
            {
                hospitalTv.setText(hospital_name);
            }

        }
        else
        {
            hospital_id = "";
            hospital_name = "";
        }


        // spinners
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<String>(this,R.layout.time_spinner_layout,R.id.sampleTvId,time);
        ArrayAdapter<String> time2Adapter = new ArrayAdapter<String>(this,R.layout.time_spinner_layout,R.id.sampleTvId,time);
        ArrayAdapter<String> ampmAdapter = new ArrayAdapter<String>(this,R.layout.time_spinner_layout,R.id.sampleTvId,ampm);
        ArrayAdapter<String> ampm2Adapter = new ArrayAdapter<String>(this,R.layout.time_spinner_layout,R.id.sampleTvId,ampm);

        // districts name
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,bd_districts);
        zillaEt.setThreshold(1);
        zillaEt.setAdapter(adapter);



        timeSpinner.setAdapter(timeAdapter);
        time2Spinner.setAdapter(time2Adapter);
        ampmSpinner.setAdapter(ampmAdapter);
        ampm2Spinner.setAdapter(ampm2Adapter);

        addDoctorBtn.setOnClickListener(this);
        hospitalTv.setOnClickListener(this);
        dropDownIv.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if (v == addDoctorBtn)
        {
            DoctorAdd();
        }
        else if (v == hospitalTv || v == dropDownIv)
        {
            Intent intent = new Intent(getApplicationContext(), HospitalListAdmin.class);
            startActivity(intent);
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
        String speciality = specialityEt.getText().toString().trim();
        final String fee = feeEt.getText().toString().trim();
        final String active_day = activeDayEt.getText().toString().trim();

        final String time = timeSpinner.getSelectedItem().toString();
        final String time2 = time2Spinner.getSelectedItem().toString();
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


        if(active_day.isEmpty())
        {
            activeDayEt.setError("Enter Active Days");
            activeDayEt.requestFocus();
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
        if (phone_number.length() < 11)
        {
            phoneNumberEt.setError("Enter Valid Number");
            phoneNumberEt.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);


        String key = databaseReference.push().getKey();
        uid = key;

        AddDoctorByAdminAdapter adapter = new AddDoctorByAdminAdapter(uid,key,name,hospital_name,hospital_id,degree,speciality,fee,chamber_address,zilla,phone_number,active_day,time,ampm,time2,ampm2,imageUri);
        databaseReference.child(key).setValue(adapter);

        progressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(),"Your information uploaded successfully",Toast.LENGTH_LONG).show();
        finish();
        startActivity(new Intent(getApplicationContext(), Doctors.class));


    }


}