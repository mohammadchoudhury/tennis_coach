package com.example.mohammad.tenniscoach;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        if (savedInstanceState == null) {
            String fromId = getIntent().getStringExtra("fromId");
            Fragment frag = new ChatFragment();
            if (fromId != null && !fromId.equals("")) {
                Bundle extras = new Bundle();
                extras.putString("fromId", fromId);
                frag = new ViewChatFragment();
                frag.setArguments(extras);
            }
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, frag)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
    }
}
