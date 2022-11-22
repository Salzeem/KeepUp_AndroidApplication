package com.example.android;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppointmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentFragment extends Fragment {
    protected static final String FRAGMENT_NAME="AppointmentFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedLayout=inflater.inflate(R.layout.fragment_appointment, container, false);

        TextView appointmentsList = inflatedLayout.findViewById(R.id.appointmentList);
        if (Event.eventsList.size()>0){
            appointmentsList.setText("");
            appointmentsList.append("List of all appointments: \n");
            for(Event event:Event.eventsList){
                appointmentsList.append("Course: "+event.getCourse()+", ");
                appointmentsList.append("Title: "+event.getName()+", ");
                appointmentsList.append("Date: "+event.getDate()+", ");
                appointmentsList.append("Time: "+event.getTime()+"\n \n");
            }
        }else{
            appointmentsList.setText("You do not have any booked appointments");
        }

        Button bookAppBtn=inflatedLayout.findViewById(R.id.bookAppointmentBtn);
        bookAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,BookAppointmentFragment.class,null)
                        .setReorderingAllowed(true)
                        .addToBackStack("tempBackStack")
                        .commit();
            }
        });

        Button cancelAppBtn = inflatedLayout.findViewById(R.id.cancelAppointmentBtn);
        cancelAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,CancelAppointmentFragment.class,null)
                        .setReorderingAllowed(true)
                        .addToBackStack("tempBackStack")
                        .commit();
            }
        });
        return inflatedLayout;
    }
}