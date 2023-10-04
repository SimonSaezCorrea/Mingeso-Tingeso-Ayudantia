package tingeso_mingeso.topEducation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tingeso_mingeso.topEducation.entities.EstudianteEntity;

public interface EstudianteRepository extends JpaRepository<EstudianteEntity, String> {
    @Query("select e from EstudianteEntity e where e.rut = :rut")
    EstudianteEntity findByRut(@Param("rut") String rut);
}
