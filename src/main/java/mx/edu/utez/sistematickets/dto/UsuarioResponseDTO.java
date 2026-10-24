package mx.edu.utez.sistematickets.dto;

import mx.edu.utez.sistematickets.model.Usuario;

public record UsuarioResponseDTO(Long id, String nombre, boolean activo) {

    public static UsuarioResponseDTO from(Usuario u) {
        return new UsuarioResponseDTO(u.getId(), u.getNombre(), u.isActivo());
    }
}
