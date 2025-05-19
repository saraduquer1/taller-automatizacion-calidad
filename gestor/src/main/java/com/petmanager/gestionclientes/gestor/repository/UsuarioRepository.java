package com.petmanager.gestionclientes.gestor.repository;

import com.petmanager.gestionclientes.gestor.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
}
