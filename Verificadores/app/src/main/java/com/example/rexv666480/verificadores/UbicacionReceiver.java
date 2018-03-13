package com.example.rexv666480.verificadores;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.rexv666480.verificadores.Servicios.ServiceUbicacion;
import com.example.rexv666480.verificadores.Servicios.ServicioBackgroundUbicacion;
import com.google.gson.Gson;

public class UbicacionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        try {

            Log.d("MyBootReceiver", "Boot Completed!");
            Toast.makeText(context,"Carga del Sistema Completo",Toast.LENGTH_LONG).show();
            Intent intentUbicacion = new Intent(context, ServicioBackgroundUbicacion.class);
            context.startService(intentUbicacion);
        }catch (Exception ex)
        {
            Log.d("UbicacionReceiver",ex.getMessage());
        }

    }
}
