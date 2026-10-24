package mx.edu.utez.sistematickets.exception;

public class UsuarioNoEncontradoException extends RecursoNoEncontradoException {
    public UsuarioNoEncontradoException(Long id) {
        super("No existe un usuario con id " + id);
    }
}
