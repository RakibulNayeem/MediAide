package com.rakibulnayeem.mediaide.Posts;

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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rakibulnayeem.mediaide.MainActivity;
import com.rakibulnayeem.mediaide.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Post extends AppCompatActivity implements View.OnClickListener {

    private EditText hospitalNameEt,detailsEt,phoneNumberEt,dateTimeEt;
    private AutoCompleteTextView bloodGroupEt, zillaEt;
    private Button addPostBtn;
    private ProgressBar progressBar;
    Calendar calendar;

    String[] bloodGroupNames, bd_districts;
    String uid;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        setTitle("Create Post");


        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        bloodGroupNames = getResources().getStringArray(R.array.blood_group_names);
       bd_districts = getResources().getStringArray(R.array.bd_districts);
       uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        hospitalNameEt = findViewById(R.id.hospitalNameEtId);
        bloodGroupEt = (AutoCompleteTextView) findViewById(R.id.bloodGroupEtId);
        detailsEt = findViewById(R.id.detailsEtId);
        dateTimeEt = findViewById(R.id.dateTimeEtId);
        zillaEt = (AutoCompleteTextView) findViewById(R.id.zillaEtId);
        phoneNumberEt = findViewById(R.id.phoneNumberEtId);
        progressBar = findViewById(R.id.progressbarId);
        addPostBtn = findViewById(R.id.postBtnId);

        //gender spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,bloodGroupNames);
        bloodGroupEt.setThreshold(1);
        bloodGroupEt.setAdapter(adapter);


        //zilla
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,bd_districts);
        zillaEt.setThreshold(1);
        zillaEt.setAdapter(adapter2);


        addPostBtn.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {

        if(v == addPostBtn)
        {
            addPost();
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

    private void addPost() {

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

        if (hospital_name.isEmpty()){
            hospitalNameEt.setError("Please enter hospital name");
            hospitalNameEt.requestFocus();
            return;
        }


        if (zilla.isEmpty()){
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


        progressBar.setVisibility(View.VISIBLE);

        calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a dd-MM-yyyy");
        String current_time = simpleDateFormat.format(calendar.getTime());

        String key = databaseReference.push().getKey();

        PostAdapter adapter = new PostAdapter(uid, key,current_time, hospital_name,blood_group,date_time,details,zilla,phone_number);

        databaseReference.child(key).setValue(adapter);

        progressBar.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(),"Post uploaded successfully",Toast.LENGTH_LONG).show();
        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));


    }
}
