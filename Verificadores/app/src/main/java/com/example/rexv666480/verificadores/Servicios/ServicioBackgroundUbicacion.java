package com.example.rexv666480.verificadores.Servicios;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.example.rexv666480.verificadores.Entidades.Ubicacion;
import com.example.rexv666480.verificadores.Entidades.Verificador;
import com.example.rexv666480.verificadores.ServiciosWeb.Respuestas.RespEstatus;
import com.example.rexv666480.verificadores.ServiciosWeb.RetrofitClient;
import com.example.rexv666480.verificadores.ServiciosWeb.ServiciosWeb;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by victor on 12/03/18.
 */

public class ServicioBackgroundUbicacion extends Service {
    private static final String TAG = "ServiceBackground";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 6000; // seis segundos
    private static final float LOCATION_DISTANCE = 0f; // 10 metros
    private RetrofitClient retrofitClient = null;
    private Verificador verificador;
    private Timer mTimer;
    private TimerTask timerTask;
    private Location locationWS;
    private boolean puedoActualizar  = true;
    ServicioBackgroundUbicacion.LocationListenerBack[] mLocationListeners = new ServicioBackgroundUbicacion.LocationListenerBack[]{
            new ServicioBackgroundUbicacion.LocationListenerBack(LocationManager.GPS_PROVIDER),
            new ServicioBackgroundUbicacion.LocationListenerBack(LocationManager.NETWORK_PROVIDER)
    };


    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                Log.e(TAG, "ejecutando");
                try {

                    if(locationWS != null && puedoActualizar) {
                        puedoActualizar=false;
                        Log.e(TAG, "onLocationChanged: " + getDateTime() + " " + locationWS);
                        ServiciosWeb sw = retrofitClient.getRetrofit().create(ServiciosWeb.class);
                        LatLng latLng = new LatLng(locationWS.getLatitude(), locationWS.getLongitude());
                        ObtenerVerificador();
                        verificador.setUbicacion(new Ubicacion(latLng, "Ubicacion Actual del Operador"));
                        sw.enviarUbicacionActual(verificador).enqueue(new Callback<RespEstatus>() {
                            @Override
                            public void onResponse(Call<RespEstatus> call, Response<RespEstatus> response) {
                                if (response.code() == 200) {
                                    RespEstatus resp = response.body();
                                    Log.d(TAG," ServicioRespWS: "+resp.getEstatus());
                                    if (resp.getEstatus().toString().equals("1"))
                                        Log.d(TAG, "actualizacion correcta");
                                } else
                                    Log.d(TAG, "Web service no responde con los parametros acutuales");
                                puedoActualizar = true;
                            }

                            @Override
                            public void onFailure(Call<RespEstatus> call, Throwable t) {
                                puedoActualizar=true;
                                try {
                                    throw t;
                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (Exception ex) {
                    puedoActualizar=true;
                    Log.d(TAG, ex.getMessage());
                }
            }
        };
    }

    public void ObtenerVerificador()
    {
        try {
            if(verificador == null) {
                SharedPreferences settings = getApplicationContext().getSharedPreferences("MisPreferencias", getApplicationContext().MODE_PRIVATE);
                String x = settings.getString("paramVerificador", "");
                Log.d(TAG, x);
                verificador = new Gson().fromJson(x, Verificador.class);
            }
        }catch (Exception ex)
        {
            Log.e(TAG,"error en ObtenerVerificador"+ex.getMessage());
        }
    }

    public void startTimer() {
        //set a new Timer
        mTimer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        mTimer.schedule(timerTask, 1000, (1000 * 5)); //
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            Log.e(TAG, "onStartCommand");
            super.onStartCommand(intent, flags, startId);

            verificador = new Gson().fromJson(intent.getStringExtra("paramVerificador"), Verificador.class);
            ObtenerVerificador();

            startTimer();
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {

        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.d(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.d(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }

    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }


    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.e(TAG, "onDestroy");
        Log.e(TAG,"Evento de destruir");

        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
        Log.e(TAG,"Evento de destruir");
        Intent intent = new Intent("com.example.rexv666480.verificadores.ReiniciarServicio");
        sendBroadcast(intent);
        stoptimertask();

    }

    private void initializeLocationManager() {
        try {
            Log.e(TAG, "initializeLocationManager");
            if (mLocationManager == null) {
                mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            }

            if (retrofitClient == null) {
                retrofitClient = new RetrofitClient();
            }
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }
    }

    private class LocationListenerBack implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListenerBack(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        private String getDateTime() {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            return dateFormat.format(date);
        }

        @Override
        public void onLocationChanged(Location location) {
            try {

                Log.e(TAG, "onLocationChanged: " + getDateTime() + " " + location);
                mLastLocation.set(location);
                locationWS=location;
            } catch (Exception ex) {
                Log.d(TAG, ex.getMessage());
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }
}


