package com.example.android;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateGroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * This fragment provides a View to create groups
 */
public class CreateGroupFragment extends Fragment {

    protected static final String FRAGMENT_NAME="CreateGroupFragment";
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();
    protected static final int MAXIMUM = 4;
    protected Spinner StudentSpinner;
    protected Spinner CourseSpinner;
    protected Button AddButton;
    protected LinearProgressIndicator ProgressIndicator;
    protected EditText GroupNameInput;
    protected EditText GroupDescInput;

    protected static HashMap removeditems =new HashMap<String, String>();
    Button Class  ;
    Button Group ;
    Button appoints ;

    protected static ArrayList<String> memberIds = new ArrayList<>();

    protected  ArrayList<String> finalmemberEmails = new ArrayList<>();
    protected  static ArrayList<String> finalmemberIds = new ArrayList<>();
    protected static RecyclerViewAdapter_AddStudent addstudentadapter;
    protected static ArrayAdapter<CharSequence> adapter;
    protected  ArrayAdapter<CharSequence> adapter2;
    protected RecyclerView StudentList;
    public static  ArrayList<String> user = new ArrayList<String >();
    public ArrayList<String> courses = new ArrayList<>();
    public static ArrayList<String> students = new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "userID";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String UserId;
    private String mParam2;

