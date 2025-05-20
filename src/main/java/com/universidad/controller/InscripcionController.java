package com.universidad.controller;

import com.universidad.dto.InscripcionDTO;
import com.universidad.service.IInscripcionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/inscripciones")
@Tag(name = "Inscripciones", description = "Operaciones para la gestión de inscripciones")
public class InscripcionController {

    @Autowired
    private IInscripcionService inscripcionService;

    @Operation(summary = "Obtener todas las inscripciones", description = "Retorna la lista completa de inscripciones.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de inscripciones obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = List.class, subTypes = {InscripcionDTO.class}))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<InscripcionDTO>> obtenerTodasLasInscripciones() {
        List<InscripcionDTO> inscripciones = inscripcionService.obtenerTodasLasInscripciones();
        return ResponseEntity.ok(inscripciones);
    }

    @Operation(summary = "Obtener inscripción por ID", description = "Retorna una inscripción específica basada en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscripción encontrada",
                    content = @Content(schema = @Schema(implementation = InscripcionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Inscripción no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<InscripcionDTO> obtenerInscripcionPorId(
            @Parameter(name = "id", description = "ID de la inscripción a buscar", required = true, example = "1", in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH)
            @PathVariable Long id) {
        InscripcionDTO inscripcion = inscripcionService.obtenerInscripcionPorId(id);
        if (inscripcion == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(inscripcion);
    }

    @Operation(summary = "Crear una nueva inscripción", description = "Registra una nueva inscripción en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inscripción creada exitosamente",
                    content = @Content(schema = @Schema(implementation = InscripcionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    public ResponseEntity<InscripcionDTO> crearInscripcion(
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la inscripción a crear", required = true,
                    content = @Content(schema = @Schema(implementation = InscripcionDTO.class)))
            InscripcionDTO inscripcionDTO) {
        InscripcionDTO nuevaInscripcion = inscripcionService.crearInscripcion(inscripcionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaInscripcion);
    }

    @Operation(summary = "Actualizar una inscripción existente", description = "Actualiza la información de una inscripción específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Inscripción actualizada exitosamente",
                    content = @Content(schema = @Schema(implementation = InscripcionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "Inscripción no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<InscripcionDTO> actualizarInscripcion(
            @Parameter(name = "id", description = "ID de la inscripción a actualizar", required = true, example = "1", in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH)
            @PathVariable Long id,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos de la inscripción", required = true,
                    content = @Content(schema = @Schema(implementation = InscripcionDTO.class)))
            InscripcionDTO inscripcionDTO) {
        InscripcionDTO inscripcionActualizada = inscripcionService.actualizarInscripcion(id, inscripcionDTO);
        if (inscripcionActualizada == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(inscripcionActualizada);
    }

    @Operation(summary = "Eliminar una inscripción por ID", description = "Elimina una inscripción específica del sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Inscripción eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Inscripción no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarInscripcion(
            @Parameter(name = "id", description = "ID de la inscripción a eliminar", required = true, example = "1", in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH)
            @PathVariable Long id) {
        inscripcionService.eliminarInscripcion(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener inscripciones por ID de estudiante", description = "Retorna la lista de inscripciones para un estudiante específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de inscripciones del estudiante obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = List.class, subTypes = {InscripcionDTO.class}))),
            @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
    })
    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<InscripcionDTO>> obtenerInscripcionesPorEstudiante(
            @Parameter(name = "estudianteId", description = "ID del estudiante", required = true, example = "1", in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH)
            @PathVariable Long estudianteId) {
        List<InscripcionDTO> inscripciones = inscripcionService.obtenerInscripcionesPorEstudiante(estudianteId);
        return ResponseEntity.ok(inscripciones);
    }

    @Operation(summary = "Obtener inscripciones por ID de materia", description = "Retorna la lista de inscripciones para una materia específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de inscripciones de la materia obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = List.class, subTypes = {InscripcionDTO.class}))),
            @ApiResponse(responseCode = "404", description = "Materia no encontrada")
    })
    @GetMapping("/materia/{materiaId}")
    public ResponseEntity<List<InscripcionDTO>> obtenerInscripcionesPorMateria(
            @Parameter(name = "materiaId", description = "ID de la materia", required = true, example = "10", in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH)
            @PathVariable Long materiaId) {
        List<InscripcionDTO> inscripciones = inscripcionService.obtenerInscripcionesPorMateria(materiaId);
        return ResponseEntity.ok(inscripciones);
    }
}