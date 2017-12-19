package com.example.rexv666480.verificadores.Entidades;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rexv666480 on 15/12/2017.
 */

public class Verificador {

    @SerializedName("Ubicacion")
    private Ubicacion ubicacion;

    private int idVerificador;
    private String token;
    private String nombre;
    private int idRuta;
    private String contrasena;
    private String usuario;

    public Verificador() {
        this.ubicacion = new Ubicacion();
    }

    public Verificador(int id, String token, String nombre) {
        this.idVerificador = id;
        this.token = token;
        this.nombre = nombre;
    }

    public Verificador(Ubicacion ubicacion, int id, String token, String nombre) {
        this.ubicacion = ubicacion;
        this.idVerificador = id;
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

    public int getidRuta() {
        return idRuta;
    }

    public void setidRuta(int idVisita) {
        this.idRuta = idVisita;
    }


    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getId() {
        return idVerificador;
    }

    public void setId(int id) {
        this.idVerificador = id;
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
