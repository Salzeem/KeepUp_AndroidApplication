package com.example.android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * This Activity setups up a view to act as the "Main activity" screen for the admin
 */
public class AdminMainActivity extends AppCompatActivity {
    Button adminClassBtn;
    Button adminAppointmentBtn;
    Button adminGroupsBtn;
    ArrayList<Class> fragments = new ArrayList<>();



    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String userID;

    /**
     * Inflates the given menu
     * @param menu {@link Menu} to inflate
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main2, menu);
        // Might need to change, this actually loads the first fragment and pases the Bundle info

        return true;
    }

    /**
     * Handle given menu item being selected
     * @param item {@link MenuItem} that's been selected
     * @return {@link Boolean} return value from calling the supers {@code onOptionsItemSelected()}
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
//            case R.id.action_settings:
//                return true;
            case R.id.help2:
                showHelp();
                return true;
            case R.id.Signout2:
                SignUserOut();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Displays a "Help" dialog box
     */
    public void showHelp()
    {

        LayoutInflater inflater = this.getLayoutInflater();
        final View views = inflater.inflate(R.layout.admin_help_dialog_box, null);

        AlertDialog.Builder customDialog =  new AlertDialog.Builder(this);
        customDialog.setView(views)
                .setPositiveButton(R.string.Close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        Dialog dialog = customDialog.create();
        dialog.show();
    }

    /**
     * Switches view to LoginActivity
     */
    public void SignUserOut()
    {
        finish();
    }

    /**
     * Sets up the view for this activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        Bundle extras=getIntent().getExtras();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.admin_toolbar);
        setSupportActionBar(myToolbar);
        fragments.add(AdminGroupFragment.class);
        fragments.add(AppointmentFragment.class);

        ImageView LeftArrow = findViewById(R.id.LeftArrow);
        ImageView RightArrow = findViewById(R.id.RightArrow);



//        Bundle extras=getIntent().getExtras();
//        userID = extras.getString("userID");

        FragmentManager fragmentManager =getSupportFragmentManager();

        adminGroupsBtn=findViewById(R.id.admin_groups_button);
        adminGroupsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction()
                        .replace(R.id.adminFragmentContainerView,AdminGroupFragment.class,extras)
                        .setReorderingAllowed(true)
                        .addToBackStack("tempBackStack")
                        .commit();
            }
        });
        LeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getTitle().equals("Admin Appointments"))
                {
                    fragmentManager.beginTransaction()
                            .replace(R.id.adminFragmentContainerView,AdminGroupFragment.class,extras)
                            .setReorderingAllowed(true)
                            .addToBackStack("tempBackStack")
                            .commit();
                }
                else if (getTitle().equals("Groups"))
                {
                    fragmentManager.beginTransaction()
                            .replace(R.id.adminFragmentContainerView,AdminAppointmentFragment.class,extras)
                            .setReorderingAllowed(true)
                            .addToBackStack("tempBackStack")
                            .commit();
                }
            }
        });
        RightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getTitle().equals("Admin Appointments"))
                {
                    fragmentManager.beginTransaction()
                            .replace(R.id.adminFragmentContainerView,AdminGroupFragment.class,extras)
                            .setReorderingAllowed(true)
                            .addToBackStack("tempBackStack")
                            .commit();
                }
                else if (getTitle().equals("Groups"))
                {
                    fragmentManager.beginTransaction()
                            .replace(R.id.adminFragmentContainerView,AdminAppointmentFragment.class,extras)
                            .setReorderingAllowed(true)
                            .addToBackStack("tempBackStack")
                            .commit();
                }
            }
        });




        adminAppointmentBtn=findViewById(R.id.admin_schedule_button);
        adminAppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.beginTransaction()
                        .replace(R.id.adminFragmentContainerView,AdminAppointmentFragment.class,extras)
                        .setReorderingAllowed(true)
                        .addToBackStack("tempBackStack")
                        .commit();
            }
        });
        adminGroupsBtn.performClick();

    }

    /**
     * Calls the supers {@code onDestroy()}
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}