package com.example.mohammad.tenniscoach.fcm;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.mohammad.tenniscoach.MainActivity;
import com.example.mohammad.tenniscoach.R;
import com.example.mohammad.tenniscoach.SessionActivity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

public class FirebaseCloudMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Intent intent = new Intent(this, SessionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent i = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "TennisCoachAPP")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("body"))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(remoteMessage.getData().get("body")))
                .setContentIntent(i)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true);;

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(new Random().nextInt(), mBuilder.build());
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);

    }
}
