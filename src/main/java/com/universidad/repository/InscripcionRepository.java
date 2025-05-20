package com.universidad.repository;

import com.universidad.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    /**
     * Busca todas las inscripciones asociadas a un ID de estudiante específico.
     *
     * @param estudianteId El ID del estudiante.
     * @return Una lista de inscripciones para el estudiante dado.
     */
    List<Inscripcion> findByEstudianteId(Long estudianteId);

    /**
     * Busca todas las inscripciones asociadas a un ID de materia específico.
     *
     * @param materiaId El ID de la materia.
     * @return Una lista de inscripciones para la materia dada.
     */
    List<Inscripcion> findByMateriaId(Long materiaId);

    /**
     * Verifica si existe una inscripción para un estudiante y una materia específicos.
     *
     * @param estudianteId El ID del estudiante.
     * @param materiaId    El ID de la materia.
     * @return `true` si existe una inscripción, `false` en caso contrario.
     */
    boolean existsByEstudianteIdAndMateriaId(Long estudianteId, Long materiaId);

}