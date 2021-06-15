package com.rakibulnayeem.mediaide.UserFeedbacks;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.rakibulnayeem.mediaide.R;

public class UserFeedback extends AppCompatActivity implements View.OnClickListener {

    private Button sendButton, clearButton;
    private EditText nameEditText, messageEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feedback);
        this.setTitle("User Feedback");

        sendButton = (Button) findViewById(R.id.sendButtonId);
        clearButton = (Button) findViewById(R.id.clearButtonId);

        nameEditText = (EditText) findViewById(R.id.nameEditTextId);
        messageEditText = (EditText) findViewById(R.id.messageEditTextId);

        sendButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);


        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


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
        try {
            String name = nameEditText.getText().toString();
            String message = messageEditText.getText().toString();

            if (v.getId() == R.id.sendButtonId) {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/email");

                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"nrakibulhasan4@gmail.com"});

                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback from app");
                intent.putExtra(Intent.EXTRA_TEXT, "Name : " + name + "\n Message : " + message);
                startActivity(Intent.createChooser(intent, "Feedback with"));
                nameEditText.setText("");
                messageEditText.setText("");

            } else if (v.getId() == R.id.clearButtonId) {
                nameEditText.setText("");
                messageEditText.setText("");

            }
        } catch (Exception e) {

            Toast.makeText(getApplicationContext(),"Exception : "+e,Toast.LENGTH_SHORT).show();
        }
    }
}
