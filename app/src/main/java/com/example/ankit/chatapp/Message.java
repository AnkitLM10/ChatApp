package com.example.ankit.chatapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class Message extends AppCompatActivity {
    RecyclerView recyclerView;
    CustomRecyclerAdapter mAdapter;
    DatabaseReference ServerId,ClientId;
    String ServerKey,ClientKey,ServerPhone,ClientPhone,Name,ServerName;
    EditText editText;
    ImageButton imageButton;
    TextView textView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        textView = (TextView)findViewById(R.id.textViewName);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        ServerKey=getIntent().getStringExtra("ServerKey");
        ClientKey = getIntent().getStringExtra("ClientKey");
        ServerPhone=getIntent().getStringExtra("ServerPhone");
        ClientPhone=getIntent().getStringExtra("ClientPhone");
        Name = getIntent().getStringExtra("Name");
        sharedPreferences=getSharedPreferences("MyData",MODE_PRIVATE);
        ServerName = sharedPreferences.getString("Name","");
        editText = (EditText)findViewById(R.id.editText);
        imageButton = (ImageButton)findViewById(R.id.btSend);
        mAdapter = new CustomRecyclerAdapter(this);
        recyclerView.setAdapter(mAdapter);
        textView.setText(Name);
        FirebaseDatabase.getInstance().getReference().child("Chat").child(ServerPhone).child(ServerKey).child("Name").setValue(Name);
        FirebaseDatabase.getInstance().getReference().child("Chat").child(ServerPhone).child(ServerKey).child("Phone").setValue(ClientPhone);
        ServerId = FirebaseDatabase.getInstance().getReference().child("Chat").child(ServerPhone).child(ServerKey);
        ClientId = FirebaseDatabase.getInstance().getReference().child("Chat").child(ClientPhone).child(ClientKey);
        FirebaseDatabase.getInstance().getReference().child("Chat").child(ClientPhone).child(ClientKey).child("Name").setValue(ServerName);
        FirebaseDatabase.getInstance().getReference().child("Chat").child(ClientPhone).child(ClientKey).child("Phone").setValue(ServerPhone);
        ServerId.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(DataSnapshot dataSnapshot, String s) {
     if(!dataSnapshot.getKey().equals("Name") && !dataSnapshot.getKey().equals("Phone")) {
         System.out.println(dataSnapshot);
         mAdapter.add(dataSnapshot.child("Message").getValue(String.class),dataSnapshot.child("Time").getValue(Long.class));
         mAdapter.notifyDataSetChanged();
         if (recyclerView.getAdapter().getItemCount() > 0)
             recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
     }
     }

           @Override
           public void onChildChanged(DataSnapshot dataSnapshot, String s) {

           }

           @Override
           public void onChildRemoved(DataSnapshot dataSnapshot) {

           }

           @Override
           public void onChildMoved(DataSnapshot dataSnapshot, String s) {

           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });


imageButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
     if(editText.getText().toString().equals(""))
         return;

        HashMap<String,Object> map = new HashMap<>();
        map.put("Message","1"+editText.getText().toString());
        map.put("Phone",ClientPhone);
        map.put("Time", ServerValue.TIMESTAMP);
        map.put("Names",ServerName);
        ServerId.push().setValue(map);
        map.clear();
        map.put("Message","0"+editText.getText().toString());
        map.put("Phone","");
        map.put("Time", ServerValue.TIMESTAMP);
        ClientId.push().setValue(map);
        map.clear();
        editText.setText("");
        recyclerView.setFocusable(true);
    }
});

    }
    @Override
    protected void onPause() {
        super.onPause();
        Profile.isAlive=false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Profile.isAlive=true;
    }

}
