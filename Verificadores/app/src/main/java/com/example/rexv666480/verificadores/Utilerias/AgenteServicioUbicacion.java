package com.example.rexv666480.verificadores.Utilerias;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.util.Log;

import com.example.rexv666480.verificadores.Entidades.Verificador;
import com.example.rexv666480.verificadores.Servicios.ServiceUbicacion;
import com.google.gson.Gson;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by rexv666480 on 17/01/2018.
 */

public class AgenteServicioUbicacion {
    private Activity activity;
    private static final String TAG ="AgenteServicioUbicacion";
    public AgenteServicioUbicacion(Activity activity) {
        this.activity = activity;
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) activity.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            Log.d(TAG,service.service.getClassName());
            if("com.example.rexv666480.verificadores.Servicios.ServiceUbicacion".toString().equals(service.service.getClassName().toString())) {
                return true;
            }
        }
        return false;
    }

    public void IniciarServicio(Verificador verificador)
    {
        try {

                //DetenerServicio();
                Intent intentServicio = new Intent(activity, ServiceUbicacion.class);
                intentServicio.putExtra("paramVerificador", new Gson().toJson(verificador));
                activity.startService(intentServicio);

        }catch (Exception ex)
        {
            throw  ex;
        }
    }

    private void DetenerServicio()
    {
        try {
                if(isServiceRunning()) {
                    Intent intentServicio = new Intent(activity, ServiceUbicacion.class);
                    activity.stopService(intentServicio);
                }

        }catch (Exception ex)
        {
            throw  ex;
        }
    }

}
