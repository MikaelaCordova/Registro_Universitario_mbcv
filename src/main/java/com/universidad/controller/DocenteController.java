package com.universidad.controller;

import com.universidad.dto.DocenteDTO;
import com.universidad.service.IDocenteService;
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
@RequestMapping("/api/docentes")
@Tag(name = "Docentes", description = "Operaciones para la gestión de docentes")
public class DocenteController {

    @Autowired
    private IDocenteService docenteService;

    @Operation(summary = "Obtener todos los docentes", description = "Retorna la lista completa de docentes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de docentes obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = List.class, subTypes = {DocenteDTO.class}))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<DocenteDTO>> obtenerTodosLosDocentes() {
        List<DocenteDTO> docentes = docenteService.obtenerTodosLosDocentes();
        return ResponseEntity.ok(docentes);
    }

    @Operation(summary = "Obtener docente por ID", description = "Retorna un docente específico basado en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Docente encontrado",
                    content = @Content(schema = @Schema(implementation = DocenteDTO.class))),
            @ApiResponse(responseCode = "404", description = "Docente no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<DocenteDTO> obtenerDocentePorId(
            @Parameter(name = "id", description = "ID del docente a buscar", required = true, example = "1", in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH)
            @PathVariable Long id) {
        DocenteDTO docente = docenteService.obtenerDocentePorId(id);
        if (docente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(docente);
    }

    @Operation(summary = "Crear un nuevo docente", description = "Registra un nuevo docente en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Docente creado exitosamente",
                    content = @Content(schema = @Schema(implementation = DocenteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    public ResponseEntity<DocenteDTO> crearDocente(
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del docente a crear", required = true,
                    content = @Content(schema = @Schema(implementation = DocenteDTO.class)))
            DocenteDTO docenteDTO) {
        DocenteDTO nuevoDocente = docenteService.crearDocente(docenteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoDocente);
    }

    @Operation(summary = "Actualizar un docente existente", description = "Actualiza la información de un docente específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Docente actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = DocenteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "Docente no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DocenteDTO> actualizarDocente(
            @Parameter(name = "id", description = "ID del docente a actualizar", required = true, example = "1", in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH)
            @PathVariable Long id,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos del docente", required = true,
                    content = @Content(schema = @Schema(implementation = DocenteDTO.class)))
            DocenteDTO docenteDTO) {
        DocenteDTO docenteActualizado = docenteService.actualizarDocente(id, docenteDTO);
        if (docenteActualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(docenteActualizado);
    }

    @Operation(summary = "Dar de baja a un docente", description = "Elimina un docente del sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Docente eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Docente no encontrado")
    })
    @DeleteMapping("/{id}/baja")
    public ResponseEntity<Void> eliminarDocente(
            @Parameter(name = "id", description = "ID del docente a eliminar", required = true, example = "1", in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH)
            @PathVariable Long id) {
        docenteService.eliminarDocente(id);
        return ResponseEntity.noContent().build();
    }
}