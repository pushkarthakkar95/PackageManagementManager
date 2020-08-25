package com.pushkar.packagemanagementadmin.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pushkar.packagemanagementadmin.R;
import com.pushkar.packagemanagementadmin.utils.Constants;
import com.pushkar.packagemanagementadmin.utils.ParserHelper;
import com.pushkar.packagemanagementadmin.view.findpackage.PickupDetailsActivity;

import java.util.ArrayList;

public class MyNotificationService extends FirebaseMessagingService {
    private final String TAG = MyNotificationService.class.getSimpleName();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived hit");
        createNotificationClass();
        showNotification(
                remoteMessage.getData().get("title"),
                remoteMessage.getData().get("body")
        );

    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

    }

    public void showNotification(String title, String message){
        Intent notifyIntent = new Intent(this, PickupDetailsActivity.class);
        ArrayList<String> barcodeList = ParserHelper.getListOfParsedBarcodes(message);
        notifyIntent.putStringArrayListExtra(Constants.BARCODE_LIST_NOTIFICATION_MESSAGE,barcodeList);
        notifyIntent.putExtra(Constants.NAME_IN_NOTIFICATION,ParserHelper.getNameFromTitle(title));
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Create the PendingIntent
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                Constants.NOTIFICATION_CHANNEL_ID)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.box_128)
                .setAutoCancel(true)
                .setContentIntent(notifyPendingIntent)
                .setContentText(message);
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        int id = (int) System.currentTimeMillis();
        manager.notify(id,builder.build());
    }

    private void createNotificationClass(){
        NotificationChannel notificationChannel = new NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID,
                Constants.NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

        NotificationManager manager = getSystemService(NotificationManager.class);
        if(manager != null){
            manager.createNotificationChannel(notificationChannel);
        }
    }
}
