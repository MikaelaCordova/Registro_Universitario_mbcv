package com.universidad.service;

import com.universidad.dto.MateriaDTO;
import java.util.List;

public interface IMateriaService {
    List<MateriaDTO> obtenerTodasLasMaterias();
    MateriaDTO obtenerMateriaPorId(Long id);
    MateriaDTO obtenerMateriaPorCodigoUnico(String codigoUnico);
    MateriaDTO crearMateria(MateriaDTO materia);
    MateriaDTO actualizarMateria(Long id, MateriaDTO materia);
    void eliminarMateria(Long id);

    MateriaDTO asignarDocente(Long materiaId, Long docenteId);
    MateriaDTO desasignarDocente(Long materiaId, Long docenteId);
}