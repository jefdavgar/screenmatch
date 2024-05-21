package com.aluracursos.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosSerie(
        //En el caso de modificar o agregar un documento/variable a la coleccion (API) de json, es posible utilizar @JsonProperty
        //Cuando hay una clase Record, para deserializar los datos utilizamos @JsonAlias
        @JsonAlias("Title") String titulo,
        @JsonAlias("totalSeasons") Integer totalDeTemporadas,
        @JsonAlias("imdbRating") String evaluacion
) {
}
