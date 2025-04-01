package com.universidad.repository; // Define el paquete al que pertenece esta clase

import com.universidad.model.Estudiante; // Importa la clase Estudiante del paquete model
import org.springframework.stereotype.Repository; // Importa la anotación Repository de Spring

import java.time.LocalDate; // Importa la clase LocalDate para manejar fechas
import java.util.ArrayList; // Importa la clase ArrayList para manejar listas
import java.util.List; // Importa la interfaz List para manejar listas
import java.util.Map; // Importa la interfaz Map para manejar mapas
import java.util.Optional; // Importa la clase Optional para manejar valores que pueden ser nulos
import java.util.concurrent.ConcurrentHashMap; // Importa la clase ConcurrentHashMap para manejar mapas concurrentes
import java.util.concurrent.atomic.AtomicLong; // Importa la clase AtomicLong para manejar contadores atómicos

@Repository // Anotación que indica que esta clase es un repositorio de Spring
public class EstudianteRepository {
    private final Map<Long, Estudiante> estudiantes = new ConcurrentHashMap<>(); // Mapa concurrente para almacenar estudiantes con su ID como clave
    private final AtomicLong idContador = new AtomicLong(1); // Contador atómico para generar IDs únicos para los estudiantes

    public Estudiante save(Estudiante estudiante) { // Método para guardar un estudiante en el repositorio
        if (estudiante.getId() == null) { // Si el estudiante no tiene ID
            estudiante.setId(idContador.getAndIncrement()); // Asigna un ID único al estudiante
        }
        estudiantes.put(estudiante.getId(), estudiante); // Agrega el estudiante al mapa
        return estudiante; // Retorna el estudiante guardado
    }

    public Optional<Estudiante> findById(Long id) { // Nuevo método para buscar un estudiante por su ID
        return Optional.ofNullable(estudiantes.get(id)); // Retorna un Optional que puede contener el estudiante o null
    }

    public List<Estudiante> findAll() { // Método para obtener todos los estudiantes
        return new ArrayList<>(estudiantes.values()); // Retorna una lista de todos los estudiantes en el mapa
    }

    public void deleteById(Long id) { // Método para eliminar un estudiante por su ID
        estudiantes.remove(id); // Elimina el estudiante del mapa
    }

    public boolean existsById(Long id) { // Método para verificar si un estudiante existe por su ID
        return estudiantes.containsKey(id); // Retorna true si el mapa contiene una clave con el ID proporcionado
    }

    // Método para inicializar algunos datos de ejemplo
    public void init() {
        Estudiante estudiante1 = Estudiante.builder() // Crea un estudiante usando el patrón builder
                .nombre("Juan") // Asigna el nombre
                .apellido("Pérez") // Asigna el apellido
                .email("juan.perez@example.com") // Asigna el email
                .fechaNacimiento(LocalDate.of(2000, 5, 15)) // Asigna la fecha de nacimiento
                .numeroInscripcion("S001") // Asigna el número de inscripción
                .build(); // Construye el objeto Estudiante

        Estudiante estudiante2 = Estudiante.builder() // Crea otro estudiante usando el patrón builder
                .nombre("María") // Asigna el nombre
                .apellido("González") // Asigna el apellido
                .email("maria.gonzalez@example.com") // Asigna el email
                .fechaNacimiento(LocalDate.of(2001, 8, 22)) // Asigna la fecha de nacimiento
                .numeroInscripcion("S002") // Asigna el número de inscripción
                .build(); // Construye el objeto Estudiante

        save(estudiante1); // Guarda el primer estudiante en el repositorio
        save(estudiante2); // Guarda el segundo estudiante en el repositorio
    }
}