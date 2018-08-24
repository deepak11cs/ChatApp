package com.example.dev.kmv_mobile.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.dev.kmv_mobile.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsHolder extends Fragment {


    public ChatsHolder() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chats_holder, container, false);
        BottomNavigationView bottomNavigationView=view.findViewById(R.id.bnvchats);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment=null;
                switch (item.getItemId()){
                    case R.id.chats:
                        selectedFragment=new Chats();
                        break;
                    case R.id.profile:
                        selectedFragment=new Profile();
                        break;
                }
                FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.chatsholder,selectedFragment);
                fragmentTransaction.commit();
                return true;
            }
        });
        FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.chatsholder,new Chats());
        fragmentTransaction.commit();
        return view;
    }

}
