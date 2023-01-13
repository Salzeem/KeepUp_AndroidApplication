package com.example.android;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * This Activity setups up a view to act as the "Group Chat" screen
 */
public class Groupchat extends AppCompatActivity {

    private static String ACTIVITY_NAME = "GROUPCHAT";
    ImageView Send;
    EditText TextField;
    ListView Texts;
    ArrayList<String> messages = new ArrayList<String>();
    protected FirebaseFirestore db = FirebaseFirestore.getInstance();
    ChatAdapter messageAdapter;
    String name;
    String GroupTitle;


    /**
     * Subclass of {@link ArrayAdapter} to be attached to {@link ListView}s that represent chat
     * messages
     */
    private class ChatAdapter extends ArrayAdapter<String> {


        /**
         * Calls super constructor
         * @param ctx
         */
        public ChatAdapter(Context ctx) {
            super(ctx, 0);

        }

        /**
         * Gets the number of messages that are stored
         * @return {@link Integer} number of messages
         */
        public int getCount() {
            return messages.size();
        }

        /**
         * Gets a message at {@code position} in the list of messages
         * @param position {@link Integer} index into message list
         * @return {@link String} message at {@code position}
         */
        public String getItem(int position) {
            return messages.get(position);
        }

        /**
         * Gets the view for the message at {@code position}
         * @param position {@link Integer} position in message list
         * @param convertView unused
         * @param parent unused
         * @return View for message at {@code position}
         */
        public View getView(int position, View convertView, ViewGroup parent) {

            View result;
            LayoutInflater inflater = Groupchat.this.getLayoutInflater();
            String[] messages = getItem(position).split(" ", 3);

            String[] temp=messages[2].split("@");

            String user = messages[0] + " " + messages[1]+" @"+temp[1];
            String messageString = temp[0];

            String nameofuser = messages[0] + " "  + messages[1];

            if (nameofuser.compareTo(name) != 0) {
                result = inflater.inflate(R.layout.message_from_user, null);
                TextView msg = result.findViewById(R.id.message);
                msg.setText(messageString);

            } else {
                result = inflater.inflate(R.layout.message_from_person, null);
                TextView msg = result.findViewById(R.id.message);
                msg.setText(messageString);
            }

            TextView name = (TextView) result.findViewById(R.id.nameMessage);
            name.setText(user); // get the string at position
            return result;
        }

    }

    /**
     * Sets up the view for the group chat
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchat);
        setTitle("Loading...");
        Send = findViewById(R.id.SendButton);
        TextField = findViewById(R.id.EnterMsg);
        Texts = findViewById(R.id.ListArray);
        messageAdapter = new ChatAdapter(this);
        Texts.setAdapter(messageAdapter);
        Intent intent = getIntent();

        String groupId = intent.getStringExtra("groupid");
        name = intent.getStringExtra("name");

        GetInformation(groupId);


        TextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextField.getText().toString().equals(""))
                {
                    Send.setClickable(true);
                    Send.setColorFilter(Color.BLUE);

                }
                else
                {
                    Send.setClickable(false);
                    Send.setColorFilter(Color.GRAY);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = TextField.getText().toString();
                LocalTime now=LocalTime.of(LocalTime.now().getHour(),LocalTime.now().getMinute());
                String parse = name + " " + message + "@"+now;
                messageAdapter.notifyDataSetChanged();
                Texts.smoothScrollToPosition(messages.size()-1);
                WriteMessagetoDatabase(messages.size(), groupId, parse);

            }
        });


    }

    /**
     * Stores the message at {@code position} in the message list in the database
     * @param position {@link Integer} index into message list
     * @param groupid {@link String} group identifier
     * @param name unused
     */
    public void WriteMessagetoDatabase(int position, String groupid, String name) {

        LocalTime now= LocalTime.now();
        db.collection("groups")
                .document(groupid)
                .update("messages", FieldValue.arrayUnion(name));

        Toast.makeText(this, "Message Sent!", Toast.LENGTH_LONG);
        TextField.setText("");

    }

    /**
     * Populates {@link Groupchat#messages} with group chat messages stored in the database baesd on
     * {@code groupid}
     * @param groupid {@link String} group identifier to retrieve messages for
     */
    public void GetInformation(String groupid) {
        final DocumentReference docRef = db.collection("groups").document(groupid);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(ACTIVITY_NAME, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(ACTIVITY_NAME, "Current data: " + snapshot.getData());

                    GroupTitle=(String) snapshot.get("name");
                    setTitle(GroupTitle);
                    messages = (ArrayList<String>) snapshot.get("messages");
                    messageAdapter.notifyDataSetChanged();
                } else {
                    Log.d(ACTIVITY_NAME, "Current data: null");
                }
            }
        });


    }


}

