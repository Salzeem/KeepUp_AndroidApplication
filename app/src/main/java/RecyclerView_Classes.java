/*
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.CardViews;
import com.example.android.Groupchat;
import com.example.android.GroupsInformation;
import com.example.android.ListView_transiton;
import com.example.android.MainActivity;
import com.example.android.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RecyclerView_Classes extends RecyclerView.Adapter<com.example.android.RecyclerViewList.RecyclerViewHolder> implements View.OnClickListener{

    Context ctx;
    ArrayList<Classsinformation> classlist ;
    static String TAG = "GroupsRender RecyclerView";

    public RecyclerView_Classes(Context ctx, ArrayList<Classinformation> list, int id )
    {
        this.classlist = list;
        this.ctx = ctx;

    }


    @NonNull
    @Override
    public com.example.android.RecyclerViewList.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.test, parent, false);
        return new com.example.android.RecyclerViewList.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.android.RecyclerViewList.RecyclerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}



public class RecyclerViewList extends RecyclerView.Adapter<com.example.android.RecyclerViewList.RecyclerViewHolder> implements View.OnClickListener {



    @NonNull
    @Override
    public com.example.android.RecyclerViewList.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.test, parent, false);
        return new com.example.android.RecyclerViewList.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.android.RecyclerViewList.RecyclerViewHolder holder, int position) {



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
                        .replace(R.id.fragmentContainerView, ListView_transiton.class,extras)
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
                                String name =  (String) document.getString("first_name") + " " + document.getString("last_name") ;
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

    @Override
    public int getItemCount() {
        return grouplist.size();
    }

    @Override
    public void onClick(View view) {

    }

    public   class RecyclerViewHolder extends  RecyclerView.ViewHolder {
        TextView description;
        TextView name;
        TextView course;
        Button chat;
        MaterialCardView card;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.GroupNameLabel);
            name = itemView.findViewById(R.id.GroupDescriptionLabel);
            course = itemView.findViewById(R.id.coursename);
            chat = itemView.findViewById(R.id.openchatbutton);
            card = itemView.findViewById(R.id.card);
            chat.setTag(grouplist.size()-1);




        }
    }


}

*/
