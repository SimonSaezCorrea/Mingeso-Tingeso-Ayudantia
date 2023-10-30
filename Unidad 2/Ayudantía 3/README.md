# Ayudantia 3

## Indice

* [Minikube](#minikube)
* [Iniciar Minikube](#iniciar)
* [Minikube con la aplicación](#miniapp)
    * [Dockerfile](#dockerfile)
    * [Docker hub](#dockerhub)
    * [Creación de los archivos](#crear_archivos)
      * [Deployments](#deployments)
      * [Services](#services)
      * [Base de dato](#bd)
* [Configurar base de dato](#configurar_bd)
* [Levantar front y funcione todo](#levantar)
  * [Levantar los servicios y los deployments](#levantar_los_servicios)
  * [Levantar la aplicación](#levantar_la_aplicacion)
* [Comandos utiles](#comandos)
    * [Ver los archivos](#ver_archivos)
    * [Ver los logs](#ver_logs)
* [Cambios a configuraciones de la aplicación](#cambios)
* [Recomendaciones](#recomendaciones)

## Minikube <a name="minikube"></a>

Para descargar [minikube](https://minikube.sigs.k8s.io/docs/start/) solo deben acceder y descargar el .exe y ejecutarlo.

Luego se debe agregar al path y lo pueden hacer mediante:

```
$oldPath = [Environment]::GetEnvironmentVariable('Path', [EnvironmentVariableTarget]::Machine)
if ($oldPath.Split(';') -inotcontains 'C:\minikube'){ `
  [Environment]::SetEnvironmentVariable('Path', $('{0};C:\minikube' -f $oldPath), [EnvironmentVariableTarget]::Machine) `
}
```

## Iniciar Minikube <a name="iniciar"></a>

Comando para ver si está corriendo minikube
```
minikube status
```

Si al hacer **minikube status**, muestra que no lo encontró.
```
🤷  Profile "minikube" not found. Run "minikube profile list" to view all profiles.
👉  To start a cluster, run: "minikube start"
```

Tendrán que ejecutar el siguiente comando

```
minikube start
```

Esto creará el contenedor de minikube, por lo tanto tendrán que tener abierto docker para que esto funcione. **Esto tarda su tiempo**

Al hacer nuevamente **minikube status** les mostrará que está listo
```
D:\Mingeso-Tingeso-Ayudantia\Unidad 2\Aplicación-kubernetes-postgresql> minikube status
minikube
type: Control Plane
host: Running
kubelet: Running
apiserver: Running
kubeconfig: Configured
```



## Minikube con la aplicación <a name="miniapp"></a>

### Dockerfile <a name="dockerfile"></a>

Se necesesitará crear un Dockerfile por servicio (Incluido el del frontend).

Para los servicios de Springboot
```
FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} algo-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/algo-service-0.0.1-SNAPSHOT.jar"]
```

Para el frontend
```
FROM node:20.2.0
WORKDIR /app
COPY package.json .
RUN npm install
COPY . .
EXPOSE 3000
CMD ["npm", "start"]
```

### Docker hub <a name="dockerhub"></a>

Se tendrá que crear una repositorio para cada servicio de la aplicación (Añadiendo a config, eureka, gateway y frontend)

Esto implica que sus servicios deben estar subidos al docker hub, ya que kubernetes sacará las imagenes desde Docker Hub y las utilizará. Así que deben estar atentos a que siempre esté actualizado.

A su misma vez, ya no se usará el archivo **docker-compose.yml**, ya que los encargados de hacer esa tarea se encuentra en los archivos de deployment.

### Creación de los archivos <a name="crear_archivos"></a>

Pueden crear los archivos de deployments y services de un servicio en el mismo archivo, para esto se debe colocar un '---' de separación.

#### Deployments <a name="deployments"></a>

El deployment permite representar la aplicación el el clúster (kubernetes).

En este se debe hacer para cada servicio, incluido el de la base de datos.

Una forma de hacerlo para un servicio, como el de estudiantes:

```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: backend-estudiantes-deployment
  labels:
    app: backend-estudiantes-deployment
spec:
  selector:
    matchLabels:
      app: backend-estudiantes-deployment
  replicas: 1
  template:
    metadata:
      labels:
        app: backend-estudiantes-deployment
    spec:
      containers:
        - name: topeducation-estudiantes-postgres
          image: simonsaez/topeducation-estudiantes-postgres:latest
          ports:
            - containerPort: 8081
          env:
            - name: SPRING_CLOUD_CONFIG_URI
              value: http://backend-config-service:8081
            - name: EUREKA_CLIENT_SERVICEURL_DEFAULTZONE
              value: http://backend-eureka-service:8761
            - name: eureka.instance.preferIpAddress
              value: "true"
            - name: POSTGRES_DB_HOST
              valueFrom:
                configMapKeyRef:
                  name: postgres-config-map
                  key: POSTGRES_DB_HOST
            - name: POSTGRES_DB_ESTUDIANTES
              valueFrom:
                configMapKeyRef:
                  name: postgres-config-map
                  key: POSTGRES_DB_ESTUDIANTES
            - name: POSTGRES_USER
              valueFrom:
                secretKeyRef:
                  name: postgres-credentials
                  key: POSTGRES_USER
            - name: POSTGRES_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: postgres-credentials
                  key: POSTGRES_PASSWORD
```

Lo importante a tener en cuenta es la zona del contenedor, en los "env:". En esa zona se debe declarar las variables de la base de datos, en lo que se refiere al nombre de la base de dato, el usuario y la contraseña, pero además se añaden el url del config y de eureka.

#### Services <a name="services"></a>

El services es una abstracción que define un conjunto lógico de Pods y una política por la cual acceder a ellos.

En este se debe hacer para cada servicio, incluido el de la base de datos.

Una forma de hacerlo para un servicio, como el de estudiantes:

```
apiVersion: v1
kind: Service
metadata:
  name: backend-estudiantes-service
  labels:
    app: backend-estudiantes-service
spec:
  selector:
    app: backend-estudiantes-deployment
  ports:
    - protocol: TCP
      port: 8085
      targetPort: 8085
  type: LoadBalancer
```

Es importante que el nombre del servicio de kubernetes se llame igual al del servicio del microservicio y colocar el puerto correspondiente a este.

#### Base de dato <a name="bd"></a>

En la base de datos se usarán varios tipos de archivos, se usará el Deployment, el Service, el PersistentVolume, el PersistentVolumeClaim, el Secret y el ConfigMap.

En el **ConfigMap** se declaran las variables de la base de datos

```
apiVersion: v1
kind: ConfigMap
metadata:
  name: postgres-config-map
  labels:
    app: postgres-config-map
data:
  POSTGRES_DB_HOST: postgres
  #base de dato Cuotas
  POSTGRES_DB_CUOTAS: topeducationcuotas
  #base de dato Estudiantes
  POSTGRES_DB_ESTUDIANTES: topeducationestudiantes
```

En el **Secret** se declaran las contraseñas que debe estar cifrado en Base64, pueden utilizar la pagina https://www.base64decode.org/es/ para cifrar y decifrar.
Un ejemplo de uso es:
```
apiVersion: v1
kind: Secret
metadata:
  name: postgres-credentials
type: Opaque
data:
  POSTGRES_USER: cG9zdGdyZXM=
  POSTGRES_PASSWORD: U2ltb25fNzg5
```

En el **PersistentVolumeClaim** se escribe:

```
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: postgres-persistent-volume-claim
  labels:
    app: postgres
    tier: database
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 1Gi 
```

En el **PersistentVolume** se escribe:

```
apiVersion: v1
kind: PersistentVolume
metadata:
  name: postgres-persistent-volume-claim
  labels:
      type: local
spec:
  storageClassName: manual
  capacity:
      storage: 1Gi
  accessModes:
      - ReadWriteOnce
  hostPath:
      path: "/Users/nayxx/docker/postgres/docker-pg-vol/data"
```

Ambos nos permite para el almacenamiento de información de la base de dato.

Ahora para finalizar con **Service** y **Deployment**

```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: postgres
  labels:
    app: postgres
    tier: database
spec:
  selector:
    matchLabels:
      app: postgres
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: postgres
        tier: database
    spec:
      containers:
        - name: postgres
          image: postgres:12
          imagePullPolicy: "IfNotPresent"
          ports:
            - containerPort: 5432
              name: postgres
          env:
            - name: POSTGRES_USER
              valueFrom:
                secretKeyRef:
                  name: postgres-credentials
                  key: POSTGRES_USER
            - name: POSTGRES_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: postgres-credentials
                  key: POSTGRES_PASSWORD
            - name: POSTGRES_DB_CUOTAS
              valueFrom:
                configMapKeyRef:
                  name: postgres-config-map
                  key: POSTGRES_DB_CUOTAS
            - name: POSTGRES_DB_ESTUDIANTES
              valueFrom:
                configMapKeyRef:
                  name: postgres-config-map
                  key: POSTGRES_DB_ESTUDIANTES
          volumeMounts:
            - mountPath: /var/lib/postgresql/data
              name: db-data
      volumes:
        - name: db-data
          persistentVolumeClaim:
            claimName: postgres-persistent-volume-claim
---
apiVersion: v1
kind: Service
metadata:
  name: postgres
  labels:
    app: postgres
    tier: database
spec:
  ports:
    - port: 5432
      targetPort: 5432
  selector:
    app: postgres
    tier: database
  clusterIP: None
```

La importancia de este apartado es en el Deployment, debido a que se define la configuración para la gestión de la base de datos en las contraseñas, las base de datos, el usuario.

## Configurar base de dato <a name="configurar_bd"></a>

Para configurar la base de dato, primero que nada hay levantarlo en minikube con los comandos:

```
kubectl apply -f postgres-secrets.yml
kubectl apply -f postgres-configmap.yml
kubectl apply -f postgres-dp-sv-pvc.yml
```
El tercer archivo añade el deployment, service, PersistentVolumeClaim y PersistentVolume allí mismo.

Luego toca configurtar la base de dato agregando las bases de datos de cada servicio, en caso de ser necesario. Para ello hay que acceder mediante el siguiente comando:

```
kubectl exec -it <Name pods postgres> bash
```

En el **Name pods postgres** se puede sacar mediante el **kubectl get pods**


Esto abrirá como una interfaz, alli habrá que acceder a la bd con lo siguiente:

```
psql -h postgres -U postgres
```
Al hacer este comando nos solicitará la contraseña que fue agregada en el archivo Secret.

Ahora solo necesitamos crear la tablas

```
CREATE DATABASE topeducationestudiantes;
```
```
CREATE DATABASE topeducationcuotas;
```

**Es muy importante tener en cuenta que la base de datos no puede tener mayusculas, ya que al crearlas no diferencia mayusculas de minusculas y las genera en minusculas todas las base de datos**

## Levantar front y funcione todo <a name="levantar"></a>

### Levantar los servicios y los deployments <a name="levantar_los_servicios"></a>

Para levantar los servicios de frontend y del backend es mediante los comandos:

```
kubectl apply -f frontend-deployment-service.yml
kubectl apply -f backend-config-deployment-service.yml
```
```
kubectl apply -f backend-eureka-deployment-service.yml
```
```
kubectl apply -f backend-estudiantes-deployment-service.yml
kubectl apply -f backend-cuotas-deployment-service.yml
kubectl apply -f backend-gateway-deployment-service.yml
```

Mi recomendación es levantar primero config y esperar que esté corriendo correctamente (Que diga started), para eso puede verse con el comando **kubectl logs < Nombre del pods > -f**, el -f permite verlo en tiempo real la consola.
Luego de config, levantar eureka.
Luego de eureka, levantar los otros servicios restantes.

Cuando eureka esté levantando, recomiendo dejar abierto en otra consola la consola de eureka y verificar que los servicios se agreguen correctamente al eureka.

El caso del frontend se puede levantar al inicio junto con config.

### Levantar la aplicación <a name="levantar_la_aplicacion"></a>

Habrá que ejecutar los dos comandos que se muestran

```
minikube service frontend-service
```
```
minikube tunnel
```

Ambos comandos se deben ejecutar el consolas distintas, el primero permite levantar el frontend para ser usado y el segunde permite hacer la conexión de los servicios con el frontend

## Comandos utiles <a name="comandos"></a>

### Ver los archivos <a name="ver_archivos"></a>

Para ver los pods

```
kubectl get pods
```

Para ver los services

```
kubectl get services
```
Para ver los deployments

```
kubectl get deployments
```
Para ver los persistentvolumeclaims

```
kubectl get persistentvolumeclaims
```
Para ver los persistentvolume

```
kubectl get persistentvolume
```
Para ver los configmaps

```
kubectl get configmaps
```
Para ver los secrets

```
kubectl get secrets
```
Para ver todo

```
kubectl get all
```

### Ver los logs <a name="ver_logs"></a>

Si queremos ver el log de un service y ver lo que muestra por consola de su ejecución, se puede usar el siguiente comando

```
kubectl logs <Name pods>
```

Es bastante útil para saber si todo va bien

## Cambios a configuraciones de la aplicación <a name="cambios"></a>

Este es un factor importante a tener en cuenta, y es que se tendrá que editar las configuraciones de la aplicación, debido a eureka. Para ello los cambios a hacer son los siguientes:

```
eureka:
  client:
    fetch-registry: true
    register-with-eureka: true
    service-url:
      defaultZone: http://backend-eureka-service:8761/eureka/
```
Esto se le añade a todos los bootstrap.yml de las aplicaciones que dependan de eureka. El nombre **backend-eureka-service** corresponderá al nombre de su servicio de eureka.

En el caso del config-data se modifica la linea de **default-zone**, el contendio ahora será;
```
default-zone: http://backend-eureka-service:8761/eureka
```

Otro cambio importante en las configuraciones de los servicios que usan base de dato, es el uso de las variables definidas en los archivos de minikube. Con esto referirme a que ahora se escribe como:
```
datasource:
    plataform: postgres
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://${POSTGRES_DB_HOST}:5432/${POSTGRES_DB_ESTUDIANTES}?useSSL=false
    username: ${POSTGRES_USER}
    password: ${POSTGRES_PASSWORD}
```

## Recomendaciones <a name="recomendaciones"></a>

Recomiendo encarecidamente crear un documento .txt o de otro formato, pero agregar los comandos a utilizar para agilizar este proceso. Como tambien colocar los comandos para agilizar el proceso de docker. Pueden tomar de ejemplo el **comandos.txt** del proyecto en donde se encuentra los comandos que utilizo o podría utilizar.

Se les recuerda que ya no es necesario hacer pruebas unitarias, el uso de jenkins o el uso de la nube. Pueden utilizarlos, pero ya no será evaluado.