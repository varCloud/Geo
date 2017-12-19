package com.example.rexv666480.verificadores.Entidades;

import android.support.v7.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rexv666480 on 13/12/2017.
 */

public class Ruta   {

    private int idRuta;
    private Ubicacion origen;
    private Ubicacion destino;
    private String descripcionRuta;

    public int getId() {
        return idRuta;
    }

    public void setId(int id) {
        this.idRuta = id;
    }

    public Ubicacion getOrigen() {
        return origen;
    }

    public void setOrigen(Ubicacion origen) {
        this.origen = origen;
    }

    public Ubicacion getDestino() {
        return destino;
    }

    public void setDestino(Ubicacion destino) {
        this.destino = destino;
    }

    public String getDescripcionRuta() {
        return descripcionRuta;
    }

    public void setDescripcionRuta(String descripcionRuta) {
        this.descripcionRuta = descripcionRuta;
    }

    public Ruta(int id, Ubicacion origen, Ubicacion destino, String descripcionRuta) {
        this.idRuta = id;
        this.origen = origen;
        this.destino = destino;
        this.descripcionRuta = descripcionRuta;
    }

    public Ruta() {
    }

    public List<Ruta> rutas;

    // This method creates an ArrayList that has three Person objects
// Checkout the project associated with this tutorial on Github if
// you want to use the same images.
    public void initializeData(){
        rutas = new ArrayList<>();
        rutas.add(new Ruta(1,new Ubicacion(new LatLng(19.088594,-102.356685),"Apatzingán"),new Ubicacion( new LatLng(19.208278,-101.707215),"Arios de Rosales"),"Apatzingán - Arios de Rosales"));
        rutas.add(new Ruta(2,new Ubicacion( new LatLng(0,0),""),new Ubicacion( new LatLng(0,0),""),"Visita Estadio"));
        rutas.add(new Ruta(3,new Ubicacion( new LatLng(0,0),""),new Ubicacion( new LatLng(0,0),""),"Visita Bancomer"));
    }
}

