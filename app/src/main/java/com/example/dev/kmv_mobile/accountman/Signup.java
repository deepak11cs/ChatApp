package com.example.dev.kmv_mobile.accountman;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dev.kmv_mobile.R;
import com.example.dev.kmv_mobile.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class Signup extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    Button bsignin,bsignup;
    EditText eemail,epassword,ename,econfirmpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //mFirebaseAuth=FirebaseAuth.getInstance();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mDatabaseReference=mFirebaseDatabase.getReference().child("Users");
        bsignup=findViewById(R.id.bsignup);
        bsignin=findViewById(R.id.bsignin);
        eemail=findViewById(R.id.eemail);
        ename=findViewById(R.id.ename);
        epassword=findViewById(R.id.epassword);
        econfirmpassword=findViewById(R.id.econfirmpassword);
        bsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Signup.this,Welcomeuser.class);
                startActivity(intent);
                finish();
            }
        });
        bsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password=epassword.getText().toString();
                String confirmPassword=econfirmpassword.getText().toString();
                if(!password.equals(confirmPassword)){
                    Toast.makeText(getApplicationContext(),"your passwords don't match please enter again !",Toast.LENGTH_LONG).show();
                }
                else{
                    final String email=eemail.getText().toString().trim().toLowerCase();
                    final String name=ename.getText().toString().trim();
                    mFirebaseAuth=FirebaseAuth.getInstance();
                    mFirebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                final String uid=mFirebaseAuth.getCurrentUser().getUid();

                                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(Signup.this,new OnSuccessListener<InstanceIdResult>() {
                                    @Override
                                    public void onSuccess(InstanceIdResult instanceIdResult) {
                                        String mToken = instanceIdResult.getToken();
                                        User user=new User(name,email,uid);
                                        user.setDevice_token(mToken);
                                        user.setProfilepic_lg("https://firebasestorage.googleapis.com/v0/b/kmv-mobile.appspot.com/o/default_dp.jpg?alt=media&token=f783e415-ae9c-4991-9a52-02e2bc1cb818");
                                        user.setProfilepic_sm("https://firebasestorage.googleapis.com/v0/b/kmv-mobile.appspot.com/o/default_dp.jpg?alt=media&token=f783e415-ae9c-4991-9a52-02e2bc1cb818");
                                        user.setStatus("Don't shy, say hi");
                                        user.setStore_points("100");
                                        //String mail=email.replace('@','_');
                                        String mail=email.replaceAll("[^a-zA-Z0-9]","R0R");
                                        //Toast.makeText(getApplicationContext(),mail,Toast.LENGTH_SHORT).show();
                                        mDatabaseReference.child(mail).setValue(user);
                                        mDatabaseReference=mFirebaseDatabase.getReference().child("UidMapping");
                                        mDatabaseReference.child(uid).child("email").setValue(mail);
                                        mDatabaseReference.child(uid).child("online_status").setValue(false);
                                        Intent intent=new Intent(Signup.this,Welcomeuser.class);
                                        startActivity(intent);
                                        finish();
                                        //db.getReference().child("Users").child(email).child("device_token").setValue(mToken);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Unable to get device token Try signing up again !", Toast.LENGTH_SHORT).show();
                                        mFirebaseAuth.getCurrentUser().delete();
                                    }
                                });
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
