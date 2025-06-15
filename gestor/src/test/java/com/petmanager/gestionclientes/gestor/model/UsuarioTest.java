package com.petmanager.gestionclientes.gestor.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
    }

    @Test
    void constructor_withParameters_success() {
        // Arrange & Act
        Usuario usuarioConParametros = new Usuario(
                1,
                "Juan",
                "Pérez",
                "juan.perez@email.com",
                "password123",
                "ADMIN"
        );

        // Assert
        assertEquals(1, usuarioConParametros.getId());
        assertEquals("Juan", usuarioConParametros.getNombre());
        assertEquals("Pérez", usuarioConParametros.getApellido());
        assertEquals("juan.perez@email.com", usuarioConParametros.getCorreoElectronico());
        assertEquals("password123", usuarioConParametros.getContrasena());
        assertEquals("ADMIN", usuarioConParametros.getRol());
    }

    @Test
    void constructor_default_success() {
        // Act
        Usuario usuarioDefault = new Usuario();

        // Assert
        assertNotNull(usuarioDefault);
        assertNull(usuarioDefault.getId());
        assertNull(usuarioDefault.getNombre());
    }

    @Test
    void setNombre_validName_success() {
        // Act
        usuario.setNombre("Juan");

        // Assert
        assertEquals("Juan", usuario.getNombre());
    }

    @Test
    void setNombre_nullName_throwsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> usuario.setNombre(null));
        
        assertEquals("El nombre no puede ser vacio o nulo", exception.getMessage());
    }

    @Test
    void setNombre_emptyName_throwsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> usuario.setNombre(""));
        
        assertEquals("El nombre no puede ser vacio o nulo", exception.getMessage());
    }

    @Test
    void setNombre_whitespaceOnlyName_throwsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> usuario.setNombre("   "));
        
        assertEquals("El nombre no puede ser vacio o nulo", exception.getMessage());
    }

    @Test
    void setCorreoElectronico_validEmail_success() {
        // Act
        usuario.setCorreoElectronico("juan.perez@email.com");

        // Assert
        assertEquals("juan.perez@email.com", usuario.getCorreoElectronico());
    }

    @Test
    void setCorreoElectronico_nullEmail_throwsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> usuario.setCorreoElectronico(null));
        
        assertEquals("El email no tiene un formato válido", exception.getMessage());
    }

    @Test
    void setCorreoElectronico_invalidEmail_throwsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> usuario.setCorreoElectronico("email-invalido"));
        
        assertEquals("El email no tiene un formato válido", exception.getMessage());
    }

    @Test
    void setCorreoElectronico_complexValidEmail_success() {
        // Act
        usuario.setCorreoElectronico("juan.perez+test@universidad.edu.co");

        // Assert
        assertEquals("juan.perez+test@universidad.edu.co", usuario.getCorreoElectronico());
    }

    @Test
    void setContrasena_validPassword_success() {
        // Act
        usuario.setContrasena("password123");

        // Assert
        assertEquals("password123", usuario.getContrasena());
    }

    @Test
    void setContrasena_nullPassword_throwsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> usuario.setContrasena(null));
        
        assertEquals("La contraseña debe tener al menos 8 caracteres", exception.getMessage());
    }

    @Test
    void setContrasena_shortPassword_throwsException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> usuario.setContrasena("1234567"));
        
        assertEquals("La contraseña debe tener al menos 8 caracteres", exception.getMessage());
    }

    @Test
    void setContrasena_exactlyEightCharacters_success() {
        // Act
        usuario.setContrasena("12345678");

        // Assert
        assertEquals("12345678", usuario.getContrasena());
    }

    @Test
    void setContrasena_longPassword_success() {
        // Act
        usuario.setContrasena("thisIsAVeryLongPasswordWithMoreThanEightCharacters");

        // Assert
        assertEquals("thisIsAVeryLongPasswordWithMoreThanEightCharacters", usuario.getContrasena());
    }

    @Test
    void allGettersAndSetters_workCorrectly() {
        // Act
        usuario.setId(1);
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");
        usuario.setCorreoElectronico("juan.perez@email.com");
        usuario.setContrasena("password123");
        usuario.setRol("ADMIN");

        // Assert
        assertEquals(1, usuario.getId());
        assertEquals("Juan", usuario.getNombre());
        assertEquals("Pérez", usuario.getApellido());
        assertEquals("juan.perez@email.com", usuario.getCorreoElectronico());
        assertEquals("password123", usuario.getContrasena());
        assertEquals("ADMIN", usuario.getRol());
    }
}