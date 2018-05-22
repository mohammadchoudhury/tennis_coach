package com.example.mohammad.tenniscoach;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class BookingListViewAdapter extends BaseAdapter {

    List<String> bookings;

    BookingListViewAdapter(List<String> bookings) {
        this.bookings = bookings;
    }

    @Override
    public int getCount() {
        return bookings.size();
    }

    @Override
    public String getItem(int position) {
        return bookings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return bookings.get(position).hashCode();
    }

    @Override
    public View getView(int position, View listItemView, ViewGroup parent) {
        if (listItemView == null) {
            listItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_booking, parent, false);
        }
        final String[] booking = getItem(position).split("\\|");
        ((TextView) listItemView.findViewById(R.id.tv_item_court)).setText(booking[0]);
        ((TextView) listItemView.findViewById(R.id.tv_item_session_type)).setText(booking[1]);
        ((TextView) listItemView.findViewById(R.id.tv_item_date)).setText(booking[2]);
        ((TextView) listItemView.findViewById(R.id.tv_item_time)).setText(booking[3]);
        ImageView sessionImage = listItemView.findViewById(R.id.iv_session_icon);
        switch (booking[1]) {
            case "Normal Session":
                sessionImage.setImageResource(R.drawable.ic_court);
                break;
            case "Group Session":
                sessionImage.setImageResource(R.drawable.ic_balls);
                break;
            default:
                sessionImage.setImageResource(R.drawable.ic_racket);
        }
        return listItemView;
    }

}