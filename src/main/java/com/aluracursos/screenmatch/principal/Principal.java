package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.DatosEpisodio;
import com.aluracursos.screenmatch.model.DatosSerie;
import com.aluracursos.screenmatch.model.DatosTemporada;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner entradaDeUsuario = new Scanner(System.in);

    private ConsumoAPI consumoApi = new ConsumoAPI();

    private final String URL_BASE = "https://www.omdbapi.com/?t=";

    private final String API_KEY = "&apikey=5406a25f";

    private ConvierteDatos convierteDatos = new ConvierteDatos();

    public void muestraElMenu() {
        System.out.println("Escribe el nombre de la serie que deseas buscar: ");
        var nombreSerie = entradaDeUsuario.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        DatosSerie datos = convierteDatos.obtenerDatos(json, DatosSerie.class);

//        Sout de los datos
        System.out.println(datos);

//        Busca los datos relacionados a las temporadas de la serie
        List<DatosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= datos.totalDeTemporadas(); i++) {
            json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + "&Season=" + i + API_KEY);
            DatosTemporada datosTemporada = convierteDatos.obtenerDatos(json, DatosTemporada.class);
            temporadas.add(datosTemporada);
        }
//        temporadas.forEach(System.out::println);

//        Mostrar solo el titulo del episodio por las temporadas
//        List<DatosEpisodio> episodiosTemporadas = temporadas.getFirst().episodios();
//        for (int j = 0; j < episodiosTemporadas.size(); j++) {
//            System.out.println(episodiosTemporadas.get(j).titulo());
//        }

//        Mostrando el titulo pero utilizando funciones lambda
//        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

//        Convertir todas las informaciones a una lista del tipo DatosEpisodio
        List<DatosEpisodio> datosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                /*Es posible utilizar dos metodos para hacer la conversion
                 * toList -> cuando gestionamos una lista inmutable
                 * collect - Collectors to list -> cuando gestionamos una lista mutable
                 * */
                .collect(Collectors.toList());

//        Stream a la lista de los datosEpisodios para seleccionar los primero 5 episodios con mejor evaluacion
//        System.out.println("Top 5 mejores episodios");

//        La API stream puede asimilarse a los middleware para modificar listas a partir de metodos
//        datosEpisodios.stream()
//                El metodo .peek verifica y ayuda a entender el flujo de datos
//                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
//                .peek(verificar -> System.out.println("Primer filtro (N/A)    " + verificar))
//                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
//                .peek(verificar -> System.out.println("Segundo, ordenación (M>m)    " + verificar))
//                .map(e -> e.titulo().toUpperCase())
//                .peek(verificar -> System.out.println("Tercero, titulos en UpperCase    " + verificar))
//                .limit(5)
//                .peek(verificar -> System.out.println("Cuarto, limitando (5 max)    " + verificar))
//                .forEach(System.out::println);

//        Convirtiendo los datos a una lista del tipo Episodio
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numeroSesion(), d)))
                .collect(Collectors.toList());

//        episodios.forEach(System.out::println);

//        Buscar Episodios a partir de una fecha (año)
//        System.out.println("Por favor indica el año para buscar el capitulo: ");

//        var fecha = entradaDeUsuario.nextInt();
//        entradaDeUsuario.nextLine();

//        Se describe la estructura de la fecha que llega desde la API
//        LocalDate fechaBusqueda = LocalDate.of(fecha, 1, 1);

//        Definimos nuestro propio formato de fecha
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        episodios.stream()
//                .filter(f -> f.getFechaDeLanzamiento() != null && f.getFechaDeLanzamiento().isAfter(fechaBusqueda))
//                .forEach(e -> System.out.println(
//                        "Temporada " + e.getTemporada() +
//                                "Episodio " + e.getTitulo() +
//                                "Fecha de lanzamiento " + e.getFechaDeLanzamiento().format(dtf)
//                ));

//        Buscar Episodio por terminos en comun segun el input de usuario
//        System.out.println("Digite el titulo del episodio que desea buscar: ");
//        var episodioBuscado = entradaDeUsuario.nextLine();
//        Optional<Episodio> episodioEncontrado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(episodioBuscado.toUpperCase()))
//                .findFirst();
//        if (episodioEncontrado.isPresent()) {
//            System.out.println(episodioEncontrado.get());
//        } else {
//            System.out.println("No existe el episodio que desea buscar");
//        }

//        Creando un Map de datos por temporada
        Map<Integer, Double> datosTemporada = episodios.stream()
                .filter(e-> e.getEvaluacion()>0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getEvaluacion)));
        System.out.println(datosTemporada);

//        Estructura para realizar un resumen de estadisticas
        DoubleSummaryStatistics est = episodios.stream()
                .filter(e-> e.getEvaluacion()>0)
                .collect(Collectors.summarizingDouble(Episodio::getEvaluacion));
        System.out.println(est);
    }
}
