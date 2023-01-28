package com.example.android;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * This Activity setups up a view to act as the "Home" screen
 */
public class MainActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME="MainActivity";
    public static String UserId;


    /**
     * Inflates {@link R.menu#menu_main} with {@code menu}
     * @param menu {@link Menu} menu to be inflated
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public  String getUserId() {
        return UserId;
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
        finish();
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
        setContentView(R.layout.activity_main);
        Bundle extras=getIntent().getExtras();
        UserId= extras.getString("userID");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        FragmentManager fragmentManager =getSupportFragmentManager();
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.appointments:
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainerView,AppointmentFragment.class,null)
                                .setReorderingAllowed(true)
                                .addToBackStack(null)
                                .commit();
                        return true;

                    case R.id.classes:
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainerView,ClassFragment.class,extras)
                                .addToBackStack(null)
                                .commit();
                        return true;
                    case R.id.groups:
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainerView,GroupFragment.class,extras)
                                .setReorderingAllowed(true)
                                .addToBackStack(null)
                                .commit();
                        return true;
                    default:
                        return true;



                }
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