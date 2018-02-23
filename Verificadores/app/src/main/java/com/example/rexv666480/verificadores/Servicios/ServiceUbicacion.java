package com.example.rexv666480.verificadores.Servicios;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.example.rexv666480.verificadores.Entidades.Respuesta;
import com.example.rexv666480.verificadores.Entidades.Ubicacion;
import com.example.rexv666480.verificadores.Entidades.Verificador;
import com.example.rexv666480.verificadores.ServiciosWeb.Respuestas.RespEstatus;
import com.example.rexv666480.verificadores.ServiciosWeb.RetrofitClient;
import com.example.rexv666480.verificadores.ServiciosWeb.ServiciosWeb;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rexv666480 on 14/12/2017.
 */

public class ServiceUbicacion extends Service
{
    private static final String TAG = "ServicioUbicacion";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 5000 ; // seis segundos
    private static final float LOCATION_DISTANCE = 0f; // 10 metros
    private RetrofitClient retrofitClient=null;
    private Verificador verificador;
    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };
    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        try {
            Log.e(TAG, "onStartCommand");
            super.onStartCommand(intent, flags, startId);
            verificador = new Gson().fromJson(intent.getStringExtra("paramVerificador"), Verificador.class);

        }catch (Exception ex)
        {
            Log.d(TAG,ex.getMessage());
        }
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {

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

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        try{
            Log.e(TAG, "initializeLocationManager");
            if (mLocationManager == null) {
                mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            }

            if (retrofitClient == null) {
                retrofitClient = new RetrofitClient();
            }
        }catch (Exception ex)
        {
            Log.d(TAG, ex.getMessage());
        }

    }

    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;
        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location)
        {
            try {

                Log.e(TAG, "onLocationChanged: " + location);
                mLastLocation.set(location);
                ServiciosWeb sw = retrofitClient.getRetrofit().create(ServiciosWeb.class);
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                verificador.setUbicacion(new Ubicacion(latLng, "Ubicacion Actual del Operador"));
                sw.enviarUbicacionActual(verificador).enqueue(new Callback<RespEstatus>() {
                    @Override
                    public void onResponse(Call<RespEstatus> call, Response<RespEstatus> response) {
                        if (response.code() == 200) {
                            RespEstatus resp = response.body();
                            Log.d(TAG, resp.getEstatus());
                            if (resp.getEstatus().toString().equals("1"))
                                Log.d(TAG, "actualizacion correcta");
                        } else
                            Log.d(TAG, "Web servoce no responde con los parametros acutuales");
                    }

                    @Override
                    public void onFailure(Call<RespEstatus> call, Throwable t) {
                        try {
                            throw  t;
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                });
            }catch (Exception ex)
            {
                Log.d(TAG, ex.getMessage());
            }
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

}