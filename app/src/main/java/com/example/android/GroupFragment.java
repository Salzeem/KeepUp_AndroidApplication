package com.example.android;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * This fragment creates a view for displaying "Group" information
 */


/**
 * Todo: Create a temp group and check for duplicates
 */
public class GroupFragment extends Fragment {
    protected static final String FRAGMENT_NAME="GroupFragment";


    private static ArrayList<GroupsInformation> groups = new ArrayList<GroupsInformation>();
    ArrayList<CardViews> cards = new ArrayList<>();
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    protected static RecyclerViewList card_adpater;
    protected RecyclerView CardList;
    public String name;




    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String UserID  = "userID";
    private static final String username = "username";

    private static String mParam1;
    private String mParam2;

    /**
     * Required empty public constructor
     */
    public GroupFragment() {
        // Required empty public constructor

    }

    /**
     * Create new fragment and set fragment parameters with {@code param1} and {@code param2}
     * @param param1
     * @param param2
     * @return Created fragment
     */
    public static GroupFragment newInstance(String param1, String param2) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putString(UserID, param1);
        args.putString(username, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Creates and returns the view for this fragment, populating it with appropriate data pertaining
     * to groups
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return Created view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        TransitionInflater inflaters = TransitionInflater.from(requireContext());
        setEnterTransition(inflaters.inflateTransition(R.transition.slide_right));
         View test =inflater.inflate(R.layout.view_items, container, false);



        if (user != null) {
            String title="Groups";
            getActivity().setTitle(title);
            CardList = test.findViewById(R.id.GroupinformationList2);
            //nogroupinfo = test.findViewById(R.id.NoGroupinfo);

            card_adpater = new RecyclerViewList(this.getContext(), groups, 0 );
            CardList.setAdapter(card_adpater);
            CardList.setLayoutManager(new GridLayoutManager(this.getContext(), 2));



            CardList.setVisibility(View.INVISIBLE);

            FloatingActionButton btn=test.findViewById(R.id.Banner2);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView,CreateGroupFragment.class,getArguments())
                            .setReorderingAllowed(true)
                            .addToBackStack("tempBackStack")
                            .commit();
                }
            });

            Log.i(FRAGMENT_NAME, "This is the user ID: " + mParam1 );
            DisplayGroupInfo(mParam1);

            if (groups.size() == 0 )
            {
                ShapeableImageView v = test.findViewById(R.id.NoGroupinf);
                v.setVisibility(View.VISIBLE);
                v.setImageResource(R.drawable.no_groups);


            }
            else
            {
                ShapeableImageView v = test.findViewById(R.id.NoGroupinf);
                v.setVisibility(View.INVISIBLE);
                v.setImageResource(R.drawable.no_groups);
            }



            return test;
        } else {
            getActivity().finish();
        }

        return test;

    }


    /**
     * Stores fragment parameters in {@link GroupFragment#mParam1} and {@link GroupFragment#mParam2}. And
     * grab the specified users (@link GroupFragment#mParam1}) name from the database
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        if (getArguments() != null) {
            mParam1 = getArguments().getString(UserID);
            mParam2 = getArguments().getString(username);
            DocumentReference docRef = db.collection("user").document(mParam1);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            name =  (String) document.getString("first_name") + " " + document.getString("last_name") ;

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

    /**
     * Calls supers {@code onStop()} and dismisses dialog if it is open
     */
    @Override
    public void onStop() {
        super.onStop();
        Log.i(FRAGMENT_NAME, "In OnStop");

    }

    /**
     * Calls supers {@code onPaus()} and dismisses dialog if it is open
     */
    @Override
    public void onPause() {
        super.onPause();
        Log.i(FRAGMENT_NAME, "In onPause");

    }

    /**
     * Calls supers {@code onDestroy()}
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * Calls supers {@code onResume()}
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * Queries the database, filtering information based on user registered and adding it to the UI
     * Refreshes page every 3 seconds for updates from the firebase
     * @param name of the user that queries the data
     */
    public void DisplayGroupInfo(String name)
    {

        db.collection("groups").whereArrayContains("memberIds", name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            groups.clear();

                            for (DocumentChange document : task.getResult().getDocumentChanges() ) {


                                String groupName = document.getDocument().getString("name");
                                String groupDesc = document.getDocument().getString("description");
                                String courseName = document.getDocument().getString("course");
                                CardViews info = new CardViews(groupName, groupDesc, courseName);
                                Log.i( " Array information for members : ", "  " +  document.getDocument().get("members") ) ;
                                groups.add(new GroupsInformation(document.getDocument().getId(), groupName, groupDesc, courseName, (ArrayList<String>) document.getDocument().get("members"), (ArrayList<String>) document.getDocument().get("memberEmails"), (ArrayList<String>) document.getDocument().get("membersId"))) ;
                                cards.add(info);
                                card_adpater.notifyDataSetChanged();
                                if (groups.size()>0 )
                                {
                                    CardList.setVisibility(View.VISIBLE);
                                   // nogroupinfo.setVisibility(View.INVISIBLE);

                                }
                            }

                            //Toast.makeText(getActivity(), "Page Refreshed ", Toast.LENGTH_LONG).show();
                        } else {
                            Log.w(FRAGMENT_NAME, "Error getting documents or no changes yet.", task.getException());
                        }
                    }

                });

        // Log.i(FRAGMENT_NAME, "This is the second param: " + name );

    }


    /**
     * Debugging functionality for Snackbar
     * @param message message to display
     * @param color   color of the snackbar
     * @param duration duration of the time it takes to display
     */
    private void PrintSnackbar(String message, int color, int duration)
    {
        Snackbar snackbar = Snackbar.make(this.getActivity().findViewById(R.id.linearLayout), message, duration)
                .setAction("Action", null);
        View snackview = snackbar.getView();
        snackview.setBackgroundColor(color);
        snackbar.show();
    }



}