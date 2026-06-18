package ucr.ac.cr.PruebaUsuarios.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucr.ac.cr.PruebaUsuarios.exception.EmailDuplicadoException;
import ucr.ac.cr.PruebaUsuarios.exception.UsuarioNoEncontradoException;
import ucr.ac.cr.PruebaUsuarios.model.Usuario;
import ucr.ac.cr.PruebaUsuarios.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario buscarPorIdOrThrow(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));
    }

    public Usuario crearUsuario(Usuario usuario) {
        // Verificar si el email ya existe
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new EmailDuplicadoException(usuario.getEmail());
        }
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = buscarPorIdOrThrow(id);

        // Actualizar campos
        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setEmail(usuarioActualizado.getEmail());
        usuarioExistente.setPassword(usuarioActualizado.getPassword());

        return usuarioRepository.save(usuarioExistente);
    }

    public void eliminarUsuario(Long id) {
        Usuario usuario = buscarPorIdOrThrow(id);
        usuarioRepository.delete(usuario);
    }

    public Usuario desactivarUsuario(Long id) {
        Usuario usuario = buscarPorIdOrThrow(id);
        usuario.setActivo(false);
        return usuarioRepository.save(usuario);
    }
}
