package com.andolasoft.visitorsmanagement.activity;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.andolasoft.visitorsmanagement.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFcmListenerService";
    SharedPreferences prefs, myprefs;
    SharedPreferences.Editor editor;
    public String APP_PUSH_NOTIFICATION_TITLE = "Visitor Management";


    @Override
    public void onMessageReceived(RemoteMessage message) {
        // Check if message contains a data payload.
        // String from = message.getFrom();

        try{


            Map<String,String> data1 = message.getData();



            //if (NotificationManagerCompat.from(getApplicationContext()).areNotificationsEnabled()){
            //sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());




            //if (NotificationManagerCompat.from(getApplicationContext()).areNotificationsEnabled()){
            //sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());




            // Check if message contains a notification payload.
            String messageBody = data1.get("body");
            String phone = data1.get("phone");
            //String message_type = message.getMessageType();
            //String message_id = message.getMessageId();
            //int data_1 = data.size();

            //saveNotification(messageBody,currentTime(),phone);
            sendNotification(messageBody);
           /* db = new DbHelper(this);
            tinydb = new TinyDB(this);

            db.insertTips(messageBody,String.valueOf(System.currentTimeMillis()));

            if(note_link.equalsIgnoreCase("NA")){
                sendNotification(messageBody,message_title,note_link);
            }else{
                sendLinkNotification(messageBody,message_title,note_link);
            }*/

            Log.i(TAG, "Push Notificatoin Received");

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    public String currentTime(){
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        return df.format(new Date());
    }


    //This method to generate push notification
    private void sendNotification(String messageBody) {


        Intent intent = new Intent(this, VisitorDetails.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Intent_Status","Push");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        //Take Notification Sound
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        //Generate the Notification
        NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(this)
                //.setSmallIcon(R.drawable.bookbus_appicon)
                .setSmallIcon(R.mipmap.app_icon, 100)
                .setLargeIcon(largeIcon)
                .setAutoCancel(true)
                .setContentTitle(APP_PUSH_NOTIFICATION_TITLE)
                .setContentText("A new Visitor wants to meet you")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        //Create Push Notification
        notificationManager.notify(0, notificationBuilder.build());

        Intent intent1 = new Intent(MyFirebaseMessagingService.this,UserDetailsPopup.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);
    }

    private void sendLinkNotification(String messageBody, String messageTitle,String note_link) {

      /*  Intent intent = new Intent(this, NoteLinkActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("isTIPS", true);
        intent.putExtra("note_link", note_link);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        //Take Notification Sound
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_small);
        //Generate the Notification
        NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(this)
                //.setSmallIcon(R.drawable.bookbus_appicon)
                .setSmallIcon(R.drawable.icon_small, 100)
                .setLargeIcon(largeIcon)
                .setAutoCancel(true)
                .setContentTitle(APP_PUSH_NOTIFICATION_TITLE)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        //Create Push Notification
        notificationManager.notify(0, notificationBuilder.build());*/
    }

    /*public String getPushNotificationTitle(String title_code){

        String title_text = null;
        if(title_code != null &&
                title_code.equalsIgnoreCase(String.valueOf(KeyConstant.PUSH_NOTIFICATION_SUCCESSFUL_PAYMENT))){
            title_text =
        }


    }*/

    /*public static int PUSH_NOTIFICATION_SUCCESSFUL_PAYMENT = 300;
    public static int PUSH_NOTIFICATION_BUS_OFFER = 301;
    public static int PUSH_NOTIFICATION_UPCOMING_TRIP = 302;
    public static int PUSH_NOTIFICATION_JOURNEY_FEEDBACK = 303;
    public static int PUSH_NOTIFICATION_CANCEL_TICKET = 304;
    public static int PUSH_NOTIFICATION_BUS_CANCELLATION = 305;*/
}
