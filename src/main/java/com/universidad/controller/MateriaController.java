package com.universidad.controller;

import com.universidad.model.Materia;
import com.universidad.service.IMateriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import com.universidad.dto.MateriaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import java.util.List;

@RestController
@RequestMapping("/api/materias")
@Validated
@Tag(name = "Materias", description = "Operaciones para la gestión de materias")
public class MateriaController {

    private final IMateriaService materiaService;
    private static final Logger logger = LoggerFactory.getLogger(MateriaController.class);

    @Autowired
    public MateriaController(IMateriaService materiaService) {
        this.materiaService = materiaService;
    }

    @Operation(summary = "Obtener todas las materias", description = "Retorna la lista completa de materias.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de materias obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = List.class, subTypes = {MateriaDTO.class}))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<MateriaDTO>> obtenerTodasLasMaterias() {
        long inicio = System.currentTimeMillis();
        logger.info("[MATERIA] Inicio obtenerTodasLasMaterias: {}", inicio);
        List<MateriaDTO> result = materiaService.obtenerTodasLasMaterias();
        long fin = System.currentTimeMillis();
        logger.info("[MATERIA] Fin obtenerTodasLasMaterias: {} (Duracion: {} ms)", fin, (fin - inicio));
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Obtener materia por ID", description = "Retorna una materia específica basada en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Materia encontrada",
                    content = @Content(schema = @Schema(implementation = MateriaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Materia no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MateriaDTO> obtenerMateriaPorId(
            @Parameter(name = "id", description = "ID de la materia a buscar", required = true, example = "1", in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH)
            @PathVariable Long id) {
        long inicio = System.currentTimeMillis();
        logger.info("[MATERIA] Inicio obtenerMateriaPorId: {}", inicio);
        MateriaDTO materia = materiaService.obtenerMateriaPorId(id);
        long fin = System.currentTimeMillis();
        logger.info("[MATERIA] Fin obtenerMateriaPorId: {} (Duracion: {} ms)", fin, (fin - inicio));
        if (materia == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(materia);
    }

    @Operation(summary = "Obtener materia por código único", description = "Retorna una materia específica basada en su código único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Materia encontrada",
                    content = @Content(schema = @Schema(implementation = MateriaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Materia no encontrada")
    })
    @GetMapping("/codigo/{codigoUnico}")
    public ResponseEntity<MateriaDTO> obtenerMateriaPorCodigoUnico(
            @Parameter(name = "codigoUnico", description = "Código único de la materia a buscar", required = true, example = "MAT-101", in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH)
            @PathVariable String codigoUnico) {
        MateriaDTO materia = materiaService.obtenerMateriaPorCodigoUnico(codigoUnico);
        if (materia == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(materia);
    }

    @Operation(summary = "Crear una nueva materia", description = "Registra una nueva materia en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Materia creada exitosamente",
                    content = @Content(schema = @Schema(implementation = MateriaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida")
    })
    @PostMapping
    public ResponseEntity<MateriaDTO> crearMateria(
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la materia a crear", required = true,
                    content = @Content(schema = @Schema(implementation = MateriaDTO.class)))
            MateriaDTO materia) {
        MateriaDTO nueva = materiaService.crearMateria(materia);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    @Operation(summary = "Actualizar una materia existente", description = "Actualiza la información de una materia específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Materia actualizada exitosamente",
                    content = @Content(schema = @Schema(implementation = MateriaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida"),
            @ApiResponse(responseCode = "404", description = "Materia no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MateriaDTO> actualizarMateria(
            @Parameter(name = "id", description = "ID de la materia a actualizar", required = true, example = "1", in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH)
            @PathVariable Long id,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos de la materia", required = true,
                    content = @Content(schema = @Schema(implementation = MateriaDTO.class)))
            MateriaDTO materia) {
        MateriaDTO actualizadaDTO = materiaService.actualizarMateria(id, materia);
        return ResponseEntity.ok(actualizadaDTO);
    }

    @Operation(summary = "Eliminar una materia", description = "Elimina una materia específica del sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Materia eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Materia no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMateria(
            @Parameter(name = "id", description = "ID de la materia a eliminar", required = true, example = "1", in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH)
            @PathVariable Long id) {
        materiaService.eliminarMateria(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Verificar si una materia formaría un círculo con un prerequisito", description = "Verifica si la asignación de un prerequisito a una materia causaría una dependencia circular.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado de la verificación",
                    content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "404", description = "Materia no encontrada"),
            @ApiResponse(responseCode = "400", description = "Formaría un círculo")
    })
    @GetMapping("/formaria-circulo/{materiaId}/{prerequisitoId}")
    @Transactional
    public ResponseEntity<Boolean> formariaCirculo(
            @Parameter(name = "materiaId", description = "ID de la materia", required = true, example = "1", in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH)
            @PathVariable Long materiaId,
            @Parameter(name = "prerequisitoId", description = "ID del prerequisito", required = true, example = "2", in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH)
            @PathVariable Long prerequisitoId) {
        MateriaDTO materiaDTO = materiaService.obtenerMateriaPorId(materiaId);
        if (materiaDTO == null) {
            return ResponseEntity.notFound().build();
        }
        Materia materia = new Materia(materiaDTO.getId(), materiaDTO.getNombreMateria(), materiaDTO.getCodigoUnico());
        boolean circulo = materia.formariaCirculo(prerequisitoId);
        if (circulo) {
            return ResponseEntity.badRequest().body(circulo);
        }
        return ResponseEntity.ok(circulo);
    }

    @Operation(summary = "Asignar un docente a una materia", description = "Asigna un docente específico a una materia.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Materia actualizada exitosamente",
                    content = @Content(schema = @Schema(implementation = MateriaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Materia o Docente no encontrado")
    })
    @PostMapping("/{materiaId}/docentes/{docenteId}")
    public ResponseEntity<MateriaDTO> asignarDocente(
            @Parameter(name = "materiaId", description = "ID de la materia", required = true, example = "1", in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH)
            @PathVariable Long materiaId,
            @Parameter(name = "docenteId", description = "ID del docente a asignar", required = true, example = "3", in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH)
            @PathVariable Long docenteId) {
        MateriaDTO materiaActualizada = materiaService.asignarDocente(materiaId, docenteId);
        return ResponseEntity.ok(materiaActualizada);
    }

    @Operation(summary = "Desasignar un docente de una materia", description = "Elimina la asignación de un docente de una materia.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Materia actualizada exitosamente",
                    content = @Content(schema = @Schema(implementation = MateriaDTO.class))),
            @ApiResponse(responseCode = "404", description = "Materia o Docente no encontrado")
    })
    @DeleteMapping("/{materiaId}/docentes/{docenteId}")
    public ResponseEntity<MateriaDTO> desasignarDocente(
            @Parameter(name = "materiaId", description = "ID de la materia", required = true, example = "1", in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH)
            @PathVariable Long materiaId,
            @Parameter(name = "docenteId", description = "ID del docente a desasignar", required = true, example = "3", in = io.swagger.v3.oas.annotations.enums.ParameterIn.PATH)
            @PathVariable Long docenteId) {
        MateriaDTO materiaActualizada = materiaService.desasignarDocente(materiaId, docenteId);
        return ResponseEntity.ok(materiaActualizada);
    }
}