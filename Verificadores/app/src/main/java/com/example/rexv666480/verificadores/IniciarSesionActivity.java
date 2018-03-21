package com.example.rexv666480.verificadores;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rexv666480.verificadores.Entidades.Verificador;
import com.example.rexv666480.verificadores.ServiciosWeb.Respuestas.RespSesion;
import com.example.rexv666480.verificadores.ServiciosWeb.RetrofitClient;
import com.example.rexv666480.verificadores.ServiciosWeb.ServiciosWeb;
import com.example.rexv666480.verificadores.Utilerias.Loading;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IniciarSesionActivity extends AppCompatActivity {

    private static final String TAG = "Error inicio sesion";
    Loading loading =null;
    Button btnIniciar= null;
    EditText etUsuario,etContra = null;
    Context context;
    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        btnIniciar = (Button) findViewById(R.id.btnIniciarSesion);
        etContra = (EditText) findViewById(R.id.etContra);
        etUsuario = (EditText) findViewById(R.id.etUsuario);
        this.context = this;
        loading = new Loading(this.context);
        settings = getSharedPreferences("MisPreferencias",context.getApplicationContext().MODE_PRIVATE);
        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitClient retrofitClient  = new RetrofitClient();
                ServiciosWeb sw =  retrofitClient.getRetrofit().create(ServiciosWeb.class);
                Verificador veri = new Verificador();
                veri.setContrasena(etContra.getText().toString());
                veri.setUsuario(etUsuario.getText().toString());
                //String x = settings.getString("paramVerificador","");

                String token = settings.getString("token","");
                veri.setToken(token);
                veri.setToken(FirebaseInstanceId.getInstance().getToken());
                loading.ShowLoading("Cargando...");

                sw.ValidaUsuario(veri).enqueue(new Callback<RespSesion>() {
                    @Override
                    public void onResponse(Call<RespSesion> call, Response<RespSesion> response) {
                        RespSesion respSesion = response.body();
                        loading.CerrarLoading();
                        if(response.code() == 200)
                        {
                            try {
                                    if((respSesion.getEstatus().toString().equals("200"))) {
                                        Verificador verificador = respSesion.getVerificador();
                                        Intent intent = null;
                                        //PARA LAS PRUEBAS DE SIP
                                        if(true){
                                            intent = new Intent(IniciarSesionActivity.this, SipActivity.class);
                                        }
                                        else {
                                            intent = new Intent(IniciarSesionActivity.this, VisitasActivity.class);
                                            intent.putExtra("paramVerificador", new Gson().toJson(verificador));
                                        }
                                        GuardarPreferences(verificador);
                                        startActivity(intent);
                                        finish();
                                    }
                            }catch (Exception ex)
                            {
                                Toast(ex.getMessage());
                                Log.d(TAG,ex.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RespSesion> call, Throwable t) {

                        loading.CerrarLoading();
                        Log.d(TAG,t.getMessage());
                        Toast(t.getMessage());

                    }
                });
            }
        });
    }
    public void Toast(String mensaje)
    {
        try {
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        }catch (Exception ex)
        {
            Log.d(TAG,ex.getMessage());
        }
    }

    public void GuardarPreferences(Verificador verificador)
    {
        try{
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("paramVerificador", new Gson().toJson(verificador));
            editor.commit();
        }catch (Exception ex)
        {
            Toast("Error del sistema GuardarPreferences"+ex.getMessage());
        }
    }

}
