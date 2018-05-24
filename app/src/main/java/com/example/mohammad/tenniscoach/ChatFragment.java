package com.example.mohammad.tenniscoach;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohammad.tenniscoach.model.Booking;
import com.example.mohammad.tenniscoach.model.Session;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class ChatFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        String[] chats = new String[]{"John Doe-Hello there", "Jane Doe-Thank you", "Joe Doe-Bye"};
        final ChatListViewAdapter chatsAdapter = new ChatListViewAdapter(Arrays.asList(chats));
        ListView chatsListView = (ListView) rootView.findViewById(R.id.lv_chats);
        chatsListView.setAdapter(chatsAdapter);
        chatsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Toast.makeText(getActivity(), (String)parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    private class ChatListViewAdapter extends BaseAdapter {

        List<String> chats;

        ChatListViewAdapter(List<String> chats) {
            this.chats = chats;
        }

        @Override
        public int getCount() {
            return chats.size();
        }

        @Override
        public String getItem(int position) {
            return chats.get(position);
        }

        @Override
        public long getItemId(int position) {
            return chats.get(position).hashCode();
        }

        @Override
        public View getView(int position, View listItemView, ViewGroup parent) {
            if (listItemView == null) {
                listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat, parent, false);
            }
            String[] chatDet = chats.get(position).split("-");
            ((TextView) listItemView.findViewById(R.id.tv_item_name)).setText(chatDet[0]);
            ((TextView) listItemView.findViewById(R.id.tv_item_message)).setText(chatDet[1]);
            ((Button) listItemView.findViewById(R.id.btn_call)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Clicked call button", Toast.LENGTH_SHORT).show();
                }
            });
            ((Button) listItemView.findViewById(R.id.btn_email)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Clicked email button", Toast.LENGTH_SHORT).show();
                }
            });

            return listItemView;
        }

    }

}
