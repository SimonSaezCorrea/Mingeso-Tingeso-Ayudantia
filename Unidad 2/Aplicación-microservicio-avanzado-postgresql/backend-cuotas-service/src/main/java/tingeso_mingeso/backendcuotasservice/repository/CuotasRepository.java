package tingeso_mingeso.backendcuotasservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tingeso_mingeso.backendcuotasservice.entity.CuotasEntity;

import java.util.List;

@Repository
public interface CuotasRepository extends JpaRepository<CuotasEntity, String> {
    @Query("select e from CuotasEntity e where e.rut = :rut and e.estado=true")
    List<CuotasEntity> findCuotaByRut(@Param("rut") String rut);
}
