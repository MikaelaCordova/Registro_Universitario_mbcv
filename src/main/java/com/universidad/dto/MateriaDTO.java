package com.universidad.dto;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MateriaDTO implements Serializable {

    private Long id;

    @NotBlank(message = "El nombre de la materia es obligatorio")
    private String nombreMateria;

    @NotBlank(message = "El código único de la materia es obligatorio")
    private String codigoUnico;

    @NotNull(message = "Los créditos de la materia son obligatorios")
    @Positive(message = "Los créditos deben ser un número positivo")
    private Integer creditos;

    /**
     * Lista de IDs de materias que son prerequisitos para esta materia.
     */
    private List<Long> prerequisitos;

    /**
     * Lista de IDs de materias para las que esta materia es prerequisito.
     */
    private List<Long> esPrerequisitoDe;

    /**
     * Lista de IDs de los docentes asignados a esta materia.
     */
    private List<Long> docentesIds;

    public List<Long> getDocentesIds() {
        return docentesIds;
    }

    public void setDocentesIds(List<Long> docentesIds) {
        this.docentesIds = docentesIds;
    }
}