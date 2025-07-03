package com.gestion.dto;

import org.springframework.hateoas.RepresentationModel;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendedorDTO extends RepresentationModel<VendedorDTO> {
    private Integer idVendedor;
    private Integer idUsuario;
    private String nombreCompleto;
    private String rut;
    private String areaVentas;
}