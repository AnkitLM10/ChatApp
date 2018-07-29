package com.example.ankit.chatapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {

    EditText etSignEmail,etSignPassword,etSignPhone,etSignName;
    Button btSignUp,btSignLog;
    int j=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
FirebaseAuth.getInstance().signInWithEmailAndPassword("ankit.singh249@gmail.com","iamdon");
etSignEmail=(EditText)findViewById(R.id.etSignEmail);
etSignName=(EditText)findViewById(R.id.etSignName);
etSignPassword=(EditText)findViewById(R.id.etSignPassword);
        etSignPhone=(EditText)findViewById(R.id.etSignPhone);
btSignLog=(Button)findViewById(R.id.btSignLog);
btSignUp=(Button)findViewById(R.id.btSignUp);
btSignUp.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(etSignEmail.getText().toString().equals(""))
        {
            Toast.makeText(MainActivity.this, "Please Enter Valid Email", Toast.LENGTH_SHORT).show();
        return;
        }
        if(etSignPassword.getText().toString().equals(""))
        {
            Toast.makeText(MainActivity.this, "Please Enter Valid Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(etSignPassword.getText().toString().length()<6)
        {
            Toast.makeText(MainActivity.this, "Please Enter Password of minimum 6 length", Toast.LENGTH_SHORT).show();
            return;
        }
        if(etSignPhone.getText().toString().equals(""))
        {
            Toast.makeText(MainActivity.this, "Please Enter Valid Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }
        if(etSignName.getText().toString().equals(""))
        {
            Toast.makeText(MainActivity.this, "Please Enter Valid Name", Toast.LENGTH_SHORT).show();
            return;
        }
FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for(DataSnapshot snapshot:dataSnapshot.getChildren())
        {
            if(snapshot.child("Email").getValue(String.class).equals(etSignEmail.getText().toString()))
            {
                Toast.makeText(MainActivity.this,"Email Already In Use",Toast.LENGTH_LONG).show();
j=1;
                break;
            }

            if(snapshot.child("Phone").getValue(String.class).equals(etSignPhone.getText().toString()))
            {
                Toast.makeText(MainActivity.this,"Phone Already In Use",Toast.LENGTH_LONG).show();
                j=1;
                break;
            }


        }


    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});
        if(j==1)
        {
            j=0;
            return;
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(etSignEmail.getText().toString(),etSignPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
              DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").push();
                SharedPreferences sharedPreferences=getSharedPreferences("MyData",MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();

                ref.child("Email").setValue(etSignEmail.getText().toString());
                ref.child("Name").setValue(etSignName.getText().toString());
                ref.child("Phone").setValue(etSignPhone.getText().toString());
               DatabaseReference refe=FirebaseDatabase.getInstance().getReference().child("Chat");
               refe = refe.child(etSignPhone.getText().toString());
                refe.child("Name").setValue(etSignName.getText().toString());
                refe.child("Phone").setValue(etSignPhone.getText().toString());
             //   refe.child("Name").child(etSignName.getText().toString());



                startActivity(new Intent(MainActivity.this,LogIn.class));
finish();
            }
        });

    }
});




btSignLog.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(MainActivity.this,LogIn.class));
finish();
    }
});




    }








    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences=getSharedPreferences("MyData",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        if(sharedPreferences.getInt("LogIn",0)==1)
        {
            startActivity(new Intent(MainActivity.this,Profile.class));
            finish();
        }


    }
}
