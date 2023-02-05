package com.example.android;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private static ArrayList<Classes> StudentsClass = new ArrayList<Classes>();
    protected static final String FRAGMENT_NAME="ClassFragment";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final ArrayList<String> Studentclass = new ArrayList<String>();
    private Map<String,String> StudentDesc = new HashMap<String,String>();
    private ArrayList<String> courses = new ArrayList<String>();
    private static RecyclerViewList card_adpater;
    protected RecyclerView CardList;
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
     * Stores fragment parameters in {@link ClassFragment#mParam1} and {@link ClassFragment#mParam2}
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TransitionInflater inflaters = TransitionInflater.from(requireContext());
        setEnterTransition(inflaters.inflateTransition(R.transition.slide_right));
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
        View test=inflater.inflate(R.layout.view_items, container, false);
        CardList = test.findViewById(R.id.GroupinformationList2);

        card_adpater = new RecyclerViewList(this.getContext(), StudentsClass, 1  );
        CardList.setAdapter(card_adpater);
        CardList.setLayoutManager(new GridLayoutManager(this.getContext(), 2));

        FloatingActionButton btn=test.findViewById(R.id.Banner2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView,AddClassFragment.class,getArguments())
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
                                                            String courseDesc=document.getDocument().getString("description");

                                                            if(temp.contains(courseName+" "+courseSection) && !StudentDesc.containsKey(courseName + " " + courseSection)){
                                                                Log.i(FRAGMENT_NAME,courseName+" "+courseSection);
                                                                StudentDesc.put(courseName+" "+courseSection,courseDesc);
                                                                Classes item = new Classes(courseName + courseSection,  courseDesc );
                                                                StudentsClass.add(item);
                                                            }
                                                        }
                                                        card_adpater.notifyDataSetChanged();

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
     * Removes selected class from {@link ClassFragment#Studentclass} and updates the view
     * @param view
     *//*
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

*/
/*    *//**
     * Deletes course from database given {@code coursename} and {@code userID}
     * @param coursename
     * @param userID
     *//*
    public void deletefromdatabase(String coursename, String userID )
    {
        DocumentReference groupsRef = db.collection("user").document(userID);
        groupsRef.update("courses", FieldValue.arrayRemove(coursename));

    }*/

    @Override
    public void onStop() {
        super.onStop();
        StudentsClass.clear();
    }

    @Override
    public void onPause() {
        super.onPause();
        StudentsClass.clear();
    }
}