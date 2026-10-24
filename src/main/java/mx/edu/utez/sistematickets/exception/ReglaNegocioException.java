package mx.edu.utez.sistematickets.exception;

/** Base para violaciones de reglas de negocio (se responden con HTTP 409). */
public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
