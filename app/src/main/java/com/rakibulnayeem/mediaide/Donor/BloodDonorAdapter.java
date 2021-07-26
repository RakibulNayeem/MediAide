package com.rakibulnayeem.mediaide.Donor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.rakibulnayeem.mediaide.R;
import com.rakibulnayeem.mediaide.SignUpLogIn.SignUpAdapter;
import com.rakibulnayeem.mediaide.Profile.UsersProfile;

import java.util.List;

public class BloodDonorAdapter extends RecyclerView.Adapter<BloodDonorAdapter.MyViewHolder> {


    private Context context;
    private List<SignUpAdapter> uploadList;

 //   private InterstitialAd mInterstitialAd;
    private int click_counter;
    String current_uid;
    DatabaseReference clickDataRef;

    public BloodDonorAdapter(Context context, List<SignUpAdapter> uploadList) {
        this.context = context;
        this.uploadList = uploadList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.users_row_sample_view,parent,false);

     /*   current_uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //click counter
        clickDataRef = FirebaseDatabase.getInstance().getReference("item_click_counter");

        Query query = clickDataRef.orderByChild("uid").equalTo(current_uid);
         query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String click= ""+dataSnapshot.child("counter_child").getValue();
                try {
                    click_counter = Integer.parseInt(click);
                } catch(NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,"Failed to load info",Toast.LENGTH_SHORT).show();
            }
        });

        // interstitial ad
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


      */
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //using for get data

        final String uid = uploadList.get(position).getUid();
        final String village = uploadList.get(position).getVillage();
      final  String blood_group = uploadList.get(position).getBlood_group();
      final String upazila = uploadList.get(position).getUpazila();
      final  String last_donation_date = uploadList.get(position).getLast_donation_date();

        //set data
        holder.bloodGroupTv.setText(blood_group);
        holder.upazilaTv.setText(upazila);
        holder.villageTv.setText(village);
        holder.lastDDTv.setText(last_donation_date);



        //handle item click(if clicked on item)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UsersProfile.class);
                intent.putExtra("uid",uid);
                context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return uploadList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView bloodGroupTv,upazilaTv,villageTv,lastDDTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            bloodGroupTv = itemView.findViewById(R.id.bloodGroupCardTvId);
            upazilaTv = itemView.findViewById(R.id.upazilaCardTvId);
            villageTv = itemView.findViewById(R.id.villageCardTvId);
            lastDDTv = itemView.findViewById(R.id.lastDDCardTvId);

        }
    }
}