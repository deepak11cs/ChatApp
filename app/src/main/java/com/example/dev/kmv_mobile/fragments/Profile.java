package com.example.dev.kmv_mobile.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dev.kmv_mobile.R;
import com.example.dev.kmv_mobile.models.ChatHead;
import com.example.dev.kmv_mobile.models.Request;
import com.example.dev.kmv_mobile.utility.CircleTransform;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {

    ListView listView;
    Query query;
    FirebaseListAdapter<Request> firebaseListAdapter;
    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
        listView=view.findViewById(R.id.friendrequestlist);
        query= FirebaseDatabase.getInstance().getReference().child("Requests").child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("[^a-zA-Z0-9]","R0R"));
        firebaseListAdapter=new FirebaseListAdapter<Request>(new FirebaseListOptions.Builder<Request>().setQuery(query,Request.class).setLayout(R.layout.requestlistitem).build()) {
            @Override
            protected void populateView(final View v, final Request model, final int position) {

                final String sendermail=getRef(position).getKey();
                //Toast.makeText(getContext(),sendermail,Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference().child("Users").child(sendermail).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        TextView tvname=v.findViewById(R.id.request_name);
                        TextView tvemail=v.findViewById(R.id.request_mailid);
                        TextView tvmessage=v.findViewById(R.id.request_message);
                        ImageView imageView=v.findViewById(R.id.request_dp);
                        Button accept=v.findViewById(R.id.request_accept);
                        Button decline=v.findViewById(R.id.request_decline);
                        String name=dataSnapshot.child("name").getValue().toString();
                        tvname.setText(name);
                        String email=dataSnapshot.child("email").getValue().toString();
                        tvemail.setText(email);
                        tvmessage.setText(model.getMesssage());
                        Picasso.with(getContext()).load(dataSnapshot.child("profilepic_sm").getValue().toString()).transform(new CircleTransform()).into(imageView);
                        accept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ChatHead chatHead=new ChatHead();
                                chatHead.setFriendshipdate("date here");
                                chatHead.setTime("time ");
                                chatHead.setLast_msg("say hi ...");
                                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("ChatHeads");
                                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("[^a-zA-Z0-9]","R0R")).child(sendermail).setValue(chatHead);
                                databaseReference.child(sendermail).child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("[^a-zA-Z0-9]","R0R")).setValue(chatHead);
                                FirebaseDatabase.getInstance().getReference().child("Requests").child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("[^a-zA-Z0-9]","R0R")).child(sendermail).setValue(null);
                            }
                        });
                        decline.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FirebaseDatabase.getInstance().getReference().child("Requests").child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("[^a-zA-Z0-9]","R0R")).child(sendermail).setValue(null);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        };
        listView.setAdapter(firebaseListAdapter);
        firebaseListAdapter.startListening();
        return view;
    }

}
