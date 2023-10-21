package tingeso_mingeso.backendcuotasservice.service;

import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Data
@Service
public class AdministracionService {

    private final int matricula = 70000;
    private final int arancel = 1500000;

    private final int contado=50;

    public int descuentoTipoColegio(String tipoColegio){
        if(tipoColegio.equals("Municipal")){
            return 20;
        }
        else if(tipoColegio.equals("Subvencionado")){
            return 10;
        }
        else{
            return 0;
        }
    }
    public int descuentoTipoColegio(int id_tipoColegio){
        if(id_tipoColegio == 1){
            return 20;
        }
        else if(id_tipoColegio == 2){
            return 10;
        }
        else{
            return 0;
        }
    }


    public int descuentoEgresoColegio(LocalDate fechaEgresoColegio, LocalDate fechaIngresoPreu){
        int difAnio = fechaIngresoPreu.getYear() - fechaEgresoColegio.getYear();
        int difDays = fechaIngresoPreu.getDayOfYear() - fechaEgresoColegio.getDayOfYear();
        System.out.println("difAnio = " + difAnio);
        System.out.println("DifDays = " + difDays);
        if(difDays < 0) {
            difAnio--;
        }
        if(difAnio <= 1){
            return 15;
        }
        else if(difAnio <= 2){
            return 8;
        }
        else if(difAnio <= 4){
            return 4;
        }
        else{
            return 0;
        }
    }

    public int descuentePuntajePruebas(int puntaje){
        if(puntaje >= 950 && puntaje <= 1000){
            return 10;
        }
        else if(puntaje >= 900 && puntaje<=949){
            return 5;
        }
        else if(puntaje >= 850 && puntaje<=899){
            return 5;
        }
        else{
            return 0;
        }
    }

    public int interesMesesAtraso(int mesesInteres){
        if(mesesInteres==0){
            return 0;
        } else if (mesesInteres == 1) {
            return 3;
        } else if (mesesInteres == 2) {
            return 6;
        } else if (mesesInteres == 3) {
            return 9;
        }else{
            return 15;
        }
    }

    public boolean PreguntarCuotas(int id_tipoColegio, int cantidadCuotas){
        if(id_tipoColegio==1){
            return cantidadCuotas <= 10;
        } else if (id_tipoColegio==2) {
            return cantidadCuotas <= 7;
        } else{
            return cantidadCuotas <= 4;
        }
    }
}
