package com.rakibulnayeem.mediaide;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AboutUs extends AppCompatActivity implements View.OnClickListener {

    private ImageButton facebookLinkIv,emailLinkIv,facebookPageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        this.setTitle("About Us");

        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        facebookLinkIv = findViewById(R.id.facebookLinkBtnId);
        emailLinkIv = findViewById(R.id.emailImageBtnId);
        facebookPageBtn = findViewById(R.id.fbPageBtnId);

        facebookLinkIv.setOnClickListener(this);
        emailLinkIv.setOnClickListener(this);
        facebookPageBtn.setOnClickListener(this);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        if (v == facebookLinkIv){
            String url = "https://facebook.com/rakibul.nayeem.06";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }

        else if (v == emailLinkIv)
        {
            startActivity(new Intent(getApplicationContext(),UserFeedback.class));
        }


        else if (v == facebookPageBtn)
        {
            String url = "https://www.facebook.com/mediaid4u";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }

    }
}
