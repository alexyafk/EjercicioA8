package mx.edu.utez.sistematickets.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mx.edu.utez.sistematickets.model.PrioridadTicket;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrioridadRequestDTO {

    @NotNull(message = "La prioridad es obligatoria (BAJA, MEDIA, ALTA)")
    private PrioridadTicket prioridad;
}
