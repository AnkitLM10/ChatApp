package com.example.ankit.chatapp;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserPeople extends Fragment {
Context context;
    ArrayList<String> contacts;
    SharedPreferences sharedPreferences;
List<String> Phone,Name;
    View view;
String phone;
    ArrayAdapter<String> arrayAdapter;
    String promoMan = "";
ListView listView;
    public UserPeople() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
sharedPreferences= context.getSharedPreferences("MyData",Context.MODE_PRIVATE);
        phone = sharedPreferences.getString("Phone","");
        Log.d("mere",phone);

        Name=new ArrayList<String>();
Phone=new ArrayList<String>();
        view = inflater.inflate(R.layout.fragment_user_people, container, false);
listView=(ListView)view.findViewById(R.id.ListView);

        arrayAdapter = new ArrayAdapter<String>(context,R.layout.row,R.id.textViewMain,Name);
listView.setAdapter(arrayAdapter);


        ContentResolver cr = context.getContentResolver(); //Activity/Application android.content.Context

        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if(cursor.moveToFirst())
        {
            contacts = new ArrayList<String>();
            do
            {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);
                    while (pCur.moveToNext())
                    {
                        String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String w="";
                        for(int i=0;i<contactNumber.length();i++)
                        {
                            if(contactNumber.charAt(i)!=' ')
                                w+=contactNumber.charAt(i);
                        }
                        contacts.add(w);
                        break;
                    }
                    pCur.close();
                }

            } while (cursor.moveToNext()) ;
        }
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Chat");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

for(DataSnapshot snapshot:dataSnapshot.getChildren())
{
    String num = snapshot.child("Phone").getValue(String.class);
    String name=snapshot.child("Name").getValue(String.class);

    Log.d("mere",num+ "  "+ name);
   if(num.equals(phone))
       continue;
    for(String s:contacts)
    {
        if(s.length()>3 && (num.equals(s) || num.equals(s.substring(3))))
        {
Phone.add(num);
            Name.add(name+"\n"+num);
            Log.d("mere",num+ "  "+ name+" tu ha kar");
arrayAdapter.notifyDataSetChanged();
        break;
        }
    }



}






            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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



 return  view;
    }

}
