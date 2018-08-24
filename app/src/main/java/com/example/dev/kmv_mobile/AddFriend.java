package com.example.dev.kmv_mobile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dev.kmv_mobile.utility.CircleTransform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AddFriend extends AppCompatActivity {

    CardView mButton;
    TextView mDisplayName,memail;
    TextView mStatus,mAddfriendText;
    ImageView mImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        Intent intent=getIntent();
        final String name=intent.getStringExtra("name");
        String status=intent.getStringExtra("status");
        final String mailid=intent.getStringExtra("email");
        final String image=intent.getStringExtra("image");
        mButton= findViewById(R.id.profile_button);
        memail=findViewById(R.id.profile_email);
        mDisplayName= findViewById(R.id.profile_displayname);
        mStatus= findViewById(R.id.profile_status);
        mImage=findViewById(R.id.profile_image);
        mAddfriendText= findViewById(R.id.textaddfriend);
        Picasso.with(getApplicationContext()).load(image).transform(new CircleTransform()).into(mImage);
        mDisplayName.setText(name);
        memail.setText(mailid);
        mStatus.setText(status);
        if(mailid.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
            mButton.setVisibility(View.INVISIBLE);
            mAddfriendText.setVisibility(View.INVISIBLE);
        }
        else {
            mButton.setVisibility(View.VISIBLE);
            mAddfriendText.setVisibility(View.VISIBLE);
        }
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("ChatHeads").child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("[^a-zA-Z0-9]","R0R")).child(mailid.replaceAll("[^a-zA-Z0-9]","R0R"));
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    mAddfriendText.setText("unfriend");
                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("Requests").child(mailid.replaceAll("[^a-zA-Z0-9]","R0R")).child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("[^a-zA-Z0-9]","R0R")).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                            if (dataSnapshot1.exists()){
                                mAddfriendText.setText("cancel request");
                            }
                            else{
                                mAddfriendText.setText("add friend");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent1=new Intent(getApplicationContext(), ImageViewer.class);
                //intent1.putExtra("uid",uid);
                //startActivity(intent1);
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAddfriendText.getText().toString().equals("add friend")) {
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Requests");
                    mDatabase.child(mailid.replaceAll("[^a-zA-Z0-9]","R0R")).child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("[^a-zA-Z0-9]","R0R")).child("text").setValue("I want to be your friend :)");
                    mAddfriendText.setText("cancel request");
                }
                else if(mAddfriendText.getText().toString().equals("cancel request")){
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Requests");
                    mDatabase.child(mailid.replaceAll("[^a-zA-Z0-9]","R0R")).child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("[^a-zA-Z0-9]","R0R")).child("text").setValue(null);
                    mAddfriendText.setText("add friend");
                }
                else{
                    DatabaseReference mDatabase =FirebaseDatabase.getInstance().getReference().child("ChatHeads").child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("[^a-zA-Z0-9]","R0R")).child(mailid.replaceAll("[^a-zA-Z0-9]","R0R"));
                    FirebaseDatabase.getInstance().getReference().child("ChatHeads").child(mailid.replaceAll("[^a-zA-Z0-9]","R0R")).child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("[^a-zA-Z0-9]","R0R")).setValue(null);
                    mDatabase.setValue(null);
                    mAddfriendText.setText("add friend");
                }
            }
        });

    }
}
