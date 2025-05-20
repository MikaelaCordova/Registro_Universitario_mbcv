package com.universidad.service.impl;

import com.universidad.dto.InscripcionDTO;
import com.universidad.model.Estudiante;
import com.universidad.model.Inscripcion;
import com.universidad.model.Materia;
import com.universidad.repository.EstudianteRepository;
import com.universidad.repository.InscripcionRepository;
import com.universidad.repository.MateriaRepository;
import com.universidad.service.IInscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InscripcionServiceImpl implements IInscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private MateriaRepository materiaRepository;

    private InscripcionDTO mapToDTO(Inscripcion inscripcion) {
        return new InscripcionDTO(
                inscripcion.getId(),
                inscripcion.getEstudiante().getId(),
                inscripcion.getMateria().getId(),
                inscripcion.getFechaInscripcion(),
                inscripcion.getEstado(),
                inscripcion.getCalificacion()
        );
    }

    private Inscripcion mapToEntity(InscripcionDTO inscripcionDTO) {
        Estudiante estudiante = estudianteRepository.findById(inscripcionDTO.getEstudianteId())
                .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado con ID: " + inscripcionDTO.getEstudianteId()));
        Materia materia = materiaRepository.findById(inscripcionDTO.getMateriaId())
                .orElseThrow(() -> new IllegalArgumentException("Materia no encontrada con ID: " + inscripcionDTO.getMateriaId()));
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setId(inscripcionDTO.getId());
        inscripcion.setEstudiante(estudiante);
        inscripcion.setMateria(materia);
        inscripcion.setFechaInscripcion(inscripcionDTO.getFechaInscripcion());
        inscripcion.setEstado(inscripcionDTO.getEstado());
        inscripcion.setCalificacion(inscripcionDTO.getCalificacion());
        return inscripcion;
    }

    @Override
    public List<InscripcionDTO> obtenerTodasLasInscripciones() {
        return inscripcionRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "inscripcion", key = "#id")
    public InscripcionDTO obtenerInscripcionPorId(Long id) {
        return inscripcionRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    @Override
    @CacheEvict(value = {"inscripcionPorEstudiante", "inscripcionPorMateria"}, allEntries = true)
    public InscripcionDTO crearInscripcion(InscripcionDTO inscripcionDTO) {
        if (inscripcionRepository.existsByEstudianteIdAndMateriaId(inscripcionDTO.getEstudianteId(), inscripcionDTO.getMateriaId())) {
            throw new IllegalStateException("El estudiante ya está inscrito en esta materia.");
        }
        Inscripcion inscripcion = mapToEntity(inscripcionDTO);
        Inscripcion savedInscripcion = inscripcionRepository.save(inscripcion);
        return mapToDTO(savedInscripcion);
    }

    @Override
    @CacheEvict(value = {"inscripcion", "inscripcionPorEstudiante", "inscripcionPorMateria"}, key = "#id")
    public InscripcionDTO actualizarInscripcion(Long id, InscripcionDTO inscripcionDTO) {
        return inscripcionRepository.findById(id)
                .map(existingInscripcion -> {
                    Estudiante estudiante = estudianteRepository.findById(inscripcionDTO.getEstudianteId())
                            .orElseThrow(() -> new IllegalArgumentException("Estudiante no encontrado con ID: " + inscripcionDTO.getEstudianteId()));
                    Materia materia = materiaRepository.findById(inscripcionDTO.getMateriaId())
                            .orElseThrow(() -> new IllegalArgumentException("Materia no encontrada con ID: " + inscripcionDTO.getMateriaId()));
                    existingInscripcion.setEstudiante(estudiante);
                    existingInscripcion.setMateria(materia);
                    existingInscripcion.setFechaInscripcion(inscripcionDTO.getFechaInscripcion());
                    existingInscripcion.setEstado(inscripcionDTO.getEstado());
                    existingInscripcion.setCalificacion(inscripcionDTO.getCalificacion());
                    Inscripcion updatedInscripcion = inscripcionRepository.save(existingInscripcion);
                    return mapToDTO(updatedInscripcion);
                })
                .orElse(null);
    }

    @Override
    @CacheEvict(value = {"inscripcion", "inscripcionPorEstudiante", "inscripcionPorMateria"}, key = "#id")
    public void eliminarInscripcion(Long id) {
        inscripcionRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "inscripcionPorEstudiante", key = "#estudianteId")
    public List<InscripcionDTO> obtenerInscripcionesPorEstudiante(Long estudianteId) {
        return inscripcionRepository.findByEstudianteId(estudianteId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "inscripcionPorMateria", key = "#materiaId")
    public List<InscripcionDTO> obtenerInscripcionesPorMateria(Long materiaId) {
        return inscripcionRepository.findByMateriaId(materiaId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}