package com.rakibulnayeem.mediaide;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.rakibulnayeem.mediaide.Ambulances.Ambulance;
import com.rakibulnayeem.mediaide.BloodBanks.BloodBank;
import com.rakibulnayeem.mediaide.Doctor.Doctors;
import com.rakibulnayeem.mediaide.Donor.SearchActivity;
import com.rakibulnayeem.mediaide.Hospital.Hospital;
import com.rakibulnayeem.mediaide.Hospital.HospitalList;
import com.rakibulnayeem.mediaide.Hospital.HospitalListAdmin;
import com.rakibulnayeem.mediaide.Organization.Organizations;

public class ZillaList extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private  String[] districtNames;
    private ListView listView;
    ArrayAdapter<String> adapter;
    String Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zilla_list);
        this.setTitle("Select Zilla");

        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //get value
        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            Name = bundle.getString("name");
        }

        listView = (ListView) findViewById(R.id.listViewId);

        districtNames = getResources().getStringArray(R.array.bd_districts);

        adapter = new ArrayAdapter<String>(ZillaList.this,R.layout.zilla_listview_sample_layout,R.id.sampleViewId,districtNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String value = adapter.getItem(position);


        if (Name.equals("hospital"))
        {
            Intent intent = new Intent(getApplicationContext(), Hospital.class);
            intent.putExtra("zillaName",value);
            startActivity(intent);
            finish();
        }


        else if (Name.equals("hospital_list"))
        {
            Intent intent = new Intent(getApplicationContext(), HospitalList.class);
            intent.putExtra("zillaName",value);
            startActivity(intent);
            finish();
        }

        else if (Name.equals("hospital_list_admin"))
        {
            Intent intent = new Intent(getApplicationContext(), HospitalListAdmin.class);
            intent.putExtra("zillaName",value);
            startActivity(intent);
            finish();
        }


        else if (Name.equals("organizations"))
        {
            Intent intent = new Intent(getApplicationContext(), Organizations.class);
            intent.putExtra("zillaName",value);
            startActivity(intent);
            finish();
        }

        else if (Name.equals("blood_bank"))
        {
            Intent intent = new Intent(getApplicationContext(), BloodBank.class);
            intent.putExtra("zillaName",value);
            startActivity(intent);
            finish();
        }

        else if (Name.equals("donor_search"))
        {
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            intent.putExtra("zillaName",value);
            startActivity(intent);
            finish();
        }

        else if (Name.equals("doctors"))
        {
            Intent intent = new Intent(getApplicationContext(), Doctors.class);
            intent.putExtra("zillaName",value);
            startActivity(intent);
            finish();
        }


        else if (Name.equals("ambulance"))
        {
            Intent intent = new Intent(getApplicationContext(), Ambulance.class);
            intent.putExtra("zillaName",value);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_hospital_menu_layout,menu);

        MenuItem menuItem = menu.findItem(R.id.searchActionMenuId);
        SearchView searchView =(SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Here");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return false;
            }
        });



        return super.onCreateOptionsMenu(menu);
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
