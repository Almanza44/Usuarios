package ucr.ac.cr.PruebaUsuarios.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import ucr.ac.cr.PruebaUsuarios.exception.UsuarioNoEncontradoException;
import ucr.ac.cr.PruebaUsuarios.model.Usuario;
import ucr.ac.cr.PruebaUsuarios.service.UsuarioService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private UsuarioController usuarioController;

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

    // Prueba: buscar usuario -> 200 OK
    @Test
    void testBuscarPorId_UsuarioExiste() {
        when(usuarioService.buscarPorIdOrThrow(1L)).thenReturn(usuarioEjemplo);

        ResponseEntity<Usuario> response = usuarioController.buscarPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    // Prueba: buscar usuario que NO existe -> 404 Not Found
    @Test
    void testBuscarPorId_UsuarioNoExiste_RetornaNotFound() {
        when(usuarioService.buscarPorIdOrThrow(99L)).thenThrow(new UsuarioNoEncontradoException(99L));

        ResponseEntity<Usuario> response = usuarioController.buscarPorId(99L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    // Prueba: crear usuario -> 201 Created
    @Test
    void testCrearUsuario_Exitoso() {
        Usuario nuevo = new Usuario();
        nuevo.setEmail("nuevo@example.com");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(usuarioService.crearUsuario(any(Usuario.class))).thenReturn(usuarioEjemplo);

        ResponseEntity<?> response = usuarioController.crearUsuario(nuevo, bindingResult);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    // Prueba: actualizar usuario -> 200 OK
    @Test
    void testActualizarUsuario_Exitoso() {
        Usuario actualizado = new Usuario();
        actualizado.setNombre("Nuevo Nombre");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(usuarioService.actualizarUsuario(anyLong(), any(Usuario.class))).thenReturn(usuarioEjemplo);

        ResponseEntity<?> response = usuarioController.actualizarUsuario(1L, actualizado, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    // Prueba: eliminar usuario -> 204 No Content
    @Test
    void testEliminarUsuario_Exitoso() {
        doNothing().when(usuarioService).eliminarUsuario(1L);

        ResponseEntity<Void> response = usuarioController.eliminarUsuario(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(usuarioService, times(1)).eliminarUsuario(1L);
    }
}