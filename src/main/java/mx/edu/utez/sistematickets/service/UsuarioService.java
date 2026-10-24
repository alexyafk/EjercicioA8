package mx.edu.utez.sistematickets.service;

import mx.edu.utez.sistematickets.dto.UsuarioRequestDTO;
import mx.edu.utez.sistematickets.dto.UsuarioResponseDTO;
import mx.edu.utez.sistematickets.exception.UsuarioNoEncontradoException;
import mx.edu.utez.sistematickets.model.Usuario;
import mx.edu.utez.sistematickets.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre().trim());
        usuario.setActivo(true);
        return UsuarioResponseDTO.from(usuarioRepository.save(usuario));
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream().map(UsuarioResponseDTO::from).toList();
    }

    /** desactivarUsuario: solo cambia activo=false, no elimina datos. */
    @Transactional
    public UsuarioResponseDTO desactivarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));
        usuario.setActivo(false);
        return UsuarioResponseDTO.from(usuarioRepository.save(usuario));
    }
}
