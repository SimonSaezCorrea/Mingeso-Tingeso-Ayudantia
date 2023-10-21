package tingeso_mingeso.backendcuotasservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tingeso_mingeso.backendcuotasservice.entity.CuotasEntity;
import tingeso_mingeso.backendcuotasservice.model.EstudianteEntity;
import tingeso_mingeso.backendcuotasservice.service.CuotasService;

import java.util.List;

@RestController
@RequestMapping("/cuotas")
public class CuotasController {
    @Autowired
    CuotasService cuotasService;

    @GetMapping("/{rut}/{cuotas}")
    public ResponseEntity<List<CuotasEntity>> cuotas(@PathVariable("rut") String rut, @PathVariable("cuotas") String cuotas){
        EstudianteEntity estudianteEntity = cuotasService.findByRut(rut);
        System.out.println(estudianteEntity);
        if(estudianteEntity != null){
            if(cuotasService.findCuotaByRut(estudianteEntity.getRut()).isEmpty()){
                cuotasService.descuentoArancel_generacionCuotas(estudianteEntity, Integer.parseInt(cuotas));
                List<CuotasEntity> cuotasEntities = cuotasService.findCuotaByRut(estudianteEntity.getRut());
                System.out.println(cuotasEntities);
                return ResponseEntity.ok(cuotasEntities);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{rut}")
    public ResponseEntity<List<CuotasEntity>> listado(@PathVariable("rut") String rut){
        System.out.println("Listar");
        System.out.println("rut: "+rut+"\n");
        EstudianteEntity estudianteEntity = cuotasService.findByRut(rut);
        System.out.println(estudianteEntity);
        if(estudianteEntity != null){
            List<CuotasEntity> cuotasEntities = cuotasService.findCuotaByRut(estudianteEntity.getRut());
            return ResponseEntity.ok(cuotasEntities);
        }
        return ResponseEntity.ok(null);
    }
}
