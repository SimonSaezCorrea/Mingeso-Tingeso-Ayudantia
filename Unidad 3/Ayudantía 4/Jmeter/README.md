# JMETER

## Indice

## Iniciar Jmeter

Para utilizar Jmeter, debemos descargar la [aplicación](https://jmeter.apache.org/download_jmeter.cgi), especificamente el .zip.

Ya descargada se debe descomprimir y ubicar en donde uno le acomode.

Para abrir la aplicación accedemos a la carpeta extraida, entramos a la carpeta bin y abrimos el que dice **ApacheJMeter.jar**

## Cómo usarlo

Ya con la aplicación abierta, ir donde dice "Plan de pruebas" y hacer click derecho y apretar **Añadir>Hilos (Usuarios)>Montar grupos de hilos**

Se creara un apartado, este puede ser llamado a gusto, hacer click derecho y **Añadir>Muestreador>Petición HTTP**

Allí generará un test el cual será tipo http que al acceder tendrá varias propiedades:
* Nombre de Servidor o IP: Allí irá el nombre del url (Debe ir sin los path que tenga)
* Petición HTTP: se puede elegir el tipo de Request (GET por default) y se puede colocar la ruta en caso de ser necesario.

En el "Plan de pruebas" se puede generar receptores, para ver que sucedio con las peticiones, y para ello se generarán dos, "Reporte resumen" y "Ver Árbol de Resultados".