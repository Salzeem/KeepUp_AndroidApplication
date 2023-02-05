package com.example.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * This Activity setups up a view to act as the "Registration" screen
 */
public class RegisterActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME="RegisterActivity";
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Sets up "Register Activity" view and stores Firebase authorization instance in
     * {@link RegisterActivity#mAuth}
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Registration");

        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Creates a new user in Firebase and stores a document in the database with information regarding
     * the created user
     * @param view
     */
    public void registerAction(View view){
        EditText firstNameET =findViewById(R.id.firstNametext);
        String firstName = firstNameET.getText().toString();
        EditText lastNameET = findViewById(R.id.lastNametext);
        String lastName = lastNameET.getText().toString();
        EditText emailET =  findViewById(R.id.emailtext);
        String email = emailET.getText().toString();
        EditText passwordET= findViewById(R.id.regPasswordtext);
        String password = passwordET.getText().toString();
        EditText confirmPasswordET = findViewById(R.id.passwordConfirmationtext);
        String confirmPassword = confirmPasswordET.getText().toString();

        if (validateRegistration(firstName,lastName,email,password,confirmPassword)){
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(ACTIVITY_NAME, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                String userID = user.getUid();

                                CollectionReference users = db.collection("user");

                                Map<String, Object> currUser = new HashMap<>();
                                currUser.put("first_name", firstName);
                                currUser.put("last_name", lastName);
                                currUser.put("email", email);
                                users.document(userID).set(currUser);

                                updateUI(user, userID);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(ACTIVITY_NAME, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
        }

    }

    /**
     * Switch view and pass {@code userID} to {@link RegisterActivity2}
     * @param user unused
     * @param userID {@link String} user identifier
     */
    private void updateUI(FirebaseUser user, String userID) {

        Intent intent=new Intent(RegisterActivity.this,RegisterActivity2.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }

//    public Boolean validatePassword(String password){
//        String regex = "^(?=.*[0-9])"
//                + "(?=.*[a-z])(?=.*[A-Z])"
//                + "(?=.*[@#$%^&+=])"
//                + "(?=\\S+$).{8,20}$";
//        Pattern password_pattern= Pattern.compile(regex);
//        Matcher password_check = password_pattern.matcher(password);
//        return password_check.matches();
//    }

    /**
     * Validates the email, making sure it has a valid email format and the email address belongs to
     * Wilfrid Laurier University
     * @param email {@link String} email to be validated
     * @return
     */
    public Boolean validateEmail(String email){
        String[] emailArray=email.split("@");
        String laurierDomain="mylaurier.ca";
        if(TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return false;
        }else if(!emailArray[1].equals(laurierDomain)){
            return false;
        }else{
            return true;
        }
    }

    /**
     * Verifies the details of the registration form such as: no empty fields, passwords match, email is valid
     * @param firstName {@link String} registering users first name
     * @param lastName {@link String} registering users last name
     * @param email {@link String} registering users email
     * @param password {@link String} registering users password
     * @param password2 {@link String} registering users password confirmation
     * @return true if validation was successful
     */
    public Boolean validateRegistration(String firstName,String lastName,String email, String password,String password2){
        if(TextUtils.isEmpty(firstName)){
            Toast.makeText(RegisterActivity.this, R.string.fName_error_message, Toast.LENGTH_SHORT).show();
            return false;
        }else if(TextUtils.isEmpty(lastName)){
            Toast.makeText(RegisterActivity.this, R.string.lName_error_message, Toast.LENGTH_SHORT).show();
            return false;
        }else if(!validateEmail(email)){
            Toast.makeText(RegisterActivity.this,R.string.email_error_message, Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(password) || TextUtils.isEmpty(password2)) {
            Toast.makeText(RegisterActivity.this, R.string.reg_psswrd_error_message, Toast.LENGTH_SHORT).show();
            return false;
        }else if (!password.equals(password2)){
            Toast.makeText(RegisterActivity.this, R.string.confirm_password_error_message,Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }
/*   public String encryptHmacSha256(String key, String s) {
    byte[] message=s.getBytes(StandardCharsets.UTF_8);
      byte[] secretKey=key.getBytes(StandardCharsets.UTF_8);
      byte[] hmacSha256 = null;
      try {
           Mac mac = Mac.getInstance("HmacSHA256");
         SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256");
         mac.init(secretKeySpec);
          hmacSha256 = mac.doFinal(message);
      } catch (Exception e) {
        throw new RuntimeException("Failed to calculate hmac-sha256", e);
      }
        String encryption = new String(hmacSha256, StandardCharsets.UTF_8);
      return encryption;
    }*/


}