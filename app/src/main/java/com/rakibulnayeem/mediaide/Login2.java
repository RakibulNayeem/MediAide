package com.rakibulnayeem.mediaide;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rakibulnayeem.mediaide.SignUpLogIn.SignUp;
import com.rakibulnayeem.mediaide.SignUpLogIn.VerifyPhoneNo_3;

public class Login2 extends AppCompatActivity implements View.OnClickListener {

    private EditText phoneNumberEt;
    private TextView gotoRegisterTv;
    private Button loginBtn;
    private ProgressBar progressBar;
    InternetConnection internetConnection;
    FirebaseAuth mAuth;
    DatabaseReference ref;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        //No notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();
        internetConnection = new InternetConnection(this);

        phoneNumberEt  = findViewById(R.id.phoneNumberEtId);
        loginBtn = findViewById(R.id.btnLoginId);
        progressBar = findViewById(R.id.progressbarId);
        gotoRegisterTv = findViewById(R.id.gotoRegisterId);

        mAuth = FirebaseAuth.getInstance();
        ref  = FirebaseDatabase.getInstance().getReference("persons_info");


        //if the objects getCurrentUser method is not null
        //means user is already logged in

        if(mAuth.getCurrentUser() != null){
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            //close this activity
            finish();
        }

        loginBtn.setOnClickListener(this);
        gotoRegisterTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == loginBtn)
        {
            String phone_number = phoneNumberEt.getText().toString().trim();
            phoneNumber = phone_number;

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

                Query query = ref.orderByChild("phone_number").equalTo("+88" + phone_number);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //check until required data get
                        if (dataSnapshot.exists()) {

                            //if account is exists then goto verify the account
                            Intent intent = new Intent(getApplicationContext(), VerifyPhoneNo_3.class);
                            intent.putExtra("phone_number",phone_number);
                            startActivity(intent);

                        }
                        else
                        {
                            Toast.makeText(Login2.this, "You don't have any account.Create an account first!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), SignUp2.class));
                        }
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(getApplicationContext(), "Failed to load information ", Toast.LENGTH_LONG).show();
                    }
                });

            }
            else
            {
                //not available...
                Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
            }

        }

        else if (v == gotoRegisterTv)
        {
            Intent intent = new Intent(getApplicationContext(), SignUp2.class);
            startActivity(intent);
            finish();
        }
    }

}