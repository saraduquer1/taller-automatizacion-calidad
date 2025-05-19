package com.petmanager.gestionclientes.gestor.model;

import jakarta.persistence.*;

import java.util.regex.Pattern;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    @Column(name = "apellidos")
    private String apellido;

    private String correo_electronico;

    @Column(name = "password")
    private String contrasena;

    private String rol;

    public Usuario(){}
    public Usuario(Integer id, String nombre, String apellido, String correo_electronico, String contrasena, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo_electronico = correo_electronico;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()){
            throw new IllegalArgumentException("El nombre no puede ser vacio o nulo");
        }
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo_electronico() {
        return correo_electronico;
    }

    public void setCorreo_electronico(String correo_electronico) {
        if (correo_electronico == null || !Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", correo_electronico)) {
            throw new IllegalArgumentException("El email no tiene un formato válido");
        }
        this.correo_electronico = correo_electronico;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        if (contrasena == null || contrasena.length()<8){
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        }
            this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
