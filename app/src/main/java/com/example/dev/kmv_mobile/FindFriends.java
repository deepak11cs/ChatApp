package com.example.dev.kmv_mobile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dev.kmv_mobile.models.User;
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

public class FindFriends extends AppCompatActivity {

    ListView listView;
    Query query;
    DatabaseReference userDatabaseReference;
    FirebaseListAdapter<User> firebaseListAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase.getInstance().getReference().child("UidMapping").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("online_status").setValue(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);
        listView=findViewById(R.id.addfriendslist);
        userDatabaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
        //query = userDatabaseReference.orderByChild("id").startAt(mailid).endAt(mailid + "\uf8ff");
        query=userDatabaseReference;
        firebaseListAdapter=new FirebaseListAdapter<User>(new FirebaseListOptions.Builder<User>().setQuery(query,User.class).setLayout(R.layout.addfriendlistitem).build()) {
            @Override
            protected void populateView(View v, final User model, int position) {

                final TextView tvname=v.findViewById(R.id.friend_name);
                final TextView tvstatus=v.findViewById(R.id.friend_status);
                final TextView tvmailid=v.findViewById(R.id.friend_emailid);
                final ImageView dp=v.findViewById(R.id.friend_dp);
                tvname.setText(model.getName());
                tvmailid.setText(model.getEmail());
                tvstatus.setText(model.getStatus());
                Picasso.with(getApplicationContext()).load(model.getProfilepic_sm()).networkPolicy(NetworkPolicy.OFFLINE).transform(new CircleTransform()).into(dp, new Callback() {
                    @Override
                    public void onSuccess() {
                        //Toast.makeText(getContext(),"Image loaded",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {

                        Picasso.with(getApplicationContext()).load(model.getProfilepic_sm()).transform(new CircleTransform()).into(dp);
                    }
                });
            }
        };
        listView.setAdapter(firebaseListAdapter);
        firebaseListAdapter.startListening();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getApplicationContext(),AddFriend.class);
                intent.putExtra("email",firebaseListAdapter.getItem(i).getEmail());
                intent.putExtra("name",firebaseListAdapter.getItem(i).getName());
                intent.putExtra("status",firebaseListAdapter.getItem(i).getStatus());
                intent.putExtra("image",firebaseListAdapter.getItem(i).getProfilepic_sm());
                startActivity(intent);
                finish();
            }
        });
    }



    @Override
    protected void onPause() {
        super.onPause();

        FirebaseDatabase.getInstance().getReference().child("UidMapping").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("online_status").setValue(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        FirebaseDatabase.getInstance().getReference().child("UidMapping").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("online_status").setValue(false);
    }
}
