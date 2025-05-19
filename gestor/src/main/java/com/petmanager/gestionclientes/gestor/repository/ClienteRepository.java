package com.petmanager.gestionclientes.gestor.repository;

import com.petmanager.gestionclientes.gestor.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    Optional<Cliente> findByCorreoElectronico(String correoElectronico);
}
