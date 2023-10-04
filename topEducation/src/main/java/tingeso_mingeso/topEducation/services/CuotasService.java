package tingeso_mingeso.topEducation.services;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso_mingeso.topEducation.entities.CuotasEntity;
import tingeso_mingeso.topEducation.entities.EstudianteEntity;
import tingeso_mingeso.topEducation.repositories.CuotasRepository;

import java.time.LocalDate;
import java.util.List;


@Service
public class CuotasService {
    @Autowired
    CuotasRepository cuotasRepository;

    @Autowired
    EstudianteService estudianteService;

    @Autowired
    AdministracionService administracionService;

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
        return estudianteService.findByRut(rut);
    }
    public List<CuotasEntity> findCuotaByRut(String rut){
        return cuotasRepository.findCuotaByRut(rut);
    }
}
