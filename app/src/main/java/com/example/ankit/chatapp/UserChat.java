package com.example.ankit.chatapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserChat extends Fragment {
    List<String> Phone,Name;
    View view;
    String phone;
    private ArrayAdapter<String> arrayAdapter;
    ListView listViewChat;
SharedPreferences sharedPreferences;
    public UserChat() {
        // Required empty public constructor
    }

    Context context;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_chat, container, false);

        sharedPreferences= context.getSharedPreferences("MyData",Context.MODE_PRIVATE);
        phone = sharedPreferences.getString("Phone","");
        Log.d("mere",phone);

        Name=new ArrayList<String>();
        Phone=new ArrayList<String>();
        listViewChat=(ListView)view.findViewById(R.id.ListViewChat);

        arrayAdapter = new ArrayAdapter<String>(context,R.layout.row,R.id.textViewMain,Name);
        listViewChat.setAdapter(arrayAdapter);

        FirebaseDatabase.getInstance().getReference().child("Chat").child(phone).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(!dataSnapshot.getKey().equals("Name") && !dataSnapshot.getKey().equals("Phone")) {
                    Log.d("hey Baby", dataSnapshot.child("Name").getValue(String.class));
                    String p =dataSnapshot.child("Phone").getValue(String.class);
                    Name.add(dataSnapshot.child("Name").getValue(String.class)+"\n"+p);
                    Phone.add(p);
                    arrayAdapter.notifyDataSetChanged();
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

listViewChat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("mere",Name.get(position));
        Intent intent =new Intent(context,Message.class);
        String name=Name.get(position);
        intent.putExtra("Name",name.substring(0,name.indexOf('\n')));
        intent.putExtra("ServerKey",phone+"_"+Phone.get(position));
        intent.putExtra("ClientKey",Phone.get(position)+"_"+phone);
        intent.putExtra("ClientPhone",Phone.get(position));
        intent.putExtra("ServerPhone",phone);
        startActivity(intent);









    }
});





        return view;
    }

}
