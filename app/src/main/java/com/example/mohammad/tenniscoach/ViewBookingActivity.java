package com.example.mohammad.tenniscoach;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohammad.tenniscoach.model.Booking;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class ViewBookingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_session);

        Intent intent = getIntent();
        final String bookingId = intent.getStringExtra("bookingId");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        final FirebaseFirestore fsdb = FirebaseFirestore.getInstance();

        fsdb.document("bookings/" + bookingId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Booking booking = documentSnapshot.toObject(Booking.class);
                ImageView sessionImage = findViewById(R.id.iv_session_icon);
                String bookingType = "";
                if (booking.getType().equalsIgnoreCase("private")) {
                    sessionImage.setImageResource(R.drawable.ic_racket);
                    bookingType = "Private Session";
                    findViewById(R.id.ll_coach).setVisibility(View.VISIBLE);
                } else if (booking.getType().equalsIgnoreCase("group")) {
                    sessionImage.setImageResource(R.drawable.ic_balls);
                    bookingType = "Group Session";
                    findViewById(R.id.ll_coach).setVisibility(View.VISIBLE);
                } else {
                    sessionImage.setImageResource(R.drawable.ic_court);
                    bookingType = "Court Booking";
                }
                ((TextView) findViewById(R.id.textView_type)).setText(bookingType);
                ((TextView) findViewById(R.id.textView_date)).setText(booking.getDateString());
                ((TextView) findViewById(R.id.textView_time)).setText(booking.getTimeString());
                ((TextView) findViewById(R.id.textView_price)).setText(booking.getPriceString());

                ((TextView) findViewById(R.id.textView_coach)).setText(booking.getCoach());
                ((TextView) findViewById(R.id.textView_court)).setText(booking.getCourt());

                if (booking.getDate().before(new Date())) findViewById(R.id.btn_cancel_session).setVisibility(View.INVISIBLE);

            }
        });

        findViewById(R.id.ll_court).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_cancel_session).setVisibility(View.VISIBLE);

        findViewById(R.id.btn_cancel_session).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DocumentReference bookingRef = fsdb.document("bookings/" + bookingId);
                bookingRef.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "Booking cancelled", Toast.LENGTH_LONG).show();
                                bookingRef.delete();
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        });
            }
        });

    }

}

