package com.universidad.service; // Define el paquete al que pertenece esta interfaz

import com.universidad.dto.EstudianteDTO; // Importa la clase EstudianteDTO del paquete dto

import java.util.List; // Importa la interfaz List para manejar listas

public interface IEstudianteService { // Define la interfaz IEstudianteService

    List<EstudianteDTO> obtenerTodosLosEstudiantes(); // Método para obtener una lista de todos los EstudianteDTO

    EstudianteDTO obtenerEstudiantePorId(Long id); // Método para obtener un estudiante por su ID

    EstudianteDTO actualizarEstudiante(Long id, EstudianteDTO estudianteDTO); // Método para actualizar un estudiante

    EstudianteDTO crearEstudiante(EstudianteDTO estudianteDTO); // Método para crear un estudiante

    boolean eliminarEstudiante(Long id); // Método para eliminar un estudiante por su ID
}