package com.universidad.service.impl;

import com.universidad.dto.DocenteDTO;
import com.universidad.model.Docente;
import com.universidad.model.Materia;
import com.universidad.repository.DocenteRepository;
import com.universidad.service.IDocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocenteServiceImpl implements IDocenteService {

    @Autowired
    private DocenteRepository docenteRepository;

    private DocenteDTO mapToDTO(Docente docente) {
        if (docente == null) return null;
        return DocenteDTO.builder()
                .id(docente.getId())
                .nombre(docente.getNombre())
                .apellido(docente.getApellido())
                .email(docente.getEmail())
                .fechaNacimiento(docente.getFechaNacimiento())
                .nroEmpleado(docente.getNroEmpleado())
                .departamento(docente.getDepartamento())
                .materiasIds(docente.getMaterias() != null ?
                        docente.getMaterias().stream().map(Materia::getId).collect(Collectors.toList()) : null)
                .build();
    }

    private Docente mapToEntity(DocenteDTO docenteDTO) {
        if (docenteDTO == null) return null;
        Docente docente = Docente.builder()
                .id(docenteDTO.getId())
                .nombre(docenteDTO.getNombre())
                .apellido(docenteDTO.getApellido())
                .email(docenteDTO.getEmail())
                .fechaNacimiento(docenteDTO.getFechaNacimiento())
                .nroEmpleado(docenteDTO.getNroEmpleado())
                .departamento(docenteDTO.getDepartamento())
                // No mapeamos materiasIds aquí, se manejará en métodos específicos
                .build();
        return docente;
    }

    @Override
    @Cacheable(value = "docentes")
    public List<DocenteDTO> obtenerTodosLosDocentes() {
        return docenteRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "docente", key = "#id")
    public DocenteDTO obtenerDocentePorId(Long id) {
        return docenteRepository.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    @Override
    @CachePut(value = "docente", key = "#result.id")
    @CacheEvict(value = "docentes", allEntries = true)
    public DocenteDTO crearDocente(DocenteDTO docenteDTO) {
        Docente docente = mapToEntity(docenteDTO);
        Docente savedDocente = docenteRepository.save(docente);
        return mapToDTO(savedDocente);
    }

    @Override
    @CachePut(value = "docente", key = "#id")
    @CacheEvict(value = "docentes", allEntries = true)
    public DocenteDTO actualizarDocente(Long id, DocenteDTO docenteDTO) {
        return docenteRepository.findById(id)
                .map(existingDocente -> {
                    existingDocente.setNombre(docenteDTO.getNombre());
                    existingDocente.setApellido(docenteDTO.getApellido());
                    existingDocente.setEmail(docenteDTO.getEmail());
                    existingDocente.setFechaNacimiento(docenteDTO.getFechaNacimiento());
                    existingDocente.setNroEmpleado(docenteDTO.getNroEmpleado());
                    existingDocente.setDepartamento(docenteDTO.getDepartamento());
                    Docente updatedDocente = docenteRepository.save(existingDocente);
                    return mapToDTO(updatedDocente);
                })
                .orElse(null);
    }

    @Override
    @CacheEvict(value = {"docente", "docentes"}, key = "#id")
    public void eliminarDocente(Long id) {
        docenteRepository.deleteById(id);
    }
}