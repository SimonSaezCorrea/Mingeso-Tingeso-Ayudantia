package tingeso_mingeso.backendestudiantesservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso_mingeso.backendestudiantesservice.entity.EstudianteEntity;
import tingeso_mingeso.backendestudiantesservice.entity.TipoColegioEntity;
import tingeso_mingeso.backendestudiantesservice.repository.EstudianteRepository;

import java.util.List;

@Service
public class EstudianteService {
    @Autowired
    EstudianteRepository estudianteRepository;

    @Autowired
    TipoColegioService tipoColegioService;

    public int findIdByTipo(String tipo){
       TipoColegioEntity tipoColegioEntity = tipoColegioService.findByTipo(tipo);
       return tipoColegioEntity.getId();
    }

    public void save(EstudianteEntity estudianteEntity){
        estudianteRepository.save(estudianteEntity);
    }

    public List<EstudianteEntity> findAll(){
        return estudianteRepository.findAll();
    }

    public EstudianteEntity findByRut(String rut){
        return estudianteRepository.findByRut(rut);
    }

}
