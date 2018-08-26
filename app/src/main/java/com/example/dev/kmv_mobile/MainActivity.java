package com.example.dev.kmv_mobile;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.dev.kmv_mobile.fragments.ChatsHolder;
import com.example.dev.kmv_mobile.fragments.StoreHolder;
import com.example.dev.kmv_mobile.fragments.UpdatesHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    DatabaseReference uidmapreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toast.makeText(getApplicationContext(), FirebaseAuth.getInstance().getCurrentUser().getEmail(),Toast.LENGTH_SHORT).show();
        viewPager=findViewById(R.id.viewpager);

        uidmapreference= FirebaseDatabase.getInstance().getReference().child("UidMapping").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        tabLayout =findViewById(R.id.tablayout);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch(position){
                    case 0:
                        return new ChatsHolder();
                    case 1:
                        return new UpdatesHolder();
                    case 2:
                        return new StoreHolder();
                    default: return null;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

        tabLayout.setupWithViewPager(viewPager,true);
        tabLayout.getTabAt(1).select();
        tabLayout.getTabAt(0).setText("Chats");
        tabLayout.getTabAt(1).setText("Updates");
        tabLayout.getTabAt(2).setText("Store");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uidmapreference.child("online_status").setValue(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        uidmapreference.child("online_status").setValue(true);

    }


    @Override
    protected void onPause() {
        super.onPause();
        uidmapreference.child("online_status").setValue(false);
    }
}
