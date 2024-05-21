# Screenmatch API Client

Este proyecto es un cliente para interactuar con la API de OMDb (Open Movie Database) utilizando Java y Spring Boot. La API de OMDb proporciona información sobre películas y series, incluyendo detalles como títulos, actores, calificaciones y más.

## Características

- Consulta información sobre series populares, como "Game of Thrones".
- Utiliza la biblioteca JACKSON para mapear las respuestas de la API a objetos Java.
- Aplica funciones lambda y API Stream para manipular el flujo de datos.
- Implementa un resumen de estadísticas basado en la evaluación de los episodios.

## Requisitos

- Java 11 o superior.
- Spring Boot 2.5 o superior.
- Dependencia JACKSON para el manejo de JSON.

## Uso

1. Clona este repositorio.
2. Configura tus credenciales de la API de OMDb en `application.properties`.
3. Ejecuta la aplicación.

## Ejemplo de consulta

```java
//       Interaccion de entrada y salidad de datos
System.out.println("Escribe el nombre de la serie que deseas buscar: ");
        var nombreSerie = entradaDeUsuario.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        DatosSerie datos = convierteDatos.obtenerDatos(json, DatosSerie.class);

//        Sout de los datos
        System.out.println(datos);
