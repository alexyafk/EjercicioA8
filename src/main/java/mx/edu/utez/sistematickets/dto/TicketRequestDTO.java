package mx.edu.utez.sistematickets.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.sistematickets.model.PrioridadTicket;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketRequestDTO {

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @NotNull(message = "La prioridad es obligatoria (BAJA, MEDIA, ALTA)")
    private PrioridadTicket prioridad;

    @NotNull(message = "El id del usuario es obligatorio")
    private Long usuarioId;
}
