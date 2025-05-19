package com.petmanager.gestionclientes.gestor.DTO;

import java.util.UUID;

public class ClienteVistaDto {

    private UUID id;
    private String nombre;
    private String apellido;

    public ClienteVistaDto() {
    }

    public ClienteVistaDto(UUID id, String nombre, String apellido) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}
