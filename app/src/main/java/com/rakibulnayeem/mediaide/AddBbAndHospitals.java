package com.rakibulnayeem.mediaide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.rakibulnayeem.mediaide.UserFeedbacks.UserFeedback;

public class AddBbAndHospitals extends AppCompatActivity implements View.OnClickListener {

    private ImageButton fbLinkBtn, emailBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bb_and_hospitals);
        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        fbLinkBtn = findViewById(R.id.facebookLinkBtnId);
        emailBtn = findViewById(R.id.emailImageBtnId);

        fbLinkBtn.setOnClickListener(this);
        emailBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if (v == fbLinkBtn){
            String url = "https://facebook.com/rakibul.nayeem.06";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }

        else if (v == emailBtn)
        {
            startActivity(new Intent(getApplicationContext(), UserFeedback.class));
        }


    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }

}
