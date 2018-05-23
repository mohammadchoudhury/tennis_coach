package com.example.mohammad.tenniscoach;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mohammad.tenniscoach.model.Session;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SessionTabFragment extends Fragment {

    public SessionTabFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_booking, container, false);

        final FirebaseFirestore fsdb = FirebaseFirestore.getInstance();

        final List<Session> sessions = new ArrayList<>();

        final BookingListViewAdapter sessionsAdapter = new BookingListViewAdapter(sessions);
        ListView sessionsListView = (ListView) rootView.findViewById(R.id.lv_bookings);
        sessionsListView.setAdapter(sessionsAdapter);
        sessionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Toast.makeText(parent.getContext(), sessions.get(position).getID(), Toast.LENGTH_LONG).show();

                String sessionId = sessions.get(position).getID();
                Intent intent = new Intent(getContext(), ViewSessionActivity.class)
                        .putExtra("sessionId", sessionId);
                startActivity(intent);
            }
        });

        fsdb.collection("sessions")
                .orderBy("date")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        sessions.clear();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Session session = document.toObject(Session.class);
                            session.setID(document.getId());
                            sessions.add(session);
                        }
                        sessionsAdapter.notifyDataSetChanged();
                    }
                });

        return rootView;
    }
}
