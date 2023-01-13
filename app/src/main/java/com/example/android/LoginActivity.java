package com.example.android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * This Activity setups up a view to act as the "Login" screen
 */
public class LoginActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME="LoginActivity";
    private FirebaseAuth mAuth;
    private  EditText usernameET;
    private  EditText passwordET;
    private  static final String email_identifier = "@gmail.com";

    /**
     * Sets up the view for {@link LoginActivity}, grabs the firebase authorization instance and
     * stores it in {@link LoginActivity#mAuth}
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setAllowEnterTransitionOverlap(false);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        setTitle("KeepUp!");

        mAuth = FirebaseAuth.getInstance();
        Log.i(ACTIVITY_NAME,"In OnCreate()");
        usernameET = (EditText) findViewById(R.id.usernametext);
        passwordET=(EditText) findViewById(R.id.passwordtext);
    }

    /**
     * Calls the super {@code onStart()} and gets the logged in user
     */
    @Override
    public void onStart() {
        super.onStart();
//        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            reload();
//        }
    }

//    private void reload() {
//        Intent intent = new Intent(LoginActivity.this,LoginActivity.class);
////        startActivity(intent);
//    }

    /**
     * Attempts to sign in the user based on their inputted username and password, so long as they're
     * not empty
     * @param view
     */
    public void logInAction(View view){
        usernameET = (EditText) findViewById(R.id.usernametext);
        String email = usernameET.getText().toString();
        EditText passwordET=(EditText) findViewById(R.id.passwordtext);
        String password = passwordET.getText().toString();


        //need to check if user exists in database

        if (email.compareTo("") == 0 || password.compareTo("") == 0 )
        {
            usernameET.setError("Please input the email and password.");
        }
        else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(ACTIVITY_NAME, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                String userID = user.getUid();
                                updateUI(user, userID);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(ACTIVITY_NAME, "signInWithEmail:failure", task.getException());

                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    /**
     * Switches the view and passes the {@code userID} to {@link MainActivity}
     * @param user unused
     * @param userID {@link String} user ID to be passed to {@link MainActivity}
     */
    private void updateUI(FirebaseUser user, String userID) {
        if (user.getEmail().endsWith(email_identifier))
        {
            Intent intent = new Intent(this, AdminMainActivity.class);
            intent.putExtra("userID", userID);
            startActivity(intent);

        }
        else {
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        }

    }

    /**
     * Switches the view to {@link RegisterActivity}
     * @param view
     */
    public void goToRegisterActivity(View view){
        Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

/*
    */
/**
     * Calls the super {@code onActivityResult()} and calls {@link LoginActivity#SignUserOut()}
     * @param requestCode
     * @param resultCode
     * @param data
     *//*

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SignUserOut();

    }

    */
/**
     * Does nothing
     *//*

    public void SignUserOut()
    {

    }
*/

    /**
     * Sends a reset password email to the inputted email so long as the email
     * is not empty and the email is associated with an account
     * @param view
     */
    public void ForgotPassword(View view)
    {

        String emailAddress = usernameET.getText().toString();
        Log.i(ACTIVITY_NAME, "Email add: " + emailAddress);

        if (emailAddress.compareTo("") != 0 ) {
            mAuth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.i(ACTIVITY_NAME, "Email sent.");
                                Toast.makeText(LoginActivity.this, "Email Sent!", Toast.LENGTH_SHORT).show();
                                passwordET.setVisibility(View.VISIBLE);
                            }
                        }
                    });
        }
        else
        {

            passwordET.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Please enter an email address and click forgot password", Toast.LENGTH_LONG).show();
        }
    }

    public void CreateAdmin(View view )
    {
        startActivity(new Intent(this, Admin_Create.class));
    }
    //    public Boolean validate(String username,String password){
//        if (TextUtils.isEmpty(password)){
//            Toast.makeText(LoginActivity.this,R.string.psswrd_error_message, Toast.LENGTH_SHORT).show();
//            return false;
//        }else if(username.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(username).matches()){
//            Toast.makeText(LoginActivity.this,R.string.email_error_message, Toast.LENGTH_SHORT).show();
//            return false;
//        }else{
//            return true;
//        }
//    }
//
//    public String encryptHmacSha256(String key, String s) {
//        byte[] message=s.getBytes(StandardCharsets.UTF_8);
//        byte[] secretKey=key.getBytes(StandardCharsets.UTF_8);
//        byte[] hmacSha256 = null;
//        try {
//            Mac mac = Mac.getInstance("HmacSHA256");
//            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256");
//            mac.init(secretKeySpec);
//            hmacSha256 = mac.doFinal(message);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to calculate hmac-sha256", e);
//        }
//        String encryption = new String(hmacSha256, StandardCharsets.UTF_8);
//        return encryption;
//    }



//    public void logInAction(View view){
//        EditText usernameET = (EditText) findViewById(R.id.usernametext);
//        String emailText = usernameET.getText().toString();
//        EditText passwordET=(EditText) findViewById(R.id.passwordtext);
//        String passwordText = passwordET.getText().toString();
//        //need to check if user exists in database
//        if(validate(emailText,passwordText)){
//            //check
//            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//            startActivity(intent);
//        }
//    }


}