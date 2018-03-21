package com.example.rexv666480.verificadores;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Location;
import android.renderscript.Double2;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.rexv666480.verificadores.Entidades.Ubicacion;
import com.example.rexv666480.verificadores.Entidades.Verificador;
import com.example.rexv666480.verificadores.Utilerias.UbicacionActual;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SipActivity extends AppCompatActivity {

    @BindView(R.id.txtID)
    TextView txtID;

    @BindView(R.id.txtNombre)
    TextView txtNombre;

    @BindView(R.id.txtLat)
    TextView txtLat;

    @BindView(R.id.txtLng)
    TextView txtLng;

    @BindView(R.id.txtTelefono)
    TextView txtTelefono;

    @BindView(R.id.txtPuesto)
    TextView txtPuesto;

    @BindView(R.id.btnAceptar)
    TextView btnAceptar;


    private  Verificador verificador;
    private  static String TAG="SIP ACTIVITY";
    private UbicacionActual ubicacionActual;
    private Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sip);
        ButterKnife.bind(this);
        activity= this;
        PintaInformacion();
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public  void PintaInformacion()
    {
        try {
            if(verificador == null) {

                ubicacionActual = new UbicacionActual();
                Location location =  ubicacionActual.getLocation(activity);
                SharedPreferences settings = getApplicationContext().getSharedPreferences("MisPreferencias", getApplicationContext().MODE_PRIVATE);
                String x = settings.getString("paramVerificador", "");
                verificador = new Gson().fromJson(x, Verificador.class);
                txtID.setText(String.valueOf(verificador.getId()));
                txtNombre.setText(verificador.getNombre());
                txtLat.setText(String.valueOf(location.getLatitude()));
                txtLng.setText(String.valueOf(location.getLongitude()));
                txtTelefono.setText(verificador.getTelefono());
                txtPuesto.setText("Operador");
            }
        }catch (Exception ex)

        {
            Log.e(TAG,"error en PintaInformacion"+ex.getMessage());
        }
    }
}
