package com.petmanager.gestionclientes.gestor.repository;

import com.petmanager.gestionclientes.gestor.model.Preferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenciaRepository extends JpaRepository<Preferencia, Integer> {
}
