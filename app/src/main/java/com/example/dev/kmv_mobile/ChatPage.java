package com.example.dev.kmv_mobile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatPage extends AppCompatActivity {

    String uid,email,name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);
        Intent intent=getIntent();
        email=intent.getStringExtra("receiver_email");
        FirebaseDatabase.getInstance().getReference().child("Users").child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uid=dataSnapshot.child("uid").getValue().toString();
                name=dataSnapshot.child("name").getValue().toString();
                setTitle(name);
                FirebaseDatabase.getInstance().getReference().child("UidMapping").child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Boolean stat=(Boolean) dataSnapshot.child("online_status").getValue();
                        if(stat){
                            setTitle((Html.fromHtml("<font color=\"#00CC00\">" + name + "</font>")));
                        }
                        else{
                            setTitle((Html.fromHtml("<font color=\"#000000\">" + name + "</font>")));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Toast.makeText(getApplicationContext(),intent.getStringExtra("receiver_email"),Toast.LENGTH_SHORT).show();
    }
}
