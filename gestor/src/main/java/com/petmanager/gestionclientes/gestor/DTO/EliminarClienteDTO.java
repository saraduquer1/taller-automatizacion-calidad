package com.petmanager.gestionclientes.gestor.DTO;

import java.util.UUID;

public class EliminarClienteDTO {

    private UUID idCliente;
    private Integer idUsuario; // quien hace la eliminación
    private String confirmacion; // debe ser "BORRAR"

    public EliminarClienteDTO() {
    }

    public EliminarClienteDTO(UUID idCliente, Integer idUsuario) {
        this.idCliente = idCliente;
        this.idUsuario = idUsuario;
        this.confirmacion = "BORRAR";
    }

    public UUID getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(UUID idCliente) {
        this.idCliente = idCliente;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getConfirmacion() {
        return confirmacion;
    }

    public void setConfirmacion(String confirmacion) {
        this.confirmacion = confirmacion;
    }
}
