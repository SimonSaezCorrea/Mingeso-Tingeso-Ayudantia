package tingeso_mingeso.topEducation.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tingeso_mingeso.topEducation.entities.EstudianteEntity;
import tingeso_mingeso.topEducation.services.EstudianteService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping
public class EstudianteController {
    @Autowired
    EstudianteService estudianteService;

    @GetMapping("/agregar_estudiante")
    public String estudiante(){
        return "agregar_estudiante";
    }

    @PostMapping("/agregar_estudiante")
    public String newEstudiante(@RequestParam("rut") String rut,
                                @RequestParam("nombres") String nombres,
                                @RequestParam("apellidos") String apellidos,
                                @RequestParam("fecha_nacimiento") String fecha_nacimiento,
                                @RequestParam("tipo_colegio") String tipo_colegio,
                                @RequestParam("nombre_colegio") String nombre_colegio,
                                @RequestParam("anio_egreso") String anio_egreso) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate NewFechaNacimiento = LocalDate.parse(fecha_nacimiento, formato);
        LocalDate NewAnioEgreso = LocalDate.parse(anio_egreso, formato);
        System.out.println(rut + "  " + nombres + "  " + apellidos + "  " + fecha_nacimiento + "  " + tipo_colegio + "  " + nombre_colegio + "  " + anio_egreso);
        int idTipoColegio = estudianteService.findIdByTipo(tipo_colegio);
        LocalDate anio_ingreso = LocalDate.now();
        EstudianteEntity estudianteEntity = new EstudianteEntity(rut, nombres, apellidos, NewFechaNacimiento, idTipoColegio, nombre_colegio, NewAnioEgreso, anio_ingreso);
        estudianteService.save(estudianteEntity);
        return "agregar_estudiante";
    }

    @GetMapping("/lista_estudiante")
    public String listar(Model model) {
        List<EstudianteEntity> estudianteEntities = estudianteService.findAll();
        model.addAttribute("estudiantes", estudianteEntities);
        return "lista_estudiante";
    }
}
