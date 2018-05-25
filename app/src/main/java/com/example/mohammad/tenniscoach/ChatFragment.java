package com.example.mohammad.tenniscoach;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mohammad.tenniscoach.model.Message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore fsdb = FirebaseFirestore.getInstance();
        final List<Message> chat = new ArrayList<>();
        final ChatListViewAdapter chatsAdapter = new ChatListViewAdapter(chat);
        ListView chatsListView = (ListView) rootView.findViewById(R.id.lv_chats);
        chatsListView.setAdapter(chatsAdapter);
        chatsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Bundle extras = new Bundle();
                extras.putString("fromId", ((Message) parent.getItemAtPosition(position)).getFrom().getId());
                Fragment frag = new ViewChatFragment();
                frag.setArguments(extras);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, frag)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }
        });

        fsdb.collection("messages")
                .whereEqualTo("to", fsdb.document("coach/" + user.getUid()))
                .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        chat.clear();
                        List<DocumentReference> users = new ArrayList<>();
                        if (snapshots != null) {
                            for (QueryDocumentSnapshot document : snapshots) {
                                Message message = document.toObject(Message.class);
                                if (!users.contains(message.getFrom())) {
                                    users.add(message.getFrom());
                                    chat.add(message);
                                }
                            }
                        }
                        chatsAdapter.notifyDataSetChanged();
                    }
                });

        return rootView;
    }

    private class ChatListViewAdapter extends BaseAdapter {

        List<Message> chat;

        ChatListViewAdapter(List<Message> chat) {
            this.chat = chat;
        }

        @Override
        public int getCount() {
            return chat.size();
        }

        @Override
        public Message getItem(int position) {
            return chat.get(position);
        }

        @Override
        public long getItemId(int position) {
            return chat.get(position).hashCode();
        }

        @Override
        public View getView(int position, View listItemView, ViewGroup parent) {
            if (listItemView == null) {
                listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_contact, parent, false);
            }
            Message message = chat.get(position);
            final View finalListItemView = listItemView;
            message.getFrom().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(final DocumentSnapshot snapshot) {
                    ((TextView) finalListItemView.findViewById(R.id.tv_item_name)).setText((String) snapshot.get("name"));
                    ((Button) finalListItemView.findViewById(R.id.btn_call)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + snapshot.get("phone"))));
                        }
                    });
                    ((Button) finalListItemView.findViewById(R.id.btn_email)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(Intent.ACTION_SENDTO).setData(Uri.parse("mailto:" + (String) snapshot.get("email") + "?subject=Message from Energy Tennis Club&body=Hi " + (String) snapshot.get("name") + ",")));
                        }
                    });
                }
            });
            ((TextView) listItemView.findViewById(R.id.tv_item_message)).setText(message.getMessage());

            return listItemView;
        }

    }

}
