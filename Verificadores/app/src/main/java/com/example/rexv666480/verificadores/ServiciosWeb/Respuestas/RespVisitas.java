package com.example.rexv666480.verificadores.ServiciosWeb.Respuestas;

import com.example.rexv666480.verificadores.Entidades.Visita;

import java.util.List;

/**
 * Created by rexv666480 on 05/01/2018.
 */

public class RespVisitas {

    private String estatus;
    private String mensaje;
    private List<Visita> visitas;

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

    public List<Visita> getVisitas() {
        return visitas;
    }

    public void setVisitas(List<Visita> visitas) {
        this.visitas = visitas;
    }
}
