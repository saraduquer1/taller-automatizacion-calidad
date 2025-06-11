package com.petmanager.gestionclientes.gestor.dto;
import java.util.List;
import java.util.regex.Pattern;

public class ClienteRegistroDTO {

    private Integer usuarioId;
    private String nombre;
    private String apellidos;
    private String correoElectronico;
    private String telefono;
    private String direccion;
    private List<Integer> categorias;

    public ClienteRegistroDTO(){}

    public ClienteRegistroDTO(Integer usuarioId, String nombre, String apellidos, String correoElectronico, String telefono, String direccion, List<Integer> categorias) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correoElectronico = correoElectronico;
        this.telefono = telefono;
        this.direccion = direccion;
        this.categorias = categorias;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()){
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacio");
        }
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
        if (correoElectronico == null || !Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", correoElectronico)) {
            throw new IllegalArgumentException("El email no tiene un formato válido");
        }
        this.correoElectronico = correoElectronico;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Integer> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Integer> categorias) {
        this.categorias = categorias;
    }
}
