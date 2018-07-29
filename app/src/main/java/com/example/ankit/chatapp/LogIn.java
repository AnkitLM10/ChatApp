package com.example.ankit.chatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class LogIn extends AppCompatActivity {
    EditText etLogEmail,etLogPassword;
    Button btLogIn;
    String Phone,Name;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        etLogEmail=(EditText)findViewById(R.id.etLogEmail);
        etLogPassword=(EditText)findViewById(R.id.etLogPassword);
        btLogIn=(Button)findViewById(R.id.btLogIn);
    btLogIn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
             email=etLogEmail.getText().toString().trim();
            String password=etLogPassword.getText().toString().trim();
            if(email.equals(""))
            {
                Toast.makeText(LogIn.this,"Email Cannot Be Empty",Toast.LENGTH_LONG).show();
            return;
            }
            if(password.equals(""))
            {
                Toast.makeText(LogIn.this,"Email Cannot Be Empty",Toast.LENGTH_LONG).show();
                return;
            }
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users");
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
int k=0;
                            for(DataSnapshot snapshot:dataSnapshot.getChildren())
                            {
                                if(snapshot.child("Email").getValue(String.class).equals(email))
                                {

                                    Phone=snapshot.child("Phone").getValue(String.class);
                                    Name=snapshot.child("Name").getValue(String.class);
                                    Log.d("hmm",Phone+"  "+Name);
                                    SharedPreferences sharedPreferences=getSharedPreferences("MyData",MODE_PRIVATE);
                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                    editor.putInt("LogIn",1);
                                    editor.putString("Phone",Phone);
                                    editor.putString("Name",Name);


                                    String token = FirebaseInstanceId.getInstance().getToken();
                                    FirebaseDatabase.getInstance().getReference().child("Token").child(Phone).setValue(token);
                                    editor.putString("Token",token);
                                    editor.commit();
                                    startActivity(new Intent(LogIn.this,Profile.class));

                                    finish();
                                }
                            }



                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });




                }
            });




        }
    });

    }



}
