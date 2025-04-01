package com.universidad.controller; // Define el paquete al que pertenece esta clase

import com.universidad.dto.EstudianteDTO; // Importa la clase EstudianteDTO del paquete dto
import com.universidad.service.IEstudianteService; // Importa la interfaz IEstudianteService del paquete service

import org.springframework.beans.factory.annotation.Autowired; // Importa la anotación Autowired de Spring
import org.springframework.http.HttpStatus; // Importa la enumeración HttpStatus de Spring
import org.springframework.http.ResponseEntity; // Importa la clase ResponseEntity de Spring para manejar respuestas HTTP
import org.springframework.web.bind.annotation.*; // Importa las anotaciones de Spring para controladores web
import org.springframework.web.servlet.support.ServletUriComponentsBuilder; // Importa la clase ServletUriComponentsBuilder para construir URIs

import java.net.URI; // Importa la clase URI para trabajar con URIs
import java.util.List; // Importa la interfaz List para manejar listas

@RestController // Anotación que indica que esta clase es un controlador REST de Spring
@RequestMapping("/api") // Define la ruta base para las solicitudes HTTP a este controlador
public class EstudianteController { // Define la clase EstudianteController

    private final IEstudianteService estudianteService; // Declara una variable final para el servicio de estudiantes

    @Autowired // Anotación que indica que el constructor debe ser usado para inyección de dependencias
    public EstudianteController(IEstudianteService estudianteService) { // Constructor que recibe el servicio de estudiantes
        this.estudianteService = estudianteService; // Asigna el servicio de estudiantes a la variable de instancia
    }

    @GetMapping("/estudiantes") // Anotación que indica que este método maneja solicitudes GET a /api/estudiantes
    public ResponseEntity<List<EstudianteDTO>> obtenerTodosLosEstudiantes() { // Método para obtener una lista de todos los EstudianteDTO
        List<EstudianteDTO> estudiantes = estudianteService.obtenerTodosLosEstudiantes(); // Llama al servicio para obtener todos los estudiantes
        return ResponseEntity.ok(estudiantes); // Retorna una respuesta HTTP 200 OK con la lista de estudiantes
    }

    @GetMapping("/estudiantes/{id}") // Anotación que indica que este método maneja solicitudes GET a /api/estudiantes/{id}
    public ResponseEntity<EstudianteDTO> obtenerEstudiantePorId(@PathVariable Long id) { // Método para obtener un estudiante por su ID
        EstudianteDTO estudianteDTO = estudianteService.obtenerEstudiantePorId(id); // Llama al servicio para obtener el estudiante por ID
        if (estudianteDTO != null) { // Si el estudiante existe
            return ResponseEntity.ok(estudianteDTO); // Retorna una respuesta HTTP 200 OK con el estudiante
        } else { // Si el estudiante no existe
            return ResponseEntity.notFound().build(); // Retorna una respuesta HTTP 404 Not Found
        }
    }

    @PutMapping("/estudiantes/{id}/actualizar") // Anotación que indica que este método maneja solicitudes PUT a /api/estudiantes/{id}
    public ResponseEntity<EstudianteDTO> actualizarEstudiante(@PathVariable Long id, @RequestBody EstudianteDTO estudianteDTO) { // Método para actualizar un estudiante
        EstudianteDTO estudianteActualizado = estudianteService.actualizarEstudiante(id, estudianteDTO); // Llama al servicio para actualizar el estudiante
        if (estudianteActualizado != null) { // Si el estudiante fue actualizado exitosamente
            return ResponseEntity.ok(estudianteActualizado); // Retorna una respuesta HTTP 200 OK con el estudiante actualizado
        } else { // Si el estudiante con el ID proporcionado no existe
            return ResponseEntity.notFound().build(); // Retorna una respuesta HTTP 404 Not Found
        }
    }

    @PostMapping("/estudiantes/crear") // Anotación que indica que este método maneja solicitudes POST a /api/estudiantes
    public ResponseEntity<EstudianteDTO> crearEstudiante(@RequestBody EstudianteDTO estudianteDTO) { // Método para crear un nuevo estudiante
        EstudianteDTO nuevoEstudiante = estudianteService.crearEstudiante(estudianteDTO); // Llama al servicio para crear el estudiante
        URI location = ServletUriComponentsBuilder.fromCurrentRequest() // Construye la URI del nuevo recurso creado
                .path("/{id}")
                .buildAndExpand(nuevoEstudiante.getId())
                .toUri();
        return ResponseEntity.created(location).body(nuevoEstudiante); // Retorna una respuesta HTTP 201 Created con la URI y el estudiante creado
    }

    @DeleteMapping("/estudiantes/{id}/eliminar") // Anotación que indica que este método maneja solicitudes DELETE a /api/estudiantes/{id}
    public ResponseEntity<Void> eliminarEstudiante(@PathVariable Long id) { // Método para eliminar un estudiante por su ID
        boolean eliminado = estudianteService.eliminarEstudiante(id); // Llama al servicio para eliminar el estudiante
        if (eliminado) { // Si el estudiante fue eliminado exitosamente
            return ResponseEntity.noContent().build(); // Retorna una respuesta HTTP 204 No Content
        } else { // Si el estudiante con el ID proporcionado no existe
            return ResponseEntity.notFound().build(); // Retorna una respuesta HTTP 404 Not Found
        }
    }
}