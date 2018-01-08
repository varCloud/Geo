package com.example.rexv666480.verificadores.ServiciosWeb.Respuestas;

import com.example.rexv666480.verificadores.Entidades.Verificador;

/**
 * Created by rexv666480 on 08/01/2018.
 */

public class RespSesion {

    private String estatus;
    private String mensaje;
    private Verificador verificador;

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Verificador getVerificador() {
        return verificador;
    }

    public void setVerificador(Verificador verificador) {
        this.verificador = verificador;
    }
}
