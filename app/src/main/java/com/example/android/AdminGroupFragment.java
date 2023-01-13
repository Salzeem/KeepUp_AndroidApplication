package com.example.android;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * This fragment creates a view for displaying "Group" information
 */
public class AdminGroupFragment extends Fragment {
    protected static final String FRAGMENT_NAME="ADMINGroupFragment";
    protected static int colorIcon[] = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};

    private ViewGroupsAdapter adapter;
    private ArrayList<GroupsInformation> groups = new ArrayList<GroupsInformation>();
    private ListView GroupLists;
    private TextView GroupName;
    private View result;
    private TextView message;
    private ImageView GroupIcon;
    private  TextView GroupDescription;
    private   GroupsInformation info;
    private CardView CardGroup;
    private LayoutInflater inflater;
    private  TextView nogroupinfo;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private  ImageView refresh;
    private  AlertDialog.Builder customDialog;
    private  Dialog dialog;
    private Button StartChatBtn;
    private  String name;
    protected static RecyclerViewAdapter_AddStudent addstudentadapter;
    protected RecyclerView StudentList;

    /**
     * Class for storing information about a group
     */
    private static class GroupsInformation
    {
        private final String  id;  //Primary Key
        private String NameofGroup;
        private String GroupDescription;
        private  String coursename ;
        private ArrayList<String> memberEmails;

        public  ArrayList<String> members;

        /**
         * Creates a new {@link GroupsInformation} with given parameters
         * @param id {@link String} group identifier
         * @param NameofGroup {@link String} group name
         * @param GroupDescription {@link String} group description
         * @param coursename {@link String} name of the course the group is for
         * @param members {@link ArrayList} of the members names
         * @param memberEmails {@link ArrayList} of the members emails
         * @param memberIds unused
         */
        private GroupsInformation(String id , String NameofGroup, String GroupDescription,String coursename, ArrayList<String> members, ArrayList<String> memberEmails,  ArrayList<String> memberIds) {
            this.id = id;
            this.NameofGroup = NameofGroup ;
            this.GroupDescription =GroupDescription;
            this.coursename = coursename;
            this.members = members;
            this.memberEmails = memberEmails;


        }

        /**
         * Gets the name of the course the group is for
         * @return course name
         */
        public String getCoursename() {return  this.coursename;}

        /**
         * Gets the group identifier
         * @return group identifier
         */
        public String  getid()
        {
            return this.id;
        }

        /**
         * Gets the name of the group
         * @return name of group
         */
        public String getNameofGroup() {return this.NameofGroup;}

        /**
         * Gets the description of the group
         * @return group description
         */
        public String getGroupDescription() {return this.GroupDescription;}

        /**
         * Gets the members of the groups emails
         * @return member emails
         */
        public ArrayList<String> getMemberEmails(){return  this.memberEmails;}

        /**
         * Sets the members names of the group to {@code newmembers}
         * @param newmembers {@link ArrayList} of members names
         */
        public void setMembers(ArrayList<String> newmembers){this.members = newmembers;}

        /**
         * Sets the description of the group
         * @param newGroupDesc {@link String} description of the group
         */
        public void setGroupDescription(String newGroupDesc){this.GroupDescription = newGroupDesc; }

        /**
         * Sets the name of the group
         * @param newName {@link String} name of the group
         */
        public void setGroupName(String newName){this.NameofGroup = newName; }

        /**
         * Gets the members names of the group
         * @return members names of the group
         */
        public ArrayList<String> getMembers() {return this.members;}

    }

    /**
     * Subclass of {@link ArrayAdapter} to be attached to ListViews containing groups
     */
    private class ViewGroupsAdapter extends ArrayAdapter<String> {
        /**
         * Calls the supers constructor
         * @param context
         * @param resource
         */
        public ViewGroupsAdapter(@NonNull Context context, int resource) {
            super(context, resource);
        }

        /**
         * Gets the number of groups
         * @return number of groups
         */
        public int getCount() {
            return groups.size();
        }

        /**
         * Get a group based on its {@code position} in the list
         * @param position {@link Integer} index into list of groups
         * @return {@link GroupsInformation} indexed group
         */
        public GroupsInformation fetchitem(int position) {
            return groups.get(position);
        }

        /**
         * Sets up the view for displaying group information and populates it with
         * group information based on {@code position}
         * @param position {@link Integer} index into list of groups
         * @param convertView unused
         * @param parent unused
         * @return created {@link View}
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            inflater = AdminGroupFragment.this.getLayoutInflater();
            result = inflater.inflate(R.layout.group_list_view, null);
            message =  result.findViewById(R.id.GroupNameLabel);
            ImageView deleteButton = result.findViewById(R.id.RemoveGroup);
            ImageView Editbutton = result.findViewById(R.id.EditIcon);
            Editbutton.setVisibility(View.INVISIBLE);
            GroupIcon =  result.findViewById(R.id.GroupIconDisplay);
            GroupDescription = result.findViewById(R.id.GroupDescriptionLabel);
            CardGroup = result.findViewById(R.id.CardGroup);




            info  = fetchitem(position);
            deleteButton.setTag(position);
            GroupIcon.setTag(position);
            CardGroup.setTag(position);
            Editbutton.setTag(position);

            message.setText(info.getNameofGroup());
            GroupDescription.setText(info.getGroupDescription());
            GroupIcon.setColorFilter((colorIcon[ (int)Math.floor(Math.random()*(colorIcon.length))]));



            CardView cardGroupCV=result.findViewById(R.id.CardGroup);
            cardGroupCV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ShowGroupInfo(view);
                }
            });


            ImageView removeGroupIV=result.findViewById(R.id.RemoveGroup);
            removeGroupIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DeleteGroupEntry(view);
                }
            });

            ImageView editIconIV=result.findViewById(R.id.EditIcon);
            editIconIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditGroups(view);
                }
            });



            return result;
        }

    }

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String UserID  = "userID";
    private static final String username = "username";

    private String mParam1;
    private String mParam2;

    /**
     * Required empty public constructor
     */
    public AdminGroupFragment() {
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

        String title="Groups";
        getActivity().setTitle(title);
        View test=inflater.inflate(R.layout.fragment_group, container, false);
        GroupLists = test.findViewById(R.id.GroupinformationList);
        nogroupinfo = test.findViewById(R.id.NoGroupinfo);
        refresh = test.findViewById(R.id.refresh);

        adapter = new ViewGroupsAdapter(this.getContext(), 0 );
        GroupLists.setAdapter(adapter);
        nogroupinfo.setText(R.string.NoGroupsPage);
        nogroupinfo.setVisibility(View.VISIBLE);
        GroupLists.setVisibility(View.INVISIBLE);

        CardView btn=test.findViewById(R.id.Banner);
        btn.setVisibility(View.INVISIBLE);
        //btn.setVisibility(View.INVISIBLE);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RefreshPage(view);
            }
        });
        Log.i(FRAGMENT_NAME, "This is the user ID: " + mParam1 );
        DisplayGroupInfo(mParam1);



        return test;
    }


    /**
     * Stores fragment parameters in {@link AdminGroupFragment#mParam1} and {@link AdminGroupFragment#mParam2}. And
     * grab the specified users ({@link AdminGroupFragment#mParam1}) name from the database
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(UserID);
            mParam2 = getArguments().getString(username);

        }
    }

    /**
     * Calls supers {@code onStop()} and dismisses dialog if it is open
     */
    @Override
    public void onStop() {
        super.onStop();
        Log.i(FRAGMENT_NAME, "In OnStop");
        if (dialog != null)
        {
            dialog.dismiss();
        }

    }

    /**
     * Calls supers {@code onPause()} and dismisses dialog if it is open
     */
    @Override
    public void onPause() {
        super.onPause();
        if (dialog != null)
        {
            dialog.dismiss();
        }
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
/*        groups.clear();
        DisplayGroupInfo(mParam2);*/

        // Toast.makeText(getActivity(), "New Change", Toast.LENGTH_SHORT).show();

    }

    /**
     * Queries the database, filtering information based on user registered and adding it to the UI
     * @param name of the user that queries the data
     */
    public void DisplayGroupInfo(String name)
    {
        ArrayList<String> course_namecode = new ArrayList<String>();
        Log.i(FRAGMENT_NAME, "THis is the instructor id " + name);
        db.collection("courses").whereEqualTo("instructor_id", name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                course_namecode.add(document.get("code") + " " + document.get("section"));

                            }
                            completeAdminDisplay(course_namecode);





                            //Toast.makeText(getActivity(), "Page Refreshed ", Toast.LENGTH_LONG).show();
                        } else {
                            Log.w("AdminGroup", "Error getting documents or no changes yet.", task.getException());
                        }
                    }

                });

        // Log.i(FRAGMENT_NAME, "This is the second param: " + name );

    }

    /**
     * Populates the {@link AdminGroupFragment#groups} array list with the groups that exist
     * for the courses the admin teaches
     * @param courses_taught {@link ArrayList} courses the admin teaches
     */
    public void completeAdminDisplay(ArrayList<String> courses_taught)
{
    groups.clear();

    for (int i =0; i< courses_taught.size(); i++ ) {
        Log.i(FRAGMENT_NAME, "Courses taught: " + courses_taught.get(i));
        db.collection("groups").whereEqualTo("course", courses_taught.get(i))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentChange document : task.getResult().getDocumentChanges()) {
                                String groupName = document.getDocument().getString("name");
                                String groupDesc = document.getDocument().getString("description");
                                String courseName = document.getDocument().getString("course");
                                Log.i(" Array information for members : ", "  " + document.getDocument().get("members"));
                                groups.add(new GroupsInformation(document.getDocument().getId(), groupName, groupDesc, courseName, (ArrayList<String>) document.getDocument().get("members"), (ArrayList<String>) document.getDocument().get("memberEmails"), (ArrayList<String>) document.getDocument().get("membersId")));
                                adapter.notifyDataSetChanged();
                                if (groups.size() > 0) {
                                    GroupLists.setVisibility(View.VISIBLE);
                                    nogroupinfo.setVisibility(View.INVISIBLE);

                                }
                            }

                            //Toast.makeText(getActivity(), "Page Refreshed ", Toast.LENGTH_LONG).show();
                        } else {
                            Log.w(FRAGMENT_NAME, "Error getting documents or no changes yet.", task.getException());
                        }
                    }

                });
    }
}








    /**
     * On click handler to display more information about the group information when clicked
     * @param view
     */

    public void ShowGroupInfo(View view)
    {

        int positionitem= (int) view.getTag();

        LayoutInflater inflater = this.getLayoutInflater();
        final View views = inflater.inflate(R.layout.groups_custom_dialog_box, null);
        StartChatBtn = views.findViewById(R.id.StartChat);
        StartChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Groupchat.class);
                intent.putExtra("groupid", groups.get(positionitem).id);
                DocumentReference docRef = db.collection("user").document(mParam1);

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


        Log.i(FRAGMENT_NAME, "List Item Clicked");
        GroupsInformation group = groups.get(positionitem);
        GroupName = views.findViewById(R.id.ViewGroupName);
        TextView GroupDesc = views.findViewById(R.id.ViewGroupDesc);
        TextView CourseName  = views.findViewById(R.id.ViewGroupInstructor);
        addstudentadapter = new RecyclerViewAdapter_AddStudent(this.getContext(),group.getMembers() , 2);
        StudentList = views.findViewById(R.id.RecyclerViewmembers);
        StudentList.setAdapter(addstudentadapter);
        StudentList.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false));

        GroupName.setText(group.getNameofGroup());
        GroupDesc.setText(group.getGroupDescription()) ;

        CourseName.setText(group.getCoursename());

        customDialog =  new AlertDialog.Builder(this.getContext());
        customDialog.setView(views)
                .setPositiveButton(R.string.Close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                    }
                });

        Dialog dialog = customDialog.create();
        dialog.show();

    }




    /**
     * Enables the user to edit their information, either about the group or description of the group
     * OnClick Handler of the green edit pencil icon gets called by this
     * @param view
     */

    public void EditGroups(View view )
    {

        LayoutInflater inflater = this.getLayoutInflater();
        final View views = inflater.inflate(R.layout.custom_dialog_edit_groupinfo, null);

        Log.i(FRAGMENT_NAME, "List Item Clicked");
        int positionitem= (int) view.getTag();
        GroupsInformation group = groups.get(positionitem);

        customDialog =  new AlertDialog.Builder(this.getContext());
        customDialog.setView(views)
                .setPositiveButton(R.string.SaveButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        boolean changed = false;
                        EditText NewGroupName = views.findViewById(R.id.GroupNameEdit);
                        EditText NewGroupDesc = views.findViewById(R.id.GroupDescEdit);

                        if (NewGroupName.getText().toString().compareTo("") != 0 ){
                            changed = true;
                            group.setGroupName(NewGroupName.getText().toString());
                        }
                        if (NewGroupDesc.getText().toString().compareTo("") != 0 ) {
                            changed = true;
                            group.setGroupDescription( NewGroupDesc.getText().toString());
                        }

                        if(changed) {
                            adapter.notifyDataSetChanged();
                            updateDatabase(group.getid(), group.getNameofGroup(), group.getGroupDescription());
                        }

                    }

                });
        dialog = customDialog.create();
        dialog.show();


    }


    /**
     * Gets called to update the database information once user changes either the group name or the group Description
     * @param id : The group id key of the new group instance
     * @param groupname : Name of the new group
     * @param groupdescription : Description of the group
     * @return true: If data has been succefully updated
     *
     */
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


    /***
     * On Click Handler for delete button icon, gets called when user wants to remove themselves from the group
     * Updates the arrayList and notifys data has been changed, calls the delete database fuction to notify
     * of item being deleted to the firebase
     * @param view
     */
    public void DeleteGroupEntry(View view){

        int positionitemToDelete = (int) view.getTag();
        GroupsInformation groupdel = groups.get(positionitemToDelete);
        String GroupName = groups.get(positionitemToDelete).getNameofGroup();


        AlertDialog.Builder builder =
                new AlertDialog.Builder(this.getContext());

        builder.setTitle("Are you sure you want to delete:" +  GroupName + " from your class?");
        builder.setPositiveButton(R.string.Yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        groups.remove(positionitemToDelete);
                        adapter.notifyDataSetChanged();
                        if (groups.size() == 0 )
                        {
                            nogroupinfo.setVisibility(View.VISIBLE);
                            GroupLists.setVisibility(View.INVISIBLE);
                        }
                        deletegroup(groupdel.getid() );
                    } });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    } });
        dialog = builder.create();
        dialog.show();

    }


    /***
     * Removes the user from the group.
     * @param groupid : Instance id of the group
     */
    public void deletegroup(String groupid) {
        db.collection("groups").document(groupid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        PrintToast("Group Successfully Deleted");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        PrintToast("Error Deleting Group, please try again later");
                    }
                });
    }



    /**
     * Refreshes the page when clicked
     */

    public void RefreshPage(View view)
    {
        Toast.makeText(getActivity(), "Page refreshed ", Toast.LENGTH_SHORT).show();
        DisplayGroupInfo(mParam1);
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

    /**
     * Helper method to print toast messages
     * @param message {@link String} message to print
     */
    private void PrintToast(String message)
    {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


}