package mx.edu.utez.sistematickets.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponseDTO(
        LocalDateTime timestamp,
        int status,
        String error,
        String mensaje,
        Map<String, String> detalles) {

    public static ErrorResponseDTO of(int status, String error, String mensaje, Map<String, String> detalles) {
        return new ErrorResponseDTO(LocalDateTime.now(), status, error, mensaje, detalles);
    }
}
