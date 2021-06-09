package com.rakibulnayeem.mediaide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity implements View.OnClickListener {


    private EditText villageEt,upazilaEt,emailEt,lastDonationDateEt,nameEt,statusEt;
    private AutoCompleteTextView bloodGroupEt,zillaEt;
    private Button updateDataBtn;
    private ProgressBar progressBar;
    private TextView  idontKnowTv,more4MonthTv;

    String[] bloodGroupNames,bd_districts;
   // private Spinner bloodGroupSpinner;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference databaseReference;
    String lastDonationDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Create Profile");


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("persons_info");


        bloodGroupNames = getResources().getStringArray(R.array.blood_group_names);
        bd_districts = getResources().getStringArray(R.array.bd_districts);
      //  bloodGroupSpinner = findViewById(R.id.bloodGroupSpinnerId);

        nameEt = findViewById(R.id.updateNameEtId);
        villageEt = findViewById(R.id.updateVillageEtId);
        upazilaEt = findViewById(R.id.updateUpazilaEtId);
        zillaEt = findViewById(R.id.updateZillaEtId);
        bloodGroupEt = findViewById(R.id.updateBloodGroupEtId);
        emailEt = findViewById(R.id.updateEmailEtId);
        lastDonationDateEt = findViewById(R.id.lastDDEtId);
        statusEt = findViewById(R.id.updateStatusEtId);
        idontKnowTv = findViewById(R.id.idontKnowTvId);
        more4MonthTv = findViewById(R.id.more4MonthTvId);


        progressBar = findViewById(R.id.updateProgressbarId);




        updateDataBtn = findViewById(R.id.updateProfileBtnId);

        //blood group autocomplete textView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,bloodGroupNames);
        bloodGroupEt.setThreshold(1);
        bloodGroupEt.setAdapter(adapter);

        // districts name
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,bd_districts);
        zillaEt.setThreshold(1);
        zillaEt.setAdapter(adapter2);



        updateDataBtn.setOnClickListener(this);
        idontKnowTv.setOnClickListener(this);
        more4MonthTv.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if(v == updateDataBtn)
        {
            UpdateInfo();
        }
        else if(v == idontKnowTv){
            lastDonationDate = "I Don't Know";
            lastDonationDateEt.setText(lastDonationDate);
        }
        else if (v == more4MonthTv)
        {
            lastDonationDate = "More than 4 months";
            lastDonationDateEt.setText(lastDonationDate);
        }


    }


    private void UpdateInfo() {



           String village = villageEt.getText().toString().trim();
           String email = emailEt.getText().toString().trim();
           String upazila = upazilaEt.getText().toString().trim();
           String zilla = zillaEt.getText().toString().trim();
           String name = nameEt.getText().toString().trim();
           String last_donation_date = lastDonationDateEt.getText().toString().trim();
           String status = statusEt.getText().toString().trim();
           String blood_group = bloodGroupEt.getText().toString().trim();
           String donate_switch = "true";
           String adder_uid = "null";


       // final  String blood_group = bloodGroupSpinner.getSelectedItem().toString();

        final  String uid = user.getUid();
        final String phone_number = user.getPhoneNumber();


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

        if(blood_group.isEmpty())
        {
            bloodGroupEt.setError("Enter Blood Group");
            bloodGroupEt.requestFocus();
            return;
        }


        if (status.isEmpty())
        {
            statusEt.setError("Enter status");
            statusEt.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);

        SignUpAdapter adapter = new SignUpAdapter(name,uid,adder_uid,email,village,blood_group,upazila,zilla,phone_number,last_donation_date,status,donate_switch);

        databaseReference.child(uid).setValue(adapter);
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(),"Profile info  added successfully",Toast.LENGTH_LONG).show();

        finish();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));


    }

}
