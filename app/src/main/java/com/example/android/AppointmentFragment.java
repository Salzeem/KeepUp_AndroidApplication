package com.example.android;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppointmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * This fragment provides a View to view appointments
 */
public class AppointmentFragment extends Fragment {
    protected static final String FRAGMENT_NAME="AppointmentFragment";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<ArrayList<String>> bookedAppointments;

    protected static int[] colorIcon = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
    private ViewAppointmentAdapter adapter;
    private final ArrayList<ArrayList<String>> StudentAppointment = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<String>> appointments = new ArrayList<ArrayList<String>>();

    private ListView Appointmentlist;
    TextView noAppinfo;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String UserID  = "userID";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Required empty public constructor
     */
    public AppointmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppointmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointmentFragment newInstance(String param1, String param2) {
        AppointmentFragment fragment = new AppointmentFragment();
        Bundle args = new Bundle();
        args.putString(UserID, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * An {@link ArrayAdapter} subclass that implements {@link ViewAppointmentAdapter#getCount()}
     * and {@link ViewAppointmentAdapter#getView(int, View, ViewGroup)} methods
     */
    private class ViewAppointmentAdapter extends ArrayAdapter<String> {
        public ViewAppointmentAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        /**
         * Gets the number of appointments the user has scheduled
         * @return {@link Integer} of appointments user has scheduled
         */
        public int getCount() {
            return StudentAppointment.size();
        }

        /**
         * Inflates {@link R.layout#group_list_view} and adjusts contents (titles, button text, etc...)
         * accordingly with appointment related content.
         *
         * @param position
         * @param convertView
         * @param parent
         * @return Inflated view
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            LayoutInflater inflater = AppointmentFragment.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.group_list_view, null);
            if (user != null) {

                TextView message = result.findViewById(R.id.GroupNameLabel);
                ImageView deleteButton = result.findViewById(R.id.RemoveGroup);
                ImageView classIcon = result.findViewById(R.id.GroupIconDisplay);
                TextView groupDescription = result.findViewById(R.id.GroupDescriptionLabel);
                CardView cardGroup = result.findViewById(R.id.CardGroup);
                ImageView EditIcon = result.findViewById(R.id.EditIcon);
                EditIcon.setVisibility(View.INVISIBLE);


                deleteButton.setTag(position);
                classIcon.setTag(position);
                cardGroup.setTag(position);

                message.setText(StudentAppointment.get(position).get(3) + " meeting at " + StudentAppointment.get(position).get(1) + " on " + StudentAppointment.get(position).get(2));
                groupDescription.setText(StudentAppointment.get(position).get(4) + ". " + StudentAppointment.get(position).get(5));
                if (StudentAppointment.get(position).size() > 7) {
                    cardGroup.setCardBackgroundColor(Color.GREEN);
                }
                classIcon.setImageResource(R.drawable.class_icon);
                classIcon.setColorFilter((colorIcon[(int) Math.floor(Math.random() * (colorIcon.length))]));
                classIcon.setVisibility(View.INVISIBLE);

                ImageView removeClassIV = result.findViewById(R.id.RemoveGroup);
                removeClassIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RemoveAppointment(view);
                    }
                });

            }
            else {
                getActivity().finish();
            }

            return  result;
        }

    }

    /**
     * Stores fragment parameters in {@link AppointmentFragment#mParam1}
     * and {@link AppointmentFragment#mParam2}
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(UserID);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * Sets up view with appropriate contents. Gathers appointment information based on signed
     * in user. And calls {@link AppointmentFragment#DisplayRegApps(ArrayList)}
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        String title="Appointments";
        getActivity().setTitle(title);
        StudentAppointment.clear();
        View test=inflater.inflate(R.layout.fragment_group, container, false);
        Appointmentlist= test.findViewById(R.id.GroupinformationList);
        noAppinfo = test.findViewById(R.id.NoGroupinfo);
       // TextView addAppBtn= test.findViewById(R.id.BannerText);
       // addAppBtn.setText("Book an Appointment");
        adapter = new AppointmentFragment.ViewAppointmentAdapter(this.getContext(), 0);
        Appointmentlist.setAdapter(adapter);
        noAppinfo.setText(R.string.noAppAdded);
        noAppinfo.setVisibility(View.VISIBLE);
        Appointmentlist.setVisibility(View.INVISIBLE);
        //ImageView refreshBtn=test.findViewById(R.id.refresh);
        //refreshBtn.setVisibility(View.INVISIBLE);

        TransitionInflater inflators = TransitionInflater.from(requireContext());

        setEnterTransition(inflators.inflateTransition(R.transition.slide_right));
        //setExitTransition(inflators.inflateTransition(R.transition.slide_left));

        FloatingActionButton btn=test.findViewById(R.id.Banner);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,BookAppointmentFragment.class,getArguments()) //TODO: Change to Add Class Fragment
                        .setReorderingAllowed(true)
                        .addToBackStack("tempBackStack")
                        .commit();
            }
        });

        Log.i(FRAGMENT_NAME, "This is the param "+ MainActivity.UserId);
        if (MainActivity.UserId!=null) {
            db.collection("user").whereEqualTo(FieldPath.documentId(), MainActivity.UserId)  // ERR; Main activity is not passing the dat
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    ArrayList<String> temp=(ArrayList<String>) document.get("appointments");
                                    ArrayList<ArrayList<String>> temp2=new ArrayList<ArrayList<String>>();
                                    db.collection("appointment")
                                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (DocumentChange document : task.getResult().getDocumentChanges()) {
                                                            String appID=document.getDocument().getId();
                                                            String userKey = document.getDocument().getString("user");
                                                            String time = document.getDocument().getString("time");
                                                            String date=document.getDocument().getString("date");
                                                            String course=document.getDocument().getString("course");
                                                            String title=document.getDocument().getString("title");
                                                            String desc=document.getDocument().getString("desc");
                                                            LocalDate currDate=LocalDate.now();
                                                            String[] dateInfo=date.split(" ");
                                                            LocalDate appDate=LocalDate.of(Integer.valueOf(dateInfo[2]),getNumberFromMonthName(dateInfo[1]),Integer.valueOf(dateInfo[0]));
                                                            Log.i(FRAGMENT_NAME,currDate.toString());

                                                            if(userKey.equals(MainActivity.UserId) && (appDate.isAfter(currDate) || appDate.isEqual(currDate))){
                                                                ArrayList<String> val=new ArrayList<>();
                                                                val.add(appID);
                                                                val.add(time);
                                                                val.add(date);
                                                                val.add(course);
                                                                val.add(title);
                                                                val.add(desc);
                                                                temp2.add(val);
                                                            }

                                                        }

                                                        DisplayRegApps(temp2);

                                                    } else {
                                                        Log.w(FRAGMENT_NAME, "Error getting documents or no changes yet.", task.getException());
                                                    }
                                                }
                                            });
                                }
                            } else {
                                Log.w(FRAGMENT_NAME, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }

        return test;

//        View inflatedLayout=inflater.inflate(R.layout.fragment_appointment, container, false);
//
//        db.collection("appointment")
//                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            bookedAppointments=new ArrayList<ArrayList<String>>();
//                            for (DocumentChange document : task.getResult().getDocumentChanges()) {
//                                String userKey = document.getDocument().getString("user");
//                                String time = document.getDocument().getString("time");
//                                String date=document.getDocument().getString("date");
//                                String course=document.getDocument().getString("course");
//                                String title=document.getDocument().getString("title");
//                                String desc=document.getDocument().getString("desc");
//
//                                if(userKey.equals(MainActivity.UserId)){
//                                    ArrayList<String> app=new ArrayList<String>();
//                                    app.add(course);
//                                    app.add(date);
//                                    app.add(time);
//                                    app.add(title);
//                                    app.add(desc);
//                                    bookedAppointments.add(app);
//                                }
//                            }
//                            TextView appointmentsList = inflatedLayout.findViewById(R.id.appointmentList);
//                            if (bookedAppointments.size()>0){
//                                appointmentsList.setText("");
//                                appointmentsList.append("List of all appointments: \n");
//                                for(ArrayList<String> appointment:bookedAppointments){
//                                    appointmentsList.append("Course: "+appointment.get(0)+", ");
//                                    appointmentsList.append("Title: "+appointment.get(3)+", ");
//                                    appointmentsList.append("Date: "+appointment.get(1)+", ");
//                                    appointmentsList.append("Time: "+appointment.get(2)+"\n \n");
//                                }
//                            }else{
//                                appointmentsList.setText("You do not have any booked appointments");
//                            }
//                            Button bookAppBtn=inflatedLayout.findViewById(R.id.bookAppointmentBtn);
//                            bookAppBtn.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    getActivity().getSupportFragmentManager().beginTransaction()
//                                            .replace(R.id.fragmentContainerView,BookAppointmentFragment.class,null)
//                                            .setReorderingAllowed(true)
//                                            .addToBackStack("tempBackStack")
//                                            .commit();
//                                }
//                            });
//
//                            Button cancelAppBtn = inflatedLayout.findViewById(R.id.cancelAppointmentBtn);
//                            cancelAppBtn.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    getActivity().getSupportFragmentManager().beginTransaction()
//                                            .replace(R.id.fragmentContainerView,CancelAppointmentFragment.class,null)
//                                            .setReorderingAllowed(true)
//                                            .addToBackStack("tempBackStack")
//                                            .commit();
//                                }
//                            });
//
//                        } else {
//                            Log.d(FRAGMENT_NAME, "get failed with ", task.getException());
//                        }
//                    }
//                });
//
//
//        return inflatedLayout;
    }

    /**
     * Sets {@link AppointmentFragment#Appointmentlist} to visible if the student has selected at least
     * one class. Populates {@link AppointmentFragment#StudentAppointment} with contents of {@code studentClasses}
     * @param studentClasses logged in user specific classes
     */
    public void DisplayRegApps(ArrayList<ArrayList<String>> studentClasses )
    {

        if (studentClasses.size()>0 )
        {
            Appointmentlist.setVisibility(View.VISIBLE);
            noAppinfo.setVisibility(View.INVISIBLE);

        }
        for (ArrayList<String> app : studentClasses)
        {
            StudentAppointment.add(app);
        }
        Log.i(FRAGMENT_NAME, "Appointments: " + StudentAppointment.size());
        adapter.notifyDataSetChanged();
    }

    /**
     * Cancels a scheduled appointment based on user logged in, and the appointment selected
     * in the view
     * @param view
     */
    public void RemoveAppointment(View view )
    {
        int positionitemToDelete = (int) view.getTag();
        ArrayList<String> appname = StudentAppointment.get(positionitemToDelete);

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this.getContext());

        builder.setTitle("Are you sure you want to cancel this appointment?");
        builder.setPositiveButton(R.string.Yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        StudentAppointment.remove(positionitemToDelete);
                        adapter.notifyDataSetChanged();
                        if (StudentAppointment.size() == 0 )
                        {


                            noAppinfo.setVisibility(View.VISIBLE);
                            Appointmentlist.setVisibility(View.INVISIBLE);
                        }
                        deletefromdatabase(appname, MainActivity.UserId );
                    } });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    } });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Deletes appointments in {@code appointment} from database based on {@code userID}
     * @param appointment {@link ArrayList} of appointments
     * @param userID {@link String} id of the logged in user
     */
    public void deletefromdatabase(ArrayList<String> appointment, String userID )
    {
        db.collection("appointment").document(appointment.get(0)).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(FRAGMENT_NAME, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(FRAGMENT_NAME, "Error deleting document", e);
                    }
                });
        DocumentReference groupsRef = db.collection("user").document(userID);
        groupsRef.update("appointments", FieldValue.arrayRemove(appointment.get(0)));

    }

    /**
     * Returns the number corresponding to the month given by {@code monthName}
     * @param monthName {@link String} name of month
     * @return {@link Integer} month number
     */
    public static int getNumberFromMonthName(String monthName) {
        return Month.valueOf(monthName.toUpperCase()).getValue();
    }
}