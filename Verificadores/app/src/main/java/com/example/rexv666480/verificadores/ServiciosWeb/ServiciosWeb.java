package com.example.rexv666480.verificadores.ServiciosWeb;

import com.example.rexv666480.verificadores.Entidades.Respuesta;
import com.example.rexv666480.verificadores.Entidades.Verificador;
import com.example.rexv666480.verificadores.Entidades.Visita;
import com.example.rexv666480.verificadores.ServiciosWeb.Respuestas.RespEstatus;
import com.example.rexv666480.verificadores.ServiciosWeb.Respuestas.RespVisitas;
import com.google.android.gms.safetynet.SafetyNetApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by rexv666480 on 13/12/2017.
 */

public interface ServiciosWeb {
    //@FormUrlEncoded
    @POST("InsertarRuta")
    Call<RespEstatus> enviarUbicacionActual(@Body Verificador verfficador);

    @POST("ObtenerVisitas")
    Call<RespVisitas> obtenerVisitas(@Body Verificador verificador);

    @POST("ValidaUsuario")
    Call<Verificador> ValidaUsuario(@Body Verificador verfficador);
}
