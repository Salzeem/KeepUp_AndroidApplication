package com.example.android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import javax.net.ssl.HttpsURLConnection;

/**
 * This Activity setups up a view to act as the "Course Selection" screen
 */
public class CourseSelectionActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME="CourseSelectionActivity";
    String termPref;
    String yearPref;
    ArrayList<String> levelPref;
    ArrayList<ArrayList<String>> coursePreference;
    ArrayList<String> finalCourses;
    ArrayList<String> finalCourses2;

    ArrayList<String> chosenCourses;
    ArrayList<String> chosenCourses2;

    TextView courseTV;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String userID;
    private static final int[] idArray={
            R.id.courseBtn1,R.id.courseBtn2,R.id.courseBtn3,R.id.courseBtn4,R.id.courseBtn5,R.id.courseBtn6,R.id.courseBtn7,R.id.courseBtn8, R.id.courseBtn9,R.id.courseBtn10,
            R.id.courseBtn11,R.id.courseBtn12, R.id.courseBtn13,R.id.courseBtn14,R.id.courseBtn15,R.id.courseBtn16,R.id.courseBtn17,R.id.courseBtn18,R.id.courseBtn19,R.id.courseBtn20,
            R.id.courseBtn21,R.id.courseBtn22, R.id.courseBtn23,R.id.courseBtn24,R.id.courseBtn25,R.id.courseBtn26, R.id.courseBtn27,R.id.courseBtn28,R.id.courseBtn29,R.id.courseBtn30,
            R.id.courseBtn31,R.id.courseBtn32,R.id.courseBtn33,R.id.courseBtn34,R.id.courseBtn35,R.id.courseBtn36,R.id.courseBtn37,R.id.courseBtn38, R.id.courseBtn39,R.id.courseBtn40,
            R.id.courseBtn41,R.id.courseBtn42,R.id.courseBtn43,R.id.courseBtn44,R.id.courseBtn45,R.id.courseBtn46};
    private Switch[] switch_buttons=new Switch[idArray.length];

    /**
     * Sets up the Course Selection view and populates the switch buttons with appropriate courses based on
     * term preference and level preference
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_selection);
        setTitle("Course Selection");
        courseTV=findViewById(R.id.courseTextView);
        chosenCourses=new ArrayList<String>();
        chosenCourses2=new ArrayList<String>();

        finalCourses=new ArrayList<String>();
        finalCourses2=new ArrayList<String>();
//        coursePreference=new ArrayList<String>();

        Bundle extras=getIntent().getExtras();

        userID = extras.getString("userID");
        Log.i(ACTIVITY_NAME, userID);

        DocumentReference docRef = db.collection("user").document(userID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        termPref = document.getData().get("termPref").toString();
                        yearPref = document.getData().get("yearPref").toString();
                        levelPref = (ArrayList<String>) document.getData().get("levelPref");

//                        new GetAllCourses(termPref,levelPref).execute();

                        Log.i(ACTIVITY_NAME,termPref);
                        Log.i(ACTIVITY_NAME,yearPref);
                        Log.i(ACTIVITY_NAME,String.valueOf(levelPref));

                        popCourses(termPref,levelPref);

                        for (int x=0;x<idArray.length;x++){
                            switch_buttons[x]=(Switch)findViewById(idArray[x]);
                            int finalX = x;
                            switch_buttons[x].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                                    if(checked){
                                        finalCourses.add(switch_buttons[finalX].getText().toString());
                                        finalCourses2.add(switch_buttons[finalX].getText().toString().split(":")[0]);
                                    }else{
                                        if(finalCourses.contains(switch_buttons[finalX].getText().toString())){
                                            finalCourses.remove(switch_buttons[finalX].getText().toString());
                                            finalCourses2.remove(switch_buttons[finalX].getText().toString().split(":")[0]);

                                        }
                                    }
                                    Log.i(ACTIVITY_NAME,String.valueOf(finalCourses));
                                }
                            });

                        }

                    } else {
                        Log.d(ACTIVITY_NAME, "No such document");
                    }
                } else {
                    Log.d(ACTIVITY_NAME, "get failed with ", task.getException());
                }
            }
        });


    }

//    public class GetAllCourses extends AsyncTask<String,Integer,String> {
//        String contents;
//        protected String term;
//        protected ArrayList<String> level;
//
//        GetAllCourses(String term,ArrayList<String> level){
//            this.term=term;
//            this.level=level;
//        }
//        private String convertStreamToString(InputStream is) throws UnsupportedEncodingException {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//            StringBuilder sb = new StringBuilder();
//            String line = null;
//            try {
//                while ((line = reader.readLine()) != null) {
//                    sb.append(line + "\n");
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                try {
//                    is.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            return sb.toString();
//        }
//        public  ArrayList<ArrayList<String>> getCourseInfo(Elements elements,String termPreference){
//            ArrayList<ArrayList<String>> CourseInfo=new ArrayList<ArrayList<String>>();
//            int i=0;
//            while(i<elements.size()){
//                String[] temp=elements.get(i).select("h3").text().split(":");
//                if (temp.length>1){
//                    String code=temp[0].trim();
//                    String title=temp[1].trim();
//                    String desc=elements.get(i).select("p").first().text().trim();
//
//
//                    Elements sections=elements.get(i).select("table").select("tbody").select("tr");
//                    // System.out.println(sections.size());
//                    // val.add(String.valueOf(sections.size()));
//
//                    int j=0;
//                    while(j<sections.size()){
//                        ArrayList<String> final_vals=new ArrayList<String>();
//                        final_vals.add(code);
//                        final_vals.add(title);
//                        final_vals.add(desc);
//                        final_vals.add(termPreference);
//                        String section=sections.get(j).select("td").first().text().trim();
//                        if(section.length()>0 && !section.substring(0,1).equals("L")){
//                            String days =sections.get(j).select("td").get(1).text().trim();
//                            String times =sections.get(j).select("td").get(2).text().trim();
//                            String room =sections.get(j).select("td").get(3).text().trim();
//                            String instructor =sections.get(j).select("td").get(4).text().trim();
//                            if(instructor.length()==0){
//                                instructor="NA";
//                            }
//                            if(times.length()<2){
//                                times="NA";
//                            }
//                            if(days.length()==0){
//                                days="NA";
//                            }
//                            if(room.length()==0){
//                                room="NA";
//                            }
//
//                            final_vals.add(section);
//                            final_vals.add(days);
//                            final_vals.add(times);
//                            final_vals.add(room);
//                            final_vals.add(instructor);
//                            CourseInfo.add(final_vals);
//                        }
//                        j++;
//                    }
//
//
//
//                    // String temp1=elements.get(i).select("table").select("tbody").select("tr").select("td").last().text().strip();
//                    // if(temp1.length()==0){
//                    //     temp1="NA";
//                    // }
//                    // val.add(temp1);
//
//                    // CourseInfo.add(val);
//                }
//                i+=1;
//            }
//            return CourseInfo;
//        }
//
//        @Override
//        protected void onPostExecute(String a) {
//            coursePreference=new ArrayList<ArrayList<String>>();
//            Document doc = Jsoup.parse(contents);
//
//            Elements terms=doc.select("div#tab-contents > section > h2");
//            ArrayList<String> all_terms=new ArrayList<String>();
//            int  i=0;
//            while(i<terms.size()){
//                String term=terms.get(i).text();
//                all_terms.add(term.trim());
//                i+=1;
//            }
//
//            Elements term1_courses=doc.select("div#tab-contents > section#term1 > section");
//            Elements term2_courses=doc.select("div#tab-contents > section#term2 > section");
//            Elements term3_courses=doc.select("div#tab-contents > section#term3 > section");
//            Log.i(ACTIVITY_NAME,all_terms.get(0));
//            Log.i(ACTIVITY_NAME,all_terms.get(1));
//            Log.i(ACTIVITY_NAME,all_terms.get(2));
//
//            ArrayList<ArrayList<String>> term1CourseInfo=getCourseInfo(term1_courses,all_terms.get(0));
//            ArrayList<ArrayList<String>> term2CourseInfo=getCourseInfo(term2_courses,all_terms.get(1));
//            ArrayList<ArrayList<String>> term3CourseInfo=getCourseInfo(term3_courses,all_terms.get(2));
//            String term="1";
//            if(term.equals("1")) {
//                // for (String lev:level){
//                int courseIndex=0;
//                while(courseIndex<term1CourseInfo.size()){
//                    if(term1CourseInfo.get(courseIndex).get(0).substring(0,2).equals("CP")){
//                        coursePreference.add(term1CourseInfo.get(courseIndex));
//                        // System.out.println(term2CourseInfo.get(courseIndex));
//                    }
//                    courseIndex+=1;
//                }
//                // }
//            }
//            term="2";
//            if(term.equals("2")) {
//                // for (String lev:level){
//                int courseIndex=0;
//                while(courseIndex<term2CourseInfo.size()){
//                    if(term2CourseInfo.get(courseIndex).get(0).substring(0,2).equals("CP")){
//                        coursePreference.add(term2CourseInfo.get(courseIndex));
//                        // System.out.println(term2CourseInfo.get(courseIndex));
//                    }
//                    courseIndex+=1;
//                }
//                // }
//            }
//            term="3";
//            if(term.equals("3")) {
//                // for (String lev:level){
//                int courseIndex=0;
//                while(courseIndex<term3CourseInfo.size()){
//                    if(term3CourseInfo.get(courseIndex).get(0).substring(0,2).equals("CP")){
//                        coursePreference.add(term3CourseInfo.get(courseIndex));
//                        // System.out.println(term2CourseInfo.get(courseIndex));
//                    }
//                    courseIndex+=1;
//                }
//                // }
//            }
//
//            Log.i(ACTIVITY_NAME,"Before loop");
//            Log.i(ACTIVITY_NAME,String.valueOf(coursePreference.size()));
//            int x=0;
//            for(ArrayList<String> course:coursePreference){
//
//                Map<String, Object> crs = new HashMap<>();
//                crs.put("code",course.get(0));
//                crs.put("title",course.get(1));
//                crs.put("description",course.get(2));
//                crs.put("term",course.get(3));
//                crs.put("section",course.get(4));
//                crs.put("days",course.get(5));
//                crs.put("times",course.get(6));
//                crs.put("room",course.get(7));
//                crs.put("instructor",course.get(8));
//                Log.i(ACTIVITY_NAME,"Inside loo["+String.valueOf(x));
//                x++;
//                fc.collection("courses")
//                        .add(crs)
//                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//                                Log.d(ACTIVITY_NAME, "DocumentSnapshot added with ID: " + documentReference.getId());
//                                //ProgressIndicator.setProgressCompat(100, true);
//
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.w(ACTIVITY_NAME, "Error adding document", e);
//                            }
//                        });
//
//            }
//            Log.i(ACTIVITY_NAME,"After loop");
//
////            for (int j=0;j<coursePreference.size();j++){
////                switch_buttons[j]=(Switch)findViewById(idArray[j]);
////                switch_buttons[j].setVisibility(View.VISIBLE);
////                switch_buttons[j].setText(coursePreference.get(j).get(0));
////            }
//        }
//        @Override
//        protected String doInBackground(String... strings) {
//            try{
//                URL url = new URL("https://bohr.wlu.ca/courses/courseSchedules.php");
//                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//                conn.setReadTimeout(10000);
//                conn.setConnectTimeout(15000);
//                conn.setRequestMethod("GET");
//                conn.setDoInput(true);
//                conn.connect();
//                InputStream in = conn.getInputStream();
//                try{
//                    contents = convertStreamToString(in);
//                }
//                finally{
//                    in.close();
//                }
//            } catch (ProtocolException e) {
//                e.printStackTrace();
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }

    /**
     * Adds courses from database to {@link CourseSelectionActivity#chosenCourses} and
     * {@link CourseSelectionActivity#chosenCourses2} and {@link Switch}s representing
     * each course to {@link CourseSelectionActivity#switch_buttons} based on {@code termPreference}
     * and {@code levelPreference}
     * @param termPreference {@link String} term to filter courses by
     * @param levelPreference {@link String} level to filter courses by
     */
    public void popCourses(String termPreference,ArrayList<String> levelPreference)
    {
        Log.i(ACTIVITY_NAME, " IN POPCOURSES");
        db.collection("courses")
                .orderBy("code").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentChange document : task.getResult().getDocumentChanges()) {
                                ArrayList<String> crs=new ArrayList<String>();
                                String courseName = document.getDocument().getString("code");
    //                            String courseDesc = document.getDocument().getString("description");
                                String courseTitle = document.getDocument().getString("title");
                                String courseSection = document.getDocument().getString("section");
                                String courseTerm=document.getDocument().getString("term");

                                for(String lev: levelPreference) {
                                    Log.i(ACTIVITY_NAME,courseName.substring(2, 5));
                                    Log.i(ACTIVITY_NAME,lev);
                                    Log.i(ACTIVITY_NAME,courseTerm);
                                    Log.i(ACTIVITY_NAME,termPreference);


                                    if (courseName.substring(2, 3).equals(lev.substring(0,1)) && courseTerm.equals(termPreference)) {
    //                                    crs.add(courseName);
    //                                    crs.add(courseSection);
    //                                    crs.add(courseTitle);
    //                                    crs.add(courseDesc);
                                        chosenCourses.add(courseName+" "+courseSection);
                                        chosenCourses2.add(courseName+" "+courseSection+": "+courseTitle);

                                    }
                                }

                            }
                            for (int j=0;j<chosenCourses2.size();j++){
                                switch_buttons[j]=(Switch)findViewById(idArray[j]);
                                switch_buttons[j].setVisibility(View.VISIBLE);
                                switch_buttons[j].setText(chosenCourses2.get(j));
                            }

                            Log.i(ACTIVITY_NAME,"course pref size: "+String.valueOf(chosenCourses.size()));

                        } else {
                            Log.w(ACTIVITY_NAME, "Error getting documents or no changes yet.", task.getException());
                        }
                    }
                });
    }

    /**
     * Store user's courses in the database and switch view to {@link MainActivity}
     * @param view
     */
    public void goToMain(View view){

        // Update one field, creating the document if it does not already exist.
        Map<String, Object> users = new HashMap<>();
        users.put("courses", finalCourses2);;

        db.collection("user").document(userID)
                .set(users, SetOptions.merge());

        updateUI(userID);

    }

    /**
     * Switch view and pass {@code userID} to {@link MainActivity}
     * @param userID {@link String} userID to be passed to {@link MainActivity}
     */
    private void updateUI(String userID) {

        Intent intent=new Intent(CourseSelectionActivity.this,MainActivity.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
    }


}