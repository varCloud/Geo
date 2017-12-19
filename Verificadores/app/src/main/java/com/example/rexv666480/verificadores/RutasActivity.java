package com.example.rexv666480.verificadores;

import android.app.Activity;
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
import com.example.rexv666480.verificadores.Entidades.Ruta;
import com.example.rexv666480.verificadores.Entidades.Ubicacion;
import com.example.rexv666480.verificadores.Entidades.Verificador;
import com.example.rexv666480.verificadores.Servicios.ServiceUbicacion;

import com.example.rexv666480.verificadores.Utilerias.UbicacionActual;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

public class RutasActivity extends AppCompatActivity {
    private static final int ACCESS_FINE_LOCATION = 102;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ListView mListView;
    private Verificador verificador;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_rutas);
            activity = this;
            mRecyclerView = (RecyclerView) findViewById(R.id.ListRutasActuales);
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);
            //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            // specify an adapter (see also next example)
            Ruta r = new Ruta();
            r.initializeData();

            Intent i = getIntent();
            verificador = new Gson().fromJson(i.getStringExtra("paramVerificador"),Verificador.class);
            mAdapter = new AdapterRutas(r.rutas);
            mRecyclerView.setAdapter(mAdapter);
            mListView = (ListView) findViewById(R.id.lvRutasActuales);
            AdapterRuta adapterRuta = new AdapterRuta(this,r.rutas);
            mListView.setAdapter(adapterRuta);
            InitClickListView();
            PermisoUbicacion();
            Log.d("TOKEN",FirebaseInstanceId.getInstance().getToken());


        } catch (Exception ex) {
            Log.d("Mensaje Verificadores", ex.getMessage());
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

                Intent intentMapa = new Intent(RutasActivity.this, MainActivity.class);
                UbicacionActual ubicacionActual = new UbicacionActual();
                ubicacionActual.getLocation(activity);
                Ruta ruta = (Ruta) parent.getItemAtPosition(position);
                ruta.getOrigen().setLatLng(new LatLng(ubicacionActual.getLatitude(),ubicacionActual.getLongitude()));
                intentMapa.putExtra("paramRuta",  new Gson().toJson(ruta));
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
