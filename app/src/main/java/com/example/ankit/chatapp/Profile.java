package com.example.ankit.chatapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class Profile extends AppCompatActivity {

    static boolean isAlive;
    BottomNavigationView mainNav;
    FrameLayout mainFrame;
    UserChat userChat;
    UserPeople userPeople;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences sharedPreferences = getSharedPreferences("MyData",MODE_PRIVATE);
       String Phone = sharedPreferences.getString("Phone","");
       String tok=sharedPreferences.getString("Token","");
        String token = FirebaseInstanceId.getInstance().getToken();
        if(!tok.equals(token))
        {
            FirebaseDatabase.getInstance().getReference().child("Token").child(Phone).setValue(token);
        SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("Token",token);
        editor.commit();
        }
        mainNav=(BottomNavigationView)findViewById(R.id.mainNav);
        mainFrame=(FrameLayout)findViewById(R.id.mainFrame);
        userPeople=new UserPeople();
        userChat=new UserChat();
        setFragment(userChat);
        mainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {

                    case R.id.menuChat:
                        setFragment(userChat);
                        return true;

                    case R.id.menuPeople :

                        setFragment(userPeople);

                        return true;

                    default:
                        return false;

                }


            }
        });


    }
    @TargetApi(Build.VERSION_CODES.M)
    private void setFragment(Fragment fragment) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{
                    Manifest.permission.READ_CONTACTS


            },10);


            Toast.makeText(this,"Please Allow Contact Permission",Toast.LENGTH_LONG).show();
return;

        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainFrame,fragment);
        fragmentTransaction.commit();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 10)
        {

            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {

            }
            else
            {
                // Toast here Please Complete

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{
                            Manifest.permission.READ_CONTACTS


                    },10);


                    Toast.makeText(this,"Please Allow Contact Permission",Toast.LENGTH_LONG).show();
                    return;

                }
            }
        }
        return;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
            System.out.println("NO USER");
        else
        {
            System.out.println(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.item1:
                SharedPreferences.Editor editor =  getSharedPreferences("MyData",MODE_PRIVATE).edit();
                editor.putInt("LogIn",0);
                editor.commit();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Profile.this,MainActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isAlive=false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isAlive=true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isAlive=false;
    }
}
