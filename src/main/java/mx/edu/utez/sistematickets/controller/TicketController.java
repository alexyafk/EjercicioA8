package mx.edu.utez.sistematickets.controller;

import jakarta.validation.Valid;
import mx.edu.utez.sistematickets.dto.PrioridadRequestDTO;
import mx.edu.utez.sistematickets.dto.TicketRequestDTO;
import mx.edu.utez.sistematickets.dto.TicketResponseDTO;
import mx.edu.utez.sistematickets.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketResponseDTO> crear(@Valid @RequestBody TicketRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.crearTicket(dto));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<TicketResponseDTO> avanzarEstado(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.actualizarEstadoTicket(id));
    }

    @PatchMapping("/{id}/prioridad")
    public ResponseEntity<TicketResponseDTO> reasignarPrioridad(@PathVariable Long id,
                                                                @Valid @RequestBody PrioridadRequestDTO dto) {
        return ResponseEntity.ok(ticketService.reasignarPrioridad(id, dto));
    }
}
