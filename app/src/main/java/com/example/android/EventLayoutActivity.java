package com.example.android;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This Activity setups up a view to act as the "Event Layout" screen
 */
public class EventLayoutActivity extends AppCompatActivity {
    protected String ActivityNAME="EventLayoutActivity";
    private EditText eventNameET;
    private EditText eventDescET;
    private TextView eventDateTV, eventTimeTV, eventCourseTV;
    private LocalTime time;
    private String tempTime;
    String eventCourse;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String currAppointment;
    ArrayList<String> prof;

    /**
     * Sets up "Event Layout" view and stores extras from intent in {@link EventLayoutActivity} fields
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ActivityNAME,String.valueOf(time));

        setContentView(R.layout.activity_event_layout);

        setTitle("Book Appointment");
        Bundle extras=getIntent().getExtras();

        tempTime=extras.getString("time");
        Log.i(ActivityNAME,tempTime);
        time=LocalTime.parse(tempTime,DateTimeFormatter.ISO_TIME);
        eventCourse=extras.getString("course");

        eventNameET = findViewById(R.id.eventNameET);
        eventDescET=findViewById(R.id.eventDescET);
        eventCourseTV=findViewById(R.id.eventCourseTV);
        eventDateTV = findViewById(R.id.eventDateTV);
        eventTimeTV = findViewById(R.id.eventTimeTV);
        eventCourseTV.setText("Course: "+eventCourse);
        eventDateTV.setText("Date: " + CalendarUtils.formattedDate(CalendarUtils.selectedDate));
        eventTimeTV.setText("Time: " + CalendarUtils.formattedTime(time));
    }

//    public void saveEventAction(View view) {
//        String eventName = eventNameET.getText().toString();
//        String eventDesc=eventDescET.getText().toString();
//        Event newEvent = new Event(eventName, eventDesc,eventCourse,CalendarUtils.selectedDate, time);
//        Event.eventsList.add(newEvent);
//        Log.i("evenlayoutactivity list size",String.valueOf(Event.eventsList.size()));
//        finish();
//    }

    /**
     * Stores {@link Event} information in {@link Event#eventsList}, as well as adding
     * the event to the database
     * @param view
     */
    public void saveEventAction(View view) {
        String eventName = eventNameET.getText().toString();
        String eventDesc=eventDescET.getText().toString();
        String eventCal=CalendarUtils.formattedDate(CalendarUtils.selectedDate);
        String eventTime=time.toString();

        Event newEvent = new Event(eventName, eventDesc,eventCourse,CalendarUtils.selectedDate, time);
        Event.eventsList.add(newEvent);

//        db.collection("courses").

        Map<String, Object> appointment = new HashMap<>();
        appointment.put("user",MainActivity.UserId);
        appointment.put("title", eventName);
        appointment.put("desc", eventDesc);
        appointment.put("course", eventCourse);
        appointment.put("date", eventCal);
        appointment.put("time", eventTime);

        db.collection("appointment")
                .add(appointment)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(ActivityNAME, "DocumentSnapshot written with ID: " + documentReference.getId());
                        currAppointment = documentReference.getId();
                        DocumentReference userRef = db.collection("user").document(MainActivity.UserId);
                        userRef.update("appointments", FieldValue.arrayUnion(currAppointment));

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(ActivityNAME, "Error adding document", e);
                    }
                });

        Log.i(ActivityNAME,String.valueOf(Event.eventsList.size()));
        finish();

    }


}