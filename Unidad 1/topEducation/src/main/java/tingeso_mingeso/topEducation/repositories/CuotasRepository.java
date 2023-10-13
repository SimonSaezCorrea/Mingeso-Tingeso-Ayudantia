package tingeso_mingeso.topEducation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tingeso_mingeso.topEducation.entities.CuotasEntity;

import java.util.List;

public interface CuotasRepository extends JpaRepository<CuotasEntity, String> {
    @Query("select e from CuotasEntity e where e.rut = :rut and e.estado=true")
    List<CuotasEntity> findCuotaByRut(@Param("rut") String rut);
}
