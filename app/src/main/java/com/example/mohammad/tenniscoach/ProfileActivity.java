package com.example.mohammad.tenniscoach;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.mohammad.tenniscoach.model.Booking;
import com.example.mohammad.tenniscoach.model.Session;
import com.example.mohammad.tenniscoach.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final String userId = getIntent().getStringExtra("userId");

        final CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, ChatActivity.class).putExtra("fromId", userId));
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseFirestore fsdb = FirebaseFirestore.getInstance();

        final List<Session> bookings = new ArrayList<>();

        final BookingListViewAdapter bookingsAdapter = new BookingListViewAdapter(bookings);
        ListView bookingsListView = (ListView) findViewById(R.id.lv_bookings);
        bookingsListView.setAdapter(bookingsAdapter);
        bookingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(ProfileActivity.this, ViewBookingActivity.class);
                intent.putExtra("bookingId", bookings.get(position).getID());
                startActivity(intent);
            }
        });


        ViewCompat.setNestedScrollingEnabled(bookingsListView, true);

        DocumentReference userRef = fsdb.document("users/" + userId);
        userRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot snapshot) {
                        User user = snapshot.toObject(User.class);
                        toolbarLayout.setTitle(user.getName());
                    }
                });

        fsdb.collection("bookings")
                .orderBy("date", Query.Direction.DESCENDING)
                .whereEqualTo("user", userRef)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        bookings.clear();
                        if (snapshots != null) {
                            for (QueryDocumentSnapshot document : snapshots) {
                                Booking booking = document.toObject(Booking.class);
                                booking.setID(document.getId());
                                bookings.add(booking);
                            }
                        }
                        bookingsAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("ERRO:", e.toString());
                    }
                });
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
//                        bookings.clear();
//                        if (queryDocumentSnapshots != null) {
//                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
//                                Booking booking = document.toObject(Booking.class);
//                                booking.setID(document.getId());
//                                bookings.add(booking);
//                            }
//                        }
//                        bookingsAdapter.notifyDataSetChanged();
//                    }
//                });
    }
}
