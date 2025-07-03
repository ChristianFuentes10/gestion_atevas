package com.gestion.dto;

import org.springframework.hateoas.RepresentationModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO extends RepresentationModel<UsuarioDTO> {
    private Integer idUsuario;
    private String nombreUsuario;
    private String email;
    private String rol;
    private String estado;
}
