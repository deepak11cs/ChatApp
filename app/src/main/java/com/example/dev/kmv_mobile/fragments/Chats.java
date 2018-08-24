package com.example.dev.kmv_mobile.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dev.kmv_mobile.ChatPage;
import com.example.dev.kmv_mobile.FindFriends;
import com.example.dev.kmv_mobile.R;
import com.example.dev.kmv_mobile.models.ChatHead;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class Chats extends Fragment {

    DatabaseReference chatheadreference;
    ListView listView;
    Button button;
    Query query;

    FirebaseListAdapter<ChatHead> firebaseListAdapter;

    public Chats() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view= inflater.inflate(R.layout.fragment_chats, container, false);
        chatheadreference= FirebaseDatabase.getInstance().getReference().child("ChatHeads").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        chatheadreference.keepSynced(true);
        //query=FirebaseDatabase.getInstance().getReference().child("ChatHeads").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query=FirebaseDatabase.getInstance().getReference().child("ChatHeads").child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("[^a-zA-Z0-9]","R0R"));
        listView=view.findViewById(R.id.chatslistview);
        button=view.findViewById(R.id.btnfindfriends);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), FindFriends.class);
                startActivity(intent);
            }
        });
        firebaseListAdapter=new FirebaseListAdapter<ChatHead>(new FirebaseListOptions.Builder<ChatHead>().setQuery(query,ChatHead.class).setLayout(R.layout.chatlistitem).build()) {
            @Override
            protected void populateView(View v, final ChatHead model, final int position) {
                final TextView tvname=v.findViewById(R.id.chats_name);
                final TextView tvmail=v.findViewById(R.id.intent_email);
                final TextView tvlastmsg=v.findViewById(R.id.chats_last_message);
                final TextView tvlastmsgtime=v.findViewById(R.id.chats_last_time);
                final ImageView dp=v.findViewById(R.id.chats_dp);
                FirebaseDatabase.getInstance().getReference().child("Users").child(getRef(position).getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String name=dataSnapshot.child("name").getValue().toString();
                        final String image=dataSnapshot.child("profilepic_sm").getValue().toString();
                        tvname.setText(name);
                        tvmail.setText(getRef(position).getKey());
                        tvlastmsg.setText(model.getLast_msg());
                        tvlastmsgtime.setText(model.getTime());
                        Picasso.with(getContext()).load(image).networkPolicy(NetworkPolicy.OFFLINE).transform(new CircleTransform()).into(dp, new Callback() {
                            @Override
                            public void onSuccess() {
                                //Toast.makeText(getContext(),"Image loaded",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError() {

                                Picasso.with(getContext()).load(image).transform(new CircleTransform()).into(dp);
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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //new activity chat page
                Intent intent=new Intent(getContext(), ChatPage.class);
                TextView rmail=listView.getChildAt(i).findViewById(R.id.intent_email);
                String rcmail=rmail.getText().toString();
                intent.putExtra("receiver_email",rcmail);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //firebaseListAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        //firebaseListAdapter.stopListening();
    }
}
