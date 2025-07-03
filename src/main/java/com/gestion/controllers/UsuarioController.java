package com.gestion.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gestion.dto.CrearUsuarioRequest;
import com.gestion.dto.UsuarioDTO;
import com.gestion.models.Usuario;
import com.gestion.services.UsuarioService;

import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.Link;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    @GetMapping
    public List<UsuarioDTO> getAll() {
        return service.listarUsuarios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try {
            UsuarioDTO usuario = service.buscarUsuarioPorId(id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body(Map.of("mensaje", ex.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<Usuario> crear(@RequestBody CrearUsuarioRequest request) {
        Usuario creado = service.crearUsuario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> editarUsuario(@PathVariable Integer id, @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO actualizado = service.actualizarUsuario(id, usuarioDTO);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    // METODO HATEOAS para buscar por ID
    @GetMapping("/hateoas/{id}")
    public UsuarioDTO obtenerHATEOAS(@PathVariable Integer id) {
        UsuarioDTO dto = service.buscarUsuarioPorId(id);

        // links urls de la misma API
        dto.add(linkTo(methodOn(UsuarioController.class).obtenerHATEOAS(id)).withSelfRel());
        dto.add(linkTo(methodOn(UsuarioController.class).obtenerTodosHATEOAS()).withRel("todos"));
        dto.add(linkTo(methodOn(UsuarioController.class).eliminar(id)).withRel("eliminar"));

        // links HATEOAS para API Gateway "A mano"
        dto.add(Link.of("http://localhost:8888/api/proxy/usuarios/" + dto.getIdUsuario()).withSelfRel());
        dto.add(Link.of("http://localhost:8888/api/proxy/usuarios/" + dto.getIdUsuario()).withRel("Modificar HATEOAS").withType("PUT"));
        dto.add(Link.of("http://localhost:8888/api/proxy/usuarios/" + dto.getIdUsuario()).withRel("Eliminar HATEOAS").withType("DELETE"));

        return dto;
    }

    // METODO HATEOAS para listar todos los usuarios utilizando HATEOAS
    @GetMapping("/hateoas")
    public List<UsuarioDTO> obtenerTodosHATEOAS() {
        List<UsuarioDTO> lista = service.listarUsuarios();

        for (UsuarioDTO dto : lista) {
            // link url de la misma API
            dto.add(linkTo(methodOn(UsuarioController.class).obtenerHATEOAS(dto.getIdUsuario())).withSelfRel());

            // links HATEOAS para API Gateway "A mano"
            dto.add(Link.of("http://localhost:8888/api/proxy/usuarios").withRel("Get todos HATEOAS"));
            dto.add(Link.of("http://localhost:8888/api/proxy/usuarios/" + dto.getIdUsuario()).withRel("Crear HATEOAS").withType("POST"));
        }

        return lista;
    }
}
