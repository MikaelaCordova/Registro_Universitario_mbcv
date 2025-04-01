package com.universidad.service.impl; // Define el paquete al que pertenece esta clase

import com.universidad.dto.EstudianteDTO; // Importa la clase EstudianteDTO del paquete dto
import com.universidad.model.Estudiante; // Importa la clase Estudiante del paquete model
import com.universidad.repository.EstudianteRepository; // Importa la clase EstudianteRepository del paquete repository
import com.universidad.service.IEstudianteService; // Importa la interfaz IEstudianteService del paquete service

import jakarta.annotation.PostConstruct; // Importa la anotación PostConstruct de Jakarta
import org.springframework.beans.factory.annotation.Autowired; // Importa la anotación Autowired de Spring
import org.springframework.stereotype.Service; // Importa la anotación Service de Spring
import org.springframework.web.server.ResponseStatusException; // Importa la excepción ResponseStatusException

import java.util.ArrayList; // Importa la clase ArrayList para manejar listas
import java.util.List; // Importa la interfaz List para manejar listas
import java.util.Optional; // Importa la clase Optional para manejar valores que pueden ser nulos

import static org.springframework.http.HttpStatus.NOT_FOUND; // Importa la constante NOT_FOUND de HttpStatus

@Service // Anotación que indica que esta clase es un servicio de Spring
public class EstudianteServiceImpl implements IEstudianteService { // Define la clase EstudianteServiceImpl que implementa la interfaz IEstudianteService

    private final EstudianteRepository estudianteRepository; // Declara una variable final para el repositorio de estudiantes

    @Autowired // Anotación que indica que el constructor debe ser usado para inyección de dependencias
    public EstudianteServiceImpl(EstudianteRepository estudianteRepository) { // Constructor que recibe el repositorio de estudiantes
        this.estudianteRepository = estudianteRepository; // Asigna el repositorio de estudiantes a la variable de instancia
    }

    @PostConstruct // Anotación que indica que este método debe ejecutarse después de la construcción del bean
    public void init() { // Método para inicializar datos de ejemplo
        estudianteRepository.init(); // Llama al método init del repositorio de estudiantes
    }

    @Override // Anotación que indica que este método sobrescribe un método de la interfaz
    public List<EstudianteDTO> obtenerTodosLosEstudiantes() { // Método para obtener una lista de todos los EstudianteDTO
        List<Estudiante> estudiantes = estudianteRepository.findAll(); // Obtiene todos los estudiantes del repositorio
        List<EstudianteDTO> estudiantesDTO = new ArrayList<>(); // Crea una nueva lista para los EstudianteDTO

        for (Estudiante estudiante : estudiantes) { // Itera sobre la lista de estudiantes
            estudiantesDTO.add(convertToDTO(estudiante)); // Convierte cada estudiante a EstudianteDTO y lo agrega a la lista
        }
        return estudiantesDTO; // Retorna la lista de EstudianteDTO
    }

    @Override
    public EstudianteDTO obtenerEstudiantePorId(Long id) {
        Optional<Estudiante> estudianteOptional = estudianteRepository.findById(id);
        return estudianteOptional.map(this::convertToDTO).orElse(null);
    }

    @Override
    public EstudianteDTO actualizarEstudiante(Long id, EstudianteDTO estudianteDTO) {
        Optional<Estudiante> estudianteOptional = estudianteRepository.findById(id);
        if (estudianteOptional.isPresent()) {
            Estudiante estudianteExistente = estudianteOptional.get();
            // Actualiza los campos del estudiante existente con los datos del DTO
            estudianteExistente.setNombre(estudianteDTO.getNombre());
            estudianteExistente.setApellido(estudianteDTO.getApellido());
            estudianteExistente.setEmail(estudianteDTO.getEmail());
            estudianteExistente.setFechaNacimiento(estudianteDTO.getFechaNacimiento());
            estudianteExistente.setNumeroInscripcion(estudianteDTO.getNumeroInscripcion());
            Estudiante estudianteActualizado = estudianteRepository.save(estudianteExistente);
            return convertToDTO(estudianteActualizado);
        }
        return null; 
    }

    @Override
    public EstudianteDTO crearEstudiante(EstudianteDTO estudianteDTO) {
        Estudiante estudiante = convertToEntity(estudianteDTO);
        Estudiante nuevoEstudiante = estudianteRepository.save(estudiante);
        return convertToDTO(nuevoEstudiante);
    }

    @Override
    public boolean eliminarEstudiante(Long id) {
        if (estudianteRepository.existsById(id)) {
            estudianteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Método auxiliar para convertir entidad a DTO
    private EstudianteDTO convertToDTO(Estudiante estudiante) {
        return EstudianteDTO.builder()
                .id(estudiante.getId())
                .nombre(estudiante.getNombre())
                .apellido(estudiante.getApellido())
                .email(estudiante.getEmail())
                .fechaNacimiento(estudiante.getFechaNacimiento())
                .numeroInscripcion(estudiante.getNumeroInscripcion())
                .build();
    }

    // Método auxiliar para convertir DTO a entidad
    private Estudiante convertToEntity(EstudianteDTO estudianteDTO) {
        return Estudiante.builder()
                .id(estudianteDTO.getId())
                .nombre(estudianteDTO.getNombre())
                .apellido(estudianteDTO.getApellido())
                .email(estudianteDTO.getEmail())
                .fechaNacimiento(estudianteDTO.getFechaNacimiento())
                .numeroInscripcion(estudianteDTO.getNumeroInscripcion())
                .build();
    }
}