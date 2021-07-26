package com.rakibulnayeem.mediaide.SignUpLogIn;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rakibulnayeem.mediaide.MainActivity;
import com.rakibulnayeem.mediaide.R;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class VerifyPhoneNo extends AppCompatActivity implements View.OnClickListener {

    EditText otp;
    TextView resendOtpBtn, countDownTimerTv;
    CountDownTimer countDownTimer;
    Button verifyBtn;
    String no;
    String phoneNumber;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private ProgressBar progressBar;
    DatabaseReference databaseReference;
    FirebaseUserMetadata metadata;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_no);

        //No notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mAuth = FirebaseAuth.getInstance();
        otp = findViewById(R.id.otpEtId);
        countDownTimerTv = findViewById(R.id.countDownTimerTvId);
        progressBar = findViewById(R.id.progressBarVerifyId);
        databaseReference = FirebaseDatabase.getInstance().getReference("persons_info");

        no = getIntent().getStringExtra("phone_number");
        phoneNumber = no;



        sendVerificationCode(no);

        verifyBtn = (Button) findViewById(R.id.verifyOTPBtnId);
        resendOtpBtn = findViewById(R.id.resendOtpId);

        countDownTimer = new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                countDownTimerTv.setVisibility(View.VISIBLE);
                resendOtpBtn.setEnabled(false);
                resendOtpBtn.setTextColor(Color.parseColor("#9e9a99"));
                //when tick
                //convert millisecond to minute and second
                //countDownTimerTv.setText(millisUntilFinished/1000 + " sec left");
                String sDuration = String.format(Locale.ENGLISH, "%02d : %02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                        , TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));

                countDownTimerTv.setText(sDuration);
            }

            @Override
            public void onFinish() {
                countDownTimerTv.setVisibility(View.GONE);
                resendOtpBtn.setTextColor(Color.parseColor("#DD4E4E"));
                resendOtpBtn.setEnabled(true);
            }
        };



        countDownTimer.start();



        verifyBtn.setOnClickListener(this);
        resendOtpBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v == verifyBtn)
        {

            String code = otp.getText().toString().trim();
            progressBar.setVisibility(View.VISIBLE);
            if (code.isEmpty() || code.length() < 6) {
                otp.setError("Enter valid code");
                otp.requestFocus();
                return;
            }

            //verifying the code entered manually
            verifyVerificationCode(code);
        }


        else if(v == resendOtpBtn)
        {
            Toast.makeText(getApplicationContext(),"Code again sent  to +88"+no,Toast.LENGTH_SHORT).show();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+88"+no,     // Phone number to verify
                    60  ,               // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    (Activity) TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                    mCallbacks,         // OnVerificationStateChangedCallbacks
                    resendToken);// Force Resending Token from callbacks
            countDownTimer.start();
        }

    }

    private void sendVerificationCode(String no) {
        Toast.makeText(this,"Verification(OTP) code sent to +88"+no,Toast.LENGTH_SHORT).show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+88" + no,
                60,
                TimeUnit.SECONDS,
                (Activity) TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();
            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                otp.setText(code);
                //verifying the code
                progressBar.setVisibility(View.VISIBLE);
                verifyVerificationCode(code);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneNo.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
            resendToken = forceResendingToken;
        }
    };

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyPhoneNo.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            //verify old and new user

                            Query query = databaseReference.orderByChild("phone_number").equalTo("+88" + no);
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    //check until required data get
                                    if (dataSnapshot.exists()) {
                                        //add the username
                                        Toast.makeText(VerifyPhoneNo.this, "Welcome Back!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                    else
                                    {
                                        Toast.makeText(VerifyPhoneNo.this, "Welcome to MediAid!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), SignUp.class));
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                    Toast.makeText(getApplicationContext(), "Failed to load information ", Toast.LENGTH_LONG).show();
                                }
                            });










                        } else {

                            //verification unsuccessful.. display an error message
                            showInfoToUser(task);


                        }
                    }
                });
    }


    private void showInfoToUser(Task<AuthResult> task) {

        //here manage the exceptions and show relevant information to user
        progressBar.setVisibility(View.GONE);

        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
            //invalid phone /otp
            otp.setError("Invalid code.");
            otp.requestFocus();
        }
        else {
            Toast.makeText(getApplicationContext(),"Register is unsuccessful",Toast.LENGTH_LONG).show();
        }

    }


}