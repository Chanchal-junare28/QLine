package com.ssgmc.qline;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private TextView name, email, appointdate;
    private Button logout;

    private ImageView book_appointment;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();

    private DatabaseReference databaseReference, databaseReference2, databaseReferenceForDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.userName);
        email = findViewById(R.id.userEmail);
        logout = findViewById(R.id.userlogout);
        book_appointment = findViewById(R.id.book_appointment);



        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        if(user != null){
            name.setText(user.getDisplayName());
            email.setText(user.getEmail());
        }


//
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        String url = "https://www.googleapis.com/calendar/v3/calendars/en.indian%23holiday@group.v.calendar.google.com/events?key=AIzaSyBaA3se6yP-Gik6nvE3oqX3X_DfEI0-lSQ";
//        JsonArrayRequest
//                jsonArrayRequest
//                = new JsonArrayRequest(
//                Request.Method.GET,
//                url,
//                null,
//                new Response.Listener() {
//
//                    @Override
//                    public void onResponse(Object response) {
//                        JSONArray jsonArray = (JSONArray) response;
//                        for(int i=0; i<jsonArray.length(); i++){
//                            Log.d("XXXXXXXXXXXXXXXXXX", "/n"+jsonArray.toString());
//                        }
//                    }
//
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error)
//                    {
//                        Log.d("XXXXXXXXXXXXXXXXXX", "Errrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr");
//                    }
//                });
//        requestQueue.add(jsonArrayRequest);


        book_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseReference.child(auth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("FIREBASE", "Error getting data", task.getException());
                        }
                        else {
                            User currentUser = task.getResult().getValue(User.class);
                            check(currentUser);
                            Log.d("FIREBASE", String.valueOf(task.getResult().getValue()));
                        }
                    }
                });
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    private void check(User currentUser){

        databaseReference2 = FirebaseDatabase.getInstance().getReference();

        databaseReference2.child("AppointmentDates").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("FIREBASE", "Error getting data", task.getException());
                }
                else {
                    AppointmentDates apdates = task.getResult().getValue(AppointmentDates.class);
                    try {
                        Log.d("FIREBASE", "dates"+String.valueOf(task.getResult().getValue()));
                        AppointmentDates  modifiedDates = checkAppointment(currentUser, apdates);
                        databaseReference2.child("AppointmentDates").setValue(modifiedDates);
                        Log.d("FIREBASE", "dates"+String.valueOf(task.getResult().getValue()));
                        //startActivity(new Intent(MainActivity.this, CheckAppointment.class));
                        //finish();
                        Toast.makeText(MainActivity.this, "Appointment Done", Toast.LENGTH_SHORT).show();

                        book_appointment.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(user == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        appointdate = findViewById(R.id.appointment_date);

        databaseReference.child(auth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("FIREBASE", "Error getting data", task.getException());
                }
                else {
                    User apdates = task.getResult().getValue(User.class);


                    if(apdates != null && !apdates.getAppointmentDate().equals("NA")) {
                        appointdate.setText("Appointment Date :" + apdates.getAppointmentDate());
                        book_appointment.setVisibility(View.GONE);
                    }else{
                        appointdate.setText("Appointment Date : NA");
                        book_appointment.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

    public AppointmentDates checkAppointment(User currentUser, AppointmentDates appointmentDates) throws Exception{

        String pattern = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date date = new Date();
        date.setDate(date.getDate()+1);

        if(date.getDay() == 0) date.setDate(date.getDate()+1);

        String strDate = simpleDateFormat.format(date);
        Date todayDate =simpleDateFormat.parse(strDate);
        String lastDate = simpleDateFormat.format(date);

        if(appointmentDates != null){
            HashMap<String, Integer> dataMap = appointmentDates.getDateAp();
            if(dataMap.containsKey(lastDate) && dataMap.get(lastDate) < 2){
                dataMap.put(lastDate, dataMap.get(lastDate)+1);
                updateAppointmentSchedule(currentUser, lastDate);
                appointmentDates.setDateAp(dataMap);
                return appointmentDates;
            }else if(!dataMap.containsKey(simpleDateFormat.format(todayDate.getDate()-1))){
                if(dataMap.containsKey(lastDate)){

                    if(dataMap.get(lastDate) < 2){
                        dataMap.put(lastDate, dataMap.get(lastDate)+1);
                        updateAppointmentSchedule(currentUser, lastDate);
                        appointmentDates.setDateAp(dataMap);
                        return appointmentDates;
                    }
                }else{
                    dataMap.put(lastDate, 1);
                    updateAppointmentSchedule(currentUser, lastDate);
                    appointmentDates.setDateAp(dataMap);
                    return appointmentDates;
                }
            }
            TreeMap<String, Integer> sortedMap = new TreeMap<>();
            sortedMap.putAll(dataMap);
            for(Map.Entry<String, Integer> m: sortedMap.entrySet()){
                int no_of_appointments = m.getValue();
                if(simpleDateFormat.parse(m.getKey()).compareTo(todayDate) >= 0 && no_of_appointments < 2 ){
                    no_of_appointments = no_of_appointments+1;
                    sortedMap.put(m.getKey(),no_of_appointments);
                    updateAppointmentSchedule(currentUser, m.getKey());
                    dataMap.putAll(sortedMap);
                    appointmentDates.setDateAp(dataMap);
                    return appointmentDates;
                }
                lastDate = m.getKey();
                Log.e("FIREBASE", "ffffffffffffffff"+m.getKey()+" "+m.getValue()+" ");

            }

            Date last = simpleDateFormat.parse(lastDate);
            last.setDate(last.getDate()+1);
            if(last.getDay() == 0) last.setDate(last.getDate()+1);
            sortedMap.put(simpleDateFormat.format(last), 1);
            updateAppointmentSchedule(currentUser, simpleDateFormat.format(last));
            dataMap.putAll(sortedMap);
            appointmentDates.setDateAp(dataMap);
            return appointmentDates;
        }

        AppointmentDates newDates = new AppointmentDates();
        HashMap<String, Integer> newDataMap = new HashMap<>();
        newDataMap.put(strDate, 1);
        updateAppointmentSchedule(currentUser, strDate);
        newDates.setDateAp(newDataMap);
        return newDates;
    }

    private void updateAppointmentSchedule(User currentUser, String date) {
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("AppointmentsScheduled");
        //AppointmentSchedule appointmentSchedule = new AppointmentSchedule(uid, date);
        databaseReference3.child(auth.getUid()).setValue(date);
        currentUser.setAppointmentDate(date);
        databaseReference.child(auth.getUid()).setValue(currentUser);
        appointdate.setText("Appointment Date : "+date);
        databaseReference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AppointmentSchedule app = snapshot.getValue(AppointmentSchedule.class);
                Log.d("SUCCESS", "Changed"+currentUser.getUid()+" "+date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FAIL", error.getMessage());
            }
        });
    }
}