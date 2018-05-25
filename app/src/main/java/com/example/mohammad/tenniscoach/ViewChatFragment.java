package com.example.mohammad.tenniscoach;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mohammad.tenniscoach.model.Message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewChatFragment extends Fragment {


    public ViewChatFragment() {
    }

    static DocumentReference userRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_view_chat, container, false);

        Bundle extras = getArguments();
        String fromId = extras.getString("userId");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore fsdb = FirebaseFirestore.getInstance();
        final ArrayList<Message> messages = new ArrayList<>();
        final MessageListViewAdapter messagesAdapter = new MessageListViewAdapter(messages);
        ListView chatsListView = (ListView) rootView.findViewById(R.id.lv_messages);
        chatsListView.setAdapter(messagesAdapter);
        userRef = fsdb.document("coach/" + user.getUid());
        final DocumentReference fromRef = fsdb.document("users/" + fromId);
        fromRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                ((TextView) rootView.findViewById(R.id.tv_name)).setText((String) snapshot.get("name"));
            }
        });

        fsdb.collection("messages")
                .whereEqualTo("to", userRef)
                .whereEqualTo("from", fromRef)
                .orderBy("date")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        List<DocumentReference> users = new ArrayList<>();
                        if (snapshots != null) {
                            for (DocumentChange documentChange : snapshots.getDocumentChanges()) {
                                Message message = documentChange.getDocument().toObject(Message.class);
                                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                    messages.add(message);
                                }
                            }
                        }
                        updateMessages(messages, messagesAdapter);
                    }
                });
        fsdb.collection("messages")
                .whereEqualTo("from", userRef)
                .whereEqualTo("to", fromRef)
                .orderBy("date")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        List<DocumentReference> users = new ArrayList<>();
                        if (snapshots != null) {
                            for (DocumentChange documentChange : snapshots.getDocumentChanges()) {
                                Message message = documentChange.getDocument().toObject(Message.class);
                                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                    messages.add(message);
                                }
                            }
                        }
                        updateMessages(messages, messagesAdapter);
                    }
                });

        rootView.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etMessage = ((EditText) rootView.findViewById(R.id.et_message));
                String message = etMessage.getText().toString().trim();
                if (!message.equals("")) {
                    CollectionReference messagesRef = fsdb.collection("messages/");
                    Map<String, Object> messageMap = new HashMap<>();
                    messageMap.put("from", userRef);
                    messageMap.put("to", fromRef);
                    messageMap.put("message", message);
                    messageMap.put("date", new Date());
                    messagesRef.add(messageMap);
                }
                etMessage.setText("");
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
            }
        });

        return rootView;
    }

    public void updateMessages(ArrayList<Message> messages, MessageListViewAdapter messagesAdapter) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            messages.sort(new Comparator<Message>() {
                @Override
                public int compare(Message o1, Message o2) {
                    long o1time = o1.getDate().getTime();
                    long o2time = o2.getDate().getTime();
                    return Long.compare(o1time, o2time);
                }
            });
        }
        messagesAdapter.notifyDataSetChanged();
    }

    private class MessageListViewAdapter extends BaseAdapter {

        List<Message> chat;

        MessageListViewAdapter(List<Message> chat) {
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
            Message message = chat.get(position);
            if (listItemView == null) {
                if (message.getFrom().equals(userRef)) {
                    listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat, parent, false);
                    listItemView.setBackgroundColor(getResources().getColor(R.color.primaryDark, null));
                } else {
                    listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat, parent, false);
                    listItemView.setBackgroundColor(getResources().getColor(R.color.secondaryDark, null));
                }
            }
            ((TextView) listItemView.findViewById(R.id.tv_item_message)).setText(message.getMessage());
            ((TextView) listItemView.findViewById(R.id.tv_item_timestamp)).setText(message.getDateString());

            return listItemView;
        }

    }

}
