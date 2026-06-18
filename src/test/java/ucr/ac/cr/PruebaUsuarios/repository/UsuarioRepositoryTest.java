package ucr.ac.cr.PruebaUsuarios.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ucr.ac.cr.PruebaUsuarios.model.Usuario;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario usuarioEjemplo;

    @BeforeEach
    void setUp() {
        usuarioEjemplo = new Usuario();
        usuarioEjemplo.setEmail("juan@example.com");
        usuarioEjemplo.setNombre("Juan Pérez");
        usuarioEjemplo.setPassword("password123");
        usuarioEjemplo.setFechaRegistro(LocalDateTime.now());
        usuarioEjemplo.setActivo(true);

        usuarioRepository.save(usuarioEjemplo);
    }

    @Test
    void testFindByEmail_Exitoso() {
        Optional<Usuario> resultado = usuarioRepository.findByEmail("juan@example.com");
        assertTrue(resultado.isPresent());
        assertEquals("juan@example.com", resultado.get().getEmail());
    }

    @Test
    void testFindByEmail_NoExiste_RetornaVacio() {
        Optional<Usuario> resultado = usuarioRepository.findByEmail("noexiste@example.com");
        assertFalse(resultado.isPresent());
    }

    @Test
    void testSaveUsuario_Exitoso() {
        Usuario nuevo = new Usuario();
        nuevo.setEmail("nuevo@example.com");
        nuevo.setNombre("Nuevo");
        nuevo.setPassword("pass");
        nuevo.setFechaRegistro(LocalDateTime.now());
        nuevo.setActivo(true);

        Usuario guardado = usuarioRepository.save(nuevo);

        assertNotNull(guardado.getId());
        Optional<Usuario> encontrado = usuarioRepository.findById(guardado.getId());
        assertTrue(encontrado.isPresent());
    }

    @Test
    void testDeleteUsuario_Exitoso() {
        usuarioRepository.delete(usuarioEjemplo);
        Optional<Usuario> encontrado = usuarioRepository.findById(usuarioEjemplo.getId());
        assertFalse(encontrado.isPresent());
    }
}