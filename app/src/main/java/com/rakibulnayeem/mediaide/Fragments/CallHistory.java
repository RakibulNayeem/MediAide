package com.rakibulnayeem.mediaide.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rakibulnayeem.mediaide.Posts.PostAdapter;
import com.rakibulnayeem.mediaide.Posts.RequestAdapter;
import com.rakibulnayeem.mediaide.R;

import java.util.ArrayList;
import java.util.List;


public class CallHistory extends Fragment {
    private RecyclerView recyclerView;
    CallHistoryAdapter adapter;
    private List<UploadCallHistoryAdapter> adapterList;
    DatabaseReference databaseReference;
    private ProgressBar progressBar;
    String uid;


    public CallHistory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_call_history, container, false);

        recyclerView = view.findViewById(R.id.usersRecycleViewId);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        progressBar = view.findViewById(R.id.recyclerProgressBarId);

        adapterList = new ArrayList<>();

        getAllRequest();

        return view;

    }


    private void getAllRequest() {

        //get all request
        progressBar.setVisibility(View.VISIBLE);
        databaseReference = FirebaseDatabase.getInstance().getReference("call_history").child(uid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapterList.clear();
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {

                    UploadCallHistoryAdapter upInfo = dataSnapshot1.getValue(UploadCallHistoryAdapter.class);

                    adapterList.add(upInfo);
                }

                adapter = new CallHistoryAdapter(getContext(), adapterList);

                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getContext(),"Error : "+databaseError.getMessage(),Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

}