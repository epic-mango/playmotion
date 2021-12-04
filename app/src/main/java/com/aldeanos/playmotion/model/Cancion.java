package com.aldeanos.playmotion.model;

public class Cancion {
    String nombre, artista, album;
    int id;

    public Cancion(String nombre, String artista, String album, int id) {
        this.nombre = nombre;
        this.artista = artista;
        this.album = album;
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getArtista() {
        return artista;
    }

    public String getAlbum() {
        return album;
    }

    public int getId() {
        return id;
    }
}
