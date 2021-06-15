package com.rakibulnayeem.mediaide;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.rakibulnayeem.mediaide.Donor.UpdateDonorInfo;
import com.rakibulnayeem.mediaide.Profile.UpdateProfile;

public class BloodGroupList extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private  String[] bloodGroupNames;
    private ListView listView;
    ArrayAdapter<String> adapter;
    String Name = null;
    String donor_key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_group_list);
        this.setTitle("Select Blood Group");

        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //get value
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            Name = bundle.getString("name");
            donor_key = bundle.getString("donor_key");
        }

        listView = (ListView) findViewById(R.id.listViewId);

        bloodGroupNames = getResources().getStringArray(R.array.blood_group_names);

        adapter = new ArrayAdapter<String>(BloodGroupList.this,R.layout.blood_group_sample_view,R.id.sampleTvId,bloodGroupNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String value = adapter.getItem(position);


        if (Name.equals("update_profile"))
        {
            finish();
            Intent intent = new Intent(getApplicationContext(), UpdateProfile.class);
            intent.putExtra("blood_group",value);
            startActivity(intent);

        }

        else if (Name.equals("update_donor"))
        {
            finish();
            Intent intent = new Intent(getApplicationContext(), UpdateDonorInfo.class);
            intent.putExtra("blood_group",value);
            intent.putExtra("donor_key", donor_key);
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

}
