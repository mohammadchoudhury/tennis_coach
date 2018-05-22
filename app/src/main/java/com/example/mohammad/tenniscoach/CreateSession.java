package com.example.mohammad.tenniscoach;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class CreateSession extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new CreateSessionFragment())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
    }

    public static class CreateSessionFragment extends Fragment {

        public CreateSessionFragment() {
        }

        static DatePickerDialog.OnDateSetListener mDateSetListener;
        static Calendar mCalendar;
        static ArrayList<String> mOptions;
        static ArrayAdapter mOptionsAdapter;

        static ArrayList<String> mTimesTaken;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_create_session, container, false);

            final FirebaseFirestore fsdb = FirebaseFirestore.getInstance();

            mOptions = new ArrayList<>();
            mOptions.add("Choose a date");

            mCalendar = Calendar.getInstance();


            ListView lvBookings = rootView.findViewById(R.id.lv_existing_bookings);
            final ArrayList<String> bookings = new ArrayList<>();
            final ArrayAdapter bookingAdapter = new ArrayAdapter(getContext(), R.layout.list_item_centred, bookings);
            lvBookings.setAdapter(bookingAdapter);

            mOptionsAdapter = new ArrayAdapter<String>(getContext(), R.layout.list_item_centred, mOptions);
            ListView lvOptions = rootView.findViewById(R.id.lv_booking_options);
            lvOptions.setAdapter(mOptionsAdapter);
            lvOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            new DatePickerFragment().show(getFragmentManager(), "datePicker");
                            break;
                        case 1:
                            new TimePickerFragment().show(getFragmentManager(), "timePicker");
                            break;
                        case 2:
                            new SessionTypePickerFragment().show(getFragmentManager(), "sessionTypePicker");
                            break;
                    }
                }
            });

            mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    while (mOptions.size() > 1) mOptions.remove(1);
                    mCalendar.clear();
                    mOptions.add("Choose a time");
                    mCalendar.set(year, month, day);
                    String dateString = mCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH) + ", "
                            + mCalendar.get(Calendar.DAY_OF_MONTH) + " "
                            + mCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH) + " "
                            + mCalendar.get(Calendar.YEAR);
                    mOptions.set(0, dateString);
                    mOptionsAdapter.notifyDataSetChanged();

                    bookings.clear();
                    bookingAdapter.notifyDataSetChanged();

                    Date nDate = mCalendar.getTime();
                    nDate.setTime(mCalendar.getTimeInMillis() + 86400000);
                    if (!mOptions.get(0).equals("")) {
                        fsdb.collection("sessions")
                                .whereGreaterThanOrEqualTo("date", mCalendar.getTime())
                                .whereLessThan("date", nDate)
//                                .get()
//                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                        bookings.clear();
//                                        mTimesTaken = new ArrayList<>();
//                                        for (QueryDocumentSnapshot document : task.getResult()) {
//                                            Map session = document.getData();
//                                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.UK);
//                                            String time = sdf.format(session.get("date"));
//                                            mTimesTaken.add(time);
//                                            bookings.add(session.get("type") + " session at " + time);
//                                        }
//                                        bookingAdapter.notifyDataSetChanged();
//                                    }
//                                });
                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                                        bookings.clear();
                                        mTimesTaken = new ArrayList<>();
                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                            Map session = document.getData();
                                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.UK);
                                            String time = sdf.format(session.get("date"));
                                            mTimesTaken.add(time);
                                            bookings.add(session.get("type") + " session at " + time);
                                        }
                                        bookingAdapter.notifyDataSetChanged();
                                    }
                                });
                    }
                }
            };

            ListView lvSession = rootView.findViewById(R.id.lv_existing_bookings);

            rootView.findViewById(R.id.btn_create_session).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String msg = "";
                    if (mOptions.get(0).equals("Choose a date")) {
                        msg = "You must choose a date";
                    } else if (mOptions.get(1).equals("Choose a time")) {
                        msg = "You must choose a time";
                    } else if (!mCalendar.after(Calendar.getInstance())) {
                        msg = "Session must be in the future";
                    } else if (mOptions.get(2).equals("Choose a session type")) {
                        msg = "You must choose a session type";
                    } else {
                        CollectionReference cities = fsdb.collection("sessions");
                        Map<String, Object> session = new HashMap<>();
                        session.put("date", mCalendar.getTime());
                        session.put("type", mOptions.get(2));
                        cities.add(session);
                        while (mOptions.size() > 1) mOptions.remove(1);
                        mOptions.add("Choose a time");
                        mOptionsAdapter.notifyDataSetChanged();
                        bookingAdapter.notifyDataSetChanged();
                        msg = "Successfully created new session";
                    }
                    Snackbar.make(rootView, msg, Snackbar.LENGTH_SHORT).show();
                }
            });

            return rootView;
        }

        public static class DatePickerFragment extends DialogFragment {

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), mDateSetListener, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 1209600000);
                return datePickerDialog;
            }
        }

        public static class TimePickerFragment extends DialogFragment {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_Dialog);
                String[] timings = {"09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00"};
                ArrayList<String> timingsAL = new ArrayList<String>(Arrays.asList(timings));
                timingsAL.removeAll(mTimesTaken);
                final ArrayList<String> times = timingsAL;
                ListAdapter timesAdapter = new ArrayAdapter<String>(getContext(), R.layout.list_item_centred, times);
                View title = View.inflate(getContext(), R.layout.dialog_title, null);
                ((TextView) title.findViewById(R.id.title)).setText("Choose a time");
                builder.setCustomTitle(title)
                        .setAdapter(timesAdapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int hourOfDay = Integer.parseInt(times.get(which).split(":")[0]);
                                mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                mCalendar.set(Calendar.MINUTE, 0);
                                mOptions.set(1, times.get(which));
                                while (mOptions.size() > 2) mOptions.remove(2);
                                mOptions.add("Choose a session type");
                                mOptionsAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                return builder.create();
            }
        }

        public static class SessionTypePickerFragment extends DialogFragment {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppTheme_Dialog);
                final String[] sessionTypes = {"Private", "Group"};
                ListAdapter courtsAdapter = new ArrayAdapter<String>(getContext(), R.layout.list_item_centred, sessionTypes);
                View title = View.inflate(getContext(), R.layout.dialog_title, null);
                ((TextView) title.findViewById(R.id.title)).setText("Choose a session type");
                builder.setCustomTitle(title)
                        .setAdapter(courtsAdapter, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mOptions.set(2, sessionTypes[which]);
                                mOptionsAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(R.string.text_cancel, null);
                return builder.create();
            }
        }
    }


}
