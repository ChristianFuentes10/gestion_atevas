package com.api_reporte.controller;

import com.api_reporte.dto.ReporteDTO;
import com.api_reporte.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService service;

    @PostMapping
    public ResponseEntity<ReporteDTO> crear(@RequestBody ReporteDTO dto) {
        return ResponseEntity.ok(service.guardar(dto));
    }

    @GetMapping
    public ResponseEntity<List<ReporteDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReporteDTO> obtener(@PathVariable Integer id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReporteDTO> actualizar(@PathVariable Integer id, @RequestBody ReporteDTO dto) {
        return service.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        return service.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    // MÉTODO HATEOAS para buscar por ID
    @GetMapping("/hateoas/{id}")
    public ReporteDTO obtenerHATEOAS(@PathVariable Integer id) {
        ReporteDTO dto = service.obtenerPorId(id).orElse(null);
        if (dto == null) {
            return null;
        }

        // links urls de la misma API
        dto.add(linkTo(methodOn(ReporteController.class).obtenerHATEOAS(id)).withSelfRel());
        dto.add(linkTo(methodOn(ReporteController.class).obtenerTodosHATEOAS()).withRel("todos"));
        dto.add(linkTo(methodOn(ReporteController.class).eliminar(id)).withRel("eliminar"));

        // links HATEOAS para API Gateway "A mano"
        dto.add(Link.of("http://localhost:8888/api/proxy/reportes/" + dto.getId()).withSelfRel());
        dto.add(Link.of("http://localhost:8888/api/proxy/reportes/" + dto.getId()).withRel("Modificar HATEOAS").withType("PUT"));
        dto.add(Link.of("http://localhost:8888/api/proxy/reportes/" + dto.getId()).withRel("Eliminar HATEOAS").withType("DELETE"));

        return dto;
    }

    // MÉTODO HATEOAS para listar todos los reportes utilizando HATEOAS
    @GetMapping("/hateoas")
    public List<ReporteDTO> obtenerTodosHATEOAS() {
        List<ReporteDTO> reportes = service.listar();
        for (ReporteDTO dto : reportes) {
            // links urls de la misma API
            dto.add(linkTo(methodOn(ReporteController.class).obtenerHATEOAS(dto.getId())).withSelfRel());
            dto.add(linkTo(methodOn(ReporteController.class).obtenerTodosHATEOAS()).withRel("todos"));
            dto.add(linkTo(methodOn(ReporteController.class).eliminar(dto.getId())).withRel("eliminar"));

            // links HATEOAS para API Gateway "A mano"
            dto.add(Link.of("http://localhost:8888/api/proxy/reportes/" + dto.getId()).withSelfRel());
            dto.add(Link.of("http://localhost:8888/api/proxy/reportes/" + dto.getId()).withRel("Modificar HATEOAS").withType("PUT"));
            dto.add(Link.of("http://localhost:8888/api/proxy/reportes/" + dto.getId()).withRel("Eliminar HATEOAS").withType("DELETE"));
        }
        return reportes;
    }
}
