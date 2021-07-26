package com.rakibulnayeem.mediaide.Profile;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.rakibulnayeem.mediaide.PhotoUpload;
import com.rakibulnayeem.mediaide.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import static java.lang.System.currentTimeMillis;

public class UpdateProfilePicture extends AppCompatActivity implements View.OnClickListener {

    private Button chooseBtn,saveBtn;
    private ImageView imageView;
    private ProgressBar progressBar;
    private Uri imageUri;

    DatabaseReference databaseReference,dRefDoctor,dataRefAmbulance;
    StorageReference storageReference,sRefDoctor,storageRefAmbulance;
    StorageTask uploadTask;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String uid;
    String name;


    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_picture);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        uid = user.getUid();


        databaseReference = FirebaseDatabase.getInstance().getReference("profile_image");
        storageReference = FirebaseStorage.getInstance().getReference("profile_image").child(uid);

        //doctor profile picture
        dRefDoctor = FirebaseDatabase.getInstance().getReference("doctors_info");
        sRefDoctor = FirebaseStorage.getInstance().getReference("doctors_profile_image").child(uid);

        //set ambulance profile picture
        dataRefAmbulance = FirebaseDatabase.getInstance().getReference("ambulance_info");
        storageRefAmbulance = FirebaseStorage.getInstance().getReference("ambulance_profile_image");


        //get activity name
        name = getIntent().getStringExtra("name");



        chooseBtn =  findViewById(R.id.chooseImageBtnId);
        saveBtn =  findViewById(R.id.saveImageBtnId);
        progressBar = findViewById(R.id.progressBarId);

        imageView = findViewById(R.id.imageViewId);

        chooseBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v == chooseBtn)
        {  //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                { // permission not granted, request it.
                    String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(permissions,PERMISSION_CODE);

                }
                else
                { // permission already  granted
                    pickImageFromGallery();
                }

            }
            else
            { // system os is less than marshmallow
                pickImageFromGallery();
            }


        }

        else if(v == saveBtn)
        {

            if(uploadTask!=null && uploadTask.isInProgress())
            {
                Toast.makeText(getApplicationContext(),"Upload in progress",Toast.LENGTH_SHORT).show();
            }
            else{
                if (name.equals("my_profile_picture"))
                {
                    MyProfilePicture();
                }
                else if (name.equals("doctor_profile_picture"))
                {
                    Doctor_profile_picture();
                }

                else if (name.equals("ambulance_profile_picture"))
                {
                    Ambulance_profile_picture();
                }


            }

        }


    }



    private void pickImageFromGallery() {
        //intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);

    }

    //handle result of runtime permission

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted
                    pickImageFromGallery();
                } else {
                    //permission was denied
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    //handle result of pick image

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data!=null && data.getData()!=null)
        {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(imageView);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



    //get extension of the image
    public String getFileExtension(Uri imageUri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

    //save data
    private void MyProfilePicture() {


        progressBar.setVisibility(View.VISIBLE);
        StorageReference ref = storageReference.child(currentTimeMillis()+"."+getFileExtension(imageUri));

        ref.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){

                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Image is uploaded successfully",Toast.LENGTH_SHORT).show();

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                        while (!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();

                        String key = databaseReference.push().getKey();
                        final PhotoUpload upload = new PhotoUpload(uid,key,downloadUri.toString());




                        databaseReference.child(uid).setValue(upload);
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(getApplicationContext(),"Image upload failed",Toast.LENGTH_SHORT).show();
                    }
                });


    }


    private void Doctor_profile_picture() {
/*
        progressBar.setVisibility(View.VISIBLE);
        StorageReference ref = sRefDoctor.child(currentTimeMillis()+"."+getFileExtension(imageUri));

        ref.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){

                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Image uploaded successfully",Toast.LENGTH_SHORT).show();

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                        while (!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();

                        dRefDoctor.child(uid).child("imageUri").setValue(downloadUri.toString());
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(getApplicationContext(),"Image upload failed",Toast.LENGTH_SHORT).show();
                    }
                });

        */



        try {

            final StorageReference ref = sRefDoctor.child(uid + "." + getFileExtension(imageUri));
            final ProgressDialog progressDialog = new ProgressDialog(UpdateProfilePicture.this);
            progressDialog.setMessage("Uploading");
            progressDialog.show();



            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap bitmap = imageView.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = ref.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Image upload failed",Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Toast.makeText(getApplicationContext(),"Image uploaded successfully",Toast.LENGTH_SHORT).show();

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                    while (!uriTask.isSuccessful());
                    Uri downloadUri = uriTask.getResult();
                    dRefDoctor.child(uid).child("imageUri").setValue(downloadUri.toString());
                    progressDialog.dismiss();
                    finish();
                }
            });


        }catch (Exception e)
        {
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(),"User information updated successfully",Toast.LENGTH_SHORT).show();
        }
    }



    private void Ambulance_profile_picture() {

        progressBar.setVisibility(View.VISIBLE);
        StorageReference ref = storageRefAmbulance.child(currentTimeMillis()+"."+getFileExtension(imageUri));

        ref.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){

                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Image uploaded successfully",Toast.LENGTH_SHORT).show();

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                        while (!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();

                        dataRefAmbulance.child(uid).child("imageUri").setValue(downloadUri.toString());
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Toast.makeText(getApplicationContext(),"Image upload failed",Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
