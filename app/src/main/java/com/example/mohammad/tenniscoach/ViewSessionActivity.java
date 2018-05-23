package com.example.mohammad.tenniscoach;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mohammad.tenniscoach.model.Session;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewSessionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_session);

        Intent intent = getIntent();
        String sessionId = intent.getStringExtra("sessionId");

        final FirebaseFirestore fsdb = FirebaseFirestore.getInstance();

        fsdb.document("sessions/" + sessionId);

        findViewById(R.id.textView_date);

        DocumentReference sessionRef = fsdb.document("sessions/" + sessionId);
        sessionRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Session session = documentSnapshot.toObject(Session.class);
                ImageView sessionImage = findViewById(R.id.iv_session_icon);
                String sessionType = "";
                if (session.getType().equalsIgnoreCase("private")){
                    sessionImage.setImageResource(R.drawable.ic_racket);
                    sessionType = "Private Session";
                } else if (session.getType().equalsIgnoreCase("group")){
                    sessionImage.setImageResource(R.drawable.ic_balls);
                    sessionType = "Group Session";
                } else {
                    sessionImage.setImageResource(R.drawable.ic_court);
                    sessionType = "Court Booking";
                }
                ((TextView) findViewById(R.id.textView_type)).setText(sessionType);
                ((TextView) findViewById(R.id.textView_date)).setText(session.getDateString());
                ((TextView) findViewById(R.id.textView_time)).setText(session.getTimeString());
                ((TextView) findViewById(R.id.textView_price)).setText("£" + String.valueOf(session.getPrice()));
            }
        });


    }

}

