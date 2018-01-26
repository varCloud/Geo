package com.example.rexv666480.verificadores;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rexv666480.verificadores.Entidades.Ruta;
import com.example.rexv666480.verificadores.Entidades.Verificador;
import com.example.rexv666480.verificadores.Entidades.Visita;
import com.example.rexv666480.verificadores.PintaRutaMapa.AsyncResponse;
import com.example.rexv666480.verificadores.PintaRutaMapa.DownloadTask;
import com.example.rexv666480.verificadores.Servicios.ServiceUbicacion;
import com.example.rexv666480.verificadores.ServiciosWeb.RetrofitClient;
import com.example.rexv666480.verificadores.ServiciosWeb.ServiciosWeb;
import com.example.rexv666480.verificadores.Utilerias.AgenteServicioUbicacion;
import com.example.rexv666480.verificadores.Utilerias.Alert;
import com.example.rexv666480.verificadores.Utilerias.Loading;
import com.example.rexv666480.verificadores.Utilerias.UbicacionActual;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity  implements OnMapReadyCallback ,GoogleMap.OnMarkerClickListener{
//AIzaSyC3NUL0UP9ON9VPD-RocNdgovfKXFEa9Nc
    private static final int ACCESS_FINE_LOCATION = 102;
    private static final String TAG ="Actividad MAIN";
    private AgenteServicioUbicacion agenteServicioUbicacion=null;
    private Visita visita;
    private Verificador verificador;
    ArrayList markerPoints= new ArrayList();
    Button btnIniciarNavegacion , btnComollegar ,  btnCancelar,btnFinalizar;
    private GoogleMap mMap;
    Loading loading;
    Activity activity;
    UbicacionActual ubicacionActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.ContenedorMapa);
        mapFragment.getMapAsync(this);
        loading = new Loading(this);
        btnIniciarNavegacion = (Button) findViewById(R.id.btnIniciarNevegacion);
        btnComollegar = (Button) findViewById(R.id.btnComollegar);
        btnCancelar = (Button) findViewById(R.id.btnCancelarnavegacion);
        btnFinalizar = (Button) findViewById(R.id.btnFinalizarVisita);
        this.activity = this;
        Intent i = getIntent();
        visita = new Gson().fromJson(i.getStringExtra("paramVisita"),Visita.class);
        verificador = new Gson().fromJson(i.getStringExtra("paramVerificador"),Verificador.class);
        ubicacionActual = new UbicacionActual();
        initClicksBotones();
        agenteServicioUbicacion = new AgenteServicioUbicacion(this);
