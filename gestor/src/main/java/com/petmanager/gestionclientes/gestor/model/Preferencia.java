package com.petmanager.gestionclientes.gestor.model;

import jakarta.persistence.*;

@Entity
@Table(name = "preferencia")
public class Preferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cliente_id") // FK en la tabla Preferencia
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "categoria_id") // FK en la tabla Preferencia
    private Categoria categoria;

    public Preferencia() {
    }

    public Preferencia(Integer id, Cliente cliente, Categoria categoria) {
        this.id = id;
        this.cliente = cliente;
        this.categoria = categoria;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
