package com.universidad.model; // Define el paquete al que pertenece esta clase

import lombok.AllArgsConstructor; // Importa la anotación AllArgsConstructor de Lombok para generar un constructor con todos los argumentos
import lombok.Data; // Importa la anotación Data de Lombok para generar getters, setters, toString, equals y hashCode
import lombok.EqualsAndHashCode; // Importa la anotación EqualsAndHashCode de Lombok para generar métodos equals y hashCode
import lombok.NoArgsConstructor; // Importa la anotación NoArgsConstructor de Lombok para generar un constructor sin argumentos
import lombok.experimental.SuperBuilder; // Importa la anotación SuperBuilder de Lombok para generar un builder que soporta herencia

@Data // Genera getters, setters, toString, equals y hashCode
@EqualsAndHashCode(callSuper = true) // Genera métodos equals y hashCode, incluyendo los campos de la clase padre
@NoArgsConstructor // Genera un constructor sin argumentos
@AllArgsConstructor // Genera un constructor con todos los argumentos
@SuperBuilder // Genera un builder que soporta herencia
public class Estudiante extends Persona { // Define la clase Estudiante que extiende de Persona
    
    private String numeroInscripcion; // Campo para almacenar el número de inscripción del estudiante

}