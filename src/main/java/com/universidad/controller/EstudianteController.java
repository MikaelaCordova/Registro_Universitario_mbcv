package com.universidad.controller;

import com.universidad.dto.EstudianteDTO;
import com.universidad.model.Materia;
import com.universidad.model.Estudiante;
import com.universidad.service.IEstudianteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
@Validated
@Tag(name = "Estudiantes", description = "Operaciones para la gestión de estudiantes")
public class EstudianteController {

    private final IEstudianteService estudianteService;
    private static final Logger logger = LoggerFactory.getLogger(EstudianteController.class);

    @Autowired
    public EstudianteController(IEstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @Operation(summary = "Obtener todos los estudiantes", description = "Retorna la lista completa de estudiantes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estudiantes obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = List.class, subTypes = {EstudianteDTO.class}))),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE')")
    public ResponseEntity<List<EstudianteDTO>> obtenerTodosLosEstudiantes() {
        long inicio = System.currentTimeMillis();
        logger.info("[ESTUDIANTE] Inicio obtenerTodosLosEstudiantes: {}", inicio);
        List<EstudianteDTO> estudiantes = estudianteService.obtenerTodosLosEstudiantes();
        long fin = System.currentTimeMillis();
        logger.info("[ESTUDIANTE] Fin obtenerTodosLosEstudiantes: {} (Duracion: {} ms)", fin, (fin-inicio));
        return ResponseEntity.ok(estudiantes);
    }

    @Operation(summary = "Obtener estudiante por número de inscripción", description = "Retorna un estudiante específico basado en su número de inscripción.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estudiante encontrado",
                    content = @Content(schema = @Schema(implementation = EstudianteDTO.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
    })
    @GetMapping("/inscripcion/{numeroInscripcion}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE')")
    public ResponseEntity<EstudianteDTO> obtenerEstudiantePorNumeroInscripcion(
            @Parameter(name = "numeroInscripcion", description = "Número de inscripción del estudiante", required = true, example = "INS-123", in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH)
            @PathVariable String numeroInscripcion) {
        long inicio = System.currentTimeMillis();
        logger.info("[ESTUDIANTE] Inicio obtenerEstudiantePorNumeroInscripcion: {}", inicio);
        EstudianteDTO estudiante = estudianteService.obtenerEstudiantePorNumeroInscripcion(numeroInscripcion);
        long fin = System.currentTimeMillis();
        logger.info("[ESTUDIANTE] Fin obtenerEstudiantePorNumeroInscripcion: {} (Duracion: {} ms)", fin, (fin-inicio));
        return ResponseEntity.ok(estudiante);
    }

    @Operation(summary = "Obtener materias de un estudiante", description = "Retorna la lista de materias en las que está inscrito un estudiante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de materias obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = List.class, subTypes = {Materia.class}))),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
    })
    @GetMapping("/{id}/materias")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE')")
    public ResponseEntity<List<Materia>> obtenerMateriasDeEstudiante(
            @Parameter(name = "id", description = "ID del estudiante", required = true, example = "1", in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH)
            @PathVariable("id") Long estudianteId) {
        List<Materia> materias = estudianteService.obtenerMateriasDeEstudiante(estudianteId);
        return ResponseEntity.ok(materias);
    }

    @Operation(summary = "Obtener estudiante con bloqueo (solo para ADMIN)", description = "Retorna un estudiante específico con bloqueo pesimista (uso interno).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estudiante encontrado",
                    content = @Content(schema = @Schema(implementation = Estudiante.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
    })
    @GetMapping("/{id}/lock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Estudiante> getEstudianteConBloqueo(
            @Parameter(name = "id", description = "ID del estudiante", required = true, example = "1", in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH)
            @PathVariable Long id) {
        Estudiante estudiante = estudianteService.obtenerEstudianteConBloqueo(id);
        return ResponseEntity.ok(estudiante);
    }

    @Operation(summary = "Crear un nuevo estudiante (solo para ADMIN)", description = "Registra un nuevo estudiante en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Estudiante creado exitosamente",
                    content = @Content(schema = @Schema(implementation = EstudianteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstudianteDTO> crearEstudiante(
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del estudiante a crear", required = true,
                    content = @Content(schema = @Schema(implementation = EstudianteDTO.class)))
            EstudianteDTO estudianteDTO) {
        EstudianteDTO nuevoEstudiante = estudianteService.crearEstudiante(estudianteDTO);
        return ResponseEntity.status(201).body(nuevoEstudiante);
    }

    @Operation(summary = "Actualizar un estudiante existente (solo para ADMIN)", description = "Actualiza la información de un estudiante específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estudiante actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = EstudianteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
    })
    @PutMapping("/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstudianteDTO> actualizarEstudiante(
            @Parameter(name = "id", description = "ID del estudiante a actualizar", required = true, example = "1", in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH)
            @PathVariable Long id,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos del estudiante", required = true,
                    content = @Content(schema = @Schema(implementation = EstudianteDTO.class)))
            EstudianteDTO estudianteDTO) {
        EstudianteDTO estudianteActualizado = estudianteService.actualizarEstudiante(id, estudianteDTO);
        return ResponseEntity.ok(estudianteActualizado);
    }

    @Operation(summary = "Dar de baja a un estudiante (solo para ADMIN)", description = "Marca a un estudiante como inactivo en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estudiante dado de baja exitosamente",
                    content = @Content(schema = @Schema(implementation = EstudianteDTO.class))),
            @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
    })
    @PutMapping("/{id}/baja")
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstudianteDTO> eliminarEstudiante(
            @Parameter(name = "id", description = "ID del estudiante a dar de baja", required = true, example = "1", in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH)
            @PathVariable Long id,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Motivo de la baja del estudiante", required = true,
                    content = @Content(schema = @Schema(implementation = EstudianteDTO.class)))
            EstudianteDTO estudianteDTO) {
        EstudianteDTO estudianteEliminado = estudianteService.eliminarEstudiante(id, estudianteDTO);
        return ResponseEntity.ok(estudianteEliminado);
    }

    @Operation(summary = "Obtener estudiantes activos", description = "Retorna la lista de estudiantes que están actualmente activos en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de estudiantes activos obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = List.class, subTypes = {EstudianteDTO.class}))),
            @ApiResponse(responseCode = "403", description = "No autorizado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/activos")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCENTE')")
    public ResponseEntity<List<EstudianteDTO>> obtenerEstudianteActivo() {
        List<EstudianteDTO> estudiantesActivos = estudianteService.obtenerEstudianteActivo();
        return ResponseEntity.ok(estudiantesActivos);
    }

}