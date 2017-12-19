package com.example.rexv666480.verificadores.ServiciosWeb.Respuestas;

/**
 * Created by rexv666480 on 19/12/2017.
 */

public class RespEstatus {

    private String estatus;
    private String mensaje;

    public RespEstatus(String estatus, String mensaje) {

        this.estatus = estatus;
        this.mensaje = mensaje;
    }

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
}
