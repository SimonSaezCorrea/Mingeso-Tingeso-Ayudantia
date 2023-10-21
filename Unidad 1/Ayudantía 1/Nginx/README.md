# Nginx

## Indice

* [Modificar Docker-Compose](#modificar-docker-compose)
* [Configurar Nginx](#configurar-nginx)

## Modificar Docker-Compose

Para esto se debe abrir el archivo **docker-compose.yml** y hacer lo siguiente:

* **Agregar al inicio:**
    * En la sección **depends_on** irán todas las réplicas a crear de la aplicación
```
  nginx:
    image: nginx:latest
    container_name: nginx-proxy
    restart: always
    ports:
      - "80:80"
    volumes:
      - ./nginx/conf.d:/etc/nginx/conf.d
    depends_on:
      - topeducation1
      - topeducation2

```
* **Agregar al final**
    * En la primera línea, debe ir el nombre de la réplica declarada en **depends_on**
    * Luego los datos necesarios del contenedor, la imagen, la base de datos
    * En el caso del puerto, se debe colocar **8091:8090** para la primera réplica, para la segunda se coloca **8092:8090**. Así sucevimante para réplicas que se tengan
```
  topeducation1:
    container_name: project-topeducation1
    image: simonsaez/project-topeducation
    ports:
      - "8091:8090"
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

Cuando inicies la aplicación, que es mediante Docker, debes acceder a la ubicación **localhost**. Ya no se debe colocar el puerto.


**IMPORTANTE:** Para el proyecto, se pide 3 réplicas. En caso de que se utilice AWS, se puede usar 2 réplicas para que no haya un cobro. Y en caso de que el Hardware de la computadora no pueda a más de dos, se puede usar una réplica, pero se le tendrá que argumentar al profesor del porque se usó solo una réplica.

## Configurar Nginx

Ahora se debe crear un archivo llamado **\<Nombre aplicación\>.conf**, esta debe estar ubicada de la siguiente manera **¨nginx>conf.d**

Es decir que deben crear una carpeta llama **nginx** en su directorio Raíz, en la carpeta **nginx** crear la carpeta **conf.d** y en la carpeta **conf.d** crear el archivo **\<Nombre aplicación\>.conf**.

El contenido de este archivo es el siguiente:
* En la parte de **upstream** se debe colocar el nombre de la aplicación y en **server** a continuación el nombre de las réplicas y el puerto 8090

```
upstream topeducation {
    server topeducation1:8090;
    server topeducation2:8090;
}

server {
    listen 80;
    charset utf-8;
    access_log off;

    location / {
        proxy_pass http://milkstgo;
        proxy_set_header Host $host:$server_port;
        proxy_set_header X-Forwarded-Host $server_name;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }

    location /static {
        access_log   off;
        expires      30d;

        alias /app/static;
    }
}
```