package com.example.rexv666480.verificadores.Servicios;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.rexv666480.verificadores.R;
import com.example.rexv666480.verificadores.VisitasActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rexv666480 on 15/12/2017.
 */

public class ServiceFCMPush extends FirebaseMessagingService {

    private static final String TAG = "Servicio Push";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {

        Intent intent = new Intent(this, VisitasActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410,
                intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("handleNotification")
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1410, notificationBuilder.build());
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {

            JSONObject data = json.getJSONObject("data");
            String title = data.getString("titulo");
            String message = data.getString("mensaje");
            String tipoNotificacion = data.getString("tipoNotificacion");

            SonidoNotificacion();
            NotificationCompat.Builder notificationBuilder = new
                    NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.logo)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true);
            if(tipoNotificacion.equals("2")) {

                Intent intent = new Intent(this, VisitasActivity.class);
                intent.putExtra("paramtipoNotificacion", tipoNotificacion);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 1410, intent, PendingIntent.FLAG_ONE_SHOT);
                notificationBuilder.setContentIntent(pendingIntent);
                notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
            }
            notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1410, notificationBuilder.build());

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    public void MuestraNotificacion()
    {
        try{

        }catch (Exception ex)
        {
            Log.d(TAG , ex.getMessage());
        }

    }


    public void SonidoNotificacion()
    {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}