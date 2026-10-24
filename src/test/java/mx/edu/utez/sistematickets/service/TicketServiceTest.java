package mx.edu.utez.sistematickets.service;

import mx.edu.utez.sistematickets.dto.PrioridadRequestDTO;
import mx.edu.utez.sistematickets.dto.TicketRequestDTO;
import mx.edu.utez.sistematickets.dto.TicketResponseDTO;
import mx.edu.utez.sistematickets.exception.TicketCerradoException;
import mx.edu.utez.sistematickets.exception.UsuarioInactivoException;
import mx.edu.utez.sistematickets.exception.UsuarioNoEncontradoException;
import mx.edu.utez.sistematickets.model.EstadoTicket;
import mx.edu.utez.sistematickets.model.PrioridadTicket;
import mx.edu.utez.sistematickets.model.Ticket;
import mx.edu.utez.sistematickets.model.Usuario;
import mx.edu.utez.sistematickets.repository.TicketRepository;
import mx.edu.utez.sistematickets.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TicketServiceTest {

    private TicketRepository ticketRepository;
    private UsuarioRepository usuarioRepository;
    private TicketService service;

    @BeforeEach
    void setUp() {
        ticketRepository = mock(TicketRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);
        service = new TicketService(ticketRepository, usuarioRepository);
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(i -> i.getArgument(0));
    }

    private Usuario usuario(boolean activo) {
        Usuario u = new Usuario();
        u.setId(1L);
        u.setNombre("Ana");
        u.setActivo(activo);
        return u;
    }

    private Ticket ticket(EstadoTicket estado) {
        Ticket t = new Ticket();
        t.setId(10L);
        t.setDescripcion("No prende la PC");
        t.setEstado(estado);
        t.setPrioridad(PrioridadTicket.BAJA);
        t.setUsuario(usuario(true));
        return t;
    }

    @Test
    void crearTicketQuedaAbierto() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario(true)));
        TicketResponseDTO r = service.crearTicket(new TicketRequestDTO("  Falla de red ", PrioridadTicket.ALTA, 1L));
        assertEquals(EstadoTicket.ABIERTO, r.estado());
        assertEquals(PrioridadTicket.ALTA, r.prioridad());
        assertEquals("Falla de red", r.descripcion());
    }

    @Test
    void crearTicketConUsuarioInactivoFalla() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario(false)));
        assertThrows(UsuarioInactivoException.class,
                () -> service.crearTicket(new TicketRequestDTO("x", PrioridadTicket.BAJA, 1L)));
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void crearTicketConUsuarioInexistenteFalla() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(UsuarioNoEncontradoException.class,
                () -> service.crearTicket(new TicketRequestDTO("x", PrioridadTicket.BAJA, 99L)));
    }

    @Test
    void estadoAvanzaDeFormaSecuencialYCerradoNoCambia() {
        Ticket t = ticket(EstadoTicket.ABIERTO);
        when(ticketRepository.findById(10L)).thenReturn(Optional.of(t));

        assertEquals(EstadoTicket.EN_PROCESO, service.actualizarEstadoTicket(10L).estado());
        assertEquals(EstadoTicket.CERRADO, service.actualizarEstadoTicket(10L).estado());
        assertThrows(TicketCerradoException.class, () -> service.actualizarEstadoTicket(10L));
    }

    @Test
    void reasignarPrioridadFuncionaSiNoEstaCerrado() {
        when(ticketRepository.findById(10L)).thenReturn(Optional.of(ticket(EstadoTicket.EN_PROCESO)));
        assertEquals(PrioridadTicket.ALTA,
                service.reasignarPrioridad(10L, new PrioridadRequestDTO(PrioridadTicket.ALTA)).prioridad());
    }

    @Test
    void reasignarPrioridadEnTicketCerradoFalla() {
        when(ticketRepository.findById(10L)).thenReturn(Optional.of(ticket(EstadoTicket.CERRADO)));
        assertThrows(TicketCerradoException.class,
                () -> service.reasignarPrioridad(10L, new PrioridadRequestDTO(PrioridadTicket.ALTA)));
    }

    @Test
    void obtenerTicketsDeUsuarioInexistenteFalla() {
        when(usuarioRepository.existsById(5L)).thenReturn(false);
        assertThrows(UsuarioNoEncontradoException.class, () -> service.obtenerTicketsUsuario(5L));
    }
}
