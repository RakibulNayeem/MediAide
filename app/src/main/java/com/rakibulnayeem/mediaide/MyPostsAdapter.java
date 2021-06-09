package com.rakibulnayeem.mediaide;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MyPostsAdapter extends RecyclerView.Adapter<MyPostsAdapter.MyViewHolder> {
    private Context context;
    private List<PostAdapter> adapterList;
    InternetConnection internetConnection;

    public MyPostsAdapter(Context context, List<PostAdapter> adapterList) {
        this.context = context;
        this.adapterList = adapterList;
    }

    @NonNull
    @Override
    public MyPostsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.my_blood_request_sample_layout,parent,false);
        internetConnection = new InternetConnection(context);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //using for get data
        final  String post_key = adapterList.get(position).getKey();

        final String blood_group = adapterList.get(position).getBlood_group();
        final String hospital_name = adapterList.get(position).getHospital_name();
        final String details = adapterList.get(position).getDetails();
        final String zilla = adapterList.get(position).getZilla();
        final String phone_number = adapterList.get(position).getPhone_number();
        final String date_time = adapterList.get(position).getDate_time();
        final String current_time = adapterList.get(position).getCurrent_time();


        holder.bloodGroupTv.setText(blood_group);
        holder.hospitalNameTv.setText(hospital_name);
        holder.detailsTv.setText(details);
        holder.zillaTv.setText(zilla);
        holder.phoneNumberTv.setText(phone_number);
        holder.dateTimeTv.setText(date_time);
        holder.currenTimeTv.setText(current_time);

        holder.dotsIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             EditDeleteDialog(post_key);

            }
        });

    }




    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView hospitalNameTv,detailsTv,zillaTv,phoneNumberTv,dateTimeTv,bloodGroupTv,currenTimeTv;
        ImageView dotsIv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            bloodGroupTv = itemView.findViewById(R.id.bloodGroupCardTvId);
            hospitalNameTv = itemView.findViewById(R.id.hospitalNameCardTvId);
            detailsTv = itemView.findViewById(R.id.detailsCardTvId);
            zillaTv = itemView.findViewById(R.id.zillaCardTvId);
            phoneNumberTv = itemView.findViewById(R.id.phoneNumberCardTvId);
            dateTimeTv = itemView.findViewById(R.id.dateTimeCardTvId);
            dotsIv = itemView.findViewById(R.id.dotsIvId);
            currenTimeTv = itemView.findViewById(R.id.currentTimeCardTvId);

        }
    }



    private void EditDeleteDialog(final String post_key) {

        //AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose option:");
        builder.setMessage("What are you want to do?");


        //set layout linear layout
        LinearLayout linearLayout = new LinearLayout(context);

        //buttons recover
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(internetConnection.checkConnection())
                {

                    DeleteDialog(post_key);
                }
                else
                {
                    // Not Available...
                    Toast.makeText(context,"No Internet Connection",Toast.LENGTH_LONG).show();
                }

            }
        });


        //buttons edit

        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //edit dialog
                if(internetConnection.checkConnection())
                {

                    Intent intent = new Intent(context, PostEdit.class);
                    intent.putExtra("post_key", post_key);
                    context.startActivity(intent);
                }
                else
                {
                    // Not Available...
                    Toast.makeText(context,"No Internet Connection",Toast.LENGTH_LONG).show();
                }

            }
        });

        //show dialog
        builder.create().show();

    }

    private void DeleteDialog(final String post_key) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Post?");
        builder.setMessage("Are you sure to delete blood request post?");


        //set layout linear layout
        LinearLayout linearLayout = new LinearLayout(context);

        //buttons recover
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(internetConnection.checkConnection())
                {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("posts");

                    Query query = databaseReference.orderByChild("key").equalTo(post_key);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()){
                                ds.getRef().removeValue();
                            }
                            Toast.makeText(context, "Post deleted",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(context,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    // Not Available...
                    Toast.makeText(context,"No Internet Connection",Toast.LENGTH_LONG).show();
                }

            }
        });


        //buttons cancel
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dismiss dialog
                dialog.dismiss();

            }
        });

        //show dialog
        builder.create().show();

    }


}