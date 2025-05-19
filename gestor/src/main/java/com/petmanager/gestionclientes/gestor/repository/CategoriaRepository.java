package com.petmanager.gestionclientes.gestor.repository;

import com.petmanager.gestionclientes.gestor.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}
