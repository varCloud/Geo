package com.example.rexv666480.verificadores.Entidades;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by rexv666480 on 13/12/2017.
 */
public class Ubicacion
{
    private LatLng latLng;
    private String Direccion;

    public Ubicacion(LatLng latLng, String direccion) {
        this.latLng = latLng;
        Direccion = direccion;
    }

    public Ubicacion() {
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }
}
