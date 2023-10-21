package tingeso_mingeso.backendcuotasservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tingeso_mingeso.backendcuotasservice.entity.CuotasEntity;
import tingeso_mingeso.backendcuotasservice.model.EstudianteEntity;
import tingeso_mingeso.backendcuotasservice.repository.CuotasRepository;

import java.util.List;


@Service
public class CuotasService {
    @Autowired
    CuotasRepository cuotasRepository;

    @Autowired
    AdministracionService administracionService;

    @Autowired
    RestTemplate restTemplate;

    public void descuentoArancel_generacionCuotas(EstudianteEntity estudianteEntity, int cantidadCuotas){
        if(administracionService.PreguntarCuotas(estudianteEntity.getTipo_colegio(), cantidadCuotas)){
            int descuentoTipoColegio = administracionService.descuentoTipoColegio(estudianteEntity.getTipo_colegio());
            int descuentoAnio = administracionService.descuentoEgresoColegio(estudianteEntity.getAnio_egreso(), estudianteEntity.getAnio_ingreso());
            int descuento_total = descuentoTipoColegio + descuentoAnio;
            float descuento_decimal = (float) descuento_total/100;
            int valorArancel = (int) (administracionService.getArancel() * (1-descuento_decimal));
            float cuota = (float) valorArancel/cantidadCuotas;
            int i = 1;
            CuotasEntity cuotasEntity;
            while (i <= cantidadCuotas){
                cuotasEntity = new CuotasEntity();
                cuotasEntity.setNumeroCuota(i);
                cuotasEntity.setValorCuota(cuota);
                cuotasEntity.setRut(estudianteEntity.getRut());
                cuotasEntity.setEstado(true);
                cuotasRepository.save(cuotasEntity);
                i++;
            }
        }
    }

    public EstudianteEntity findByRut(String rut){
        System.out.println("rut: "+rut);
        ResponseEntity<EstudianteEntity> response = restTemplate.exchange(
                "http://localhost:8080/estudiante/"+rut,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<EstudianteEntity>() {}
        );
        return response.getBody();
    }
    public List<CuotasEntity> findCuotaByRut(String rut){
        return cuotasRepository.findCuotaByRut(rut);
    }
}
