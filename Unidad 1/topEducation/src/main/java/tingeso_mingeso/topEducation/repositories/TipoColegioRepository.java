package tingeso_mingeso.topEducation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tingeso_mingeso.topEducation.entities.TipoColegioEntity;

public interface TipoColegioRepository extends JpaRepository<TipoColegioEntity, String> {
    @Query("select e from TipoColegioEntity e where e.tipo = :tipo")
    TipoColegioEntity findByTipo(@Param("tipo") String tipo);
}
