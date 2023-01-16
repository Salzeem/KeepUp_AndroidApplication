package com.example.android;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookAppointmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * This fragment provides a View to book appointments
 */
public class BookAppointmentFragment extends Fragment {
    private String FRAGMENT_NAME = "BookAppointmentFragment";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView monthDayText;
    private ListView hourListView;
    private TextView dayOfWeekTV;
    private Spinner courseSpinner;
    String coursePreference;
    List<String> courseList;
    ArrayList<ArrayList<String>> bookedApps;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Required empty public constructor
     */
    public BookAppointmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * Sets fragment arguments
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookAppointmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookAppointmentFragment newInstance(String param1, String param2) {
        BookAppointmentFragment fragment = new BookAppointmentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Stores fragment arguments in {@link BookAppointmentFragment#mParam1} and
     * {@link BookAppointmentFragment#mParam2}
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    /**
     * Sets up {@link R.layout#fragment_book_appointment} view. Then
     * Stores user appointments from database in {@link BookAppointmentFragment#bookedApps}.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView=inflater.inflate(R.layout.fragment_book_appointment, container, false);
        courseSpinner=inflatedView.findViewById(R.id.courseSelectionSpinner);
        get_a_course(courseSpinner);

        monthDayText=inflatedView.findViewById(R.id.monthDayText);
        hourListView=inflatedView.findViewById(R.id.hourListView);
        dayOfWeekTV=inflatedView.findViewById(R.id.dayOfWeekTV);
        CalendarUtils.selectedDate = LocalDate.now();

        db.collection("appointment")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            bookedApps=new ArrayList<ArrayList<String>>();
                            for (DocumentChange document : task.getResult().getDocumentChanges()) {
                                String userKey = document.getDocument().getString("user");
                                String time = document.getDocument().getString("time");
                                String date=document.getDocument().getString("date");
                                String course=document.getDocument().getString("course");
                                String title=document.getDocument().getString("title");

                                if(userKey.equals(MainActivity.UserId)){
                                    ArrayList<String> app=new ArrayList<String>();
                                    app.add(course);
                                    app.add(date);
                                    app.add(time);
                                    bookedApps.add(app);
                                }
                            }

                        setDayView();

                        Button nextBtn=inflatedView.findViewById(R.id.nextDayBtn);
                        nextBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusDays(1);
                                setDayView();
                            }
                        });
                        Button prevBtn=inflatedView.findViewById(R.id.prevDayBtn);
                        prevBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusDays(1);
                                setDayView();
                            }
                        });

                        hourListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent=new Intent(getActivity(),EventLayoutActivity.class);
                                String time=((HourEvent)hourListView.getItemAtPosition(i)).getTime().toString();
                                intent.putExtra("time",time);
                                intent.putExtra("course",coursePreference);
                                startActivity(intent);
                            }
                        });
                } else {
                    Log.d(FRAGMENT_NAME, "get failed with ", task.getException());
                }
            }
        });

        return inflatedView;
    }

    /**
     * Stores booked appointments from database in {@link BookAppointmentFragment#bookedApps}
     */
    @Override
    public void onResume(){
        super.onResume();
        db.collection("appointment")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            bookedApps=new ArrayList<ArrayList<String>>();
                            for (DocumentChange document : task.getResult().getDocumentChanges()) {
                                String userKey = document.getDocument().getString("user");
                                String time = document.getDocument().getString("time");
                                String date=document.getDocument().getString("date");
                                String course=document.getDocument().getString("course");
                                String title=document.getDocument().getString("title");

                                if(userKey.equals(MainActivity.UserId)){
                                    ArrayList<String> app=new ArrayList<String>();
                                    app.add(course);
                                    app.add(date);
                                    app.add(time);
//                                    app.add(title);
                                    bookedApps.add(app);
                                }
                            }
                            setDayView();
                        } else {
                            Log.d(FRAGMENT_NAME, "get failed with ", task.getException());
                        }
                    }
                });
    }

    /**
     * Sets views day and month text via {@link BookAppointmentFragment#monthDayText} and
     * {@link BookAppointmentFragment#dayOfWeekTV}. Day and month information is gathered via
     * {@link CalendarUtils#selectedDate}
     */
    private void setDayView() {
        monthDayText.setText(CalendarUtils.monthDayFromDate(CalendarUtils.selectedDate));
        String dayOfWeek =CalendarUtils.selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        dayOfWeekTV.setText(dayOfWeek);
        setHourAdapter();
    }

    /**
     * Creates new {@link HourAdapter} and sets the adapter for {@link BookAppointmentFragment#hourListView}
     * to it
     */
    private void setHourAdapter() {
        HourAdapter hourAdapter = new HourAdapter(getActivity().getApplicationContext(),hourEventList());
        hourListView.setAdapter(hourAdapter);
    }
