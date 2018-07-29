package com.example.ankit.chatapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by ankit on 7/26/2018.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        Log.d("mahi",Profile.isAlive+"");
        Map<String,String> map = remoteMessage.getData();
System.out.println(map.get("message")+" "+map);

        if(!Profile.isAlive)
        {



            Intent intent = new Intent(this,Profile.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("Project",1);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);


            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .setContentTitle(map.get("fromMessage"))
                            .setContentText(map.get("body"));



            NotificationManager mNotificationManager =

                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



String ans = map.get("phone");
            if(ans.charAt(0)=='+')
                ans=ans.substring(1);
         ans =   ans.substring(2);
            mNotificationManager.notify(Integer.parseInt(ans), mBuilder.build());





















        }





    }
}