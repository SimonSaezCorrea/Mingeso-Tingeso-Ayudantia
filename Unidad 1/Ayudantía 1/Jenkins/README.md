# Jenkins

## Indice

* [Previa a instalar](#previa-a-instalar)
* [Instalar](#instalarn)
* [Ejecutar](#ejecutar)
* [Configurar](#configurar)
* [Hacer una nueva Tarea](#hacer-una-nueva-tarea)

## Previa a instalar

Verificar tener agregado al path el Git\usr\bin

En caso de no estar, buscar **Editar las variables de entorno del sistema**. Allí dirijirse a **Variables de entorno...** y en la sección de **Variables del sistema** buscar el que diga **Path**, le dan a **Editar...** y para finalizar le dan a **Nuevo** o **Examinar...**.

La ubicación, por lo general, es **C:\Program Files\Git\usr\bin**, pero también puede estar en el otro disco en caso de haber especificado el lugar, como por ejemplo: **D:\Git\usr\bin**.

## Instalar

Para instalar [Jenkins](https://www.jenkins.io/download/), se debe elegir el archivo que es **.war**

## Ejecutar

Para ejecutar Jenkins, se debe usar el comando (en la ubicación del archivo):

```
java -jar jenkins.war
```

Cuando se inicie por primera vez, les dará una contraseña la cual se usará para acceder a la cuenta. Se deberá seleccionar **Instalar plugins sugerido**. Luego deben crearse una cuenta con usuario y contraseña.

## Configurar

Tendran que ir a la siguiente ubicacion en Jenkins **Administrar Jenkins > Global Tool Configuration/Tools**, bajarán hasta encontrar donde dice **Maven**. Harán click en **Instalación de Maven** y colocarán:

* **Nombre:** maven
* **Versión:** La más reciente (3.9.4)

## Hacer una nueva Tarea

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