package com.rakibulnayeem.mediaide.SignUpLogIn;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.rakibulnayeem.mediaide.InternetConnection;
import com.rakibulnayeem.mediaide.MainActivity;
import com.rakibulnayeem.mediaide.R;
import com.rakibulnayeem.mediaide.Profile.UpdateProfile;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText signinEditTextPhoneNo;
    private EditText signinEditTextPassword;
    private Button buttonSignin;

    private TextView textViewSignup;
    private TextView recoverPassword;

    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    InternetConnection internetConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setTitle("User Login");

        mAuth = FirebaseAuth.getInstance();

        //if the objects getcurrentuser method is not null
        //means user is already logged in

        if(mAuth.getCurrentUser() != null){
            //opening profile activity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            //close this activity
            finish();

        }

        signinEditTextPhoneNo = (EditText) findViewById(R.id.signinEditTextphoneNoId);
        signinEditTextPassword = (EditText) findViewById(R.id.signinEditTextPasswordId);

        buttonSignin = (Button) findViewById(R.id.buttonSigninId);
        textViewSignup = (TextView) findViewById(R.id.textViewSignupId);
        progressBar = findViewById(R.id.progressbarId);
        recoverPassword =(TextView) findViewById(R.id.recoverPasswordTextViewId);


        buttonSignin.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
        recoverPassword.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        internetConnection = new InternetConnection(this);



    }

    @Override
    public void onClick(View v) {
        if(v==buttonSignin)
        {
            userLogin();
        }
        else if(v==textViewSignup)
        {
            finish();
            startActivity(new Intent(getApplicationContext(), UpdateProfile.class));

        }

        else if(v==recoverPassword)
        {
            recoverPasswordDialog();
        }

    }

    private void recoverPasswordDialog() {

        //AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");


        //set layout linear layout
        LinearLayout linearLayout = new LinearLayout(this);
        //views to set in dialog
      final  EditText emailEt = new EditText(this);
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailEt.setMinEms(16);

        linearLayout.addView(emailEt);
        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);

        //buttons recover
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(internetConnection.checkConnection())
                {

                    //input email
                    String email = emailEt.getText().toString().trim();

                    if(email.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(),"Enter email",Toast.LENGTH_SHORT).show();

                    }
                    else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                    {
                        Toast.makeText(getApplicationContext(),"Incorrect email",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        beginRecovery(email);
                    }

                }
                else
                {
                    // Not Available...
                    Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
                }

            }
        });


        //buttons cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dismiss dialog
                dialog.dismiss();

            }
        });

        //show dialog
        builder.create().show();

    }

   private void beginRecovery(String email)
   {

       progressDialog.setMessage("Sending email...");
       progressDialog.show();

       mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               progressDialog.dismiss();

               if(task.isSuccessful())
               {
                   Toast.makeText(LoginActivity.this,"Email sent",Toast.LENGTH_SHORT).show();
               }
               else
               {
                   Toast.makeText(LoginActivity.this,"Failed...",Toast.LENGTH_SHORT).show();
               }
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               progressDialog.dismiss();

               Toast.makeText(getApplicationContext(),"Error : "+e.getMessage(),Toast.LENGTH_SHORT).show();
           }
       });
   }


    private void userLogin() {

        String email = signinEditTextPhoneNo.getText().toString().trim();
        String password = signinEditTextPassword.getText().toString().trim();

        //checking the validity of the email
        if (email.isEmpty()) {
            signinEditTextPhoneNo.setError("Enter phone number");
            signinEditTextPhoneNo.requestFocus();
            return;
        }

        if (!Patterns.PHONE.matcher(email).matches()) {
            signinEditTextPhoneNo.setError("Enter a valid phone numbe");
            signinEditTextPhoneNo.requestFocus();
            return;
        }

        //checking the validity of the password
        if (password.isEmpty()) {
            signinEditTextPassword.setError("Enter a password");
            signinEditTextPassword.requestFocus();
            return;
        } else if (password.length() < 6) {
            signinEditTextPassword.setError("Minimum length of a password should be 6");
            signinEditTextPassword.requestFocus();
            return;
        }

        if (internetConnection.checkConnection()) {
            // Its Available..
            progressBar.setVisibility(View.VISIBLE);


        } else {
            // Not Available...
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }


    }
