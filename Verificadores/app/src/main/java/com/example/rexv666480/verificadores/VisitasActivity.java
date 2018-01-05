package com.example.rexv666480.verificadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.rexv666480.verificadores.Adapters.AdapterRuta;
import com.example.rexv666480.verificadores.Adapters.AdapterRutas;
import com.example.rexv666480.verificadores.Adapters.AdapterVisitas;
import com.example.rexv666480.verificadores.Entidades.Ruta;
import com.example.rexv666480.verificadores.Entidades.Verificador;

import com.example.rexv666480.verificadores.Entidades.Visita;
import com.example.rexv666480.verificadores.ServiciosWeb.Respuestas.RespVisitas;
import com.example.rexv666480.verificadores.ServiciosWeb.RetrofitClient;
import com.example.rexv666480.verificadores.ServiciosWeb.ServiciosWeb;
import com.example.rexv666480.verificadores.Utilerias.Loading;
import com.example.rexv666480.verificadores.Utilerias.UbicacionActual;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitasActivity extends AppCompatActivity {
    private static final int ACCESS_FINE_LOCATION = 102;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ListView mListView;
    private Verificador verificador;
    Loading loading =null;
    Context context = null;

    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_visitas);
            activity = this;
            context = this;
            //obtenemos los parametros que se pasaron de la actividad
            Intent i = getIntent();
            verificador = new Gson().fromJson(i.getStringExtra("paramVerificador"),Verificador.class);
            PermisoUbicacion();
            loading = new Loading(context);
            ObtenerVisitas();
            //Log.d("TOKEN",FirebaseInstanceId.getInstance().getToken());


        } catch (Exception ex) {
            Log.d("Mensaje Verificadores", ex.getMessage());
        }
    }
    public  void ObtenerVisitas()
    {
        try
        {
            loading.ShowLoading("Descargando visitas pendientes");
            RetrofitClient retrofitClient  = new RetrofitClient();
            ServiciosWeb sw =  retrofitClient.getRetrofit().create(ServiciosWeb.class);
            verificador.setId(1);
            sw.obtenerVisitas(verificador).enqueue(new Callback<RespVisitas>() {
                @Override
                public void onResponse(Call<RespVisitas> call, Response<RespVisitas> response) {
                    if(response.code() == 200)
                    {
                        RespVisitas r = response.body();
                        if(r.getEstatus().toString().equals("200"))
                        {
                            mListView = (ListView) findViewById(R.id.lvRutasActuales);
                            AdapterVisitas adapterVisita =  new AdapterVisitas(getApplicationContext(), r.getVisitas());
                            mListView.setAdapter(adapterVisita);
                            InitClickListView();
                        }
                    }else{

                    }
                    loading.CerrarLoading();

                }

                @Override
                public void onFailure(Call<RespVisitas> call, Throwable t) {

                }
            });

        }catch (Exception ex)
        {
            throw  ex;
        }
    }

    public void PermisoUbicacion()
    {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale( this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }else
        {
            //Intent intent = new Intent(this, ServiceUbicacion.class);
            //startService(intent);
        }
    }

    public void InitClickListView()
    {

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intentMapa = new Intent(VisitasActivity.this, MainActivity.class);
                UbicacionActual ubicacionActual = new UbicacionActual();
                ubicacionActual.getLocation(activity);
                Visita visita = (Visita) parent.getItemAtPosition(position);
                //visita.getOrigen().setLatLng(new LatLng(ubicacionActual.getLatitude(),ubicacionActual.getLongitude()));
                intentMapa.putExtra("paramVisita",  new Gson().toJson(visita));
                intentMapa.putExtra("paramVerificador",  new Gson().toJson(verificador));
                startActivity(intentMapa);

            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        //Intent intent = new Intent(this, ServiceUbicacion.class);
                        //startService(intent);
                } else {

                }

            }

        }
    }

}
