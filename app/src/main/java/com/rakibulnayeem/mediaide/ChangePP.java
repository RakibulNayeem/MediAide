package com.rakibulnayeem.mediaide;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
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
import com.google.firebase.storage.UploadTask;
import com.rakibulnayeem.mediaide.Profile.ProfileFragment;
import com.squareup.picasso.Picasso;

import static java.lang.System.currentTimeMillis;

public class ChangePP extends AppCompatActivity implements View.OnClickListener {

    private Button chooseImageBtn,saveDataBtn;
    private ImageView imageView;
    private ProgressBar progressBar;

    DatabaseReference databaseReference;
    StorageReference storageReference;
    private FirebaseAuth mAuth;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_p_p);
        this.setTitle("Change Photo");
        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Image");
        storageReference = FirebaseStorage.getInstance().getReference("Image");

        chooseImageBtn = findViewById(R.id.chooseImageBtnId);
        saveDataBtn = findViewById(R.id.saveButtonId);
        progressBar = findViewById(R.id.progressbarId);

        imageView = findViewById(R.id.imageViewId);
        chooseImageBtn.setOnClickListener(this);
        saveDataBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if (v == chooseImageBtn)
        {
            openFileChooser();
        }

        else if( v == saveDataBtn)
        {
            saveImage();
        }


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void openFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData()!=null)
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



    private void saveImage() {
        FirebaseUser user  = mAuth.getCurrentUser();
        final String uid = user.getUid();
        final  String email = user.getEmail();




        StorageReference ref = storageReference.child(currentTimeMillis()+"."+getFileExtension(imageUri));

        progressBar.setVisibility(View.VISIBLE);

        ref.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(getApplicationContext(),"Image upload successful",Toast.LENGTH_SHORT).show();

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                        while (!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();

                        PPUpload ppUpload = new PPUpload(email,downloadUri.toString());
                        databaseReference.child(uid).setValue(ppUpload);

                        finish();
                        startActivity(new Intent(getApplicationContext(), ProfileFragment.class));


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(getApplicationContext(),"Image upload unsuccessful",Toast.LENGTH_LONG).show();
                    }
                });

    }


}
