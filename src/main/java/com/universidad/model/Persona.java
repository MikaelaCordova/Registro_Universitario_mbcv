package com.universidad.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Persona {

    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private LocalDate fechaNacimiento;
}