    public CreateGroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateGroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateGroupFragment newInstance(String param1, String param2) {
        CreateGroupFragment fragment = new CreateGroupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private class MembersInfo
    {
        String name;
        String id;
        public MembersInfo(String name, String id )
        {
            this.name = name;
            this.id = id;
        }
    }

    /**
     * Calls the super's {@code onCreate()} and stores the fragments parameters in fields
     * {@link CreateGroupFragment#UserId} and {@link CreateGroupFragment#mParam2}
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            UserId = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * Sets up the "Create Group" fragment view by populating spinner and forms with content from the
     * database and setting onClickListeners
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflated_view=inflater.inflate(R.layout.fragment_create_group, container, false);




        StudentSpinner = inflated_view.findViewById(R.id.StudentList);
        CourseSpinner = inflated_view.findViewById(R.id.CourseSpinner);
        AddButton = inflated_view.findViewById(R.id.AddButton);
        StudentList = inflated_view.findViewById(R.id.UsersAdd);
        addstudentadapter = new RecyclerViewAdapter_AddStudent(this.getContext(),user , 1);
        GroupNameInput = inflated_view.findViewById(R.id.TextGroupName);
        GroupDescInput = inflated_view.findViewById(R.id.GroupDescInput);




        adapter  = new ArrayAdapter(getContext(),  android.R.layout.simple_spinner_item, students);
        adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
        StudentSpinner.setAdapter(adapter);

        adapter2 = new ArrayAdapter(getContext(),  android.R.layout.simple_spinner_item, courses );
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CourseSpinner.setAdapter(adapter2);

//        ProgressIndicator.setIndeterminate(false);

        StudentList.setAdapter(addstudentadapter);
        StudentList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));
        Class = getActivity().findViewById(R.id.class_button);
        Group = getActivity().findViewById(R.id.groups_button);
        appoints = getActivity().findViewById(R.id.schedule_button);
        Class.setVisibility(View.INVISIBLE);
        Group.setVisibility(View.INVISIBLE);
        appoints.setVisibility(View.INVISIBLE);



        PopulateFormDb(UserId);
        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroupDescInput.clearFocus();
                GroupNameInput.clearFocus();
                AddUser(view);
            }
        });

        CourseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                students.clear();
                GroupDescInput.clearFocus();
                GroupNameInput.clearFocus();
                //  finalmemberEmails.clear();
                memberIds.clear();
                finalmemberIds.clear();

                removeditems.clear();

                Log.i(FRAGMENT_NAME, " This is the final member size " + finalmemberIds.size());
                memberIds.add("None");
                students.add("None");
                adapter.notifyDataSetChanged();
                //Have to reset existing chipview data once user selects a diff item
                boolean changed = false;
                for (int j = 1; j<user.size(); j++)
                {
                    user.remove(j);
                    changed = true;
                }
                if (changed) {
                    addstudentadapter.notifyDataSetChanged();
                }

                db.collection("user")
                        .whereArrayContains("courses", courses.get(i))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String name = (String) document.get("first_name") + " " + document.getString("last_name");
                                        // memberEmails.add((String) (document.get("email")));
                                        if (!document.getId().equals(UserId))
                                        {
                                            memberIds.add(document.getId());
                                            //testmemberids.add( new MembersInfo(name, document.getId()));

                                        }
                                        if (name.compareTo(user.get(0)) != 0 ) {
                                            students.add(name);
                                        }
                                        adapter.notifyDataSetChanged();


                                    }
                                } else {
                                    Log.d(FRAGMENT_NAME, "Error getting documents: ", task.getException());
                                }
                            }
                        });




            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                GroupDescInput.clearFocus();
                GroupNameInput.clearFocus();

            }
        });

        Button saveFormbtn=inflated_view.findViewById(R.id.saveFormBtn);
        saveFormbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveForm(view);

            }
        });

        // Inflate the layout for this fragment
        return inflated_view;
    }

    /**
     * Adds the selected user from the {@link CreateGroupFragment#StudentSpinner} to the group
     * and removes the selected user as an option from the {@link CreateGroupFragment#StudentSpinner}
     * @param view
     */
    public void AddUser(View view ){
        if ( addstudentadapter.getItemCount() < MAXIMUM) {
            int spinner_position = StudentSpinner.getSelectedItemPosition();

            if (spinner_position!=0) {



                String selected_item = StudentSpinner.getItemAtPosition(spinner_position).toString();
                Log.i(FRAGMENT_NAME, "This is the Spinner Position " + spinner_position);
                finalmemberIds.add(memberIds.get(spinner_position));

                user.add(selected_item);
                // finalmemberIds.add(UserId);
                Log.i(FRAGMENT_NAME, " THis is the member Id " + memberIds.get(spinner_position));
                // finalmemberEmails.add(memberEmails.get(spinner_position));

                removeditems.put(students.get(spinner_position), memberIds.get(spinner_position));


                students.remove(StudentSpinner.getSelectedItemPosition());

                memberIds.remove(spinner_position);
                adapter.notifyDataSetChanged();

                addstudentadapter.notifyDataSetChanged();


            }

        }
        else{
            Toast.makeText(this.getContext(), "You can only select a Max of 3 members", Toast.LENGTH_LONG).show();
        }


    }

    /**
     * Writes the form information to the group document of the database if the form passes validation
     * @param view
     */
    public void SaveForm(View view){

        if (!ValidateForm()) {
            PrintSnackbar( R.string.FormErr, Color.RED, Snackbar.LENGTH_LONG);
        }
        else {
            WriteToDatabase();
        }
    }

    /**
     * Helper method to quickly show message in snackbar
     * @param message {@link Integer} message to show
     * @param color {@link Integer} color of snackbar message background
     * @param duration {@link Integer} duration of snackbar message
     */
    private void PrintSnackbar(int message, int color, int duration)
    {
        Snackbar snackbar = Snackbar.make(this.getActivity().findViewById(R.id.linearLayout), message, duration)
                .setAction("Action", null);
        View snackview = snackbar.getView();
        snackview.setBackgroundColor(color);
        snackbar.show();
    }

    /**
     * Validates the form by checking if all the necessary fields have been filled
     * @return {@link Boolean} true if validated, false if not validated
     */
    private boolean ValidateForm(){
        if (CourseSpinner.getSelectedItemPosition() != 0 )
            if (!(GroupNameInput.getText().toString().compareTo("")==0) )
                if (!(GroupDescInput.getText().toString().compareTo("") == 0))
                    return user.size() > 1;

        return false;
    }

    /**
     * Removes the student given by {@code view} from the {@link CreateGroupFragment#StudentList}
     * @param view {@link View} representing student to be deleted
     */
    public static void DeleteEntry(View view)
    {
        Chip elementDelete = (Chip) (view);
        String element  = elementDelete.getText().toString();
        boolean removed = false;

        int i = 0 ;
        Log.i(FRAGMENT_NAME, element);

        while(!removed && i< user.size()) {
            if (user.get(i).compareTo(elementDelete.getText().toString()) == 0 && i!=0) {
                students.add(user.get(i));
                String removeditem = (String) removeditems.get(user.get(i));
                memberIds.add(removeditem);

                for (int j = 0; j< finalmemberIds.size(); j++)
                {
                    if (finalmemberIds.get(j).equals(removeditem))
                    {
                        finalmemberIds.remove(j);
                    }
                }
                removeditems.remove(user.get(i));
                user.remove(i);

                removed = true;
                adapter.notifyDataSetChanged();
                addstudentadapter.notifyItemRemoved(i);
                addstudentadapter.notifyItemRangeChanged(i, user.size() - i);

            }
            i++;
        }
    }

    /**
     * Write group information to group document of database
     */
    public void WriteToDatabase()
    {
        Log.i(FRAGMENT_NAME, "Writing to database ");

        for (String member : memberIds)
        {
            Log.i(FRAGMENT_NAME, member);
        }
        finalmemberIds.add(0, UserId);
        ArrayList<String> messages = new ArrayList<String>();
        Map<String, Object> group = new HashMap<>();
        group.put("course", courses.get(CourseSpinner.getSelectedItemPosition()));
        group.put("description", GroupDescInput.getText().toString());
        group.put("name", GroupNameInput.getText().toString());
        group.put("members", user);
        group.put("memberEmails", finalmemberEmails);
        group.put("memberIds", finalmemberIds);
        group.put("messages", messages);
        //  ProgressIndicator.setProgressCompat(50, true);


        db.collection("groups")
                .add(group)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(FRAGMENT_NAME, "DocumentSnapshot added with ID: " + documentReference.getId());
                        //ProgressIndicator.setProgressCompat(100, true);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(FRAGMENT_NAME, "Error adding document", e);
                    }
                });
        Log.i(FRAGMENT_NAME, "Writing to database completed ");
        getFragmentManager().popBackStack();

    }


    /***
     * Gets the userId that is passed on and finds the name of the user to add to the list of members
     * Populates the Course Spinner with the data the user enters
     *
     * @param UserId
     */
    public void PopulateFormDb(String UserId) {
        Log.i(FRAGMENT_NAME, " " +  UserId);
        DocumentReference docRef = db.collection("user").document(UserId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        user.add ( (String) document.getString("first_name") + " " + document.getString("last_name")) ;
                        addstudentadapter.notifyItemInserted(user.size());
                        Log.i(FRAGMENT_NAME, "Courses : " + document.get("courses"));

                        courses.add("None");
                        ArrayList<String> dbCourses = (ArrayList<String> ) document.get("courses");

                        for (int i =0; i< dbCourses.size(); i++) {
                            courses.add(dbCourses.get(i));
                        }
                        adapter2.notifyDataSetChanged();


                    } else {
                        Log.d(FRAGMENT_NAME, "No such document");
                    }
                } else {
                    Log.d(FRAGMENT_NAME, "get failed with ", task.getException());
                }
            }
        });

    }

    /**
     * Calls the super's {@code onStop()} and clears {@link CreateGroupFragment#user} and
     * {@link CreateGroupFragment#students}
     */
    @Override
    public void onStop() {
        super.onStop();
        user.clear();
        students.clear();
        Class.setVisibility(View.VISIBLE);
        Group.setVisibility(View.VISIBLE);
        appoints.setVisibility(View.VISIBLE);

        // courses.clear();
    }


    public void onPause()
    {

        super.onPause();
        Class.setVisibility(View.VISIBLE);
        Group.setVisibility(View.VISIBLE);
        appoints.setVisibility(View.VISIBLE);

    }



}

//eria99400@mylaurier.ca
//abcdABCD1234$