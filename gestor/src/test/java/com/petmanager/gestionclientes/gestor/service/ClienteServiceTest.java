package com.petmanager.gestionclientes.gestor.service;

import com.petmanager.gestionclientes.gestor.dto.*;
import com.petmanager.gestionclientes.gestor.model.Cliente;
import com.petmanager.gestionclientes.gestor.repository.ClienteRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @Mock
    ClienteRepository clienteRepository;

    @Mock
    EntityManager entityManager;

    @InjectMocks
    ClienteService clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(clienteService, "entityManager", entityManager);
    }

    @Test
    void getAllClientes_success() {
        // Arrange
        Cliente cliente1 = new Cliente();
        cliente1.setId(UUID.randomUUID());
        cliente1.setNombre("Camilo");
        cliente1.setApellido("Duque");

        Cliente cliente2 = new Cliente();
        cliente2.setId(UUID.randomUUID());
        cliente2.setNombre("Carlos");
        cliente2.setApellido("Echeverri");

        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente1, cliente2));

        // Act
        List<ClienteVistaDto> resultado = clienteService.getAllClientes();

        // Assert
        assertEquals(2, resultado.size());
        assertEquals("Camilo", resultado.get(0).getNombre());
        assertEquals("Duque", resultado.get(0).getApellido());
        assertEquals("Carlos", resultado.get(1).getNombre());
        assertEquals("Echeverri", resultado.get(1).getApellido());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void getAllClientes_emptyList() {
        // Arrange
        when(clienteRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<ClienteVistaDto> resultado = clienteService.getAllClientes();

        // Assert
        assertTrue(resultado.isEmpty());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void getUsuario_success() {
        // Arrange
        UUID id = UUID.randomUUID();
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNombre("Camilo");
        cliente.setApellido("Duque");
        cliente.setCorreoElectronico("camilo.duque@udea.edu.co");
        cliente.setTelefono("123456789");
        cliente.setDireccion("Calle 123");

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));

        // Act
        ClienteDetalleDTO dto = clienteService.getUsuario(id);

        // Assert
        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals("Camilo", dto.getNombre());
        assertEquals("Duque", dto.getApellido());
        assertEquals("camilo.duque@udea.edu.co", dto.getCorreoElectronico());
        assertEquals("123456789", dto.getTelefono());
        assertEquals("Calle 123", dto.getDireccion());
        verify(clienteRepository, times(1)).findById(id);
    }

    @Test
    void getUsuario_notFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> clienteService.getUsuario(id));
        verify(clienteRepository, times(1)).findById(id);
    }

    @Test
    void buscarClientes_success() {
        // Arrange
        UUID id = UUID.randomUUID();
        ClienteBusquedaDTO busquedaDTO = new ClienteBusquedaDTO();
        busquedaDTO.setId(id);
        busquedaDTO.setNombre("Camilo");
        busquedaDTO.setApellidos("Duque");
        busquedaDTO.setCorreoElectronico("camilo.duque@udea.edu.co");

        Object[] resultado = new Object[] {
                id,
                "Camilo",
                "Duque",
                "camilo.duque@udea.edu.co",
                "123456789",
                "Calle 123"
        };
        List<Object[]> resultados = Collections.singletonList(resultado);

        Query query = mock(Query.class, RETURNS_SELF);
        when(entityManager.createNativeQuery("SELECT * FROM buscar_clientes(:id, :nombre, :apellidos, :correo)"))
                .thenReturn(query);
        when(query.getResultList()).thenReturn(resultados);

        // Act
        List<ClienteDetalleDTO> clientes = clienteService.buscarClientes(busquedaDTO);

        // Assert
        assertNotNull(clientes);
        assertEquals(1, clientes.size());
        ClienteDetalleDTO dto = clientes.get(0);
        assertEquals(id, dto.getId());
        assertEquals("Camilo", dto.getNombre());
        assertEquals("Duque", dto.getApellido());
        assertEquals("camilo.duque@udea.edu.co", dto.getCorreoElectronico());
        assertEquals("123456789", dto.getTelefono());
        assertEquals("Calle 123", dto.getDireccion());
        
        verify(query, times(1)).setParameter("id", id);
        verify(query, times(1)).setParameter("nombre", "Camilo");
        verify(query, times(1)).setParameter("apellidos", "Duque");
        verify(query, times(1)).setParameter("correo", "camilo.duque@udea.edu.co");
    }

    @Test
    void buscarClientes_noResults() {
        // Arrange
        ClienteBusquedaDTO busquedaDTO = new ClienteBusquedaDTO();
        busquedaDTO.setNombre("NoExiste");

        Query query = mock(Query.class, RETURNS_SELF);
        when(entityManager.createNativeQuery("SELECT * FROM buscar_clientes(:id, :nombre, :apellidos, :correo)"))
                .thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> clienteService.buscarClientes(busquedaDTO));
    }

    @Test
    void buscarClientes_withNullParameters() {
        // Arrange
        ClienteBusquedaDTO busquedaDTO = new ClienteBusquedaDTO();
        // Todos los campos null

        Object[] resultado = new Object[] {
                UUID.randomUUID(),
                "Juan",
                "Pérez",
                "juan.perez@email.com",
                "987654321",
                "Calle 456"
        };
        List<Object[]> resultados = Collections.singletonList(resultado);

        Query query = mock(Query.class, RETURNS_SELF);
        when(entityManager.createNativeQuery("SELECT * FROM buscar_clientes(:id, :nombre, :apellidos, :correo)"))
                .thenReturn(query);
        when(query.getResultList()).thenReturn(resultados);

        // Act
        List<ClienteDetalleDTO> clientes = clienteService.buscarClientes(busquedaDTO);

        // Assert
        assertNotNull(clientes);
        assertEquals(1, clientes.size());
        verify(query, times(1)).setParameter("id", null);
        verify(query, times(1)).setParameter("nombre", null);
        verify(query, times(1)).setParameter("apellidos", null);
        verify(query, times(1)).setParameter("correo", null);
    }

    @Test
    void crearClienteConPreferencias_success() {
        // Arrange
        ClienteRegistroDTO dto = new ClienteRegistroDTO();
        dto.setNombre("Camilo");
        dto.setApellidos("Duque");
        dto.setCorreoElectronico("camilo.duque@udea.edu.co");
        dto.setTelefono("123456789");
        dto.setDireccion("Calle 123");
        dto.setCategorias(Arrays.asList(1, 2, 3));
        Integer usuarioId = 1;

        when(clienteRepository.findByCorreoElectronico("camilo.duque@udea.edu.co"))
                .thenReturn(Optional.empty());

        Query query = mock(Query.class, RETURNS_SELF);
        when(entityManager.createNativeQuery("SELECT crear_cliente_con_preferencias_2(:nombre, :apellidos, :correo, :telefono, :direccion, :categorias, :usuarioId)"))
                .thenReturn(query);
        when(query.getSingleResult()).thenReturn(null);

        // Act
        clienteService.CrearClienteConPreferencias(dto, usuarioId);

        // Assert
        verify(clienteRepository, times(1)).findByCorreoElectronico("camilo.duque@udea.edu.co");
        verify(query, times(1)).setParameter("nombre", "Camilo");
        verify(query, times(1)).setParameter("apellidos", "Duque");
        verify(query, times(1)).setParameter("correo", "camilo.duque@udea.edu.co");
        verify(query, times(1)).setParameter("telefono", "123456789");
        verify(query, times(1)).setParameter("direccion", "Calle 123");
        verify(query, times(1)).setParameter("categorias", "1,2,3");
        verify(query, times(1)).setParameter("usuarioId", usuarioId);
    }

    @Test
    void crearClienteConPreferencias_emptyCategories_success() {
        // Arrange
        ClienteRegistroDTO dto = new ClienteRegistroDTO();
        dto.setNombre("Camilo");
        dto.setApellidos("Duque");
        dto.setCorreoElectronico("camilo.duque@udea.edu.co");
        dto.setTelefono("123456789");
        dto.setDireccion("Calle 123");
        dto.setCategorias(Collections.emptyList());
        Integer usuarioId = 1;

        when(clienteRepository.findByCorreoElectronico("camilo.duque@udea.edu.co"))
                .thenReturn(Optional.empty());

        Query query = mock(Query.class, RETURNS_SELF);
        when(entityManager.createNativeQuery("SELECT crear_cliente_con_preferencias_2(:nombre, :apellidos, :correo, :telefono, :direccion, :categorias, :usuarioId)"))
                .thenReturn(query);
        when(query.getSingleResult()).thenReturn(null);

        // Act
        clienteService.CrearClienteConPreferencias(dto, usuarioId);

        // Assert
        verify(query, times(1)).setParameter("categorias", "");
    }

    @Test
    void crearClienteConPreferencias_nullCategories_success() {
        // Arrange
        ClienteRegistroDTO dto = new ClienteRegistroDTO();
        dto.setNombre("Camilo");
        dto.setApellidos("Duque");
        dto.setCorreoElectronico("camilo.duque@udea.edu.co");
        dto.setTelefono("123456789");
        dto.setDireccion("Calle 123");
        dto.setCategorias(null);
        Integer usuarioId = 1;

        when(clienteRepository.findByCorreoElectronico("camilo.duque@udea.edu.co"))
                .thenReturn(Optional.empty());

        Query query = mock(Query.class, RETURNS_SELF);
        when(entityManager.createNativeQuery("SELECT crear_cliente_con_preferencias_2(:nombre, :apellidos, :correo, :telefono, :direccion, :categorias, :usuarioId)"))
                .thenReturn(query);
        when(query.getSingleResult()).thenReturn(null);

        // Act
        clienteService.CrearClienteConPreferencias(dto, usuarioId);

        // Assert
        verify(query, times(1)).setParameter("categorias", "");
    }

    @Test
    void crearClienteConPreferencias_emailAlreadyExists() {
        // Arrange
        ClienteRegistroDTO dto = new ClienteRegistroDTO();
        dto.setNombre("Camilo");
        dto.setApellidos("Duque");
        dto.setCorreoElectronico("camilo.duque@udea.edu.co");
        dto.setTelefono("123456789");
        dto.setDireccion("Calle 123");
        dto.setCategorias(Arrays.asList(1, 2));
        Integer usuarioId = 1;

        Cliente clienteExistente = new Cliente();
        when(clienteRepository.findByCorreoElectronico("camilo.duque@udea.edu.co"))
                .thenReturn(Optional.of(clienteExistente));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> clienteService.CrearClienteConPreferencias(dto, usuarioId));
        
        assertEquals("El correo electrónico ya está registrado.", exception.getMessage());
        verify(clienteRepository, times(1)).findByCorreoElectronico("camilo.duque@udea.edu.co");
        verifyNoInteractions(entityManager);
    }

    @Test
    void eliminarClienteConAuditoria_success() {
        // Arrange
        UUID idCliente = UUID.randomUUID();
        Integer idUsuario = 1;
        EliminarClienteDTO dto = new EliminarClienteDTO();
        dto.setIdCliente(idCliente);
        dto.setIdUsuario(idUsuario);
        dto.setConfirmacion("BORRAR");

        Query query = mock(Query.class, RETURNS_SELF);
        when(entityManager.createNativeQuery("SELECT eliminar_cliente_con_auditoria(:idCliente, :idUsuario)"))
                .thenReturn(query);
        when(query.getSingleResult()).thenReturn(null);

        // Act
        clienteService.eliminarClienteConAuditoria(dto);

        // Assert
        verify(entityManager, times(1)).createNativeQuery("SELECT eliminar_cliente_con_auditoria(:idCliente, :idUsuario)");
        verify(query, times(1)).setParameter("idCliente", idCliente);
        verify(query, times(1)).setParameter("idUsuario", idUsuario);
        verify(query, times(1)).getSingleResult();
    }

    @Test
    void eliminarClienteConAuditoria_wrongConfirmation() {
        // Arrange
        UUID idCliente = UUID.randomUUID();
        Integer idUsuario = 1;
        EliminarClienteDTO dto = new EliminarClienteDTO();
        dto.setIdCliente(idCliente);
        dto.setIdUsuario(idUsuario);
        dto.setConfirmacion("ELIMINAR"); // Palabra incorrecta

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> clienteService.eliminarClienteConAuditoria(dto));
        
        assertEquals("La palabra clave de confirmación es incorrecta.", exception.getMessage());
        verifyNoInteractions(entityManager);
    }

    @Test
    void eliminarClienteConAuditoria_caseInsensitiveConfirmation() {
        // Arrange
        UUID idCliente = UUID.randomUUID();
        Integer idUsuario = 1;
        EliminarClienteDTO dto = new EliminarClienteDTO();
        dto.setIdCliente(idCliente);
        dto.setIdUsuario(idUsuario);
        dto.setConfirmacion("borrar"); // Minúsculas

        Query query = mock(Query.class, RETURNS_SELF);
        when(entityManager.createNativeQuery("SELECT eliminar_cliente_con_auditoria(:idCliente, :idUsuario)"))
                .thenReturn(query);
        when(query.getSingleResult()).thenReturn(null);

        // Act
        clienteService.eliminarClienteConAuditoria(dto);

        // Assert
        verify(entityManager, times(1)).createNativeQuery("SELECT eliminar_cliente_con_auditoria(:idCliente, :idUsuario)");
        verify(query, times(1)).setParameter("idCliente", idCliente);
        verify(query, times(1)).setParameter("idUsuario", idUsuario);
    }

    @Test
    void eliminarClienteConAuditoria_nullConfirmation() {
        // Arrange
        UUID idCliente = UUID.randomUUID();
        Integer idUsuario = 1;
        EliminarClienteDTO dto = new EliminarClienteDTO();
        dto.setIdCliente(idCliente);
        dto.setIdUsuario(idUsuario);
        dto.setConfirmacion(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> clienteService.eliminarClienteConAuditoria(dto));
        
        assertEquals("La palabra clave de confirmación es incorrecta.", exception.getMessage());
        verifyNoInteractions(entityManager);
    }

    @Test
    void eliminarClienteConAuditoria_emptyConfirmation() {
        // Arrange
        UUID idCliente = UUID.randomUUID();
        Integer idUsuario = 1;
        EliminarClienteDTO dto = new EliminarClienteDTO();
        dto.setIdCliente(idCliente);
        dto.setIdUsuario(idUsuario);
        dto.setConfirmacion("");

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> clienteService.eliminarClienteConAuditoria(dto));
        
        assertEquals("La palabra clave de confirmación es incorrecta.", exception.getMessage());
        verifyNoInteractions(entityManager);
    }
}