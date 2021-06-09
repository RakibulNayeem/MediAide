package com.rakibulnayeem.mediaide;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AddAmbulance extends AppCompatActivity implements View.OnClickListener {
    private EditText serviceNameEt, driverNameEt, addressEt,phoneNumberEt, vehicleEt, typeEt;
    private AutoCompleteTextView zillaEt;
    private Button addAmbulanceBtn;
    private SwitchCompat availableSwitch;
    private ProgressBar progressBar;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String uid, from_user_admin;
    String available_switch_value = "true";
    String[] bd_districts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ambulance);
        this.setTitle("Create Account");

        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("ambulance_info_approve");
        bd_districts = getResources().getStringArray(R.array.bd_districts);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        uid = user.getUid();

        from_user_admin = getIntent().getStringExtra("from");

        serviceNameEt = findViewById(R.id.serviceNameEtId);
        driverNameEt = findViewById(R.id.driverNameEtId);
        availableSwitch = findViewById(R.id.availableSwitchId);
        addressEt = findViewById(R.id.addressEtId);
        zillaEt = findViewById(R.id.zillaEtId);
        vehicleEt = findViewById(R.id.vehicleEtId);
        typeEt = findViewById(R.id.typeEtId);
        phoneNumberEt = findViewById(R.id.phoneNumberEtId);
        progressBar = findViewById(R.id.progressbarId);

        // districts name
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,bd_districts);
        zillaEt.setThreshold(1);
        zillaEt.setAdapter(adapter);

        addAmbulanceBtn = findViewById(R.id.addAmbulanceBtnId);
        addAmbulanceBtn.setOnClickListener(this);
        getAvailableSwitch();


    }

    @Override
    public void onClick(View v) {

        if(v == addAmbulanceBtn)
        {
            add_ambulance();
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

    private void add_ambulance() {

        progressBar.setVisibility(View.VISIBLE);

        final String name = serviceNameEt.getText().toString().trim();
        final String driver_name = driverNameEt.getText().toString().trim();
        final String address = addressEt.getText().toString().trim();
        final String zilla= zillaEt.getText().toString().trim();
        final String vehicle_no = vehicleEt.getText().toString().trim();
        final String service_type = typeEt.getText().toString().trim();
        final String phone_number = phoneNumberEt.getText().toString().trim();



        if(name.isEmpty())
        {
            serviceNameEt.setError("Enter Name");
            serviceNameEt.requestFocus();
            return;
        }


        if(address.isEmpty())
        {
            addressEt.setError("Enter Address");
            addressEt.requestFocus();
            return;
        }

        if(zilla.isEmpty())
        {
            zillaEt.setError("Enter Zilla");
            zillaEt.requestFocus();
            return;
        }

        if(vehicle_no.isEmpty())
        {
            vehicleEt.setError("Enter Car No");
            vehicleEt.requestFocus();
            return;
        }


        if(phone_number.isEmpty())
        {
            phoneNumberEt.setError("Enter Phone Number");
            phoneNumberEt.requestFocus();
            return;
        }



        String key = databaseReference.push().getKey();

        if (from_user_admin.equals("from_admin"))
        {
            uid = "admin_upload";
            databaseReference = FirebaseDatabase.getInstance().getReference("ambulance_info");
        }

        AddAmbulanceAdapter addAmbulanceAdapter = new AddAmbulanceAdapter(uid, key,name, driver_name, address,zilla,vehicle_no, service_type, available_switch_value, phone_number);
        databaseReference.child(key).setValue(addAmbulanceAdapter);

        progressBar.setVisibility(View.GONE);
        if (from_user_admin.equals("from_admin"))
        {
            Toast.makeText(getApplicationContext(),"Dear Admin, your information uploaded successfully!",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Your information uploaded.It may take  24 hours for verification!",Toast.LENGTH_SHORT).show();
        }

        finish();
      //  startActivity(new Intent(getApplicationContext(),Ambulance.class));

    }


       private void getAvailableSwitch() {

        availableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    available_switch_value = "true";

                }
                else {
                    available_switch_value = "false";

                }

            }
        });


    }


}
