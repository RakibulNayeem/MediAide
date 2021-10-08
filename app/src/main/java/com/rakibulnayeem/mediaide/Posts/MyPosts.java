package com.rakibulnayeem.mediaide.Posts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakibulnayeem.mediaide.MainActivity;
import com.rakibulnayeem.mediaide.R;

import java.util.ArrayList;
import java.util.List;

public class MyPosts extends AppCompatActivity {

    private RecyclerView recyclerView;
    MyPostsAdapter adapter;
    private List<PostAdapter> adapterList;
    DatabaseReference databaseReference;
    private ProgressBar progressBar;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);
        this.setTitle(getString(R.string.my_posts));

        //adding back button to the tool bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        recyclerView = findViewById(R.id.usersRecycleViewId);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
       // recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.recyclerProgressBarId);

        adapterList = new ArrayList<>();

        getAllPosts();

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            this.finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }


    private void getAllPosts() {

        //get all post


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapterList.clear();
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {

                    PostAdapter upInfo = dataSnapshot1.getValue(PostAdapter.class);

                    if (upInfo.getUid().equals(uid))
                    {
                        adapterList.add(upInfo);
                    }
                }

                adapter  = new MyPostsAdapter(MyPosts.this, adapterList);

                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(),"Failed to load information"+databaseError.getMessage(),Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }


}
