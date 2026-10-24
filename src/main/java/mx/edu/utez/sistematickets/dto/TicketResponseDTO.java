package mx.edu.utez.sistematickets.dto;

import mx.edu.utez.sistematickets.model.EstadoTicket;
import mx.edu.utez.sistematickets.model.PrioridadTicket;
import mx.edu.utez.sistematickets.model.Ticket;

public record TicketResponseDTO(
        Long id,
        String descripcion,
        EstadoTicket estado,
        PrioridadTicket prioridad,
        Long usuarioId,
        String usuarioNombre) {

    public static TicketResponseDTO from(Ticket t) {
        return new TicketResponseDTO(
                t.getId(),
                t.getDescripcion(),
                t.getEstado(),
                t.getPrioridad(),
                t.getUsuario().getId(),
                t.getUsuario().getNombre());
    }
}
