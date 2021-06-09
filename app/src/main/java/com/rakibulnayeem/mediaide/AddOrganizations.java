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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddOrganizations extends AppCompatActivity implements View.OnClickListener {

    private EditText nameEt,addressEt,phoneNumberEt, openEt;
    private AutoCompleteTextView zillaEt;
    private Button addOrganizationBtn;
    private ProgressBar progressBar;
    DatabaseReference databaseReference;
    String uid, from_admin_user;
    String[] bd_districts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_organizations);
        this.setTitle("Add Organization");

        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("organizations_info_approve");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        bd_districts = getResources().getStringArray(R.array.bd_districts);

        from_admin_user = getIntent().getStringExtra("from");

        nameEt = findViewById(R.id.nameEtId);
        addressEt = findViewById(R.id.addressEtId);
        zillaEt = findViewById(R.id.zillaEtId);
        openEt = findViewById(R.id.openEtId);
        phoneNumberEt = findViewById(R.id.phoneNumberEtId);
        progressBar = findViewById(R.id.progressbarId);
        addOrganizationBtn = findViewById(R.id.addOrganizationBtnId);

        // districts name
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,bd_districts);
        zillaEt.setThreshold(1);
        zillaEt.setAdapter(adapter);
        addOrganizationBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if(v == addOrganizationBtn)
        {
            addOrganization();
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

    private void addOrganization() {

        progressBar.setVisibility(View.VISIBLE);

        final String name = nameEt.getText().toString().trim();
        final String address = addressEt.getText().toString().trim();
        final String zilla= zillaEt.getText().toString().trim();
        final String open= openEt.getText().toString().trim();
        final String phone_number = phoneNumberEt.getText().toString().trim();

        String key = databaseReference.push().getKey();

        if (from_admin_user.equals("from_admin"))
        {
            uid = "admin_upload";
            databaseReference = FirebaseDatabase.getInstance().getReference("organizations_info");
        }


        AddOrganizationsAdapter adapter = new AddOrganizationsAdapter(uid, key, name, address,open, zilla, phone_number);
        databaseReference.child(key).setValue(adapter);

        progressBar.setVisibility(View.GONE);

        if (from_admin_user.equals("from_admin"))
        {
            Toast.makeText(getApplicationContext(),"Dear Admin, your information uploaded successfully!",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Your information uploaded.It may take 24 hours for verification!",Toast.LENGTH_SHORT).show();
        }

        finish();
       // startActivity(new Intent(getApplicationContext(),Organizations.class));


    }


}
