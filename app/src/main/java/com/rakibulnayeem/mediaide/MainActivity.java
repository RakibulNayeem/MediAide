package com.rakibulnayeem.mediaide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.rakibulnayeem.mediaide.Fragments.CallHistory;
import com.rakibulnayeem.mediaide.Fragments.HomeFragment;
import com.rakibulnayeem.mediaide.Posts.RequestFragment;
import com.rakibulnayeem.mediaide.Profile.ProfileFragment;
import com.rakibulnayeem.mediaide.SignUpLogIn.LoginWithPhone;
import com.rakibulnayeem.mediaide.fcmnoti.NotifyMembers;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity  {


    ActionBar actionBar;
    String sLatestVersion,sCurrentVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Actionbar and its title
        actionBar = getSupportActionBar();

        //load func for language
        loadLocale();


        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        //home fragment transaction (default on start)
        actionBar.setTitle(R.string.home_title);//change action bar title
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content,homeFragment,"");
        ft1.commit();


        //update version of the application
        //get latest version from play store
        new GetLatestVersion().execute();


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
            startActivity(new Intent(getApplicationContext(), LoginWithPhone.class));
        }
        else if(item.getItemId() == R.id.shareMenuId)
        {
            Intent intent7 = new Intent(Intent.ACTION_SEND);
            intent7.setType("text/plain");
            String subject = "BloodPlus";
            String body = "We are helping you to collect blood and save life.\n" + "https://play.google.com/store/apps/details?id=com.rakibulnayeem.mediaide";

            intent7.putExtra(Intent.EXTRA_SUBJECT,subject);
            intent7.putExtra(Intent.EXTRA_TEXT,body);
            startActivity(Intent.createChooser(intent7,"share with"));
        }

        else if (item.getItemId() == R.id.rateMenuId)
        {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + "com.rakibulnayeem.mediaide")));
        }

        else if(item.getItemId() == R.id.changeLanguageMenuId)
        {
            showChangeLanguageDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showChangeLanguageDialog() {

        final String[] listItems = {"English", "বাংলা"};
         AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
                mBuilder.setTitle(R.string.select_language);

           mBuilder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   if(which == 0)
                   {
                       //English
                       setLocale("en");
                       recreate();
                   }
                   else if(which == 1)
                   {
                       // bangla
                       setLocale("bn");
                       recreate();
                   }

                   //dismiss alert dialog when language selected
                   dialog.dismiss();
               }
           });

           //show alert dialog
          AlertDialog mDialog = mBuilder.create();
          mDialog.show();
    }

    private void setLocale(String lang) {
     Locale locale = new Locale(lang);
     Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //save data to shared preferences
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_lang", lang);
        editor.apply();


    }

    //load language saved in shared preferences
    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_lang", "");
        setLocale(language);
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
                            actionBar.setTitle(R.string.home_title);//change action bar title
                            HomeFragment homeFragment = new HomeFragment();
                            FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                            ft1.replace(R.id.content,homeFragment,"");
                            ft1.commit();
                            return true;

                        case R.id.nav_request :
                            //fragment transaction

                            actionBar.setTitle(R.string.request_title);//change action bar title
                            RequestFragment requestFragment = new RequestFragment();
                            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.content,requestFragment,"");
                            ft2.commit();
                            return true;


                        case R.id.nav_call_history :
                            //fragment transaction

                            actionBar.setTitle(R.string.call_title);//change action bar title
                            CallHistory callHistory = new CallHistory();
                            FragmentTransaction ft4 = getSupportFragmentManager().beginTransaction();
                            ft4.replace(R.id.content,callHistory,"");
                            ft4.commit();
                            return true;



                        case R.id.nav_profile :
                            //fragment transaction

                            actionBar.setTitle(R.string.profile_title);//change action bar title
                            ProfileFragment profileFragment  = new ProfileFragment();
                            FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                            ft3.replace(R.id.content,profileFragment,"");
                            ft3.commit();
                            return true;

                    }
                    return false;
                }
            };


    @Override
    protected void onStart() {
        super.onStart();
//        NotifyMembers.UpdateToken();
    }
}
