# Ayudantia 2

## Microservicio

En comparación de una aplicación en monolitico y microservicio, es que en el monolitico se encuentra todo el proyecto y en el microservicio se divide en partes el proyecto (mini proyectos).

Cada microservicio tiene su base de datos respectiva, la ventaja que se tiene es que cada microservicio puede ser en un lenguaje distinto y una base de dato distinta.

**El frontend puede considerarse como un microservicio.**


## Código de microservicio básico

### Cosas a tener en cuenta

Solo debes tener el servicio, la entidad, el controlador y el repositorio de lo que se usará en el microservicio.

Por ejemplo, si hay un microservicio para Estudiante, en él no debe existir el servicio, el controlador, el repositorio, ni la entidad de Cuotas.

Una excepción sería en caso de que dos entidades trabajen en el mismo microservicio por planificación previa, como por ejemplo TipoColegio y Estudiante

### Cambios en el controlador

El controlador ahora debe cambiar su anotación de **@Controller** a **@RestController**.

En su anotación de **@RequestMapping** se debe modificar a lo siguiente **@RequestMapping("/< Nombre Servicio >")**. 
* Por ejemplo: @RequestMapping("/estudiante")

Esto implica que todas las rutas iniciarán por el descrito allí
* Por ejemplo: "/estudiante/listar/".

En caso de necesitar un dato, a la ruta se le añadirá **/{< variable >}**. 
* Por ejemplo: "/estudiante/{rut}"

