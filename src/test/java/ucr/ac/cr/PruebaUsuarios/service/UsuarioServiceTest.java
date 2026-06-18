package ucr.ac.cr.PruebaUsuarios.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ucr.ac.cr.PruebaUsuarios.exception.UsuarioNoEncontradoException;
import ucr.ac.cr.PruebaUsuarios.model.Usuario;
import ucr.ac.cr.PruebaUsuarios.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioEjemplo;

    @BeforeEach
    void setUp() {
        usuarioEjemplo = new Usuario();
        usuarioEjemplo.setId(1L);
        usuarioEjemplo.setEmail("juan@example.com");
        usuarioEjemplo.setNombre("Juan Pérez");
        usuarioEjemplo.setPassword("password123");
        usuarioEjemplo.setFechaRegistro(LocalDateTime.now());
        usuarioEjemplo.setActivo(true);
    }

    // Prueba: buscar usuario que SI existe
    @Test
    void testBuscarPorIdOrThrow_UsuarioExiste() {
        // Configuro el mock
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEjemplo));

        // Ejecuto
        Usuario resultado = usuarioService.buscarPorIdOrThrow(1L);

        // Verifico
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    // Prueba: buscar usuario que NO existe -> lanza excepcion
    @Test
    void testBuscarPorIdOrThrow_UsuarioNoExiste_LanzaExcepcion() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class,
                () -> usuarioService.buscarPorIdOrThrow(99L));

        verify(usuarioRepository, times(1)).findById(99L);
    }

    // Prueba: crear usuario exitosamente
    @Test
    void testCrearUsuario_Exitoso() {
        Usuario nuevo = new Usuario();
        nuevo.setEmail("nuevo@example.com");

        when(usuarioRepository.existsByEmail(nuevo.getEmail())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEjemplo);

        Usuario resultado = usuarioService.crearUsuario(nuevo);

        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    // Prueba: eliminar usuario exitosamente
    @Test
    void testEliminarUsuario_Exitoso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioEjemplo));
        doNothing().when(usuarioRepository).delete(usuarioEjemplo);

        usuarioService.eliminarUsuario(1L);

        verify(usuarioRepository, times(1)).delete(usuarioEjemplo);
    }
}