package mx.edu.utez.sistematickets.controller;

import jakarta.validation.Valid;
import mx.edu.utez.sistematickets.dto.TicketResponseDTO;
import mx.edu.utez.sistematickets.dto.UsuarioRequestDTO;
import mx.edu.utez.sistematickets.dto.UsuarioResponseDTO;
import mx.edu.utez.sistematickets.service.TicketService;
import mx.edu.utez.sistematickets.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final TicketService ticketService;

    public UsuarioController(UsuarioService usuarioService, TicketService ticketService) {
        this.usuarioService = usuarioService;
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(@Valid @RequestBody UsuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crearUsuario(dto));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<UsuarioResponseDTO> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.desactivarUsuario(id));
    }

    @GetMapping("/{id}/tickets")
    public ResponseEntity<List<TicketResponseDTO>> tickets(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.obtenerTicketsUsuario(id));
    }
}
