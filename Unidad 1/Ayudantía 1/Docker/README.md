# Docker

## Indice

* [Instalación](#instalación-docker-desktop)
* [Docker Hub](#docker-hub)
* [Inicio de Sesión en Docker Desktop](#inicio-de-sesión-en-docker-desktop)
* [Volumenes](#volumenes)
    * [Crear Volumen](#crear-volumen)
    * [Ver Volumenes](#ver-volumenes)
    * [Limpiar](#limpiar-volumenes)
* [Imagenes](#imagenes)
    * [Dockerfile](#dockerfile)
    * [Creación de la imágen](#creación-de-la-imágen)
    * [Subir la Imágen](#subir-la-imágen)
    * [Ver Imágenes creadas](#ver-imágenes-creadas)
    * [Eliminar Imágen](#eliminar-imágen)
* [Contenedores](#contenedores)
    * [Levantar Contenedores](#levantar-contenedores)
    * [Terminar uso de contenedores](#terminar-uso-de-contenedores)
* [Observaciones](#observaciones)

## Instalación Docker Desktop

Descargar [Docker Desktop](https://www.docker.com/products/docker-desktop/)

* **Observación:** Docker se iniciará siempre que se prenda la PC, por lo que se recomienda dejar desactivado el inicio automatico.
* Si al abrir Docker Desktop, aparece un mensaje que dice algo del estilo **"WSL 2 update requiered"** debemos descargar e instalar la siguiente actualización para [WSL](https://learn.microsoft.com/es-mx/windows/wsl/install-manual#step-4---download-the-linux-kernel-update-package) (el cual se instaló automáticamente al instalar Docker Desktop)

## Docker Hub

Para poder hacer uso de Docker, se necesitará tener una cuenta en [Docker Hub](https://hub.docker.com/) y ahí generar un repositorio con un nombre, idealmente el mismo que el de la aplicación.

## Inicio de Sesión en Docker Desktop

Una vez creado el archivo de Dockerfile, se debe iniciar sesión con la cuenta de Docker Hub en Docker Desktop. Para ello existen dos formas:
* Mediante el software de Docker Desktop, en la parte superior
* Mediante consola, con el comando
    ```
    docker login
    ```

## Volumenes

Los volumenes son para mantener y guardar la información de los contenedores, en particular el de las base de datos, para así no pederlos al eliminar los contenedores.

### Crear Volumen
Para PostgreSQL
```
docker volume create postgres-db-data
```

Para MySQL
```
docker volume create mysql-db-data
```

El nombre dado puede ser otro, no necesariamente debe ser el que se describió ahí.
### Ver Volumenes

```
docker volume ls
```

### Limpiar Volumenes
Borra todos los volumenes existentes.
```
docker volume prune
```

Aunque no es la única forma de eliminación, tambien se puede hacer mediante el software de Docker Desktop.

## Imagenes

 Una **Imagen** son como moldes o plantillas para crear contenedores que contiene todo lo necesario para que una aplicación funcione correctamente, como sus archivos, bibliotecas y configuraciones.

 Para ello de antemano se tiene que generar la carpeta tarjet de la aplicación, esto se puede hacer de dos formas:
 * Yendo al panel lateral, hacer click en donde dice "Maven". Apretar la opción que dice "lifecycle". De ellos elegir las opciones "clena" e "install", en ese orden. Cabe destacar que la aplicación debe funcionar correctamente.
 * En consola colocar (debe estar en la raiz del proyecto)
    ```
    mvn clean install
    ```

### Dockerfile

En los archivos generados al hacer "install" de la aplicación y generar la carpeta **target**, se ubicará el un archivo **.jar**. Este nombre es importante para lo siguiente.

En la raiz del proyecto se creará un archivo llamado "Dockerfile", no tiene extension como por ejemplo ".txt", va vacío. Su contenido es el siguiente:

```
FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} topEducation-0.0.1-SNAPSHOT.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "/topEducation-0.0.1-SNAPSHOT.jar"]
```

### Creación de la Imágen

```
docker build -t <Nombre Usuario>/<Nombre Repositorio> .
```

* El **Nombre Usuario** corresponde al nombre que se tiene en Docker Hub
* El **Nombre Repositorio** corresponde al nombre del repositorio que se creo el Docker Hub
* Importante el '.' final que hay, tiene que estar

### Subir la Imágen

```
docker push <Nombre Usuario>/<Nombre Repositorio>
```
* El **Nombre Usuario** corresponde al nombre que se tiene en Docker Hub
* El **Nombre Repositorio** corresponde al nombre del repositorio que se creo el Docker Hub
* Aquí es importante tener la sesión iniciada, si no tirará problemas

### Ver Imágenes creadas

```
docker image ls
```


### Eliminar Imágen

```
docker rmi <Nombre Imagen>
```

Cabe destacar que el nombre de la imágen creado (en este contexto) sería < Nombre Usuario >/< Nombre Repositorio >

Aunque no es la única forma de eliminación, tambien se puede hacer mediante el software de Docker Desktop.

## Contenedores

Los contenedores son como cajas mágicas que contienen aplicaciones y todo lo que necesitan para funcionar, como un juego de construcción.

Para ello, de antemano se debe tener el siguiente archivo nombrado como "docker-compose.yml" en la carpeta raíz

* Para MySQL
    ```
    version: "3.8"
    services:
    mysql-db:
        image: mysql
        restart: always
        volumes:
        - ./data/db:/var/lib/mysql
        environment:
        MYSQL_ROOT_PASSWORD: Simon_789
        MYSQL_DATABASE: topEducation
        ports:
        - 33060:3306
    app:
        container_name: project-topeducation
        image: simonsaez/project-topeducation
        ports:
        - "8090:8090"
        environment:
        - SPRING_DATASOURCE_URL=jdbc:mysql://mysql-db:3306/topEducation?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false
        - SPRING_DATASOURCE_USERNAME=root
        - SPRING_DATASOURCE_PASSWORD=Simon_789
        deploy:
        restart_policy:
            condition: on-failure
        depends_on:
        - mysql-db
    ```
* Para PostgreSQL
    ```
    version: "3.8"
    services:
    postgres-db:
        image: postgres:latest
        restart: always
        volumes:
        - ./data/db:/var/lib/postgresql
        environment:
        POSTGRES_DB: topEducation
        POSTGRES_PASSWORD: Simon_789
        POSTGRES_USER: postgres
        ports:
        - "5432:5432"
    topeducation:
        container_name: project-topeducation
        image: simonsaez/project-topeducation
        ports:
        - "8090:8090"
        environment:
        - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-db:5432/topEducation
        - SPRING_DATASOURCE_USERNAME=postgres
        - SPRING_DATASOURCE_PASSWORD=Simon_789
        - SPRING_JPA_HIBERNATE_DDL_AUTO=create
        deploy:
        restart_policy:
            condition: on-failure
        depends_on:
        - postgres-db
    ```

### Levantar Contenedores

Con esto, ahora solo debemos levantar los contenedores de la siguiente forma:

```
docker-compose up
```

### Terminar uso de contenedores

Para dejar de usar los contenedores, osea para apagarlos, hay que apretar **CTRL+C**.

Para eliminar los contenedores hay que hacer:

```
docker-compose down
```

Aunque no es la única forma de terminar, tambien se puede hacer mediante el software de Docker Desktop.

## Observaciones

* En caso de existir problemas con docker por X motivo, se puede ejecutar el siguiente comando para borrar todo de docker

    ```
    docker system prune -a
    ```
* Importante saber que al hacer **docker-compose up** este comando usa el archivo **docker-compose.yml**, que en su interior la imagen la saca de Docker Hub, osea la imagen de Docker Hub debe estar actualizada. Esto nos implica que cada cambio que hagamos debemos hacer lo siguiente:
    1. Eliminar los contenedores
    2. Eliminar las imágenes
    3. Build de las imágen
    4. Push de las imágen
    5. Hacer docker-compose up