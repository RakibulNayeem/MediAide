package com.rakibulnayeem.mediaide;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDonor extends AppCompatActivity implements View.OnClickListener {

    private EditText villageEt,upazilaEt,phoneNumberEt,statusEt,lastDDEt,nameEt;
    private AutoCompleteTextView bloodGroupEt,zillaEt;
    private Button addDonorBtn;
    private ProgressBar progressBar;

    String[] bloodGroupNames, bd_districts;
    DatabaseReference databaseReference;
    String lastDonationDate;
    private TextView idontKnowTv,more4MonthTv;
    String adder_uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_donor);
        setTitle("Add Donor");

        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("persons_info");
        bloodGroupNames = getResources().getStringArray(R.array.blood_group_names);
        bd_districts = getResources().getStringArray(R.array.bd_districts);

        adder_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        nameEt = findViewById(R.id.nameEtId);
        villageEt = findViewById(R.id.villageEtId);
        upazilaEt = findViewById(R.id.upazilaEtId);
        zillaEt = findViewById(R.id.zillaEtId);
        phoneNumberEt = findViewById(R.id.phoneNumberEtId);
        statusEt = findViewById(R.id.statusEtId);
        lastDDEt = findViewById(R.id.lastDDEtId);
        bloodGroupEt = findViewById(R.id.bloodGroupEtId);

        idontKnowTv = findViewById(R.id.idontKnowTvId);
        more4MonthTv = findViewById(R.id.more4MonthTvId);

        progressBar = findViewById(R.id.progressbarId);


        addDonorBtn = findViewById(R.id.addDonorBtnId);
        //blood group
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,bloodGroupNames);
        bloodGroupEt.setThreshold(1);
        bloodGroupEt.setAdapter(adapter);


        //zilla
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,bd_districts);
        zillaEt.setThreshold(1);
        zillaEt.setAdapter(adapter2);



        addDonorBtn.setOnClickListener(this);
        idontKnowTv.setOnClickListener(this);
        more4MonthTv.setOnClickListener(this);


    }



    @Override
    public void onClick(View v) {

        if(v == addDonorBtn)
        {
            DonorAdd();
        }

        else if(v == idontKnowTv){
            lastDonationDate = "I Don't Know";
            lastDDEt.setText(lastDonationDate);
        }
        else if (v == more4MonthTv)
        {
            lastDonationDate = "More than 4 months";
            lastDDEt.setText(lastDonationDate);
        }


    }



    //options menu and searchAction
    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater =  getMenuInflater();
        menuInflater.inflate(R.menu.add_donor_menu_layout,menu);
        return super.onCreateOptionsMenu(menu);
    }





    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            this.finish();
        }

        else if (item.getItemId() == R.id.showDonorListMenuId)
        {
            startActivity(new Intent(getApplicationContext(),DonorYouAdded.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void DonorAdd() {

        progressBar.setVisibility(View.VISIBLE);

        final   String name = nameEt.getText().toString().trim();
        final   String village = villageEt.getText().toString().trim();
        final   String phone_number = phoneNumberEt.getText().toString().trim();
        final   String upazila = upazilaEt.getText().toString().trim();
        final   String zilla = zillaEt.getText().toString().trim();
        final   String last_donation_date = lastDDEt.getText().toString().trim();
        final   String status = statusEt.getText().toString().trim();
        final   String blood_group = bloodGroupEt.getText().toString().trim();


        final String email = "";
        final String donate_switch = "true";
        final String uid = "null";


        if(name.isEmpty())
        {
            nameEt.setError("Enter Name");
            nameEt.requestFocus();
            return;
        }


        if(last_donation_date.isEmpty())
        {
            lastDDEt.setError("Enter last donation date");
            lastDDEt.requestFocus();
            return;
        }



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

        if (phone_number.length() < 11)
        {
            phoneNumberEt.setError("Enter Valid Number");
            phoneNumberEt.requestFocus();
            return;
        }

        if (phone_number.isEmpty())
        {
            phoneNumberEt.setError("Enter Phone Number");
            phoneNumberEt.requestFocus();
            return;
        }


        String key = databaseReference.push().getKey();


        AddDonorAdapter addDonor = new AddDonorAdapter(name,uid,adder_uid,key,email,village,blood_group,upazila,zilla,phone_number,last_donation_date,status,donate_switch);

        databaseReference.child(key).setValue(addDonor);

        progressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(),"Donor info is added",Toast.LENGTH_LONG).show();

        finish();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));

    }
}
