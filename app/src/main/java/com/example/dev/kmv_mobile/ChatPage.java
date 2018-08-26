package com.example.dev.kmv_mobile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dev.kmv_mobile.models.Message;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;

public class ChatPage extends AppCompatActivity {

    String uid, receiver_email, name, sender_email;
    ImageButton mSendButton, emojiButton;
    EmojiconEditText messageet;
    ListView chatList;
    EmojIconActions emojIconActions;
    FirebaseListAdapter<Message> mListAdapter;
    TextView messagetext;
    TextView messagetime;
    LinearLayout activity_chatpage;
    DatabaseReference notificationdatabase;
    DatabaseReference messageDatabase;
    Query mQuery;
    //DatabaseReference reference;
    DatabaseReference chatsdatabase;


    @Override
    protected void onResume() {
        super.onResume();

        FirebaseDatabase.getInstance().getReference().child("UidMapping").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("online_status").setValue(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);
        mSendButton = findViewById(R.id.imgsendbutton);
        chatList = findViewById(R.id.chat_page_list);
        emojiButton = findViewById(R.id.emojibutton);
        messageet = findViewById(R.id.messageedittext);
        activity_chatpage = findViewById(R.id.chatpagelinearlayout);
        emojIconActions = new EmojIconActions(getApplicationContext(), activity_chatpage, messageet, emojiButton);
        emojIconActions.ShowEmojIcon();
        Intent intent = getIntent();
        receiver_email = intent.getStringExtra("receiver_email");
        messageDatabase = FirebaseDatabase.getInstance().getReference();
        messageDatabase = messageDatabase.child("Messages");
        chatsdatabase = FirebaseDatabase.getInstance().getReference();
        chatsdatabase = chatsdatabase.child("ChatHeads");
        sender_email = FirebaseAuth.getInstance().getCurrentUser().getEmail().replaceAll("[^a-zA-Z0-9]", "R0R");
        FirebaseDatabase.getInstance().getReference().child("Users").child(receiver_email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uid = dataSnapshot.child("uid").getValue().toString();
                name = dataSnapshot.child("name").getValue().toString();
                setTitle(name);
                FirebaseDatabase.getInstance().getReference().child("UidMapping").child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Boolean stat = (Boolean) dataSnapshot.child("online_status").getValue();
                        if (stat) {
                            setTitle((Html.fromHtml("<font color=\"#00CC00\">" + name + "</font>")));
                        } else {
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


        chatsdatabase.keepSynced(true);
        chatsdatabase.child(sender_email).child(receiver_email).keepSynced(true);

        mQuery = FirebaseDatabase.getInstance().getReference().child("Messages").child(sender_email).child(receiver_email);
        mListAdapter = new FirebaseListAdapter<Message>(
                new FirebaseListOptions.Builder<Message>().setQuery(mQuery, Message.class).setLayout(R.layout.messagelistitem).build()
        ) {
            @Override
            protected void populateView(View v, Message model, int position) {

                messagetext = v.findViewById(R.id.messageboxtext);
                messagetime = v.findViewById(R.id.messageboxtime);
                messagetext.setText(model.getMsg());
                messagetime.setText(model.getTime());
                if (model.getSender().equals(sender_email)) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    messagetext.setBackgroundResource(R.drawable.in_message_bg);
                    params.gravity = Gravity.RIGHT;
                    messagetext.setLayoutParams(params);
                    messagetime.setLayoutParams(params);

                } else {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
                    messagetext.setBackgroundResource(R.drawable.out_message_bg);
                    params.gravity = Gravity.LEFT;
                    messagetext.setLayoutParams(params);
                    messagetime.setLayoutParams(params);

                }

            }
        };
        chatList.setAdapter(mListAdapter);
        mListAdapter.startListening();

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(messageet.getText())) {
                    //do nothing
                } else {
                    String message = messageet.getText().toString();
                    messageet.setText("");

                    final String time = DateFormat.getDateTimeInstance().format(new Date());


                    final Message msg = new Message(message, sender_email, time);

                    messageDatabase.child(receiver_email).child(sender_email).push().setValue(msg);
                    messageDatabase.child(sender_email).child(receiver_email).push().setValue(msg);

                    //HashMap<String, String> notificationdata = new HashMap<>();
                    //notificationdata.put("from", sender);
                    //notificationdata.put("type", message);
                    //notificationdatabase.child(receiver).child(sender).setValue(notificationdata);
                    chatsdatabase.child(sender_email).child(receiver_email).child("last_msg").setValue(msg.getMsg());
                    chatsdatabase.child(sender_email).child(receiver_email).child("time").setValue(time);
                    chatsdatabase.child(receiver_email).child(sender_email).child("last_msg").setValue(msg.getMsg());
                    chatsdatabase.child(receiver_email).child(sender_email).child("time").setValue(time);


                }

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        FirebaseDatabase.getInstance().getReference().child("UidMapping").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("online_status").setValue(false);
    }
}
