package com.example.android;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecyclerViewList extends RecyclerView.Adapter<RecyclerViewList.RecyclerViewHolder> implements View.OnClickListener {



    Context ctx;
    ArrayList<CardViews> mlist;
    static ArrayList<GroupsInformation> grouplist;
    ArrayList<Classes> classlist;
    static String TAG = "GroupsRender RecyclerView";
    int id=0;
    public RecyclerViewList(Context ctx, ArrayList<GroupsInformation> list)
    {
        this.grouplist = list;
        this.ctx = ctx;

    }

    public RecyclerViewList(Context ctx, ArrayList<Classes> list, int id )
    {
        this.classlist = list;
        this.ctx = ctx;
        this.id = id;

    }
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.test, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {


     if (this.id == 0 ) {
         holder.description.setText(grouplist.get(position).getGroupDescription());
         holder.name.setText(grouplist.get(position).getNameofGroup());
         holder.course.setText(grouplist.get(position).getCoursename());
         holder.card.setOnLongClickListener(new View.OnLongClickListener() {
             @Override
             public boolean onLongClick(View view) {
                 holder.card.setChecked(!holder.card.isChecked());
                 Toast.makeText(ctx, "CLicked", Toast.LENGTH_LONG);
                 return true;
             }
         });


         holder.card.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 int pos = holder.getAdapterPosition();
                 Bundle extras = new Bundle();
                 extras.putString("Description", holder.description.getText().toString());
                 extras.putString("Name", holder.name.getText().toString());
                 extras.putString("Course", holder.course.getText().toString());
                 extras.putInt("pos", holder.getAdapterPosition());
                 extras.putString("groupID", grouplist.get(pos).getid());
                 ((AppCompatActivity) ctx).getSupportFragmentManager().beginTransaction()
                         .replace(R.id.fragmentContainerView, ListView_transiton.class, extras)
                         .setReorderingAllowed(true)
                         .addSharedElement(view, "relTrans")
                         .addToBackStack("tempBackStack")
                         .commit();
             }
         });
         holder.chat.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 FirebaseFirestore db = FirebaseFirestore.getInstance();
                 Intent intent = new Intent(ctx, Groupchat.class);

                 intent.putExtra("groupid", grouplist.get(holder.getAdapterPosition()).getid());
                 DocumentReference docRef = db.collection("user").document(MainActivity.UserId);
                 docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                     @Override
                     public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                         if (task.isSuccessful()) {
                             DocumentSnapshot document = task.getResult();
                             if (document.exists()) {
                                 String name = (String) document.getString("first_name") + " " + document.getString("last_name");
                                 intent.putExtra("name", name);
                                 ctx.startActivity(intent);

                             } else {
                                 Log.d("FRAGMENT_NAME", "No such document");
                             }
                         } else {
                             Log.d("FRAGMENT_NAME", "get failed with ", task.getException());
                         }
                     }


                 });
             }
         });

     }
     else
     {
         holder.description.setText(classlist.get(position).getCourseDesc());
         holder.description.setMaxLines(6);
         holder.name.setText(classlist.get(position).getCoursename());
         holder.course.setText(classlist.get(position).getCoursename());
         holder.image.setImageResource(R.drawable.class_icon);
         holder.chat.setVisibility(View.INVISIBLE);
         holder.card.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ctx);

                 builder.setTitle("Course: " +  classlist.get(holder.getAdapterPosition()).getCoursename());
                 builder.setIcon(R.drawable.class_icon);
                 builder.setBackground(ctx.getResources().getDrawable(R.drawable.dialog_background, null));
                 builder.setMessage(classlist.get(holder.getAdapterPosition()).getCourseDesc());
                 Dialog dialog = builder.create();
                 dialog.show();
             }
         });
     }


    }

    @Override
    public int getItemCount() {
        if (id==0) {
            return grouplist.size();
        }
        else
        {
            return classlist.size();
        }
    }

    @Override
    public void onClick(View view) {

    }

    public static class RecyclerViewHolder extends  RecyclerView.ViewHolder {


        TextView description;
        TextView name;
        TextView course;
        Button chat;
        MaterialCardView card;
        ImageView image;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.GroupNameLabel);
            name = itemView.findViewById(R.id.GroupDescriptionLabel);
            course = itemView.findViewById(R.id.coursename);
            chat = itemView.findViewById(R.id.openchatbutton);
            card = itemView.findViewById(R.id.card);
            image = itemView.findViewById(R.id.imageView6);





        }
    }


}

