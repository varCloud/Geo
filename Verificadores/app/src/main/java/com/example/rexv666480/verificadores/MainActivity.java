package com.example.rexv666480.verificadores;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.example.rexv666480.verificadores.Utilerias.NotificacionToast;
import com.example.rexv666480.verificadores.Utilerias.UbicacionActual;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindAnim;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    @BindView(R.id.faComollegar)
    FloatingActionButton faComollegar;
    @BindView(R.id.faCanecelar)
    FloatingActionButton faCanecelar;
    @BindView(R.id.faFinalizar)
    FloatingActionButton faFinalizar;
    @BindView(R.id.faIniciar)
    FloatingActionButton faIniciar;
    @BindView(R.id.faMenu)
    FloatingActionButton faMenu;

    @BindAnim(R.anim.scale_up)
    Animation scale_up;
    @BindAnim(R.anim.scale_down)
    Animation scale_down;
    @BindAnim(R.anim.show_from_bottom)
    Animation show_from_bottom;
//    @BindView(R.id.menu_yellow)
//    FloatingActionMenu menuYellow;

    private static final int ACCESS_FINE_LOCATION = 102;
    private static final String TAG = "Actividad MAIN";
    private AgenteServicioUbicacion agenteServicioUbicacion = null;
    private Visita visita;
    private Verificador verificador;
    ArrayList markerPoints = new ArrayList();

    private GoogleMap mMap;
    Loading loading;
    Activity activity;
    UbicacionActual ubicacionActual;
    NotificacionToast notificacionToast = null;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private Boolean isFabOpen = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);
            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.ContenedorMapa);
            mapFragment.getMapAsync(this);

            /*  --------------------------ANIMACIONES ------------------------------------------*/
                fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
                fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
                rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
                rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
               // menuYellow.hideMenuButton(false);
            /*---------------------FIN DE ANIMACIONES --------------------------------------------*/
            faComollegar.setOnClickListener(this);
            faMenu.setOnClickListener(this);
            faIniciar.setOnClickListener(this);
            faFinalizar.setOnClickListener(this);
            faCanecelar.setOnClickListener(this);
            /*--------------------------------------------------------------------------------------------*/
            loading = new Loading(this);
            this.activity = this;
            Intent i = getIntent();
            visita = new Gson().fromJson(i.getStringExtra("paramVisita"), Visita.class);
            verificador = new Gson().fromJson(i.getStringExtra("paramVerificador"), Verificador.class);
            ubicacionActual = new UbicacionActual();
            agenteServicioUbicacion = new AgenteServicioUbicacion(this);
            notificacionToast= new NotificacionToast(getApplicationContext());

            if(visita.getIdEstatusVisita().toString().equals("2")) {
                verificador.setidVisita(visita.getIdVisita());
                agenteServicioUbicacion.IniciarServicio(verificador);
                faIniciar.setTag("3");
                faIniciar.setImageResource(R.mipmap.ic_stop);
            }
        } catch (Exception ex) {
            throw ex;
        }


    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        String tag = "";
        switch (id) {
            case R.id.faMenu:
                animateFAB();
                break;
            case R.id.faIniciar:
                tag = v.getTag().toString();
                if (tag.equals("2")) {
                    faIniciar.setTag("3");
                    faIniciar.setImageResource(R.mipmap.ic_stop);
                    faIniciar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAmarilo1)));
                } else {
                    faIniciar.setTag("2");
                    faIniciar.setImageResource(R.mipmap.ic_play);
                    faIniciar.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAzul3)));
                }
                ActualizarVisita(tag);
                break;
            case R.id.faCanecelar:
                tag = v.getTag().toString();
                ActualizarVisita(tag);
                break;
            case R.id.faComollegar:
            case R.id.btnComollegar:
                if (ContextCompat.checkSelfPermission(getApplication(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    Comollegar();
                } else {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            ACCESS_FINE_LOCATION);
                }
                break;
            case R.id.faFinalizar:
                tag = v.getTag().toString();
                ActualizarVisita(tag);
                break;
        }
    }


    @Override
    public void onMapReady(final GoogleMap mMapa) {
        try {
            final LatLng latitudMorelia = new LatLng(19.708812, -101.198874);
            mMap = mMapa;
            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                    latitudMorelia, 5);
            mMap.animateCamera(location);
            /* ----------------CUANDO SE TERMINA DE CARGAR EL MAPA  SE EJECUTA ESTE EVENTO---------------*/
            mMapa.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    if (markerPoints.size() > 1) {
                        markerPoints.clear();
                        mMap.clear();
                    }

                    // Adding new item to the ArrayList
                    ubicacionActual.getLocation(activity);
                    // AgregarMarker(ubicacionActual.ObtenerLatLng());
                    AgregarMarker(visita.getOrigen(), visita.getDescripcionOrigen());
                    AgregarMarker(visita.getDestino(), visita.getDescripcionDestino());

                    // Checks, whether start and end locations are captured
                    if (markerPoints.size() >= 2) {
                        final LatLng origin = (LatLng) markerPoints.get(0);
                        final LatLng dest = (LatLng) markerPoints.get(1);

                        // Getting URL to the Google Directions API
                        LatLngBounds.Builder builder = new LatLngBounds.Builder();

                        builder.include(origin);
                        builder.include(dest);
                        LatLngBounds bounds = builder.build();
                        int width = getResources().getDisplayMetrics().widthPixels;
                        int height = getResources().getDisplayMetrics().heightPixels;
                        int padding = (int) (width * 0.12);
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                        mMap.animateCamera(cu, 4000 , null);
                        loading.CerrarLoading();

                        //String url = getDirectionsUrl(origin, dest);
                        DownloadTask downloadTask = new DownloadTask(new AsyncResponse() {
                            @Override
                            public void processFinish(PolylineOptions output) {
                                try {
                                    loading.CerrarLoading();
                                    if (output != null) {
                                        mMap.addPolyline(output);
                                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                        builder.include(origin);
                                        builder.include(dest);
                                        LatLngBounds bounds = builder.build();
                                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
                                        mMap.animateCamera(cu);
                                    } else
                                        notificacionToast.Show("Las lineas del mapa son null");
                                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((LatLng) markerPoints.get(0), 10F));
                                } catch (Exception ex) {
                                    Log.d("error verificador", ex.getMessage());
                                }
                            }
                        });

                    }
                }
            });

            // se consume el web service de  Google Directions API hasta 2500 peticiones diarias
            // downloadTask.execute(url); para pintar la ruta

        } catch (Exception ex) {
            loading.CerrarLoading();
            notificacionToast.Show(ex.getMessage());
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

    private void AgregarMarker(LatLng latLng, String descripcion) {
        markerPoints.add(latLng);
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);

        if (markerPoints.size() == 1) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            options.title("Origen: " + descripcion);

        } else if (markerPoints.size() == 2) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            options.title("Destino: " + descripcion);

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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
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

    public void Comollegar() {
        try {

            ubicacionActual.getLocation(activity);
            String format = "geo:" + ubicacionActual.getLatitude() + "," + ubicacionActual.getLongitude() + "?q=" + visita.getLatitudOrigen() + "," + visita.getLongitudOrigen() + "(" + visita.getDescripcionOrigen() + ")";
            Uri uri = Uri.parse(format);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } catch (Exception ex) {
             notificacionToast.Show(ex.getMessage());
            Log.d(TAG, ex.getMessage());
        }
    }

    public void ActualizarVisita(final String estatus) {
        try {
            loading.ShowLoading("Actulizando estatus de la visita");
            Map<String, String> map = new HashMap<String, String>();
            map.put("idEstatusVisita", estatus);
            map.put("idVisita", visita.getIdVisita() + "");
            RetrofitClient retrofitClient = new RetrofitClient();
            ServiciosWeb sw = retrofitClient.getRetrofit().create(ServiciosWeb.class);
            sw.ActualizarEstatusVisitas(map).enqueue(new Callback<Map<String, String>>() {
                @Override
                public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                    if (response.code() == 200) {
                        Map<String, String> map = response.body();
                        if (map.get("estatus").toString().equals("500")) {
                            notificacionToast.Show("Ocurrio un error al actulizar la visita: " + map.get("msj"));
                        } else {
                            if (estatus.equals("2") || estatus.equals("3")) {
                                verificador.setidVisita(visita.getIdVisita());
                                agenteServicioUbicacion.IniciarServicio(verificador);
                            } else {
                                finish();
                            }
                        }
                    }
                    loading.CerrarLoading();
                }

                @Override
                public void onFailure(Call<Map<String, String>> call, Throwable t) {
                    loading.CerrarLoading();
                    notificacionToast.Show("Espere un momento y vuelva a intentarlo");
                }

            });

        } catch (Exception ex) {
            notificacionToast.Show("Espere un momento y vualva a intentarlo");
        }

    }


    public void animateFAB() {

        try {

            if (isFabOpen) {

                faMenu.startAnimation(rotate_backward);
                faComollegar.startAnimation(fab_close);
                faIniciar.startAnimation(fab_close);
                faFinalizar.startAnimation(fab_close);
                faCanecelar.startAnimation(fab_close);

                faCanecelar.setClickable(false);
                faFinalizar.setClickable(false);
                faCanecelar.setClickable(false);
                faFinalizar.setClickable(false);
                isFabOpen = false;

                faMenu.setImageResource(R.mipmap.ic_menu);

            } else {
                faMenu.startAnimation(rotate_forward);
                faComollegar.startAnimation(fab_open);
                faIniciar.startAnimation(fab_open);
                faFinalizar.startAnimation(fab_open);
                faCanecelar.startAnimation(fab_open);

                faComollegar.setClickable(true);
                faIniciar.setClickable(true);
                faFinalizar.setClickable(true);
                faCanecelar.setClickable(true);
                isFabOpen = true;
                faMenu.setImageResource(R.mipmap.ic_close);


            }
        } catch (Exception ex) {
            throw ex;
        }
    }

}
