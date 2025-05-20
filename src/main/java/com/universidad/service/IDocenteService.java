package com.universidad.service;

import com.universidad.dto.DocenteDTO;
import java.util.List;

public interface IDocenteService {
    List<DocenteDTO> obtenerTodosLosDocentes();
    DocenteDTO obtenerDocentePorId(Long id);
    DocenteDTO crearDocente(DocenteDTO docenteDTO);
    DocenteDTO actualizarDocente(Long id, DocenteDTO docenteDTO);
    void eliminarDocente(Long id);
}