//        if(visita.getIdEstatusVisita().equals("2"))
//            btnIniciarNavegacion.setVisibility(View.INVISIBLE);

    }

    public void initClicksBotones()
    {
        try{
            btnComollegar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(getApplication(),android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                            Comollegar();
                    }else
                    {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                ACCESS_FINE_LOCATION);
                    }
                }
            });

            btnIniciarNavegacion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActualizarVisita("2");
                }
            });

            btnCancelar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActualizarVisita("5");
                }
            });
            btnFinalizar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActualizarVisita("4");
                }
            });

        }catch (Exception ex)
        {
            Log.d(TAG,ex.getMessage());
        }
    }

    public void IniciarServicio()
    {
        try {
            Intent intentServicio = new Intent(this, ServiceUbicacion.class);
            intentServicio.putExtra("paramVerificador", new Gson().toJson(verificador));
            startService(intentServicio);
        }catch (Exception ex)
        {
            Toast(ex.getMessage());
        }
    }

    public void DetenerServicio()
    {
        try {
            Intent intentServicio = new Intent(this, ServiceUbicacion.class);
            stopService(intentServicio);
        }catch (Exception ex)
        {
            Toast(ex.getMessage());
        }
    }

    @Override
    public void onMapReady(final GoogleMap mMapa) {
        try {
            loading.ShowLoading("Cargando...");
            mMap = mMapa;
            final LatLng latitudMorelia = new LatLng(19.1666700, -101.8333300);
            float zoomLevel = 21.0f; //This goes up to 21
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latitudMorelia, zoomLevel));


            if (markerPoints.size() > 1) {
                markerPoints.clear();
                mMap.clear();
            }

            // Adding new item to the ArrayList
            ubicacionActual.getLocation(this.activity);
            AgregarMarker(ubicacionActual.ObtenerLatLng());
            AgregarMarker(visita.getOrigen());

            // Checks, whether start and end locations are captured
            if (markerPoints.size() >= 2) {
                final LatLng origin = (LatLng) markerPoints.get(0);
                final LatLng dest = (LatLng) markerPoints.get(1);

                // Getting URL to the Google Directions API
                String url = getDirectionsUrl(origin, dest);
                DownloadTask downloadTask = new DownloadTask(new AsyncResponse() {
                    @Override
                    public void processFinish(PolylineOptions output) {
                        try {
                            loading.CerrarLoading();
                            mMap.addPolyline(output);
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            builder.include(origin);
                            builder.include(dest);
                            LatLngBounds bounds = builder.build();
                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
                            mMap.animateCamera(cu);
                            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((LatLng) markerPoints.get(0), 10F));
                        } catch (Exception ex) {
                            Log.d("error verificador", ex.getMessage());
                        }
                    }
                });

                // se consume el web service de  Google Directions API hasta 2500 peticiones diarias
                downloadTask.execute(url);
            }
        }catch (Exception ex)
        {
            Log.d(TAG,ex.getMessage());
        }

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            Toast.makeText(this,
                    marker.getTitle(),
                    Toast.LENGTH_SHORT).show();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    private void AgregarMarker(LatLng latLng)
    {
        markerPoints.add(latLng);
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);

        if (markerPoints.size() == 1) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            options.title("Usted esta Aqui");
        } else if (markerPoints.size() == 2) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            options.title("Destino");

        }

        mMap.addMarker(options).setTag(0);
        mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);
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

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {

            case ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Comollegar();
                } else {

                }

            }
        }
    }

    public  void Comollegar()
    {
        try {

            ubicacionActual.getLocation(activity);
            String format = "geo:" + ubicacionActual.getLatitude() + "," + ubicacionActual.getLongitude() + "?q=" + visita.getLatitudOrigen() + "," + visita.getLongitudOrigen() + "(" + visita.getDescripcionOrigen() + ")";
            Uri uri = Uri.parse(format);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }catch (Exception ex)
        {
            Toast(ex.getMessage());
            Log.d(TAG,ex.getMessage());
        }
    }
    public void ActualizarVisita(final String estatus)
    {
        try
        {
            loading.ShowLoading("Actulizando estatus de la visita");
            Map<String,String> map =  new HashMap<String,String>();
            map.put("idEstatusVisita",estatus);
            map.put("idVisita",visita.getIdVisita()+"");
            RetrofitClient retrofitClient  = new RetrofitClient();
            ServiciosWeb sw =  retrofitClient.getRetrofit().create(ServiciosWeb.class);
            sw.ActualizarEstatusVisitas(map).enqueue(new Callback<Map<String, String>>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if(response.code() ==200) {
                        Map<String,String> map = response.body();
                        if(map.get("estatus").toString().equals("500"))
                        {
                            Toast("Ocurrio un error al actulizar la visita "+map.get("msj"));
                        }else
                        {
                            if(estatus.equals("2")) {
                                verificador.setidVisita(visita.getIdVisita());
                                agenteServicioUbicacion.IniciarServicio(verificador);
                            }else
                            {
                                finish();
                            }
                        }
                    }
                    loading.CerrarLoading();
                }

                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                    loading.CerrarLoading();
                    Toast(t.getMessage());
                }

            });

        }catch (Exception ex)
        {
            Toast(ex.getMessage());
        }

    }
    public void Toast(String mensaje)
    {
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
    }



}
