package com.rakibulnayeem.mediaide.fcmnoti;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class NotifyMembers {

    //remove comment

    //AZ

    //Important


//    public static void sendNotification(String title, String message, String roomid, String classid, String day, Context context, Activity activity)
//    {
//        try {
//            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Rooms").child(roomid).child("Schedule").child(day).child(classid);
//            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    Classs classs = snapshot.getValue(Classs.class);
//                    ArrayList<String> liste = new ArrayList<>();
//                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Rooms").child(roomid).child("Members");
//                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            for (DataSnapshot dataSnapshot:snapshot.getChildren()
//                            ) {
//                                Member member = dataSnapshot.getValue(Member.class);
//                                assert member != null;
//                                if (!member.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
//                                {
//                                    if (!member.getRole().equals("teacher"))
//                                    {
//                                        liste.add(member.getId());
//                                    }
//                                    else {
//                                        assert classs != null;
//                                        if (member.getEmail().equals(classs.getClassteacheremail())) {
//                                            liste.add(member.getId());
//                                        }
//                                    }
//                                }
//                            }
//                            String[] lists = new String[liste.size()];
//                            for (int i = 0; i < liste.size(); i++) {
//                                lists[i] = liste.get(i);
//                            }
//
//                            for (String userid:lists
//                            ) {
//                                try{
//                                    FirebaseDatabase.getInstance().getReference().child("Tokens").child(userid).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
//                                        @Override
//                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                            try
//                                            {
//                                                String usertoken=dataSnapshot.getValue(String.class);
//                                                FcmNotificationsSender notificationsSender = new FcmNotificationsSender(usertoken,title,message,context,activity);
//                                                notificationsSender.SendNotifications();
//                                            }
//                                            catch (Exception ignored){}
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                        }
//                                    });
//                                }catch (Exception ignored){}
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }catch (Exception ignored){}
//    }

    static public void UpdateToken(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        FirebaseMessaging.getInstance().getToken()
                                .addOnCompleteListener(new OnCompleteListener<String>() {
                                    @Override
                                    public void onComplete(@NonNull Task<String> task) {
                                        if (!task.isSuccessful()) {
                                            return;
                                        }

                                        // Get new FCM registration token
                                        String refreshtoken = task.getResult();
                                        Token token = new Token(refreshtoken);
                                        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
                                    }
                                });
    }
}
