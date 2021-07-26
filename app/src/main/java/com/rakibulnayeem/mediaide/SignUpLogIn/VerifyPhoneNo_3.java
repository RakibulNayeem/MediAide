package com.rakibulnayeem.mediaide.SignUpLogIn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
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

public class VerifyPhoneNo_3 extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "PhoneAuthActivity";


    EditText otp;
    TextView resendOtpBtn, countDownTimerTv;
    CountDownTimer countDownTimer;
    Button verifyBtn;
    String no;
    String phoneNumber;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ProgressBar progressBar;
    DatabaseReference databaseReference;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone_no3);
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
        verifyBtn = (Button) findViewById(R.id.verifyOTPBtnId);
        resendOtpBtn = findViewById(R.id.resendOtpId);

        //send verification code
        startPhoneNumberVerification(phoneNumber);

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
            if (code.isEmpty() || code.length() < 6) {
                otp.setError("Enter valid code");
                otp.requestFocus();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            //verifying the code entered manually
            verifyPhoneNumberWithCode(code);
        }


        else if(v == resendOtpBtn)
        {
            Toast.makeText(getApplicationContext(),"Code again sent  to +88"+no,Toast.LENGTH_SHORT).show();
            resendVerificationCode(no, mResendToken);

            countDownTimer.start();
        }

    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    //send verification code
    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+88"+phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        // [END start_phone_auth]
    }
           //verify verification code
    private void verifyPhoneNumberWithCode(String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
       //signing the user
        signInWithPhoneAuthCredential(credential);
        // [END verify_with_code]
    }
  /*  private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

   */


    // Initialize phone auth callbacks
    // [START phone_auth_callbacks]
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks   mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:" + credential);

            //Getting the code sent by SMS
            String code = credential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                otp.setText(code);
                //verifying the code
                progressBar.setVisibility(View.VISIBLE);
                verifyPhoneNumberWithCode(code);
            }

           // signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Toast.makeText(VerifyPhoneNo_3.this, e.getMessage(), Toast.LENGTH_LONG).show();

            // Show a message and update the UI
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
           // Log.d(TAG, "onCodeSent:" + verificationId);

            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;
            mResendToken = token;
        }
    };
    // [END phone_auth_callbacks]




    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+88"+phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    // [END resend_verification]

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
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
                                        Toast.makeText(VerifyPhoneNo_3.this, "Welcome Back!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);

                                    }
                                    else
                                    {
                                        Toast.makeText(VerifyPhoneNo_3.this, "Welcome to MediAid!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), SignUp.class));
                                    }
                                    finish();
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
    // [END sign_in_with_phone]

    private void updateUI(FirebaseUser user) {

    }

    private void showInfoToUser(Task<AuthResult> task) {

        //here manage the exceptions and show relevant information to user
        progressBar.setVisibility(View.GONE);

        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
            //invalid phone /otp
            otp.setError("Invalid code.");
            otp.requestFocus();
            return;
        }
        else {
            Toast.makeText(getApplicationContext(),"Register is unsuccessful",Toast.LENGTH_LONG).show();
        }

    }


}