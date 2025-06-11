package com.petmanager.gestionclientes.gestor.service;
import com.petmanager.gestionclientes.gestor.dto.*;
import com.petmanager.gestionclientes.gestor.model.Cliente;
import com.petmanager.gestionclientes.gestor.repository.ClienteRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }


    public List<ClienteVistaDto> getAllClientes() {
        return clienteRepository.findAll().stream()
                .map(this::ClienteToVIstaDTO)
                .toList();
    }

    public ClienteDetalleDTO getUsuario(UUID id) {
        try{
            Cliente cliente = clienteRepository.findById(id)
                    .orElseThrow(() -> new Exception("Usuario no encontrado con id: " + id));
            return ClienteToDetalleDTO(cliente);
        } catch (Exception e) {
            throw new EntityNotFoundException("Cliente no encontrado");
        }
    }

    public List<ClienteDetalleDTO> buscarClientes(ClienteBusquedaDTO dto) {
        List<Object[]> resultados = entityManager
                .createNativeQuery("SELECT * FROM buscar_clientes(:id, :nombre, :apellidos, :correo)")
                .setParameter("id", dto.getId())
                .setParameter("nombre", dto.getNombre())
                .setParameter("apellidos", dto.getApellidos())
                .setParameter("correo", dto.getCorreoElectronico())
                .getResultList();
        if (resultados.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron clientes con los criterios proporcionados.");
        }
        return resultados.stream()
                .map(row -> new ClienteDetalleDTO(
                        ((UUID) row[0]),  // id
                        (String) row[1],                // nombre
                        (String) row[2],                // apellido
                        (String) row[3],                // correo
                        (String) row[4],                // teléfono
                        (String) row[5]                 // dirección
                ))
                .toList();
    }

    @Transactional
    public void CrearClienteConPreferencias(ClienteRegistroDTO dto, Integer usuarioId) {
        // Validar si el correo ya existe
        if (clienteRepository.findByCorreoElectronico(dto.getCorreoElectronico()).isPresent()) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado.");
        }

        String categoriasCsv = dto.getCategorias() == null || dto.getCategorias().isEmpty()
                ? ""  // si no hay categorías enviamos string vacío
                : dto.getCategorias().stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));


        entityManager.createNativeQuery("SELECT crear_cliente_con_preferencias_2(:nombre, :apellidos, :correo, :telefono, :direccion, :categorias, :usuarioId)")
                .setParameter("nombre", dto.getNombre())
                .setParameter("apellidos", dto.getApellidos())
                .setParameter("correo", dto.getCorreoElectronico())
                .setParameter("telefono", dto.getTelefono())
                .setParameter("direccion", dto.getDireccion())
                .setParameter("categorias", categoriasCsv) // Ahora string CSV
                .setParameter("usuarioId", usuarioId)
                .getSingleResult();
    }


    public void eliminarClienteConAuditoria(EliminarClienteDTO dto) {
        if (!"BORRAR".equalsIgnoreCase(dto.getConfirmacion())) {
            throw new IllegalArgumentException("La palabra clave de confirmación es incorrecta.");
        }

        entityManager.createNativeQuery("SELECT eliminar_cliente_con_auditoria(:idCliente, :idUsuario)")
                .setParameter("idCliente", dto.getIdCliente())
                .setParameter("idUsuario", dto.getIdUsuario())
                .getSingleResult();  // Ejecuta la función de eliminación con auditoría
    }

    private ClienteVistaDto ClienteToVIstaDTO(Cliente cliente) {
        ClienteVistaDto clienteVistaDto = new ClienteVistaDto();
        clienteVistaDto.setId(cliente.getId());
        clienteVistaDto.setNombre(cliente.getNombre());
        clienteVistaDto.setApellido(cliente.getApellido());
        return clienteVistaDto;
    }

    private ClienteDetalleDTO ClienteToDetalleDTO(Cliente cliente) {
        ClienteDetalleDTO clienteDetalleDTO = new ClienteDetalleDTO();
        clienteDetalleDTO.setId(cliente.getId());
        clienteDetalleDTO.setNombre(cliente.getNombre());
        clienteDetalleDTO.setApellido(cliente.getApellido());
        clienteDetalleDTO.setCorreoElectronico(cliente.getCorreoElectronico());
        clienteDetalleDTO.setTelefono(cliente.getTelefono());
        clienteDetalleDTO.setDireccion(cliente.getDireccion());
        return clienteDetalleDTO;
    }
}
