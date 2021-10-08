package com.rakibulnayeem.mediaide.Posts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakibulnayeem.mediaide.R;

import java.util.ArrayList;
import java.util.List;

public class RequestFragment extends Fragment {

    private RecyclerView recyclerView;
    RequestAdapter requestAdapter;
    private List<PostAdapter> adapterList;
    DatabaseReference databaseReference;
    private ProgressBar progressBar;

    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request, container, false);

        recyclerView = view.findViewById(R.id.usersRecycleViewId);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        progressBar = view.findViewById(R.id.recyclerProgressBarId);

        adapterList = new ArrayList<>();

        getAllRequest();


        return view;
    }


    private void getAllRequest() {

        //get all request
          progressBar.setVisibility(View.VISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference("posts");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {

                    PostAdapter upInfo = dataSnapshot1.getValue(PostAdapter.class);

                    adapterList.add(upInfo);
                }

                requestAdapter = new RequestAdapter(getContext(), adapterList);

                progressBar.setVisibility(View.GONE);
                recyclerView.setAdapter(requestAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getContext(),"Error : "+databaseError.getMessage(),Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }



}
