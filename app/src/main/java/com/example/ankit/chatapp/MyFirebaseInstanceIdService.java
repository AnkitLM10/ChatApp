package com.example.ankit.chatapp;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by ankit on 7/26/2018.
 */
public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {


    //this method will be called
    //when the token is generated
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        //now we will have the token
        String token = FirebaseInstanceId.getInstance().getToken();

        SharedPreferences sharedPreferences = getSharedPreferences("MyData",MODE_PRIVATE);


            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("Token",token);
      editor.commit();
        //for now we are displaying the token in the log
        //copy it as this method is called only when the new token is generated
        //and usually new token is only generated when the app is reinstalled or the data is cleared
        Log.d("MyRefreshedToken", token);
    }
}