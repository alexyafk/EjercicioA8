package mx.edu.utez.sistematickets.exception;

public class TicketCerradoException extends ReglaNegocioException {
    public TicketCerradoException(Long id) {
        super("El ticket con id " + id + " está CERRADO y no puede modificarse");
    }
}
