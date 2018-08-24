package com.example.dev.kmv_mobile.accountman;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dev.kmv_mobile.MainActivity;
import com.example.dev.kmv_mobile.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class Welcomeuser extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    String email,password;
    EditText eemail,epassword;
    Button bsignin,bsignup;
    ProgressBar progressBar;
    //FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseDatabase db=FirebaseDatabase.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomeuser);

        eemail= findViewById(R.id.email);
        epassword= findViewById(R.id.password);
        bsignin= findViewById(R.id.signin);
        bsignup= findViewById(R.id.signup);
        progressBar=findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        mFirebaseAuth=FirebaseAuth.getInstance();
        mFirebaseUser=mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser!=null){
            Intent intent=new Intent(Welcomeuser.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        else{

            bsignin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    email=eemail.getText().toString().trim();
                    password=epassword.getText().toString();
                    if(TextUtils.isEmpty(email) ||TextUtils.isEmpty(password)){
                        Toast.makeText(getApplicationContext(),"Enter email and password !",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        progressBar.setVisibility(View.VISIBLE);
                        mFirebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    progressBar.setVisibility(View.GONE);
                                    String uid=mFirebaseAuth.getCurrentUser().getUid();
                                    FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(Welcomeuser.this,new OnSuccessListener<InstanceIdResult>() {
                                        @Override
                                        public void onSuccess(InstanceIdResult instanceIdResult) {
                                            String mToken = instanceIdResult.getToken();
                                            String mail=email.replaceAll("[^a-zA-Z0-9]","R0R");
                                            db.getReference().child("Users").child(mail).child("device_token").setValue(mToken);
                                        }
                                    });
                                    //DatabaseReference usersdatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("tokenid");
                                    //usersdatabase.setValue(deviceToken);
                                    Intent intent=new Intent(getApplicationContext(),Welcomeuser.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            });

            bsignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(Welcomeuser.this,Signup.class);
                    startActivity(intent);
                    finish();
                }
            });

        }
    }
}
