package com.example.rexv666480.verificadores;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.rexv666480.verificadores.Entidades.Ruta;
import com.example.rexv666480.verificadores.PintaRutaMapa.AsyncResponse;
import com.example.rexv666480.verificadores.PintaRutaMapa.DownloadTask;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements OnMapReadyCallback  {
//AIzaSyC3NUL0UP9ON9VPD-RocNdgovfKXFEa9Nc
    Ruta ruta;
    ArrayList markerPoints= new ArrayList();
    Button btnIniciarNavegacion = null;
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.ContenedorMapa);
        mapFragment.getMapAsync(this);

        btnIniciarNavegacion = (Button) findViewById(R.id.btnIniciarNevegacion);

        Intent i = getIntent();
        ruta = new Gson().fromJson(i.getStringExtra("paramRuta"),Ruta.class);
        Log.d("ubicacion por parametro", ruta.getDescripcionRuta());
        initClickIniciarNavegacion();
    }


    public void initClickIniciarNavegacion()
    {
        try{
            btnIniciarNavegacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String format = "geo:"+ruta.getOrigen().getLatLng().latitude+","+ruta.getOrigen().getLatLng().longitude+"?q=" + +ruta.getDestino().getLatLng().latitude + "," + +ruta.getDestino().getLatLng().longitude + "("+ruta.getDescripcionRuta()+")";
                    Uri uri = Uri.parse(format);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

        }catch (Exception ex)
        {
            Log.d("Error victor",ex.getMessage());
        }
    }



    @Override
    public void onMapReady(final GoogleMap mMapa) {
        mMap = mMapa;
        final LatLng latitudMorelia = new  LatLng(19.1666700, -101.8333300);
//        mMap.addMarker(new MarkerOptions()
//                .position(latitudMorelia)
//                .title("Marcador"));
       float zoomLevel = 21.0f; //This goes up to 21
       mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latitudMorelia, zoomLevel));

        // para activar el click en el mapa
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {

                if (markerPoints.size() > 1) {
                    markerPoints.clear();
                    mMap.clear();
                }

                // Adding new item to the ArrayList
                AgregarMarker(ruta.getOrigen().getLatLng());
                AgregarMarker(ruta.getDestino().getLatLng());

                // Checks, whether start and end locations are captured
                if (markerPoints.size() >= 2) {
                    LatLng origin = (LatLng) markerPoints.get(0);
                    LatLng dest = (LatLng) markerPoints.get(1);

                    // Getting URL to the Google Directions API
                    String url = getDirectionsUrl(origin, dest);
                    DownloadTask downloadTask = new DownloadTask(new AsyncResponse() {
                        @Override
                        public void processFinish(PolylineOptions output) {
                            try {
                                mMap.addPolyline(output);
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((LatLng) markerPoints.get(0), 10F));

                            }catch (Exception ex)
                            {
                                Log.d("error verificador", ex.getMessage());
                            }
                        }
                    });

                    // se consume el web service de  Google Directions API hasta 2500 peticiones diarias
                    downloadTask.execute(url);
                }

//            }
//        });
    }

    private void AgregarMarker(LatLng latLng)
    {
        markerPoints.add(latLng);
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);

        if (markerPoints.size() == 1) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else if (markerPoints.size() == 2) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }
        mMap.addMarker(options);
    }
    private String getDirectionsUrl(LatLng origin, LatLng dest) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        return url;
    }

}
