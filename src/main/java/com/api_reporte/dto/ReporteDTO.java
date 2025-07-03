package com.api_reporte.dto;

import org.springframework.hateoas.RepresentationModel;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteDTO extends RepresentationModel<ReporteDTO> {
    private Integer id;
    private String nombre;
    private String descripcion;
    private Double precioUnitario;
    private String categoria;
    private Boolean estado;
}
