package com.challenge.literatura.dtos;

// Definición de un enumerado llamado Genero, que representa los distintos géneros literarios
public enum Genero {
    // Lista de géneros literarios posibles con su respectiva representación en inglés
    ACCION("Action"),
    ROMANCE("Romance"),
    CRIMEN("Crime"),
    COMEDIA("Comedy"),
    DRAMA("Drama"),
    AVENTURA("Adventure"),
    FICCION("Fiction"),

    // Género por defecto en caso de que el texto no coincida con los anteriores
    DESCONOCIDO("Desconocido");

    // Atributo privado que almacenará el nombre del género en inglés
    private String genero;

    // Constructor del enum, que recibe el nombre del género como parámetro
    Genero(String generoGutendex) { this.genero = generoGutendex; }

    /**
     *  Metodo estático que convierte un String en un valor del enum Genero.
     * @param text El texto que se intenta convertir en un género.
     * @return El género correspondiente si el texto coincide, o 'DESCONOCIDO' si no hay coincidencia.
     */
    public static Genero fromString(String text) {
        // Recorre todos los valores del enum Genero
        for (Genero generoEnum : Genero.values()) {
            // Si el texto coincide con el nombre del género, lo retorna
            if (generoEnum.genero.equals(text)) {
                return generoEnum;
            }
        }
        // Si no encuentra coincidencia, retorna 'DESCONOCIDO'
        return Genero.DESCONOCIDO;
    }
}
