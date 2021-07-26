package com.rakibulnayeem.mediaide.SignUpLogIn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rakibulnayeem.mediaide.InternetConnection;
import com.rakibulnayeem.mediaide.MainActivity;
import com.rakibulnayeem.mediaide.R;

public class LoginWithPhone extends AppCompatActivity {

    private EditText phoneNumberEt;
    private Button loginBtn;
    private ProgressBar progressBar;
    InternetConnection internetConnection;
    FirebaseAuth mAuth;
    DatabaseReference ref;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_phone);

        //No notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        internetConnection = new InternetConnection(this);

        phoneNumberEt  = findViewById(R.id.loginPhoneNumberEtId);
        loginBtn = findViewById(R.id.buttonLoginId);
        progressBar = findViewById(R.id.progressbarId);
        phoneNumber = phoneNumberEt.getText().toString().trim();

        mAuth = FirebaseAuth.getInstance();
         ref  = FirebaseDatabase.getInstance().getReference("persons_info");


        //if the objects getcurrentuser method is not null
        //means user is already logged in

        if(mAuth.getCurrentUser() != null){
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            //close this activity
            finish();

        }


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone_number = phoneNumberEt.getText().toString().trim();

                if (phone_number.isEmpty())
                {
                    phoneNumberEt.setError("Enter phone number");
                    phoneNumberEt.requestFocus();
                    return;
                }
               else if(!Patterns.PHONE.matcher(phone_number).matches())
                {
                    phoneNumberEt.setError("Enter a valid phone number");
                    phoneNumberEt.requestFocus();
                    return;
                }

                else if(phone_number.length()<11)
                {
                    phoneNumberEt.setError("Enter a valid phone number");
                    phoneNumberEt.requestFocus();
                    return;
                }
                if (internetConnection.checkConnection())
                {


                    Intent intent = new Intent(getApplicationContext(), VerifyPhoneNo_3.class);
                    intent.putExtra("phone_number",phone_number);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    //not available...
                    Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
                }


            }
        });
    }




}
