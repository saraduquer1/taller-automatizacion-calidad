package com.petmanager.gestionclientes.gestor.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    private Cliente cliente;
    private UUID clienteId;

    @BeforeEach
    void setUp() {
        clienteId = UUID.randomUUID();
        cliente = new Cliente();
    }

    @Test
    void constructor_withParameters_success() {
        // Arrange & Act
        Cliente clienteConParametros = new Cliente(
                clienteId,
                "Juan",
                "Pérez",
                "juan.perez@email.com",
                "123456789",
                "Calle 123"
        );

        // Assert
        assertEquals(clienteId, clienteConParametros.getId());
        assertEquals("Juan", clienteConParametros.getNombre());
        assertEquals("Pérez", clienteConParametros.getApellido());
        assertEquals("juan.perez@email.com", clienteConParametros.getCorreoElectronico());
        assertEquals("123456789", clienteConParametros.getTelefono());
        assertEquals("Calle 123", clienteConParametros.getDireccion());
        assertNotNull(clienteConParametros.getFechaCreacion());
    }

    @Test
    void constructor_default_success() {
        // Act
        Cliente clienteDefault = new Cliente();

        // Assert
        assertNotNull(clienteDefault.getFechaCreacion());
        assertTrue(clienteDefault.getFechaCreacion().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void setNombre_validName_success() {
        // Act
        cliente.setNombre("Juan");

        // Assert
        assertEquals("Juan", cliente.getNombre());
    }

    @Test
    void setNombre_nullName_throwsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> cliente.setNombre(null));
        
        assertEquals("El nombre no puede ser nulo o vacio", exception.getMessage());
    }

    @Test
    void setNombre_emptyName_throwsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> cliente.setNombre(""));
        
        assertEquals("El nombre no puede ser nulo o vacio", exception.getMessage());
    }

    @Test
    void setNombre_whitespaceOnlyName_throwsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> cliente.setNombre("   "));
        
        assertEquals("El nombre no puede ser nulo o vacio", exception.getMessage());
    }

    @Test
    void setCorreoElectronico_validEmail_success() {
        // Act
        cliente.setCorreoElectronico("juan.perez@email.com");

        // Assert
        assertEquals("juan.perez@email.com", cliente.getCorreoElectronico());
    }

    @Test
    void setCorreoElectronico_nullEmail_throwsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> cliente.setCorreoElectronico(null));
        
        assertEquals("El email no tiene un formato válido", exception.getMessage());
    }

    @Test
    void setCorreoElectronico_invalidEmail_throwsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> cliente.setCorreoElectronico("email-invalido"));
        
        assertEquals("El email no tiene un formato válido", exception.getMessage());
    }

    @Test
    void setCorreoElectronico_emailWithoutAt_throwsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> cliente.setCorreoElectronico("emailemail.com"));
        
        assertEquals("El email no tiene un formato válido", exception.getMessage());
    }

    @Test
    void setCorreoElectronico_emailWithoutDomain_throwsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> cliente.setCorreoElectronico("email@"));
        
        assertEquals("El email no tiene un formato válido", exception.getMessage());
    }

    @Test
    void setCorreoElectronico_complexValidEmail_success() {
        // Act
        cliente.setCorreoElectronico("juan.perez+test@universidad.edu.co");

        // Assert
        assertEquals("juan.perez+test@universidad.edu.co", cliente.getCorreoElectronico());
    }

    @Test
    void equals_sameId_returnsTrue() {
        // Arrange
        Cliente cliente1 = new Cliente();
        cliente1.setId(clienteId);
        
        Cliente cliente2 = new Cliente();
        cliente2.setId(clienteId);

        // Act & Assert
        assertEquals(cliente1, cliente2);
    }

    @Test
    void equals_differentId_returnsFalse() {
        // Arrange
        Cliente cliente1 = new Cliente();
        cliente1.setId(clienteId);
        
        Cliente cliente2 = new Cliente();
        cliente2.setId(UUID.randomUUID());

        // Act & Assert
        assertNotEquals(cliente1, cliente2);
    }

    @Test
    void equals_nullObject_returnsFalse() {
        // Arrange
        Cliente cliente1 = new Cliente();
        cliente1.setId(clienteId);

        // Act & Assert
        assertNotEquals(cliente1, null);
    }

    @Test
    void equals_differentClass_returnsFalse() {
        // Arrange
        Cliente cliente1 = new Cliente();
        cliente1.setId(clienteId);
        String otherObject = "not a cliente";

        // Act & Assert
        assertNotEquals(cliente1, otherObject);
    }

    @Test
    void hashCode_sameId_sameHashCode() {
        // Arrange
        Cliente cliente1 = new Cliente();
        cliente1.setId(clienteId);
        
        Cliente cliente2 = new Cliente();
        cliente2.setId(clienteId);

        // Act & Assert
        assertEquals(cliente1.hashCode(), cliente2.hashCode());
    }

    @Test
    void toString_containsAllFields() {
        // Arrange
        cliente.setId(clienteId);
        cliente.setNombre("Juan");
        cliente.setApellido("Pérez");
        cliente.setCorreoElectronico("juan.perez@email.com");
        cliente.setTelefono("123456789");
        cliente.setDireccion("Calle 123");

        // Act
        String toString = cliente.toString();

        // Assert
        assertTrue(toString.contains(clienteId.toString()));
        assertTrue(toString.contains("Juan"));
        assertTrue(toString.contains("Pérez"));
        assertTrue(toString.contains("juan.perez@email.com"));
        assertTrue(toString.contains("123456789"));
        assertTrue(toString.contains("Calle 123"));
    }

    @Test
    void allGettersAndSetters_workCorrectly() {
        // Arrange
        LocalDateTime fechaCreacion = LocalDateTime.now();

        // Act
        cliente.setId(clienteId);
        cliente.setNombre("Juan");
        cliente.setApellido("Pérez");
        cliente.setCorreoElectronico("juan.perez@email.com");
        cliente.setTelefono("123456789");
        cliente.setDireccion("Calle 123");
        cliente.setFechaCreacion(fechaCreacion);

        // Assert
        assertEquals(clienteId, cliente.getId());
        assertEquals("Juan", cliente.getNombre());
        assertEquals("Pérez", cliente.getApellido());
        assertEquals("juan.perez@email.com", cliente.getCorreoElectronico());
        assertEquals("123456789", cliente.getTelefono());
        assertEquals("Calle 123", cliente.getDireccion());
        assertEquals(fechaCreacion, cliente.getFechaCreacion());
        assertNotNull(cliente.getPrefencias());
    }
}