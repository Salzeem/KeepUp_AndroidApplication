package com.example.android;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * This Activity setups up a view to act as the "Home" screen
 */
public class MainActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME="MainActivity";
    public static String UserId;
    Button classButton;
    Button groupButton;
    Button appointmentButton;

    /**
     * Inflates {@link R.menu#menu_main} with {@code menu}
     * @param menu {@link Menu} menu to be inflated
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
     // Might need to change, this actually loads the first fragment and pases the Bundle info

        return true;
    }

    /**
     * Assigns functions for each menu item to run when selected
     * @param item {@link MenuItem} menu item that has been selected
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
//            case R.id.action_settings:
//                return true;
            case R.id.help:
                showHelp();
                return true;
            case R.id.Signout:
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
        final View views = inflater.inflate(R.layout.help_dialog_box, null);

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
    public void SignUserOut() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(this, "Signed Out! ", Toast.LENGTH_LONG);
    }

    public static String title="Home";

    /**
     * Sets up the home screen view, and removes all fragments from the support fragment
     * manager
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(title);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        setContentView(R.layout.activity_main);
        Bundle extras=getIntent().getExtras();
        UserId= extras.getString("userID");
        appointmentButton=findViewById(R.id.schedule_button);
        classButton=findViewById(R.id.class_button);
        groupButton=findViewById(R.id.groups_button);
        classButton.setBackgroundColor(Color.GREEN);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        FragmentManager fragmentManager =getSupportFragmentManager();

        classButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classButton.setBackgroundColor(Color.GREEN);
                groupButton.setBackgroundColor(getResources().getColor(R.color.purple_500));
                appointmentButton.setBackgroundColor(getResources().getColor(R.color.purple_500));

                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView,ClassFragment.class,extras)
                        .addToBackStack("tempBackStack")
                        .commit();
            }
        });
        classButton.performClick();
        appointmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classButton.setBackgroundColor(getResources().getColor(R.color.purple_500));
                groupButton.setBackgroundColor(getResources().getColor(R.color.purple_500));
                appointmentButton.setBackgroundColor(Color.GREEN);

                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView,AppointmentFragment.class,null)
                        .setReorderingAllowed(true)
                        .addToBackStack("tempBackStack")
                        .commit();
            }
        });

        groupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                classButton.setBackgroundColor(getResources().getColor(R.color.purple_500));
                groupButton.setBackgroundColor(Color.GREEN);
                appointmentButton.setBackgroundColor(getResources().getColor(R.color.purple_500));
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerView,GroupFragment.class,extras)
                        .setReorderingAllowed(true)
                        .addToBackStack("tempBackStack")
                        .commit();
            }
        });

    }


    /**
     * Calls the super destroy method
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}