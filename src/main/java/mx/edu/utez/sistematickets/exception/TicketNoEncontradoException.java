package mx.edu.utez.sistematickets.exception;

public class TicketNoEncontradoException extends RecursoNoEncontradoException {
    public TicketNoEncontradoException(Long id) {
        super("No existe un ticket con id " + id);
    }
}
