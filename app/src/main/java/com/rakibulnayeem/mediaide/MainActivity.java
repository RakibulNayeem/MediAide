package com.rakibulnayeem.mediaide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;

import java.io.IOException;

public class MainActivity extends AppCompatActivity  {


    ActionBar actionBar;
    String sLatestVersion,sCurrentVersion;
    String appLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Actionbar and its title
        actionBar = getSupportActionBar();
        actionBar.setTitle("Profile");


        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        //home fragment transaction (default on start)
        actionBar.setTitle("Home");//change action bar title
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content,homeFragment,"");
        ft1.commit();


        //update version of the application
        //get latest version from play store
        new GetLatestVersion().execute();


        ///app link for share the app
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("1_app_link");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String app_link= ""+dataSnapshot.child("app_link_child").getValue();

                //set value
                appLink = app_link;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Failed to load info",Toast.LENGTH_SHORT).show();
            }
        });


    }




    // get latest version from playstore
    private class GetLatestVersion extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                sLatestVersion = Jsoup
                        .connect("https://play.google.com/store/apps/details?id=" + "com.rakibulnayeem.mediaide")
                        .timeout(30000)
                        .get()
                        .select("div.hAyfc:nth-child(4)>" +
                                "span:nth-child(2) > div:nth-child(1)" +
                                "> span:nth-child(1)")
                        .first()
                        .ownText();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            return sLatestVersion;
        }

        @Override
        protected void onPostExecute(String s) {
            //get current version
            sCurrentVersion = BuildConfig.VERSION_NAME;

            if (sLatestVersion!= null)
            {  //version convert to float
                float cVersion = Float.parseFloat(sCurrentVersion);
                float lVersion = Float.parseFloat(sLatestVersion);

                //check condition (latest version is greater than current version)
                if (lVersion > cVersion)
                {
                    //Create update dialog
                    showDialog(sLatestVersion);
                }

            }
        }
    }





    private void showDialog(String VersionFromRemoteConfig) {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New Update Available")
                .setMessage("Latest Version: "+VersionFromRemoteConfig)
                .setPositiveButton("Update",null)
                .setNegativeButton("Not Now",null)
                .show();

        dialog.setCancelable(false);


        //positive button
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=\" + \"com.rakibulnayeem.mediaide")));

                }catch (android.content.ActivityNotFoundException anfe){
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" + "com.rakibulnayeem.mediaide")));
                }
            }
        });


        //negative button
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    //option menu

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater =  getMenuInflater();
        menuInflater.inflate(R.menu.menu_logout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.signOutMenuId)
        {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getApplicationContext(),"Sign out successful",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(getApplicationContext(),LoginWithPhone.class));
        }
        if (item.getItemId()==R.id.share){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String subject = "BloodPlus";
            String body = "We are helping you to collect blood and save life.\n"+appLink;

            intent.putExtra(Intent.EXTRA_SUBJECT,subject);
            intent.putExtra(Intent.EXTRA_TEXT,body);
            startActivity(Intent.createChooser(intent,"share with"));
        }
        if (item.getItemId()==R.id.rate){
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + "com.rakibulnayeem.mediaide")));

        }

        return super.onOptionsItemSelected(item);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    //handle item clicks
                    switch (menuItem.getItemId())
                    {
                        case R.id.nav_home :
                            //fragment transaction
                            actionBar.setTitle("Home");//change action bar title
                            HomeFragment homeFragment = new HomeFragment();
                            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                            ft1.replace(R.id.content,homeFragment,"");
                            ft1.commit();
                            return true;

                        case R.id.nav_request :
                            //fragment transaction

                            actionBar.setTitle("Request");//change action bar title
                            RequestFragment requestFragment = new RequestFragment();
                            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.content,requestFragment,"");
                            ft2.commit();
                            return true;



                        case R.id.nav_profile :
                            //fragment transaction

                            actionBar.setTitle("Profile");//change action bar title
                            ProfileFragment profileFragment  = new ProfileFragment();
                            FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                            ft3.replace(R.id.content,profileFragment,"");
                            ft3.commit();
                            return true;

                    }
                    return false;
                }
            };



}
