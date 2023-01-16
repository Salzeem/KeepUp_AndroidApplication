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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * This fragment provides a view for interacting with student classes
 */
public class ClassFragment extends Fragment {
    protected static final String FRAGMENT_NAME="ClassFragment";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    protected static int[] colorIcon = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
    private  ViewClassAdpater adapter;
    private final ArrayList<String> Studentclass = new ArrayList<String>();
    private Map<String,String> StudentDesc = new HashMap<String,String>();
    private ArrayList<String> courses = new ArrayList<String>();

    private ListView Classlist;
    TextView noclassinfo;

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
    public ClassFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClassFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static ClassFragment newInstance(String param1, String param2) {
        ClassFragment fragment = new ClassFragment();
        Bundle args = new Bundle();
        args.putString(UserID, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Subclass of {@link ArrayAdapter} for {@link R.layout#group_list_view}
     */
    private class ViewClassAdpater extends ArrayAdapter<String> {
        /**
         * Calls parent constructor
         * @param context
         * @param resource
         */
        public ViewClassAdpater(@NonNull Context context, int resource) {
            super(context, resource);
        }

        /**
         * Returns number of elements in {@link ClassFragment#Studentclass}
         * @return {@link Integer} number of students in {@link ClassFragment#Studentclass}
         */
        public int getCount() {
            return Studentclass.size();
        }


        /**
         * Sets up view by inflating {@link R.layout#group_list_view} and setting
         * the contents of the inflated view appropriately
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            LayoutInflater inflater = ClassFragment.this.getLayoutInflater();
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

                message.setText(Studentclass.get(position));
                groupDescription.setText(StudentDesc.get(Studentclass.get(position)));


                classIcon.setImageResource(R.drawable.class_icon);
                classIcon.setColorFilter((colorIcon[(int) Math.floor(Math.random() * (colorIcon.length))]));
                classIcon.setVisibility(View.INVISIBLE);

                ImageView removeClassIV = result.findViewById(R.id.RemoveGroup);
                removeClassIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RemoveClass(view);
                    }
                });

                cardGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

            }
            else
            {
                getActivity().finish();
            }
            return result;
        }

    }

    /**
     * Stores fragment parameters in {@link ClassFragment#mParam1} and {@link ClassFragment#mParam2}
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.i(FRAGMENT_NAME, "Bundle Received");
            mParam1 = getArguments().getString(UserID);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /**
     * Sets up view for {@link ClassFragment} and replaces selected {@link ClassFragment#Classlist} item
     * with {@link AddClassFragment}. It also calls {@link ClassFragment#DisplayRegClasses(ArrayList)} if
     * {@link ClassFragment#mParam1} is not null
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        String title="Classes";
        getActivity().setTitle(title);
        Studentclass.clear();
        View test=inflater.inflate(R.layout.fragment_group, container, false);
        Classlist= test.findViewById(R.id.GroupinformationList);
        TextView nogroupinfo = test.findViewById(R.id.NoGroupinfo);
        TextView addclassBtn= test.findViewById(R.id.BannerText);
        ImageView refreshBtn=test.findViewById(R.id.refresh);
        refreshBtn.setVisibility(View.INVISIBLE);
        addclassBtn.setText("Add a class");
        adapter = new ViewClassAdpater(this.getContext(), 0);
        Classlist.setAdapter(adapter);
        nogroupinfo.setText(R.string.noClassAdded);
        nogroupinfo.setVisibility(View.INVISIBLE);
        Classlist.setVisibility(View.VISIBLE);
        TransitionInflater inflaters = TransitionInflater.from(requireContext());
        setEnterTransition(inflaters.inflateTransition(R.transition.slide));
        CardView btn=test.findViewById(R.id.Banner);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,AddClassFragment.class,getArguments()) //TODO: Change to Add Class Fragment
                        .setReorderingAllowed(true)
                        .addToBackStack("tempBackStack")
                        .commit();
            }
        });

        Log.i(FRAGMENT_NAME, "This is the param "+ mParam1);
        if (mParam1!=null) {
            db.collection("user").whereEqualTo(FieldPath.documentId(), mParam1)  // ERR; Main activity is not passing the dat
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    ArrayList<String> temp=(ArrayList<String>) document.get("courses");

                                    db.collection("courses")
                                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (DocumentChange document : task.getResult().getDocumentChanges()) {
                                                            String courseName = document.getDocument().getString("code");
                                                            String courseSection = document.getDocument().getString("section");
                                                            String courseTerm=document.getDocument().getString("term");
                                                            String courseDesc=document.getDocument().getString("description");

                                                            if(temp.contains(courseName+" "+courseSection)){
                                                                Log.i(FRAGMENT_NAME,courseName+" "+courseSection);
                                                                StudentDesc.put(courseName+" "+courseSection,courseDesc);
                                                            }
                                                        }
                                                        DisplayRegClasses(temp);

                                                    } else {
                                                        Log.w(FRAGMENT_NAME, "Error getting documents or no changes yet.", task.getException());
                                                    }
                                                }
                                            });
//
                                }
                            } else {
                                Log.w(FRAGMENT_NAME, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }

        return test;
    }

    /**
     * Updates {@link ClassFragment#Studentclass} with courses from {@code studentClasses} and updates
     * view
     * @param studentClasses {@link ArrayList} of classes
     */
    public void DisplayRegClasses(ArrayList<String> studentClasses )
    {

        for (String course : studentClasses)
        {
            Studentclass.add(course);
        }
        Log.i(FRAGMENT_NAME, "Courses: " + Studentclass.size());
        adapter.notifyDataSetChanged();
    }


    /**
     * Removes selected class from {@link ClassFragment#Studentclass} and updates the view
     * @param view
     */
    public void RemoveClass(View view )
    {
        int positionitemToDelete = (int) view.getTag();
        String coursename = Studentclass.get(positionitemToDelete);

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this.getContext());

        builder.setTitle("Are you sure you want to be removed from:" +  coursename);
        builder.setPositiveButton(R.string.Yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Studentclass.remove(positionitemToDelete);
                        adapter.notifyDataSetChanged();
                        if (Studentclass.size() == 0 )
                        {


                            noclassinfo.setVisibility(View.VISIBLE);
                            Classlist.setVisibility(View.INVISIBLE);
                        }
                        deletefromdatabase(coursename, mParam1 );
                    } });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    } });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /**
     * Deletes course from database given {@code coursename} and {@code userID}
     * @param coursename
     * @param userID
     */
    public void deletefromdatabase(String coursename, String userID )
    {
        DocumentReference groupsRef = db.collection("user").document(userID);
        groupsRef.update("courses", FieldValue.arrayRemove(coursename));

    }

}