package com.example.mohammad.tenniscoach;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mohammad.tenniscoach.model.Booking;
import com.example.mohammad.tenniscoach.model.Session;

import java.util.List;

public class BookingListViewAdapter extends BaseAdapter {

    List<Session> sessions;

    BookingListViewAdapter(List<Session> sessions) {
        this.sessions = sessions;
    }

    @Override
    public int getCount() {
        return sessions.size();
    }

    @Override
    public Session getItem(int position) {
        return sessions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return sessions.get(position).hashCode();
    }

    @Override
    public View getView(int position, View listItemView, ViewGroup parent) {
        if (listItemView == null) {
            listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_booking, parent, false);
        }

        Session session = sessions.get(position);
        ImageView sessionImage = listItemView.findViewById(R.id.iv_session_icon);
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
        ((TextView) listItemView.findViewById(R.id.tv_item_session_type)).setText(sessionType);
        ((TextView) listItemView.findViewById(R.id.tv_item_date)).setText(session.getDateString());
        ((TextView) listItemView.findViewById(R.id.tv_item_time)).setText(session.getTimeString());
        if (session instanceof Booking) {
            ((TextView) listItemView.findViewById(R.id.tv_item_court)).setText(((Booking) session).getLocation());
        } else {
            ((TextView) listItemView.findViewById(R.id.tv_item_court)).setText("Court 5");
        }

        return listItemView;
    }

}