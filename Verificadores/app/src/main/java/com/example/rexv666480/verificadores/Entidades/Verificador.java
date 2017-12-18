package com.example.rexv666480.verificadores.Entidades;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rexv666480 on 15/12/2017.
 */

public class Verificador {

    @SerializedName("ubicacion")
    private Ubicacion ubicacion;

    private int id;
    private String token;
    private String nombre;
    private int IdVisita;
    private String contrasena;
    private String usuario;

    public Verificador() {
        this.ubicacion = new Ubicacion();
    }

    public Verificador(int id, String token, String nombre) {
        this.id = id;
        this.token = token;
        this.nombre = nombre;
    }

    public Verificador(Ubicacion ubicacion, int id, String token, String nombre) {
        this.ubicacion = ubicacion;
        this.id = id;
        this.token = token;
        this.nombre = nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getIdVisita() {
        return IdVisita;
    }

    public void setIdVisita(int idVisita) {
        IdVisita = idVisita;
    }


    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
