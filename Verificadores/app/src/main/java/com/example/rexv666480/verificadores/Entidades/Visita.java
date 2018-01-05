package com.example.rexv666480.verificadores.Entidades;

/**
 * Created by rexv666480 on 05/01/2018.
 */

public class Visita {

    private int idVisita;
    private String descripcion;
    private String latitudOrigen;
    private String longitudOrigen;
    private String descripcionOrigen;
    private String descripcionDestino;
    private String latitudDestino;
    private String longituDestino;
    private String idEstatusVisita;
    private String km;
    private String timepoEstimado;

    public int getIdVisita() {
        return idVisita;
    }

    public void setIdVisita(int idVisita) {
        this.idVisita = idVisita;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLatitudOrigen() {
        return latitudOrigen;
    }

    public void setLatitudOrigen(String latitudOrigen) {
        this.latitudOrigen = latitudOrigen;
    }

    public String getLongitudOrigen() {
        return longitudOrigen;
    }

    public void setLongitudOrigen(String longitudOrigen) {
        this.longitudOrigen = longitudOrigen;
    }

    public String getDescripcionOrigen() {
        return descripcionOrigen;
    }

    public void setDescripcionOrigen(String descripcionOrigen) {
        this.descripcionOrigen = descripcionOrigen;
    }

    public String getDescripcionDestino() {
        return descripcionDestino;
    }

    public void setDescripcionDestino(String descripcionDestino) {
        this.descripcionDestino = descripcionDestino;
    }

    public String getLatitudDestino() {
        return latitudDestino;
    }

    public void setLatitudDestino(String latitudDestino) {
        this.latitudDestino = latitudDestino;
    }

    public String getLongituDestino() {
        return longituDestino;
    }

    public void setLongituDestino(String longituDestino) {
        this.longituDestino = longituDestino;
    }

    public String getIdEstatusVisita() {
        return idEstatusVisita;
    }

    public void setIdEstatusVisita(String idEstatusVisita) {
        this.idEstatusVisita = idEstatusVisita;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getTimepoEstimado() {
        return timepoEstimado;
    }

    public void setTimepoEstimado(String timepoEstimado) {
        this.timepoEstimado = timepoEstimado;
    }
}
