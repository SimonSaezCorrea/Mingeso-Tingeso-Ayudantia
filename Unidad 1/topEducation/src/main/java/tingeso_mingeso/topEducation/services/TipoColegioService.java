package tingeso_mingeso.topEducation.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso_mingeso.topEducation.entities.TipoColegioEntity;
import tingeso_mingeso.topEducation.repositories.TipoColegioRepository;

@Service
public class TipoColegioService {
    @Autowired
    TipoColegioRepository tipoColegioRepository;

    public TipoColegioEntity findByTipo(String tipo){
        TipoColegioEntity tipoColegioEntity = tipoColegioRepository.findByTipo(tipo);
        return tipoColegioEntity;
    }
}
