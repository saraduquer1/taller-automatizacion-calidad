package com.petmanager.gestionclientes.gestor.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ClienteRegistroDTOTest {

    private ClienteRegistroDTO dto;

    @BeforeEach
    void setUp() {
        dto = new ClienteRegistroDTO();
    }

    @Test
    void constructor_withParameters_success() {
        // Arrange & Act
        ClienteRegistroDTO dtoConParametros = new ClienteRegistroDTO(
                1,
                "Juan",
                "Pérez",
                "juan.perez@email.com",
                "123456789",
                "Calle 123",
                Arrays.asList(1, 2, 3)
        );

        // Assert
        assertEquals(1, dtoConParametros.getUsuarioId());
        assertEquals("Juan", dtoConParametros.getNombre());
        assertEquals("Pérez", dtoConParametros.getApellidos());
        assertEquals("juan.perez@email.com", dtoConParametros.getCorreoElectronico());
        assertEquals("123456789", dtoConParametros.getTelefono());
        assertEquals("Calle 123", dtoConParametros.getDireccion());
        assertEquals(Arrays.asList(1, 2, 3), dtoConParametros.getCategorias());
    }

    @Test
    void constructor_default_success() {
        // Act
        ClienteRegistroDTO dtoDefault = new ClienteRegistroDTO();

        // Assert
        assertNotNull(dtoDefault);
        assertNull(dtoDefault.getUsuarioId());
        assertNull(dtoDefault.getNombre());
    }

    @Test
    void setNombre_validName_success() {
        // Act
        dto.setNombre("Juan");

        // Assert
        assertEquals("Juan", dto.getNombre());
    }

    @Test
    void setNombre_nullName_throwsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> dto.setNombre(null));
        
        assertEquals("El nombre no puede ser nulo o vacio", exception.getMessage());
    }

    @Test
    void setNombre_emptyName_throwsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> dto.setNombre(""));
        
        assertEquals("El nombre no puede ser nulo o vacio", exception.getMessage());
    }

    @Test
    void setNombre_whitespaceOnlyName_throwsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> dto.setNombre("   "));
        
        assertEquals("El nombre no puede ser nulo o vacio", exception.getMessage());
    }

    @Test
    void setCorreoElectronico_validEmail_success() {
        // Act
        dto.setCorreoElectronico("juan.perez@email.com");

        // Assert
        assertEquals("juan.perez@email.com", dto.getCorreoElectronico());
    }

    @Test
    void setCorreoElectronico_nullEmail_throwsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> dto.setCorreoElectronico(null));
        
        assertEquals("El email no tiene un formato válido", exception.getMessage());
    }

    @Test
    void setCorreoElectronico_invalidEmail_throwsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> dto.setCorreoElectronico("email-invalido"));
        
        assertEquals("El email no tiene un formato válido", exception.getMessage());
    }

    @Test
    void setCorreoElectronico_emailWithoutAt_throwsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> dto.setCorreoElectronico("emailemail.com"));
        
        assertEquals("El email no tiene un formato válido", exception.getMessage());
    }

    @Test
    void setCorreoElectronico_emailWithoutDomain_throwsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> dto.setCorreoElectronico("email@"));
        
        assertEquals("El email no tiene un formato válido", exception.getMessage());
    }

    @Test
    void setCorreoElectronico_complexValidEmail_success() {
        // Act
        dto.setCorreoElectronico("juan.perez+test@universidad.edu.co");

        // Assert
        assertEquals("juan.perez+test@universidad.edu.co", dto.getCorreoElectronico());
    }

    @Test
    void allGettersAndSetters_workCorrectly() {
        // Act
        dto.setUsuarioId(1);
        dto.setNombre("Juan");
        dto.setApellidos("Pérez");
        dto.setCorreoElectronico("juan.perez@email.com");
        dto.setTelefono("123456789");
        dto.setDireccion("Calle 123");
        dto.setCategorias(Arrays.asList(1, 2, 3));

        // Assert
        assertEquals(1, dto.getUsuarioId());
        assertEquals("Juan", dto.getNombre());
        assertEquals("Pérez", dto.getApellidos());
        assertEquals("juan.perez@email.com", dto.getCorreoElectronico());
        assertEquals("123456789", dto.getTelefono());
        assertEquals("Calle 123", dto.getDireccion());
        assertEquals(Arrays.asList(1, 2, 3), dto.getCategorias());
    }

    @Test
    void setCategorias_emptyList_success() {
        // Act
        dto.setCategorias(Collections.emptyList());

        // Assert
        assertTrue(dto.getCategorias().isEmpty());
    }

    @Test
    void setCategorias_nullList_success() {
        // Act
        dto.setCategorias(null);

        // Assert
        assertNull(dto.getCategorias());
    }
}