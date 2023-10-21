package tingeso_mingeso.backendestudiantesservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso_mingeso.backendestudiantesservice.entity.TipoColegioEntity;
import tingeso_mingeso.backendestudiantesservice.repository.TipoColegioRepository;

@Service
public class TipoColegioService {
    @Autowired
    TipoColegioRepository tipoColegioRepository;

    public TipoColegioEntity findByTipo(String tipo){
        TipoColegioEntity tipoColegioEntity = tipoColegioRepository.findByTipo(tipo);
        return tipoColegioEntity;
    }
}
