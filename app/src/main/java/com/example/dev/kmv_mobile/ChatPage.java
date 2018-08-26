package com.example.dev.kmv_mobile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dev.kmv_mobile.models.Message;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;

public class ChatPage extends AppCompatActivity {

    String uid,receiver_email,name,sender_email;
    ImageButton mSendButton;
    EmojiconEditText messageet;
    ListView chatList;
    EmojIconActions emojIconActions;
    FirebaseListAdapter<Message> mListAdapter;
    TextView messagetext;
    TextView messagetime;
    LinearLayout activity_chatpage;
    DatabaseReference notificationdatabase;
    DatabaseReference messageDatabase;
    DatabaseReference reference;
    DatabaseReference chatsdatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);
        Intent intent=getIntent();
        receiver_email=intent.getStringExtra("receiver_email");
        sender_email=FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("[^a-zA-Z0-9]","R0R");
        FirebaseDatabase.getInstance().getReference().child("Users").child(receiver_email).addListenerForSingleValueEvent(new ValueEventListener() {
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

        //Toast.makeText(getApplicationContext(),intent.getStringExtra("receiver_email"),Toast.LENGTH_SHORT).show();


        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(messageet.getText())) {
                    //do nothing
                }
                else {
                    String message = messageet.getText().toString();
                    messageet.setText("");

                    final String time = DateFormat.getDateTimeInstance().format(new Date());




                        final Message msg = new Message(message, sender_email, time);
                        messageDatabase = reference.child("Messages");
                        messageDatabase.child(receiver_email).child(sender_email).push().setValue(msg);
                        messageDatabase.child(sender_email).child(receiver_email).push().setValue(msg);




                        //initialise the database





                        HashMap<String, String> notificationdata = new HashMap<>();
                        notificationdata.put("from", sender);
                        notificationdata.put("type", message);
                        notificationdatabase.child(receiver).child(sender).setValue(notificationdata);
                        chatsdatabase.child(sender).child(receiver).child("lastmsg").setValue(msg.getMsg());
                        chatsdatabase.child(sender).child(receiver).child("lasttime").setValue(time);
                        chatsdatabase.child(receiver).child(sender).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                final Chats chats = new Chats();
                                chats.setLastmsg(msg.getMsg());
                                chats.setLasttime(time);
                                chats.setChatid(sender);
                                chats.setType("single");
                                FirebaseDatabase.getInstance().getReference().child("Users").child(sender).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        chats.setChatimageurl(dataSnapshot.child("smallSizeImage").getValue().toString());
                                        chats.setChatname(dataSnapshot.child("name").getValue().toString());
                                        FirebaseDatabase.getInstance().getReference().child("Chats").child(receiver).child(sender).setValue(chats);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                }

            }
        });

    }
}
