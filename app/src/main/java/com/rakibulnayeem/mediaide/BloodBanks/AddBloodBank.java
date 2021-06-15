package com.rakibulnayeem.mediaide.BloodBanks;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rakibulnayeem.mediaide.BloodBanks.AddBBankAdapter;
import com.rakibulnayeem.mediaide.R;

public class AddBloodBank extends AppCompatActivity implements View.OnClickListener {

    private EditText nameBBEt,addressEt,phoneNumberEt, openEt;
    private AutoCompleteTextView zillaEt;
    private Button addBBankBtn;
    private ProgressBar progressBar;
    DatabaseReference databaseReference;
    String[] bd_districts;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String uid= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blood_bank);

        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("blood_bank");
        bd_districts = getResources().getStringArray(R.array.bd_districts);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        uid = user.getUid();


        nameBBEt = findViewById(R.id.nameBBEtId);
        addressEt = findViewById(R.id.addressEtId);
        openEt = findViewById(R.id.openEtId);
        zillaEt = findViewById(R.id.zillaEtId);
        phoneNumberEt = findViewById(R.id.phoneNumberEtId);
        progressBar = findViewById(R.id.progressbarId);
        addBBankBtn = findViewById(R.id.addBloodBankBtnId);

        // districts name
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,bd_districts);
        zillaEt.setThreshold(1);
        zillaEt.setAdapter(adapter);

        addBBankBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if(v == addBBankBtn)
        {
            AddBank();
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

    private void AddBank() {

        progressBar.setVisibility(View.VISIBLE);

        final String name_bb = nameBBEt.getText().toString().trim();
        final String address_bb = addressEt.getText().toString().trim();
        final String open_bb = openEt.getText().toString().trim();
        final String zilla_bb = zillaEt.getText().toString().trim();
        final String phone_number_bb = phoneNumberEt.getText().toString().trim();

        String key = databaseReference.push().getKey();

        AddBBankAdapter bBankAdapter = new AddBBankAdapter(uid,key, name_bb,address_bb, open_bb, zilla_bb,phone_number_bb);
        databaseReference.child(key).setValue(bBankAdapter);

        progressBar.setVisibility(View.GONE);
        finish();
        Toast.makeText(getApplicationContext(),"Blood bank info added successfully",Toast.LENGTH_SHORT).show();

    }


}
