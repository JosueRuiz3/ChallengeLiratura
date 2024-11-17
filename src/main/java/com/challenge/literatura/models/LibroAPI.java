package com.challenge.literatura.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.challenge.literatura.models.records.DatosLibro;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LibroAPI {
    @JsonAlias("results")
    List<DatosLibro> resultadoLibros;

    public List<DatosLibro> getResultadoLibros() {
        return resultadoLibros;
    }

    public void setResultadoLibros(List<DatosLibro> resultadoLibros) {
        this.resultadoLibros = resultadoLibros;
    }
}