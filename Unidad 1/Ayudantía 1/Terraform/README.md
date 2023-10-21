# Terraform -> AWS

## Indice

* [A tener en cuenta](#a-tener-en-cuenta)
* [Instalar Terraform](#instalar-terraform)
* [Amazon Web Service (AWS)](#amazon-web-service-aws)
    * [IAM](#iam)
    * [Inicializar proyecto parte 1](#inicializar-proyecto-parte-1)
    * [EC2](#ec2)
    * [Inicializar proyecto parte 2](#inicializar-proyecto-parte-2)
* [Recomendaciones](#recomendaciones)

## A tener en cuenta

Para el uso de Terraform, se debe hacer en una distribución de Linux. Se puede hacer uso de una máquina virtual.

[Link descarga Ubuntu](https://ubuntu.com/download/desktop)

## Instalar Terraform

[Link descarga Terraform](https://developer.hashicorp.com/terraform/downloads)

Se debe seguir una serie de pasos:

```
wget -O- https://apt.releases.hashicorp.com/gpg | sudo gpg --dearmor -o /usr/share/keyrings/hashicorp-archive-keyring.gpg
echo "deb [signed-by=/usr/share/keyrings/hashicorp-archive-keyring.gpg] https://apt.releases.hashicorp.com $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/hashicorp.list
sudo apt update && sudo apt install terraform
```

Para verificar si la instalación fue existosa, usar los comandos:

```
terraform -help
```
```
terraform -v
```

## Amazon Web Service (AWS)

Se debe crear una cuenta en **AWS**.

[Amazon Web Service](https://aws.amazon.com/es/)

Luego de crear la cuenta, cambiar la zona a **US West (Oregon)**

### IAM

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



### Inicializar proyecto parte 1

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

### EC2 

Ahora habrá que hacer otras configuraciones en **AWS** y se deberá ir a **buscar**, colocar **EC2** y hacer los siguientes pasos:

* Click en la opción **Grupos de seguridad//Security Groups**
    * En el único que aparece, hacer click
        * En **Reglas de entrada** hacer click en **Editar reglas de entrada**
            * Click en **Agregar regla**
                * Se buscará el tipo **HTTP** y se colocará Origen **0.0.0.0/0**
                * Se buscará el tipo **SSH** y se colocará Origen **0.0.0.0/0**
            * Click en **Guaradr reglas**
* Ir a **Instancias/Instances**
    * Dejar la página ahí o guardar la IPv4 Pública

### Inicializar proyecto parte 2

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
```
```
sudo yum install docker
```
```
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

Para salir de la interfaz escribir **exit**.

SE RECOMIENDA ELIMINAR TERRAFORM PARA QUE NO EXISTA ALGÚN POSIBLE COBRO EN EL FUTURO
```
terraform destroy
```

---

## Recomendaciones

Si por X o Y motivo les da error el error:

``aws_key_pair.kp: Creating...``

``Error: importing EC2 Key Pair (key): InvalidKeyPair.Duplicate: The keypair already exists  status code: 400, request id: 686c4113-b9de-47bc-aa10-a1c20ee0399b``

``with aws_key_pair.kp, on main.tf line 31, in resource "aws_key_pair" "kp": 
31: resource "aws_key_pair" "kp" {``

Tienen que eliminar en **EC2** en las **Key Pairs** la key que les aparezca
