package com.gestion.dto;

import org.springframework.hateoas.RepresentationModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearUsuarioRequest extends RepresentationModel<CrearUsuarioRequest> {
    private String nombreUsuario;
    private String email;
    private String contrasena;
    private String rol;
    private String estado;
    private String nombreCompleto;
    private String rut;
    private String direccion;
    private String telefono;
    private String areaVentas;
}