//    ArrayList<HourEvent> allHourEventslist;

    /**
     * Creates and returns an {@link ArrayList} of available
     * hours ({@link HourEvent})
     * @return {@link ArrayList} of available hours
     */
    private ArrayList<HourEvent> hourEventList() {
        ArrayList<HourEvent> allHourEventslist=new ArrayList<>();
        for(int hour=8;hour<17;hour++){
            LocalTime time = LocalTime.of(hour, 0);
            Boolean app_check=true;
                for (ArrayList<String> app : bookedApps) {
//                    Log.i(FRAGMENT_NAME, app.get(0));
//                    Log.i(FRAGMENT_NAME, coursePreference);
//                    Log.i(FRAGMENT_NAME, app.get(1));
//                    Log.i(FRAGMENT_NAME, time.toString());
//                    Log.i(FRAGMENT_NAME, app.get(2));
//                    Log.i(FRAGMENT_NAME, CalendarUtils.formattedDate(CalendarUtils.selectedDate));
                    if (app.get(0).equals(coursePreference) && app.get(2).equals(time.toString()) && app.get(1).equals(CalendarUtils.formattedDate(CalendarUtils.selectedDate))) {
                        app_check = false;
                    }
                }
            if(app_check) {
                ArrayList<Event> events = Event.eventsForDateAndTime(CalendarUtils.selectedDate, time,coursePreference);
                HourEvent hourEvent = new HourEvent(time, events);
                ArrayList<Event> hourEventCheck = hourEvent.getEvents();
                if (hourEventCheck.size() == 0) {
                    allHourEventslist.add(hourEvent);
                }
            }
        }
        return allHourEventslist;
    }

    /**
     * Populates {@code courseSpinner} with the logged in users courses. When a course is selected from
     * the spinner, {@link BookAppointmentFragment#bookedApps} is populated with the users appointments
     * @param courseSpinner {@link Spinner} to be populated with the users courses
     */
    public void get_a_course(Spinner courseSpinner) {
//        Log.i(ACTIVITY_NAME,"get a year");
        DocumentReference docRef = db.collection("user").document(MainActivity.UserId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        courseList = (ArrayList<String> ) document.get("courses");
//                        if(coursePreference.length()<2){
                            coursePreference=courseList.get(0);
//                        }
                        ArrayAdapter<CharSequence> adapter = new ArrayAdapter( getContext(), android.R.layout.simple_spinner_dropdown_item,courseList);
                        courseSpinner.setAdapter(adapter);
                        courseSpinner.setOnItemSelectedListener(
                                new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView <?> adapterView,
                                                               View view, int i, long l) {
                                        coursePreference=courseList.get(i);

                                        db.collection("appointment")
                                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            bookedApps=new ArrayList<ArrayList<String>>();
                                                            for (DocumentChange document : task.getResult().getDocumentChanges()) {
                                                                String userKey = document.getDocument().getString("user");
                                                                String time = document.getDocument().getString("time");
                                                                String date=document.getDocument().getString("date");
                                                                String course=document.getDocument().getString("course");
                                                                String title=document.getDocument().getString("title");

                                                                if(userKey.equals(MainActivity.UserId)){
                                                                    ArrayList<String> app=new ArrayList<String>();
                                                                    app.add(course);
                                                                    app.add(date);
                                                                    app.add(time);
                                                                    bookedApps.add(app);
                                                                }
                                                            }
                                                            setDayView();
                                                        } else {
                                                            Log.d(FRAGMENT_NAME, "get failed with ", task.getException());
                                                        }
                                                    }
                                                });
                                    }
                                    @Override
                                    public void onNothingSelected(AdapterView <?> adapterView) {
                                    }
                                });
                        Log.d(FRAGMENT_NAME, "DocumentSnapshot data: " + courseList);
                    } else {
                        Log.d(FRAGMENT_NAME, "No such document");
                    }
                } else {
                    Log.d(FRAGMENT_NAME, "get failed with ", task.getException());
                }
            }
        });

    }

}