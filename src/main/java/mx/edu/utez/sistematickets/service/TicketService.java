package mx.edu.utez.sistematickets.service;

import mx.edu.utez.sistematickets.dto.PrioridadRequestDTO;
import mx.edu.utez.sistematickets.dto.TicketRequestDTO;
import mx.edu.utez.sistematickets.dto.TicketResponseDTO;
import mx.edu.utez.sistematickets.exception.ReglaNegocioException;
import mx.edu.utez.sistematickets.exception.TicketCerradoException;
import mx.edu.utez.sistematickets.exception.TicketNoEncontradoException;
import mx.edu.utez.sistematickets.exception.UsuarioInactivoException;
import mx.edu.utez.sistematickets.exception.UsuarioNoEncontradoException;
import mx.edu.utez.sistematickets.model.EstadoTicket;
import mx.edu.utez.sistematickets.model.Ticket;
import mx.edu.utez.sistematickets.model.Usuario;
import mx.edu.utez.sistematickets.repository.TicketRepository;
import mx.edu.utez.sistematickets.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UsuarioRepository usuarioRepository;

    public TicketService(TicketRepository ticketRepository, UsuarioRepository usuarioRepository) {
        this.ticketRepository = ticketRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /** crearTicket: usuario existente y activo, descripción no vacía, estado inicial ABIERTO. */
    @Transactional
    public TicketResponseDTO crearTicket(TicketRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new UsuarioNoEncontradoException(dto.getUsuarioId()));

        if (!usuario.isActivo()) {
            throw new UsuarioInactivoException(usuario.getId());
        }

        // Segunda barrera por si el service se usa sin pasar por el controller (@Valid)
        if (dto.getDescripcion() == null || dto.getDescripcion().isBlank()) {
            throw new ReglaNegocioException("La descripción no puede estar vacía");
        }

        Ticket ticket = new Ticket();
        ticket.setDescripcion(dto.getDescripcion().trim());
        ticket.setEstado(EstadoTicket.ABIERTO);
        ticket.setPrioridad(dto.getPrioridad());
        ticket.setUsuario(usuario);
        return TicketResponseDTO.from(ticketRepository.save(ticket));
    }

    /** actualizarEstadoTicket: ABIERTO -> EN_PROCESO -> CERRADO; CERRADO no cambia. */
    @Transactional
    public TicketResponseDTO actualizarEstadoTicket(Long id) {
        Ticket ticket = buscarTicket(id);

        EstadoTicket siguiente = switch (ticket.getEstado()) {
            case ABIERTO -> EstadoTicket.EN_PROCESO;
            case EN_PROCESO -> EstadoTicket.CERRADO;
            case CERRADO -> throw new TicketCerradoException(id);
        };

        ticket.setEstado(siguiente);
        return TicketResponseDTO.from(ticketRepository.save(ticket));
    }

    /** reasignarPrioridad: permitido solo si el ticket NO está CERRADO. */
    @Transactional
    public TicketResponseDTO reasignarPrioridad(Long id, PrioridadRequestDTO dto) {
        Ticket ticket = buscarTicket(id);

        if (ticket.getEstado() == EstadoTicket.CERRADO) {
            throw new TicketCerradoException(id);
        }

        ticket.setPrioridad(dto.getPrioridad());
        return TicketResponseDTO.from(ticketRepository.save(ticket));
    }

    /** obtenerTicketsUsuario: valida que el usuario exista y lista sus tickets. */
    @Transactional(readOnly = true)
    public List<TicketResponseDTO> obtenerTicketsUsuario(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new UsuarioNoEncontradoException(usuarioId);
        }
        return ticketRepository.buscarPorUsuario(usuarioId).stream()
                .map(TicketResponseDTO::from).toList();
    }

    private Ticket buscarTicket(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNoEncontradoException(id));
    }
}
