package com.mityung.contactdirectory;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.example.contactplusgroup.common.Comman;
import com.example.contactplusgroup.common.SharedPreferanceData;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String questionTitle = null;
    private SharedPreferanceData shared = null;
    public static final int NOTIFICATION_ID = 1;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        try {
            questionTitle = remoteMessage.getData().get("message").toString();
        } catch (Exception e) {
        }
        shared = new SharedPreferanceData(MyFirebaseMessagingService.this);
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent i          = null;
        String[] msg = questionTitle.split("#");
        String msgSubject    = msg[0];
        String msgBody = msg[1];
        String msgType   = "";
        try {
            msgType = msg[2];
        } catch (Exception e) {
            // TODO: handle exception
        }

        //String messg = "";
        if(msgType.trim().equals("-1000")){
            //message for update
            final String appPackageName = getPackageName();
            i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName));
            //PendingIntent contentIntent = PendingIntent.getActivity(this, 0,i, 0);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            long[] pattern = {0, 100, 1000};
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    this).setSmallIcon(R.drawable.ic_custom_notification)
                    .setContentTitle(msgSubject)
                    .setAutoCancel(true)
                    .setSound(alarmSound)
                    .setVibrate(pattern)
                    .setContentText(msgBody);


            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }else if(msgType.trim().equals("-2000")){
            //message for block the app.
            shared.saveSharedData("AppUpdateNoStartStatus", new Comman().getAppVersionName(MyFirebaseMessagingService.this));
            i = new Intent(MyFirebaseMessagingService.this, AppUpdate.class);
            i.putExtra("msg",msgBody);
            //PendingIntent contentIntent = PendingIntent.getActivity(this, 0,i, 0);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            long[] pattern = {0, 100, 1000};
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    this).setSmallIcon(R.drawable.ic_custom_notification)
                    .setContentTitle(msgSubject)
                    .setAutoCancel(true)
                    .setSound(alarmSound)
                    .setVibrate(pattern)
                    .setContentText(msgBody);


            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }else{
            sendMultilineNotification(msgSubject, msgBody);
        }
    }

    private void sendMultilineNotification(String title, String messageBody) {
        //Log.e("DADA", "ADAD---"+title+"---message---"+messageBody);
        int notificationId = 0;
        Intent intent = new Intent(this, MainDashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_custom_notification)
                .setLargeIcon(largeIcon)
                .setContentTitle(title/*"Firebase Push Notification"*/)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(messageBody))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

}
