package com.ssgmc.qline;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText firstName, lastName, regEmail, regPass;
    private Button register;
    private TextView login;

    private String fname, lname, email, pass, uid;

    private FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth  = FirebaseAuth.getInstance();

        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        regEmail = findViewById(R.id.regEmail);
        regPass = findViewById(R.id.regPass);
        register = findViewById(R.id.userRegister);
        login = findViewById(R.id.openLogin);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateUser();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(auth.getCurrentUser() != null){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void validateUser() {

        fname = firstName.getText().toString().trim();
        lname = lastName.getText().toString().trim();
        email = regEmail.getText().toString().trim();
        pass = regPass.getText().toString().trim();

        if (fname.isEmpty() || lname.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            registerUser();
        }


    }

    private void registerUser() {

        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "User created", Toast.LENGTH_SHORT).show();

                            Log.d("MSG", "1");
                            User user = new User(fname, lname, email, pass, auth.getUid(), "NA");
                            myRef.child(auth.getUid()).setValue(user);

                            updateUser(user);
                        }else{
                            Toast.makeText(RegisterActivity.this, "Error: Couldn't Sign in "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("ERR", "2");
                        }
                    }
                });
    }

    private void updateUser(User user) {
        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(fname+" "+lname)
                .build();


        auth.getCurrentUser().updateProfile(changeRequest);
        auth.signOut();

        openLogin();
    }

    private void openLogin() {

        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }


}