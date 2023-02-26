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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecyclerViewList extends RecyclerView.Adapter<RecyclerViewList.RecyclerViewHolder> implements View.OnClickListener {



    Context ctx;
    static ArrayList<GroupsInformation> grouplist;
    ArrayList<Classes> classlist;
    ArrayList<Appointment> appointments;
    int id;
    public RecyclerViewList(Context ctx, ArrayList<?> list, int id )
    {
        if (id ==0 ) // Id for groups
    {
        this.grouplist = (ArrayList<GroupsInformation>)  list;
    }

        if (id == 1 ) {  // ID for classes
            this.classlist = (ArrayList<Classes>) list;
        }

        else if (id ==2) // ID for appointments
        {
            this.appointments = (ArrayList<Appointment>) list;
        }
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


     if (id == 0 ) {
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
     else if (id == 1)
     {
         holder.description.setText(classlist.get(position).getCourseDesc());
         holder.description.setMaxLines(6);
         holder.name.setText(classlist.get(position).getCoursename());
         holder.course.setText(classlist.get(position).getCourseTitle());
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

         holder.card.setOnLongClickListener(new View.OnLongClickListener() {
             @Override
             public boolean onLongClick(View view) {
                 holder.card.setChecked(!holder.card.isChecked());
                 if (holder.card.isChecked())
                 {
                     MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ctx);
                     builder.setTitle("Remove Course: " +  classlist.get(holder.getAdapterPosition()).getCoursename());
                     builder.setIcon(R.drawable.class_icon);
                     builder.setBackground(ctx.getResources().getDrawable(R.drawable.dialog_background, null));
                     builder.setMessage("Are you sure you want to be removed from this course?");
                     builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialogInterface, int i) {
                             FirebaseFirestore db = FirebaseFirestore.getInstance();
                             DocumentReference groupsRef = db.collection("user").document(MainActivity.UserId);
                             groupsRef.update("courses", FieldValue.arrayRemove(classlist.get(holder.getAdapterPosition()).getCoursename()));
                             //TODO: Set the firebase for classes as a listener to listen for changes then update, same for groups
                         }
                     });

                     builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                         @Override
                         public void onDismiss(DialogInterface dialogInterface) {
                             holder.card.setChecked(!holder.card.isChecked());
                         }
                     });
                     Dialog dialog = builder.create();

                     dialog.show();
                 }
                 return true;
             }
         });
     }

     else if (id ==2 )
     {
        holder.description.setText(appointments.get(position).getTitle());
         holder.name.setText(appointments.get(position).getCourse());
         holder.course.setText(appointments.get(position).getDescription() + "\n" + appointments.get(position).getDate() + "@" + appointments.get(position).getTime());
         holder.image.setImageResource(R.drawable.appointment_icon);
         holder.chat.setVisibility(View.INVISIBLE);
         holder.card.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(ctx);

                 builder.setTitle("Appointment: " +  appointments.get(holder.getAdapterPosition()).getTitle() + " " + appointments.get(position).getDate() + "@" + appointments.get(position).getTime());
                 builder.setIcon(R.drawable.class_icon);
                 builder.setBackground(ctx.getResources().getDrawable(R.drawable.dialog_background, null));
                 builder.setMessage(appointments.get(holder.getAdapterPosition()).getDescription() );
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
        else if (id ==1)
        {
            return classlist.size();
        }
        else
        {
            return appointments.size();
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

