package com.rakibulnayeem.mediaide.Fact;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rakibulnayeem.mediaide.R;

public class AddFacts extends AppCompatActivity implements View.OnClickListener {

    private EditText titleEt,subTitleEt,descriptionEt;
    private Button addFactsBtn;
    private ProgressBar progressBar;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_facts);

        this.setTitle("Add Facts");

        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("facts_info");

        titleEt = findViewById(R.id.titleEtId);
        subTitleEt = findViewById(R.id.subTitleEtId);
        descriptionEt = findViewById(R.id.descriptionEtId);
        addFactsBtn = findViewById(R.id.addFactsBtnId);
        progressBar = findViewById(R.id.progressbarId);

        addFactsBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v == addFactsBtn)
        {
            addFacts();
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




    private void addFacts() {
      progressBar.setVisibility(View.VISIBLE);

      final  String title = titleEt.getText().toString().trim();
      final  String sub_title = subTitleEt.getText().toString().trim();
      final  String description = descriptionEt.getText().toString().trim();

      String key = databaseReference.push().getKey();

      AddFactsAdapter adapter = new AddFactsAdapter(key,title,sub_title,description);
      databaseReference.child(key).setValue(adapter);

        progressBar.setVisibility(View.GONE);
        startActivity(new Intent(getApplicationContext(), Facts.class));
        finish();
        Toast.makeText(getApplicationContext(),"Facts info added successfully",Toast.LENGTH_SHORT).show();



    }


}
