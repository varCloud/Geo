package com.example.rexv666480.verificadores.Entidades;

/**
 * Created by rexv666480 on 15/12/2017.
 */

public class Respuesta <T>{

    private String mensajeRespuesta;
    private String estatusRespuesta;
    private  T Data;

    public Respuesta() {

    }

    public Respuesta(String mensajeRespuesta, String estatusRespuesta, T data) {

        this.mensajeRespuesta = mensajeRespuesta;
        this.estatusRespuesta = estatusRespuesta;
        Data = data;
    }

    public String getMensajeRespuesta() {
        return mensajeRespuesta;
    }

    public void setMensajeRespuesta(String mensajeRespuesta) {
        this.mensajeRespuesta = mensajeRespuesta;
    }

    public String getEstatusRespuesta() {
        return estatusRespuesta;
    }

    public void setEstatusRespuesta(String estatusRespuesta) {
        this.estatusRespuesta = estatusRespuesta;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }
}
