# Ayudantia 1

## Jenkins

### Previa a instalar

Verificar tener agregado al path el Git\usr\bin

En caso de no estar, buscar **Editar las variables de entorno del sistema**. Allí dirijirse a **Variables de entorno...** y en la sección de **Variables del sistema** buscar el que diga **Path**, le dan a **Editar...** y para finalizar le dan a **Nuevo** o **Examinar...**.

La ubicación, por lo general, es **C:\Program Files\Git\usr\bin**, pero también puede estar en el otro disco en caso de haber especificado el lugar, como por ejemplo: **D:\Git\usr\bin**.

### Instalar

El archivo tiene que ser un **.war**

[Link Jenkins](https://www.jenkins.io/download/)


### Ejecutar

Para ejecutar Jenkins, se debe usar el comando (en la ubicación del archivo):

```
java -jar jenkins.war
```

Cuando se inicie por primera vez, les dará una contraseña la cual se usará para acceder a la cuenta. Se deberá seleccionar **Instalar plugins sugerido**. Luego deben crearse una cuenta con usuario y contraseña.

### Configurar

Tendran que ir a la siguiente ubicacion en Jenkins **Administrar Jenkins > Global Tool Configuration/Tools**, bajarán hasta encontrar donde dice **Maven**. Harán click en **Instalación de Maven** y colocarán:

* **Nombre:** maven
* **Versión:** La más reciente (3.9.4)

### Hacer una nueva Tarea

Deben apretar el boton **+ Nueva Tarea**.

En la primera ventana: 
* **Enter an item name:** Colocar un nombre, por ejemplo pipeline-topEducation
* **Seleccionar el tipo de proyecto:** Pipeline
* Apretar **OK**

En la segunda ventana:
* **Descripción:** No es obligatoria
    * **Seleccionar la opción**: GitHub project
* **BuildTriggers:** 
    * **Seleccionar la opcion:** GitHub hook trigger for GITScm polling
* **Pipeline:**
    * **Definition:** Pipeline script

Se necesitará ir a **Sintaxis de Pipeline**, su ubicación está más abajo donde dice **Pipeline Syntax**. Se abre en una nueva pestaña.

* Buscar la opción **checkout: Check out from version control**
    * **SCM:** Git
        * **Repositories:**
            * **Repository URL:** \<Colocar la URL de Git del proyecto\>
            * Se recomienda tener el Git publico para saltarse el paso de Credentials
        * **Brances to build:**
            * **Branch Specifier:** \<Colocar la Branch del repositorio\>
    * **Generate Pipeline Script**
* Buscar la opción **WithCredentials: Bind credentials to variable**
    * **Bindings:** Añadir Secret Text
        * **Secret Text:**
            * **Variable:** \<Nombre de la variable\>
            * **Credentials:** Colocar en +Add
                * **Domain:** Global credentials
                * **Kind:** Secret Text
                    * **Scope:** Global
                    * **Password:** \<Contraseña de Docker\>
                    * **ID:** dckrhubpassword
    * **Generate Pipeline Script**

**Script**
```
pipeline{
    agent any
    tools{
        maven "maven"
    }
    stages{
        stage("Build JAR FILE"){
            steps{
                checkout scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/SimonSaezCorrea/Project-MilkStgo']])
                dir("topEducation"){
                    sh "mvn clean install"
                }
            }
        }
        stage("Test"){
            steps{
                dir("topEducation"){
                    sh "mvn test"
                }
            }
        }
        stage("Build Docker Image"){
            steps{
                dir("topEducation"){
                    sh "docker build -t simonsaez/project-milkstgo ."
                }
            }
        }
        stage("Build Push Docker Image"){
            steps{
                dir("topEducation"){
                    withCredentials([string(credentialsId: 'dckrhubpassword', variable: 'dckpass')]) {
                        sh "docker login -u simonsaez -p ${dckpass}"
                    }
                    sh "docker push simonsaez/project-milkstgo"
                }
            }
        }
    }
    post{
        always{
            dir("topEducation"){
                sh "docker logout"
            }
        }
    }
}
```

## Nginx

### Modificar Docker-Compose

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

**IMPORTANTE:** Para el proyecto, se pide 3 réplicas. En caso de que se utilice AWS, se puede usar 2 réplicas para que no haya un cobro. Y en caso de que el Hardware de la computadora no pueda a más de dos, se puede usar una réplica, pero se le tendrá que argumentar al profesor del porque se usó solo una réplica.

### Configurar Nginx

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

## Terraform -> AWS

### A tener en cuenta

Para el uso de Terraform, se debe hacer en una distribución de Linux. Se puede hacer uso de una máquina virtual.

[Link descarga Ubuntu](https://ubuntu.com/download/desktop)

### Instalar Terraform

Se debe seguir una serie de pasos:

```
sudo apt-get update && sudo apt-get install -y gnupg software-properties-common
```

```
wget -O- https://apt.releases.hashicorp.com/gpg | \
gpg --dearmor | \
sudo tee /usr/share/keyrings/hashicorp-archive-keyring.gpg
```

```
gpg --no-default-keyring \
--keyring /usr/share/keyrings/hashicorp-archive-keyring.gpg \
--fingerprint
```

Para verificar si la instalación fue existosa, usar los comandos:

```
terraform -help
```
```
terraform -v
```

### Amazon Web Service (AWS)

Se debe crear una cuenta en **AWS**.

[Amazon Web Service](https://aws.amazon.com/es/)

Luego de crear la cuenta, cambiar la zona a **US West (Oregon)**

#### IAM

En **buscar** de **AWS** colocar **IAM** y hacer los siguientes pasos:
* Click en **Usuarios**
    * Click en **Crear usuario**
        * Primera ventana
            * **Nombre de Usuario:** root
            * Activar la opción **Proporcione acceso de usuario a la consola de administración de AWS: opcional**
                * Seleccionar **Quiero crear un usuario de IAM**
            * **Contraseña de la consola**: Contraseña generada automáticamente
            * Click en botón **Siguiente**
        * Segunda ventana
            * **Opciones de permisos:** Adjuntar políticas directamente
                * Agregar **AdministratorAccess**
                * Agregar **AmazonEC2FullAccess**
            * Click en botón **Siguiente**
        * Tercera ventana
            * Click en botón **Crear usuario**
        * Cuarta ventana
            * Click en botón **Volver a la lista de usuarios**
    * Click en el usuario creado
        * Click en **Credenciales de seguridad**
            * Bajar hasta donde diga **Claves de acceso**
                * Click en **Crear clave de acceso**
                    * Primera ventana
                        * Seleccionar **Otros**
                        * Click en botón **Siguiente**
                    * Segunda ventana
                        * Click en botón **Crear clave de acceso**
                        * Se recomienda descargar el csv o guardar la clave de acceso

#### EC2 

En **buscar** de **AWS** colocar **EC2** y hacer los siguientes pasos:

* Click en la opción **Grupos de seguridad//Security Groups**
    * En el único que aparece, hacer click
        * En **Reglas de entrada** hacer click en **Editar reglas de entrada**
            * Click en **Agregar regla**
                * Se buscará el tipo **HTTP** y se colocará Origen **0.0.0.0/0**
                * Se buscará el tipo **SSH** y se colocará Origen **0.0.0.0/0**
            * Click en **Guaradr reglas**
* Ir a **Instancias/Instances**
    * Dejar la página ahí o guardar la IPv4 Pública

#### Inicializar proyecto

En Linux, crear un carpeta iniciar el proyecto. Abrir una consola en la ubicación de la carpeta y ejecutar los siguientes comandos:

```
export AWS_ACCESS_KEY_ID=<Access key ID>
```
```
export AWS_SECRET_ACCESS_KEY=<Secret access key>
```

Ahora generar un archivo llamado **main.tf** con el comando:

```
code main.tf
```

También abrirá el editor, donde colocaremos:

```
terraform {
	required_providers {
		aws = {
			source  = "hashicorp/aws" # Indica el servicio del proveedor a utilizar
			version = "~> 4.16"
    }
}
  required_version = ">= 1.2.0"
}
provider "aws" {
  region = "us-west-2"
}
resource "aws_instance" "app-simple-aws" {
  ami           = "ami-0c2ab3b8efb09f272" # Imagen de la maquina virtual / depende de la región
  instance_type = "t2.micro" # Capacidad de la maquina virtual
  key_name      = aws_key_pair.kp.key_name # Indicar key pair de la instancia de EC2 a crear
  tags = {
    Name = "AppSimpleAwsEC2Instance"
  }
}
resource "tls_private_key" "pk" {
  algorithm = "RSA"
  rsa_bits  = 4096
}
resource "aws_key_pair" "kp" {
  key_name   = "key"       # Crear key pair de acceso a EC2
  public_key = tls_private_key.pk.public_key_openssh
  provisioner "local-exec" { # Crear "key.pem"
    command = "echo '${tls_private_key.pk.private_key_pem}' > ./key.pem"
  }
}
```

Se ejecutará el comando para inicializar

```
terraform init
```

Luego el comando:

```
terraform apply
```

Ahora se hará el comando: 
```
chmod 400 key.pem
```
Y tambien:
```
ssh -i key.pem ec2-user@<IPv4 Pública>
```
La **\<IPv4 Pública\>** es la que se encuentra en **EC2**.

Ahora una sucesión de comandos para la instalación de **Docker** en **AWS**

```
sudo yum update
sudo yum install docker
sudo usermod -a -G docker ec2-user
id ec2-user
newgrp docker
wget https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m) 
sudo mv docker-compose-$(uname -s)-$(uname -m) /usr/local/bin/docker-compose
sudo chmod -v +x /usr/local/bin/docker-compose
sudo systemctl enable docker.service
sudo systemctl start docker.service
```

Para comprobar si se instaló bien hacer:
```
docker -v
docker-compose -v
sudo systemctl status docker.service
```

Ahora se debe crear los archivos:
* **docker-compose.yml**
    * Ejecutar el comando:
    ```
    vi docker-compose.yml
    ```
    * Aparecera una interfaz con lineas
    * Apretar la tecla 'a' para colocar el modo **Insertar**
        * Aquí se tendrá que colocar todo el contenido de tu archivo **docker-compose.yml** de la aplicación local (O en el git)
    * Se apreta la tecla 'ESQ' para salir del modo **Insertar**
    * Se escribe ':wq' para salir de la interfaz
* **\<NombreAplicacion\>.conf**
    * Primero se tiene que crear el directorio, para eso usar los comandos:
    ```
    mkdir nginx
    cd nginx
    mkdir conf.d
    cd conf.d
    ```
    * Ejecutar el comando:
    ```
    vi <NombreAplicación>.conf
    ```
    * Apretar la tecla 'a' para colocar el modo **Insertar**
        * Aquí se tendrá que colocar todo el contenido de tu archivo **\<NombreAplicacion\>.conf** de la aplicación local (O en el git)
    * Se apreta la tecla 'ESQ' para salir del modo **Insertar**
    * Se escribe ':wq' para salir de la interfaz

Para finalizar y levantar docker con sus contenedors, ejecutar:

```
docker-compose --compatibility up
```

Ya con eso se tiene listo la aplicación y poder conectarse en algun browser con la **IPv4 Pública** de **EC2**.

Para bajar el programa hará **CTRL + C** y luego
```
docker-compose down
```

SE RECOMIENDA ELIMINAR TERRAFORM PARA QUE NO EXISTA ALGÚN POSIBLE COBRO EN EL FUTURO
```
terraform destroy
```

---

### Recomendaciones

Si por X o Y motivo les da error el error:

``aws_key_pair.kp: Creating...``

``Error: importing EC2 Key Pair (key): InvalidKeyPair.Duplicate: The keypair already exists  status code: 400, request id: 686c4113-b9de-47bc-aa10-a1c20ee0399b``

``with aws_key_pair.kp, on main.tf line 31, in resource "aws_key_pair" "kp": 
31: resource "aws_key_pair" "kp" {``

Tienen que eliminar en **EC2** en las **Key Pairs** la key que les aparezca
