package com.rakibulnayeem.mediaide;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddHospital extends AppCompatActivity implements View.OnClickListener {

    private EditText hospitalNameEt, hospitalCategoryEt, addressEt,phoneNumberEt;
    private AutoCompleteTextView zillaEt;
    private Button addHospitalBtn;
    private ProgressBar progressBar;
    DatabaseReference databaseReference;
    private String[] bd_districts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hospital);
        this.setTitle("Add Hospital Details");

        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("hospital_info");
        bd_districts = getResources().getStringArray(R.array.bd_districts);


        hospitalNameEt = findViewById(R.id.hospitalNameEtId);
        hospitalCategoryEt = findViewById(R.id.hospitalCategoryEtId);
        addressEt = findViewById(R.id.addressEtId);
        zillaEt = findViewById(R.id.zillaEtId);
        phoneNumberEt = findViewById(R.id.phoneNumberEtId);
        progressBar = findViewById(R.id.progressbarId);

        // districts name
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,bd_districts);
        zillaEt.setThreshold(1);
        zillaEt.setAdapter(adapter);

        addHospitalBtn = findViewById(R.id.addHospitalBtnId);
        addHospitalBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if(v == addHospitalBtn)
        {
            addHospital();
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

    private void addHospital() {

        progressBar.setVisibility(View.VISIBLE);

        final String name = hospitalNameEt.getText().toString().trim();
        final String category = hospitalCategoryEt.getText().toString().trim();
        final String address = addressEt.getText().toString().trim();
        final String zilla= zillaEt.getText().toString().trim();
        final String phone_number = phoneNumberEt.getText().toString().trim();

        String key = databaseReference.push().getKey();

        AddHospitalAdapter addHospitalAdapter = new AddHospitalAdapter(key,name,category,address,zilla,phone_number);
        databaseReference.child(key).setValue(addHospitalAdapter);

        progressBar.setVisibility(View.GONE);
        finish();
        Toast.makeText(getApplicationContext(),"Hospital info added successfully",Toast.LENGTH_SHORT).show();

    }


}
