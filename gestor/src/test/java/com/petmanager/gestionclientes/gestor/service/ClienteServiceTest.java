package com.petmanager.gestionclientes.gestor.service;

import com.petmanager.gestionclientes.gestor.DTO.*;
import com.petmanager.gestionclientes.gestor.model.Cliente;
import com.petmanager.gestionclientes.gestor.repository.ClienteRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    }

    @Test
    void getAllClientes() {
        Cliente cliente1 = new Cliente();
        cliente1.setId(UUID.randomUUID());
        cliente1.setNombre("Camilo");
        cliente1.setApellido("Duque");

        Cliente cliente2 = new Cliente();
        cliente2.setId(UUID.randomUUID());
        cliente2.setNombre("Carlos");
        cliente2.setApellido("Echeverri");

        when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente1, cliente2));

        List<ClienteVistaDto> resultado = clienteService.getAllClientes();

        assertEquals(2, resultado.size());
        assertEquals("Camilo", resultado.get(0).getNombre());
        assertEquals("Carlos", resultado.get(1).getNombre());
    }

    @Test
    void getUsuario() throws Exception {
        UUID id = UUID.randomUUID();
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNombre("Camilo");
        cliente.setApellido("Duque");
        cliente.setCorreoElectronico("camilo.duque@udea.edu.co");
        cliente.setTelefono("123456789");
        cliente.setDireccion("Calle 123");

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));

        ClienteDetalleDTO dto = clienteService.getUsuario(id);

        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals("Camilo", dto.getNombre());
        assertEquals("Duque", dto.getApellido());
        assertEquals("camilo.duque@udea.edu.co", dto.getCorreoElectronico());
        assertEquals("123456789", dto.getTelefono());
        assertEquals("Calle 123", dto.getDireccion());
    }

    @Test
    void buscarClientes() {
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

        jakarta.persistence.Query query = mock(jakarta.persistence.Query.class);
        when(entityManager.createNativeQuery("SELECT * FROM buscar_clientes(:id, :nombre, :apellidos, :correo)"))
                .thenReturn(query);
        when(query.setParameter("id", id)).thenReturn(query);
        when(query.setParameter("nombre", "Camilo")).thenReturn(query);
        when(query.setParameter("apellidos", "Duque")).thenReturn(query);
        when(query.setParameter("correo", "camilo.duque@udea.edu.co")).thenReturn(query);
        when(query.getResultList()).thenReturn(resultados);

        List<ClienteDetalleDTO> clientes = clienteService.buscarClientes(busquedaDTO);

        assertNotNull(clientes);
        assertEquals(1, clientes.size());
        ClienteDetalleDTO dto = clientes.get(0);
        assertEquals(id, dto.getId());
        assertEquals("Camilo", dto.getNombre());
        assertEquals("Duque", dto.getApellido());
        assertEquals("camilo.duque@udea.edu.co", dto.getCorreoElectronico());
        assertEquals("123456789", dto.getTelefono());
        assertEquals("Calle 123", dto.getDireccion());
    }

    @Test
    void crearClienteConPreferencias_emptyCategories_success() {
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

        clienteService.CrearClienteConPreferencias(dto, usuarioId);

        verify(clienteRepository, times(1)).findByCorreoElectronico("camilo.duque@udea.edu.co");
        verify(entityManager, times(1)).createNativeQuery("SELECT crear_cliente_con_preferencias_2(:nombre, :apellidos, :correo, :telefono, :direccion, :categorias, :usuarioId)");
        verify(query, times(1)).setParameter("nombre", "Camilo");
        verify(query, times(1)).setParameter("apellidos", "Duque");
        verify(query, times(1)).setParameter("correo", "camilo.duque@udea.edu.co");
        verify(query, times(1)).setParameter("telefono", "123456789");
        verify(query, times(1)).setParameter("direccion", "Calle 123");
        verify(query, times(1)).setParameter("categorias", "");
        verify(query, times(1)).setParameter("usuarioId", usuarioId);
        verify(query, times(1)).getSingleResult();
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

        // Mock EntityManager and Query with RETURNS_SELF
        Query query = mock(Query.class, RETURNS_SELF);
        when(entityManager.createNativeQuery("SELECT eliminar_cliente_con_auditoria(:idCliente, :idUsuario)"))
                .thenReturn(query);
        when(query.getSingleResult()).thenReturn(null); // Assuming the function returns null on success

        // Act
        clienteService.eliminarClienteConAuditoria(dto);

        // Assert
        verify(entityManager, times(1)).createNativeQuery("SELECT eliminar_cliente_con_auditoria(:idCliente, :idUsuario)");
        verify(query, times(1)).setParameter("idCliente", idCliente);
        verify(query, times(1)).setParameter("idUsuario", idUsuario);
        verify(query, times(1)).getSingleResult();
    }
}