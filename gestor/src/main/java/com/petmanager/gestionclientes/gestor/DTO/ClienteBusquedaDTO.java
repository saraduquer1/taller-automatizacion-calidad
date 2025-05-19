package com.petmanager.gestionclientes.gestor.DTO;

import java.util.UUID;

public class ClienteBusquedaDTO {

    private UUID id;
    private String nombre;
    private String apellidos;
    private String correoElectronico;

    public ClienteBusquedaDTO() {}

    public ClienteBusquedaDTO(UUID id, String nombre, String apellidos, String correoElectronico) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correoElectronico = correoElectronico;
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

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }
}
