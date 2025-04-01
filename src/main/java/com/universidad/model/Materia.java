package com.universidad.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Materia {
    
    private Long id;
    private String nombreMateria;
    private String codigoUnico;
    private Integer creditos;

}