package com.universidad.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocenteDTO {
    private Long id;

    @NotBlank(message = "El nombre del docente es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido del docente es obligatorio")
    private String apellido;

    @NotBlank(message = "El email del docente es obligatorio")
    @Email(message = "El email debe ser válido")
    private String email;

    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El número de empleado es obligatorio")
    private String nroEmpleado;

    @NotBlank(message = "El departamento del docente es obligatorio")
    private String departamento;

    /**
     * Lista de IDs de las materias asignadas a este docente.
     */
    private List<Long> materiasIds;

    public List<Long> getMateriasIds() {
        return materiasIds;
    }

    public void setMateriasIds(List<Long> materiasIds) {
        this.materiasIds = materiasIds;
    }
}