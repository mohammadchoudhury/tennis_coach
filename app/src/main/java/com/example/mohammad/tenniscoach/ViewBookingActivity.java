package com.example.mohammad.tenniscoach;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohammad.tenniscoach.model.Booking;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ViewBookingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_session);

        final Intent intent = getIntent();
        final String bookingId = intent.getStringExtra("bookingId");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        final FirebaseUser user = auth.getCurrentUser();
        final FirebaseFirestore fsdb = FirebaseFirestore.getInstance();

        final Booking[] booking = new Booking[1];
        fsdb.document("bookings/" + bookingId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                booking[0] = documentSnapshot.toObject(Booking.class);
                ImageView sessionImage = findViewById(R.id.iv_session_icon);
                String bookingType = "";
                if (booking[0].getType().equalsIgnoreCase("private")) {
                    sessionImage.setImageResource(R.drawable.ic_racket);
                    bookingType = "Private Session";
                    findViewById(R.id.ll_coach).setVisibility(View.VISIBLE);
                } else if (booking[0].getType().equalsIgnoreCase("group")) {
                    sessionImage.setImageResource(R.drawable.ic_balls);
                    bookingType = "Group Session";
                    findViewById(R.id.ll_coach).setVisibility(View.VISIBLE);
                } else {
                    sessionImage.setImageResource(R.drawable.ic_court);
                    bookingType = "Court Booking";
                }
                ((TextView) findViewById(R.id.textView_type)).setText(bookingType);
                ((TextView) findViewById(R.id.textView_date)).setText(booking[0].getDateString());
                ((TextView) findViewById(R.id.textView_time)).setText(booking[0].getTimeString());
                ((TextView) findViewById(R.id.textView_price)).setText(booking[0].getPriceString());

                ((TextView) findViewById(R.id.textView_coach)).setText(booking[0].getCoach());
                ((TextView) findViewById(R.id.textView_court)).setText(booking[0].getCourt());

                if (booking[0].getDate().before(new Date())) findViewById(R.id.btn_cancel_session).setVisibility(View.INVISIBLE);

                booking[0].getUser().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(final DocumentSnapshot snapshot) {
                        LinearLayout llUser = findViewById(R.id.ll_user);
                        llUser.setVisibility(View.VISIBLE);
                        llUser.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(ViewBookingActivity.this, ProfileActivity.class);
                                i.putExtra("userId", snapshot.getId());
                                startActivity(i);
                            }
                        });
                        ((TextView)findViewById(R.id.tv_name)).setText((String)snapshot.get("name"));
                    }
                });

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
                                booking[0].getUser().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot snapshot) {
                                        String message = "Sorry, your booking has been canceled.\n" + booking[0].getType() + " - " + booking[0].getDateString();
                                        ViewChatFragment.sendMessage((String)snapshot.get("token"), message, "Imran Uddin", "fXbXlfiOiyA:APA91bGG6_G8McLPcnQIGFbgh_wBpeTjJC_AHjgyR2VN4QN_PzV-2tlggKbemAdcsLK4FCnsv3hMn7EUjZ1jeBXzisA8gZrGG1rg3RV36Izr1gEZc36HLb-0kPWPogg9cmzr-Z3pJCEW");
                                        CollectionReference messagesRef = fsdb.collection("messages/");
                                        Map<String, Object> messageMap = new HashMap<>();
                                        messageMap.put("from", fsdb.document("coach/tw9dXHrBf4fVgjkW8FPu4PURkPh2"));
                                        messageMap.put("to", fsdb.document("users/"+snapshot.getId()));
                                        messageMap.put("message", message);
                                        messageMap.put("date", new Date());
                                        messagesRef.add(messageMap);
                                        ((TextView)findViewById(R.id.tv_name)).setText((String)snapshot.get("name"));
                                    }
                                });
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        });
            }
        });

    }

}

