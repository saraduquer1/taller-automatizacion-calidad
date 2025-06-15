package com.petmanager.gestionclientes.gestor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petmanager.gestionclientes.gestor.dto.*;
import com.petmanager.gestionclientes.gestor.service.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID clienteId;
    private ClienteVistaDto clienteVistaDto;
    private ClienteDetalleDTO clienteDetalleDTO;

    @BeforeEach
    void setUp() {
        clienteId = UUID.randomUUID();
        
        clienteVistaDto = new ClienteVistaDto();
        clienteVistaDto.setId(clienteId);
        clienteVistaDto.setNombre("Juan");
        clienteVistaDto.setApellido("Pérez");

        clienteDetalleDTO = new ClienteDetalleDTO();
        clienteDetalleDTO.setId(clienteId);
        clienteDetalleDTO.setNombre("Juan");
        clienteDetalleDTO.setApellido("Pérez");
        clienteDetalleDTO.setCorreoElectronico("juan.perez@email.com");
        clienteDetalleDTO.setTelefono("123456789");
        clienteDetalleDTO.setDireccion("Calle 123");
    }

    @Test
    void obtenerTodos_success() throws Exception {
        // Arrange
        List<ClienteVistaDto> clientes = Arrays.asList(clienteVistaDto);
        when(clienteService.getAllClientes()).thenReturn(clientes);

        // Act & Assert
        mockMvc.perform(get("/clientes/listar"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(clienteId.toString()))
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[0].apellido").value("Pérez"));

        verify(clienteService, times(1)).getAllClientes();
    }

    @Test
    void obtenerTodos_emptyList() throws Exception {
        // Arrange
        when(clienteService.getAllClientes()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/clientes/listar"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(clienteService, times(1)).getAllClientes();
    }

    @Test
    void obtenerClientePorId_success() throws Exception {
        // Arrange
        when(clienteService.getUsuario(clienteId)).thenReturn(clienteDetalleDTO);

        // Act & Assert
        mockMvc.perform(get("/clientes/{id}", clienteId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(clienteId.toString()))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.apellido").value("Pérez"))
                .andExpect(jsonPath("$.correoElectronico").value("juan.perez@email.com"))
                .andExpect(jsonPath("$.telefono").value("123456789"))
                .andExpect(jsonPath("$.direccion").value("Calle 123"));

        verify(clienteService, times(1)).getUsuario(clienteId);
    }

    @Test
    void obtenerClientePorId_notFound() throws Exception {
        // Arrange
        when(clienteService.getUsuario(clienteId)).thenThrow(new EntityNotFoundException());

        // Act & Assert
        mockMvc.perform(get("/clientes/{id}", clienteId))
                .andExpect(status().isNotFound());

        verify(clienteService, times(1)).getUsuario(clienteId);
    }

    @Test
    void buscarClientes_success() throws Exception {
        // Arrange
        ClienteBusquedaDTO busquedaDTO = new ClienteBusquedaDTO();
        busquedaDTO.setNombre("Juan");
        busquedaDTO.setApellidos("Pérez");

        List<ClienteDetalleDTO> resultados = Arrays.asList(clienteDetalleDTO);
        when(clienteService.buscarClientes(any(ClienteBusquedaDTO.class))).thenReturn(resultados);

        // Act & Assert
        mockMvc.perform(post("/clientes/buscar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(busquedaDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(clienteId.toString()))
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[0].apellido").value("Pérez"));

        verify(clienteService, times(1)).buscarClientes(any(ClienteBusquedaDTO.class));
    }

    @Test
    void buscarClientes_notFound() throws Exception {
        // Arrange
        ClienteBusquedaDTO busquedaDTO = new ClienteBusquedaDTO();
        busquedaDTO.setNombre("NoExiste");

        when(clienteService.buscarClientes(any(ClienteBusquedaDTO.class)))
                .thenThrow(new EntityNotFoundException());

        // Act & Assert
        mockMvc.perform(post("/clientes/buscar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(busquedaDTO)))
                .andExpect(status().isNotFound());

        verify(clienteService, times(1)).buscarClientes(any(ClienteBusquedaDTO.class));
    }

    @Test
    void buscarClientes_emptyRequest() throws Exception {
        // Arrange
        ClienteBusquedaDTO busquedaDTO = new ClienteBusquedaDTO();
        List<ClienteDetalleDTO> resultados = Arrays.asList(clienteDetalleDTO);
        when(clienteService.buscarClientes(any(ClienteBusquedaDTO.class))).thenReturn(resultados);

        // Act & Assert
        mockMvc.perform(post("/clientes/buscar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(busquedaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nombre").value("Juan"));

        verify(clienteService, times(1)).buscarClientes(any(ClienteBusquedaDTO.class));
    }

    @Test
    void registrarCliente_success() throws Exception {
        // Arrange
        ClienteRegistroDTO registroDTO = new ClienteRegistroDTO();
        registroDTO.setNombre("Juan");
        registroDTO.setApellidos("Pérez");
        registroDTO.setCorreoElectronico("juan.perez@email.com");
        registroDTO.setTelefono("123456789");
        registroDTO.setDireccion("Calle 123");
        registroDTO.setCategorias(Arrays.asList(1, 2));

        doNothing().when(clienteService).CrearClienteConPreferencias(any(ClienteRegistroDTO.class), any(Integer.class));

        // Act & Assert
        mockMvc.perform(post("/clientes/registrar")
                .param("usuario_id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registroDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Cliente registrado correctamente."));

        verify(clienteService, times(1)).CrearClienteConPreferencias(any(ClienteRegistroDTO.class), eq(1));
    }

    @Test
    void eliminarCliente_success() throws Exception {
        // Arrange
        EliminarClienteDTO eliminarDTO = new EliminarClienteDTO();
        eliminarDTO.setIdCliente(clienteId);
        eliminarDTO.setIdUsuario(1);
        eliminarDTO.setConfirmacion("BORRAR");

        doNothing().when(clienteService).eliminarClienteConAuditoria(any(EliminarClienteDTO.class));

        // Act & Assert
        mockMvc.perform(delete("/clientes/eliminar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eliminarDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Cliente eliminado correctamente."));

        verify(clienteService, times(1)).eliminarClienteConAuditoria(any(EliminarClienteDTO.class));
    }

    @Test
    void eliminarCliente_badRequest() throws Exception {
        // Arrange
        EliminarClienteDTO eliminarDTO = new EliminarClienteDTO();
        eliminarDTO.setIdCliente(clienteId);
        eliminarDTO.setIdUsuario(1);
        eliminarDTO.setConfirmacion("ELIMINAR"); // Palabra incorrecta

        doThrow(new IllegalArgumentException("La palabra clave de confirmación es incorrecta."))
                .when(clienteService).eliminarClienteConAuditoria(any(EliminarClienteDTO.class));

        // Act & Assert
        mockMvc.perform(delete("/clientes/eliminar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eliminarDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("La palabra clave de confirmación es incorrecta."));

        verify(clienteService, times(1)).eliminarClienteConAuditoria(any(EliminarClienteDTO.class));
    }

    @Test
    void eliminarCliente_internalServerError() throws Exception {
        // Arrange
        EliminarClienteDTO eliminarDTO = new EliminarClienteDTO();
        eliminarDTO.setIdCliente(clienteId);
        eliminarDTO.setIdUsuario(1);
        eliminarDTO.setConfirmacion("BORRAR");

        doThrow(new RuntimeException("Error de base de datos"))
                .when(clienteService).eliminarClienteConAuditoria(any(EliminarClienteDTO.class));

        // Act & Assert
        mockMvc.perform(delete("/clientes/eliminar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eliminarDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error al eliminar cliente."));

        verify(clienteService, times(1)).eliminarClienteConAuditoria(any(EliminarClienteDTO.class));
    }
}