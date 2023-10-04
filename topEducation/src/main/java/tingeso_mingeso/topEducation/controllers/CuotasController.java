package tingeso_mingeso.topEducation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tingeso_mingeso.topEducation.entities.CuotasEntity;
import tingeso_mingeso.topEducation.entities.EstudianteEntity;
import tingeso_mingeso.topEducation.services.AdministracionService;
import tingeso_mingeso.topEducation.services.CuotasService;
import tingeso_mingeso.topEducation.services.EstudianteService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping
public class CuotasController {
    @Autowired
    CuotasService cuotasService;

    @GetMapping("/generar_cuotas")
    public String previa_cuotas(){
        return "generar_cuotas";
    }

    @GetMapping("/generar_cuotas/rut")
    public String cuotas(@RequestParam("rut") String rut, @RequestParam("Cuotas") String Cuotas, Model model){
        EstudianteEntity estudianteEntity = cuotasService.findByRut(rut);
        System.out.println(estudianteEntity);
        if(estudianteEntity != null){
            model.addAttribute("estudiante", estudianteEntity);
            if(cuotasService.findCuotaByRut(estudianteEntity.getRut()).isEmpty()){
                cuotasService.descuentoArancel_generacionCuotas(estudianteEntity, Integer.parseInt(Cuotas));
                List<CuotasEntity> cuotasEntities = cuotasService.findCuotaByRut(estudianteEntity.getRut());
                model.addAttribute("cuotas", cuotasEntities);
                if(cuotasEntities.isEmpty()){
                    model.addAttribute("mensaje_cuotas", "NO SE PUEDE ESA CANTIDAD DE CUOTAS");
                }
            }else{
                model.addAttribute("mensaje_cuotas", "YA TIENE CUOTAS DEFINIDAS");
            }

        }else{
            model.addAttribute("mensaje_rut", "NO EXISTE ESE ESTUDIANTE");
        }
        return "generar_cuotas";
    }

    @GetMapping("/lista_cuotas")
    public String previa_listado(){
        return "lista_cuotas";
    }

    @GetMapping("/lista_cuotas/rut")
    public String listado(@RequestParam("rut") String rut, Model model){
        EstudianteEntity estudianteEntity = cuotasService.findByRut(rut);
        System.out.println(estudianteEntity);
        if(estudianteEntity != null){
            model.addAttribute("estudiante", estudianteEntity);
            List<CuotasEntity> cuotasEntities = cuotasService.findCuotaByRut(estudianteEntity.getRut());
            model.addAttribute("cuotas", cuotasEntities);
            if(cuotasEntities.isEmpty()){

                model.addAttribute("mensaje_cuotas", "NO HAY CUOTAS PARA ESTE ESTUDIANTE");
            }
        }else{
            model.addAttribute("mensaje_rut", "NO EXISTE ESE ESTUDIANTE");
        }
        return "lista_cuotas";
    }
}
