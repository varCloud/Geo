package com.example.rexv666480.verificadores;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.rexv666480.verificadores.Servicios.ServiceUbicacion;
import com.google.gson.Gson;

public class UbicacionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        try {
            SharedPreferences settings = context.getSharedPreferences("MisPreferencias", context.getApplicationContext().MODE_PRIVATE);
            String verificador = settings.getString("paramVerificador", "");
            Log.d("UbicacionReceiver",verificador);
            Toast.makeText(context, "Servicio iniciado desde BroadcastReceiver", Toast.LENGTH_SHORT).show();
            Intent intentUbicacion = new Intent(context, ServiceUbicacion.class);
            intent.putExtra("paramVerificador", verificador);
            context.startService(intentUbicacion);
        }catch (Exception ex)
        {
            Log.d("UbicacionReceiver",ex.getMessage());
        }

    }
}
