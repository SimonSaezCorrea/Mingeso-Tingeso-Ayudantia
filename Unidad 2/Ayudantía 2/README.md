# Ayudantia 2

## Indice

* [Microservicio](#microservicio)
* [Dependencias para los servicios](#dependencias)
    * [Config](#dep_config)
    * [Eureka](#dep_eureka)
    * [Gateway](#dep_gateway)
    * [Otros servicios](#dep_otros)
* [Construcción de los servicios](#construccion)
    * [Config](#con_config)
    * [Eureka](#con_eureka)
    * [Gateway](#con_gatewat)
    * [Otros servicios](#con_otros)

* [Carpeta config-data](#config-data)
    * [Eureka](#config_eureka)
    * [Gateway](#config_gateway)
    * [Otros servicios](config_otros)
* [Cosas importates a tener en cuenta](#datos_importantes)

## Microservicio <a name="microservicio"></a>

En comparación de una aplicación en monolitico y microservicio, es que en el monolitico se encuentra todo el proyecto y en el microservicio se divide en partes el proyecto (mini proyectos).

Cada microservicio tiene su base de datos respectiva, la ventaja que se tiene es que cada microservicio puede ser en un lenguaje distinto y una base de dato distinta.

Cada microservicio pueden estar todos en el mismo GitHub o pueden estar en GitHub separados.

**El frontend puede considerarse como un microservicio.**

Conceptos:
* **Config**: Es un servidor de configuraciones, encargado de dar las configuraciones a los servicios necesarios (Eureka, gateway y otros) en la cual las configuraciones se deben encontrar el GitHub.
* **Eureka**: Es un servicio que nos permite localizar los servicios, ya que conoce los puertos de los servicios.
* **Gateway**: Es el servicio que nos permite encaminar a los otros servicios, esto provoca que nos tengamos que conectar mediante el puerto del gateway y el gateway deriva a los servicios.

## Dependencias para los servicios <a name="dependencias"></a>

### Config <a name="dep_config"></a>

* Config Server

### Eureka <a name="dep_eureka"></a>

* Eureka Server
* Config Client
* Spring Cloud Starter Bootstrap

### Gateway <a name="dep_gateway"></a>

* Gateway
* Config Client
* Eureka Discovery Client
* Spring Cloud Starter Bootstrap

### Otros servicios <a name="dep_otros"></a>

* Config Client
* Spring Cloud Starter Bootstrap
* Eureka Discovery Client
* PostgreSQL Driver / MySQL Driver
* Lombok
* Spring Web
* Spring Data JPA

***
**Atentos, "Spring Cloud Starter Bootstrap" no se puede obtener desde Spring Initializr. Por lo tanto se debe colocar manualmente en el POM.**

```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bootstrap</artifactId>
</dependency>
```

En caso de que falte una dependencia, colocarla.
***
## Construcción de los servicios <a name="construccion"></a>

### Config <a name="con_config"></a>

El archivo de **aplication.properties** debe ser renombrado a **aplication.yaml** por comodidad de trabajo. El contenido de este es:

```
server:
  port: 8081
spring:
  application:
    name: backend-config-service
  cloud:
    config:
      server:
        git:
          default-label: master
          uri: https://github.com/SimonSaezCorrea/Mingeso-Tingeso-Ayudantia
          search-paths: "Unidad 2/Aplicación-microservicio-básico/config-data"
```

En este es importante tener en cuentra lo siguiente:
* Debe existir una carpeta en tu repositorio de git llamada **config-data** el que contendrá unos archivos que serán explicados de antemano.
* En las rutas de git, serán:
    * **default-label:** es la branch del git.
    * **uri:** es el url del git.
    * **search-paths:** corresponde a la ubicación de la carpeta config-data.
* Es importante tener en cuenta los puertos para más adelante, recomendable tenerlo en el puerto 8081.

En el archivo de la aplicación, el .java, debe tener la etiqueta **@EnableConfigServer**.

```
@SpringBootApplication
@EnableConfigServer
public class BackendConfigServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(BackendConfigServiceApplication.class, args);
	}
}
```

### Eureka <a name="con_eureka"></a>

El archivo de **aplication.properties** debe ser renombrado a **bootstrap.yaml** por comodidad de trabajo, el llamado de bootstrap le da prioridad de lectura. El contenido de este es:

```
spring:
  application:
    name: backend-eureka-service
  cloud:
    config:
      enabled: true
      uri: http://localhost:8081
```

* En el apartado de uri es importante la parte del puerto debido a que este corresponderá a la del config, por lo mismo se coloca el puerto 8081.

En el archivo de la aplicación, el .java, debe tener la etiqueta **@EnableEurekaServer**

```
@SpringBootApplication
@EnableEurekaServer
public class BackendEurekaServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(BackendEurekaServiceApplication.class, args);
	}
}
```

### Gateway <a name="con_gatewat"></a>

El archivo de **aplication.properties** debe ser renombrado a **bootstrap.yaml** por comodidad de trabajo, el llamado de bootstrap le da prioridad de lectura. El contenido de este es:

```
spring:
  application:
    name: backend-gateway-service
  cloud:
    config:
      enabled: true
      uri: http://localhost:8081
```
* En el apartado de uri es importante la parte del puerto debido a que este corresponderá a la del config, por lo mismo se coloca el puerto 8081.

En el archivo de la aplicación, el .java, debe tener la etiqueta **@EnableEurekaClient**

```
@SpringBootApplication
@EnableEurekaClient
public class BackendGatewayServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(BackendGatewayServiceApplication.class, args);
	}
}
```

### Otros servicios <a name="con_otros"></a>

El archivo de **aplication.properties** debe ser renombrado a **bootstrap.yaml** por comodidad de trabajo, el llamado de bootstrap le da prioridad de lectura. El contenido de este es:

```
spring:
  devtools:
    restart:
      aditional-paths: src/main/java
  application:
    name: backend-otro-service
  jpa:
    database: POSTGRESQL
    show-sql: true
    hibernate:
      ddl-auto: create
  datasource:
    plataform: postgres
    driver-class-name: org.postgresql.Driver
    dbname: topEducationOtro
    url: jdbc:postgresql://localhost:5432/topEducationOtro
    username: postgres
    password: Simon_789
  cloud:
    config:
      enabled: true
      uri: http://localhost:8081
```

* Cosas importantes a tener en cuenta de esta configuración:
    * No se define el puerto
    * Se añade el apartado de cloud
    * Se usa una Base de datos distinta para cada servicio
* **OJO QUE EL EJEMPLO SE USA PARA POSTGRES EN EL JPA Y DATASOURCE, PERO EN EL PUERTO Y EL CLOUD ES IGUAL PARA MYSQL** 
* En el caso de que se use el **ddl-auto** como **create**, pueden colocar un archivo llamado import.sql para poder agregar datos al momento que se inicie el proyecto, ya que al colocarlo como **create** provoca que borre todos los datos de la base de dato.

En el archivo de la aplicación, el .java, debe tener la etiqueta **@EnableEurekaClient**
```
@SpringBootApplication
@EnableEurekaClient
public class BackendOtroServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(BackendOtroServiceApplication.class, args);
	}
}
```

En estos servicios, debe estar organizada como **controller**, **entity**, **repository**, **service** en todos los servicios. Las modificaciones a revisar en cada uno de estos es:
* **controller:** 
    * La etiqueta **@Controller** pasa a **@RestController**
    * En la etiqueta **@RequestMapping** se cambiará a lo siguiente **RequestMapping("/otro")**
    ```
    @RestController
    @RequestMapping("/otro")
    public class OtroController {
    }
    ```
* **entity:** No hay cambios
* **repository:** No hay cambios
* **service:** No hay cambios

En caso de que un servicio necesite de otro servicio por X motivo, se le tendrá que crear las carpetas **config** y **model**.
* En **model** irá la entidad que se quiere traer de la siguiente forma:
```
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BEntity {
    private String id;
    private String nombre;
    private String apellido;
}
```
* En **config** irá:
```
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

Ahora bien, para poder utilizarlo en tu servicio es de la siguiente forma:

* Se debe definir al inicio del servicio

```
@Autowired
    RestTemplate restTemplate;
```

* En el método que se hace el llamado a otro servicio contendrá:

    ```
    public BEntity findById(Integer id){
            ResponseEntity<BEntity> response = restTemplate.exchange(
                    "http://localhost:8080/B/"+id,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<BEntity>() {}
            );
            return response.getBody();
        }
    ```

## Carpeta config-data <a name="config-data"></a>

Aquí va la configuración de los puertos y eureka de cada servicio (Menos el de config)

**SE RECOMIENDA NO USAR PUERTOS ALEATORIOS, PORQUE VA A SER MÁS CÓMODO DE TRABAJAR CON KUBERNETES (MINIKUBE)**

### Eureka <a name="config_eureka"></a>

```
server:
  port: 8761
eureka:
  client:
    fetch-registry: false
    register-with-eureka: false
    service-url:
      default-zone: http://${eureka.instance.hostname}:${server.port}/eureka/

instance:
  prefer-ip-address: true
```
* El puerto de eureka se usará 8761

### Gateway <a name="config_gateway"></a>

```
server:
  port: 8080
  
eureka:
  client:
    fetch-registry: true
    register-with-eureka: true
    service-url:
      default-zone: http://${EUREKA:localhost}:8761/eureka
  instance:
    hostname: localhost

spring:
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
      default-filters:
        - DedupeResponseHeader=Access-Control-Allow-Origin Access-Control-Allow-Credentials, RETAIN_UNIQUE
      globalcors:
        cors-configurations:
          '[/**]':
            allowed-origin-patterns: '*'
            allowed-methods: "*"
            allowed-headers: "*"
            allow-credentials: true
      routes:
        - id: backend-otro-service
          uri: lb://backend-otro-service
          predicates:
            - Path=/otro/**
        - id: backend-B-service
          uri: lb://backend-B-service
          predicates:
            - Path=/B/**
```
* Se usará el puerto 8080 para el gateway, esto significa que para comunicarte con la aplicación se usará el puerto 8080.
    * Ejemplo, si quiero leer todos los datos del servicio otro, tengo que colocar **localhost:8080/otro**
* Otro punto importante es que dará problema de CORS en el frontend, para ello se colocó el globalcors

### Otros servicios <a name="config_otros"></a>

```
server:
  port: 8082
eureka:
  client:
    fetch-registry: true
    register-with-eureka: true
    service-url:
      default-zone: http://${EUREKA:localhost}:8761/eureka
  instance:
    instance-id: ${spring.application.name}:${spring.application.instance_id:${random.value}}

management:
  endpoints:
    web:
      exposure:
        include: "*"
```

* Un punto importante es que los puertos 8080, 8081 y 8761 están reservados por otas aplicaciones de tu servicio, así que habrá que comenzar desde el puerto 8082.

## Cosas importates a tener en cuenta <a name="datos_importantes"></a>

1. Hay que destacar que el config-data siempre tiene que estar subido al git y en casos de cambios, habrá que subir los cambios. Así el config-service podrá efectuar los cambios (Ya que lee el git).
2. El orden de inicio de las aplicaciones es config-service > eureka-service > Otros Servicios > gateway-service.
3. Al levantar config-service y luego eureka-service, dice que fue levantado en el puerto 8080. esto significa que hubo un problema en la sincronización de config-service con el config-data, esto puede ser debido a que hay un error en el config-data (en el archivo de eureka) o en el config-service (problema a la hora de acceder a la carpeta).
4. Usar la versión de Maven 2.5.4, ya que es por el uso de ciertos metodos que están en esa versión. En caso de querer usar una versión más actualizada, ciertos métodos se cambiarán.
