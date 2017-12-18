package com.example.rexv666480.verificadores;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.rexv666480.verificadores.Entidades.Respuesta;
import com.example.rexv666480.verificadores.Entidades.Ruta;
import com.example.rexv666480.verificadores.Entidades.Verificador;
import com.example.rexv666480.verificadores.ServiciosWeb.RetrofitClient;
import com.example.rexv666480.verificadores.ServiciosWeb.ServiciosWeb;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IniciarSesionActivity extends AppCompatActivity {

    private static final String TAG = "Error inicio sesion";

    Button btnIniciar= null;
    EditText etUsuario,etContra = null;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        btnIniciar = (Button) findViewById(R.id.btnIniciarSesion);
        etContra = (EditText) findViewById(R.id.etContra);
        etUsuario = (EditText) findViewById(R.id.etUsuario);
        this.context = this;
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitClient retrofitClient  = new RetrofitClient();
                ServiciosWeb sw =  retrofitClient.getRetrofit().create(ServiciosWeb.class);
                Verificador veri = new Verificador();
                veri.setContrasena(etContra.getText().toString());
                veri.setNombre(etUsuario.getText().toString());
                SharedPreferences settings = getSharedPreferences("MisPreferencias",context.getApplicationContext().MODE_PRIVATE);
                String token = settings.getString("token","");
                veri.setToken(token);

                sw.ValidaUsuario(veri).enqueue(new Callback<Verificador>() {
                    @Override
                    public void onResponse(Call<Verificador> call, Response<Verificador> response) {
                         Verificador respuesta = response.body();
                        if(response.code() == 200)
                        {
                            try {
                                Intent intentRutas = new Intent(IniciarSesionActivity.this, RutasActivity.class);
                                intentRutas.putExtra("paramVerificador", new Gson().toJson(respuesta));
                                startActivity(intentRutas);
                            }catch (Exception ex)
                            {
                                Log.d(TAG,ex.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Verificador> call, Throwable t) {
                        Log.d(TAG,t.getMessage());

                    }
                });
            }
        });
    }
}
