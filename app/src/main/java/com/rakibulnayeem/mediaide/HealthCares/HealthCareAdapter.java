package com.rakibulnayeem.mediaide.HealthCares;

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

import java.util.List;

public class HealthCareAdapter extends RecyclerView.Adapter<HealthCareAdapter.MyViewHolder> {

    private Context context;
    private List<AddHealthCareAdapter> adapterList;
  //  private InterstitialAd mInterstitialAd;
    private int click_counter;
    String current_uid;
    DatabaseReference clickDataRef;

    public HealthCareAdapter(Context context, List<AddHealthCareAdapter> adapterList) {
        this.context = context;
        this.adapterList = adapterList;
    }

    @NonNull
    @Override
    public HealthCareAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.health_tips_sample_layout,parent,false);

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
    public void onBindViewHolder(@NonNull HealthCareAdapter.MyViewHolder holder, int position) {

        //using for get data
        final  String title = adapterList.get(position).getTitle();
        final String sub_title = adapterList.get(position).getSub_title();
        final String key = adapterList.get(position).getKey();


        //set data
        holder.titleTv.setText(title);
        holder.subtitleTv.setText(sub_title);


        //handle item click(if clicked on item)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, HealthTipsDescriptionProfile.class);
                intent.putExtra("key",key);
                context.startActivity(intent);

             /*   if (click_counter >=25 )
                {
                    if (mInterstitialAd.isLoaded())
                    {
                        mInterstitialAd.show();
                        click_counter = 0;
                        String click = Integer.toString(click_counter);
                        clickDataRef.child(current_uid).child("counter_child").setValue(click);
                    }
                    else {
                        Intent intent = new Intent(context,HealthTipsDescriptionProfile.class);
                        intent.putExtra("key",key);
                        context.startActivity(intent);
                    }



                }
                else {

                    Intent intent = new Intent(context,HealthTipsDescriptionProfile.class);
                    intent.putExtra("key",key);
                    context.startActivity(intent);

                    // click counter and upload
                    click_counter = click_counter+1;
                    String click = Integer.toString(click_counter);
                    clickDataRef.child(current_uid).child("counter_child").setValue(click);


                }



                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        Intent intent = new Intent(context,HealthTipsDescriptionProfile.class);
                        intent.putExtra("key",key);
                        context.startActivity(intent);
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }

                });


              */

            }
        });

    }

    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titleTv,subtitleTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTv = itemView.findViewById(R.id.titleCardTvId);
            subtitleTv = itemView.findViewById(R.id.subTitleCardTvId);
        }
    }
}