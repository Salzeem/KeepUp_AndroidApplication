package com.example.android;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import org.checkerframework.common.subtyping.qual.Bottom;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListView_transiton#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListView_transiton extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "Name";
    private static final String ARG_PARAM2 = "Description";
    private static final String ARG_PARAM3 = "Course";
    private static final String ARG_PARAM4 = "pos";
    private static final String ARG_PARAM5 = "groupID";
    private  Dialog dialog;

    public static String FRAGMENT_NAME = "LISTIVIEW_TRANSITION";
    private ArrayList<String> groupmembers;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    String userID = user.getUid();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    private int  mParam4;
    private String mParam5;

    public ListView_transiton() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListView_transiton.
     */
    // TODO: Rename and change types and number of parameters
    public static ListView_transiton newInstance(String param1, String param2, String param3) {
        ListView_transiton fragment = new ListView_transiton();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = getArguments().getInt(ARG_PARAM4);
            mParam5 = getArguments().getString(ARG_PARAM5);
        }

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View Fragment_transition_view =inflater.inflate(R.layout.fragment_list_view_transiton, container, false);
        TransitionInflater inflaters = TransitionInflater.from(requireContext());
        setEnterTransition(inflaters.inflateTransition(R.transition.fade));
        setExitTransition(inflaters.inflateTransition(R.transition.fade));


        BottomAppBar AppBar = Fragment_transition_view.findViewById(R.id.bottomAppBar);

        AppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.edit:
                        EditGroupDialog();
                        return true;
                    case R.id.search:
                        RemoveGroup();
                        return true;
                    default:
                        return false;
                }
            }
        });
/*
        setHasOptionsMenu(true);
*/
        DocumentReference docRef = db.collection("groups").document(mParam5);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        groupmembers =  (ArrayList<String>) document.get("members");
                        cont(Fragment_transition_view);

                    } else {
                        Log.d(FRAGMENT_NAME, "No such document");
                    }
                } else {
                    Log.d(FRAGMENT_NAME, "get failed with ", task.getException());
                }
            }
        });




        return  Fragment_transition_view;






    }
    public void cont( View Fragment_transition_view)
    {
        TextView GroupName = Fragment_transition_view.findViewById(R.id.GroupNameLabel);
        TextView CourseName = Fragment_transition_view.findViewById(R.id.coursename);
        TextView GroupDesc = Fragment_transition_view.findViewById(R.id.GroupDesclabel);
        FloatingActionButton Chat = Fragment_transition_view.findViewById(R.id.chat);
        ArrayList<ShapeableImageView> images = new ArrayList<>();
        ArrayList<TextView> names = new ArrayList<>();

        images.add(Fragment_transition_view.findViewById(R.id.student1));
        images.add(Fragment_transition_view.findViewById(R.id.student2));
        images.add(Fragment_transition_view.findViewById(R.id.student3));
        images.add(Fragment_transition_view.findViewById(R.id.student4));

        names.add(Fragment_transition_view.findViewById(R.id.captionstudent1));
        names.add(Fragment_transition_view.findViewById(R.id.captionstudent2));
        names.add(Fragment_transition_view.findViewById(R.id.captionstudent3));
        names.add(Fragment_transition_view.findViewById(R.id.captionstudent4));



        for (int i =0 ; i< groupmembers.size(); i++)
        {
            if (!groupmembers.get(i).equals(user.getDisplayName()))
            {
                images.get(i).setVisibility(View.VISIBLE);
                names.get(i).setVisibility(View.VISIBLE);
                names.get(i).setText(groupmembers.get(i));

            }
        }


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String UserID = user.getUid();
        String Name = user.getDisplayName();

        GroupName.setText(mParam1);
        CourseName.setText(mParam3);
        GroupDesc.setText(mParam2);
        Chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), Groupchat.class);
                intent.putExtra("groupid", mParam5);
                DocumentReference docRef = db.collection("user").document(UserID);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String name =  (String) document.getString("first_name") + " " + document.getString("last_name") ;
                                intent.putExtra("name", name);
                                startActivity(intent);

                            } else {
                                Log.d(FRAGMENT_NAME, "No such document");
                            }
                        } else {
                            Log.d(FRAGMENT_NAME, "get failed with ", task.getException());
                        }
                    }



                });

            }
        });
    }



public void RemoveGroup()
{
    String GroupName = mParam1;

    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());

    builder.setTitle("Remove from: " +  GroupName);
    builder.setIcon(R.drawable.delete_icon);
    builder.setBackground(getResources().getDrawable(R.drawable.dialog_background, null));
    builder.setMessage("Are you sure you want to be removed from this group?");
    builder.setPositiveButton(R.string.Yes,
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {

                    Log.i(FRAGMENT_NAME, "This is the user: " + mParam1);
                    deletefromdatabase(mParam5, mParam1 ); //TODO: change mparam2 to username
                } });
    builder.setNegativeButton("Cancel",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                } });
    Dialog dialog = builder.create();
    dialog.show();
}
    /***
     * Removes the user from the group.
     * @param groupid : Instance id of the group
     * @param username : Name of the user who wants to be removed
     */
    public void deletefromdatabase(String groupid, String username) {


        DocumentReference groupsRef = db.collection("groups").document(groupid);
        Log.i(FRAGMENT_NAME, "Group id " + groupid + " username " + username);
        groupsRef.update("members", FieldValue.arrayRemove(username));
        groupsRef.update("memberIds", FieldValue.arrayRemove(userID));
    }

    public void EditGroupDialog()
    {
        AlertDialog.Builder customDialog;
        final View views = getLayoutInflater().inflate(R.layout.custom_dialog_edit_groupinfo, null);

        Log.i(FRAGMENT_NAME, "List Item Clicked");



        customDialog = new AlertDialog.Builder(getContext());
        customDialog.setView(views)
                .setPositiveButton(R.string.SaveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        boolean changed = false;
                        EditText NewGroupName = views.findViewById(R.id.GroupNameEdit);
                        EditText NewGroupDesc = views.findViewById(R.id.GroupDescEdit);

                        if (NewGroupName.getText().toString().compareTo("") != 0 ){
                            changed = true;

                        }
                        if (NewGroupDesc.getText().toString().compareTo("") != 0 ) {
                            changed = true;
                        }

                        if(changed) {
                            EditText gname= views.findViewById(R.id.GroupNameEdit);
                            EditText gdesc = views.findViewById(R.id.GroupDescEdit);

                            String groupname = gname.getText().toString();
                            String groupdesc = gdesc.getText().toString();

                            updateDatabase(mParam5,groupname , groupdesc);
                            getActivity().finish();
                        }

                    }

                });
        dialog = customDialog.create();
        dialog.show();

    }
    public boolean updateDatabase(String id, String groupname, String groupdescription)
    {
        Log.i(FRAGMENT_NAME, "Message id: " + id);
        DocumentReference groupRef = db.collection("groups").document(id);
        groupRef
                .update("name", groupname)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(FRAGMENT_NAME, "Group name successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(FRAGMENT_NAME, "Error updating document id:" + id, e);

                    }
                });

        groupRef
                .update("description", groupdescription)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(FRAGMENT_NAME, "Group Desc successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(FRAGMENT_NAME, "Error updating document", e);
                    }
                });
        return true;
    }
}