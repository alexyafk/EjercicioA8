package mx.edu.utez.sistematickets.exception;

public class UsuarioInactivoException extends ReglaNegocioException {
    public UsuarioInactivoException(Long id) {
        super("El usuario con id " + id + " está inactivo y no puede crear tickets");
    }
}
