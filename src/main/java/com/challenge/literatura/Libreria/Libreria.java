package com.challenge.literatura.Libreria;

import com.challenge.literatura.config.ConsumoApiGutendex;
import com.challenge.literatura.config.ConvertirDatos;
import com.challenge.literatura.models.Autor;
import com.challenge.literatura.models.Libro;
import com.challenge.literatura.models.LibroAPI;
import com.challenge.literatura.models.records.DatosLibro;
import com.challenge.literatura.repository.iAutorRepository;
import com.challenge.literatura.repository.iLibroRepository;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public class Libreria {

    private Scanner sc = new Scanner(System.in);
    private ConsumoApiGutendex consumoApi = new ConsumoApiGutendex();
    private ConvertirDatos convertir = new ConvertirDatos();
    private static String API_BASE = "https://gutendex.com/books/?search=";
    private List<Libro> datosLibro = new ArrayList<>();
    private iLibroRepository libroRepository;
    private iAutorRepository autorRepository;
    public Libreria(iLibroRepository libroRepository, iAutorRepository autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void consumo(){
        var opcion = -1;
        while (opcion != 0){
            var menu = """
                    
                    |*****       BIENVENIDO A LA BIBLIOTECA VIRTUAL       ******|
                    
                    |*****             SELECCIONE UNA OPCIÓN              ******|
                    
                    1 - Añadir un Libro por su Nombre
                    2 - Ver Libros Recientes
                    3 - Buscar Libro por Nombre
                    4 - Listar Autores de Libros Recientes
                    5 - Filtrar Autores por Año
                    6 - Buscar Libros por Idioma
                    7 - Top 10 Libros Más Populares
                     8 - Encontrar Autor por Nombre
                    
                    0 - Salir
                    
                    |***********************************************************|
                    """;

            try {
                System.out.println(menu);
                opcion = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {

                System.out.println("|****************************************|");
                System.out.println("|  Por favor, ingrese un número válido.  |");
                System.out.println("|****************************************|\n");
                sc.nextLine();
                continue;
            }

            switch (opcion){
                case 1:
                    buscarLibroEnLaWeb();
                    break;
                case 2:
                    librosBuscados();
                    break;
                case 3:
                    buscarLibroPorNombre();
                    break;
                case 4:
                    BuscarAutores();
                    break;
                case 5:
                    buscarAutoresPorAnio();
                    break;
                case 6:
                    buscarLibrosPorIdioma();
                    break;
                case 7:
                    top10LibrosMasDescargados();
                    break;
                case 8:
                    buscarAutorPorNombre();
                    break;
                case 0:
                    opcion = 0;
                    System.out.println("|********************************|");
                    System.out.println("|       Aplicación finalizada.   |");
                    System.out.println("|********************************|\n");
                    break;
                default:
                    System.out.println("|***********************|");
                    System.out.println("|  Opción no válida.    |");
                    System.out.println("|***********************|\n");
                    System.out.println("Por favor, elija una opción válida");
                    consumo();
                    break;
            }
        }
    }

    private Libro getDatosLibro(){
        System.out.println("Ingrese el nombre del libro: ");
        var nombreLibro = sc.nextLine().toLowerCase();
        var json = consumoApi.obtenerDatos(API_BASE + nombreLibro.replace(" ", "%20"));

        LibroAPI datos = convertir.convertirDatosJsonAJava(json, LibroAPI.class);

            if (datos != null && datos.getResultadoLibros() != null && !datos.getResultadoLibros().isEmpty()) {
                DatosLibro primerLibro = datos.getResultadoLibros().get(0); // Obtener el primer libro de la lista
                return new Libro(primerLibro);
            } else {
                System.out.println("No se encontraron coincidencias.");
                return null;
            }
    }


    private void buscarLibroEnLaWeb() {
        Libro libro = getDatosLibro();

        if (libro == null){
            System.out.println("Libro no encontrado, el valor es nulo.");
            return;
        }

        //datosLibro.add(libro);
        try{
            boolean libroExists = libroRepository.existsByTitulo(libro.getTitulo());
            if (libroExists){
                System.out.println("El libro ya se encuentra en la base de datos.");
            }else {
                libroRepository.save(libro);
                System.out.println(libro.toString());
            }
        }catch (InvalidDataAccessApiUsageException e){
            System.out.println("No se puede guardar el libro solicitado.");
        }
    }

    @Transactional(readOnly = true)
    private void librosBuscados(){
        //datosLibro.forEach(System.out::println);
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos.");
        } else {
            System.out.println("Libros disponibles en la base de datos:");
            for (Libro libro : libros) {
                System.out.println(libro.toString());
            }
        }
    }

    private void buscarLibroPorNombre() {
        System.out.println("Ingrese el título del libro que desea buscar:");
        var titulo = sc.nextLine();
        Libro libroBuscado = libroRepository.findByTituloContainsIgnoreCase(titulo);
        if (libroBuscado != null) {
            System.out.println("El libro encontrado es: " + libroBuscado);
        } else {
            System.out.println("No se encontró ningún libro con el título '" + titulo + "'.");
        }
    }

    private  void BuscarAutores(){
        //LISTAR AUTORES DE LIBROS BUSCADOS
        List<Autor> autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos. \n");
        } else {
            System.out.println("Se encontraron los siguientes libros en la base de datos: \n");
            Set<String> autoresUnicos = new HashSet<>();
            for (Autor autor : autores) {
                // add() retorna true si el nombre no estaba presente y se añade correctamente
                if (autoresUnicos.add(autor.getNombre())){
                    System.out.println(autor.getNombre()+'\n');
                }
            }
        }
    }

    private void  buscarLibrosPorIdioma(){
        System.out.println("Ingrese el idioma en el que desea buscar: \n");
        System.out.println("|***********************************|");
        System.out.println("|  Opción - es: Libros en español.  |");
        System.out.println("|  Opción - en: Libros en inglés.   |");
        System.out.println("|***********************************|\n");

        var idioma = sc.nextLine();
        List<Libro> librosPorIdioma = libroRepository.findByIdioma(idioma);

        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en la base de datos.");
        } else {
            System.out.println("Libros disponibles según el idioma en la base de datos:");
            for (Libro libro : librosPorIdioma) {
                System.out.println(libro.toString());
            }
        }

    }

    private void buscarAutoresPorAnio() {
//        //BUSCAR AUTORES POR ANIO
        System.out.println("Ingresa el año para consultar qué autores están vivos: \n");
        var anioBuscado = sc.nextInt();
        sc.nextLine();

        List<Autor> autoresVivos = autorRepository.findByCumpleaniosLessThanOrFechaFallecimientoGreaterThanEqual(anioBuscado, anioBuscado);

        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + anioBuscado + ".");
        } else {
            System.out.println("Los autores que estuvieron vivos en el año " + anioBuscado + " son:");
            Set<String> autoresUnicos = new HashSet<>();

            for (Autor autor : autoresVivos) {
                if (autor.getCumpleanios() != null && autor.getFechaFallecimiento() != null) {
                    if (autor.getCumpleanios() <= anioBuscado && autor.getFechaFallecimiento() >= anioBuscado) {
                        if (autoresUnicos.add(autor.getNombre())) {
                            System.out.println("Autor: " + autor.getNombre());
                        }
                    }
                }
            }
        }
    }

    private void top10LibrosMasDescargados(){
        List<Libro> top10Libros = libroRepository.findTop10ByTituloByCantidadDescargas();
        if (!top10Libros.isEmpty()){
            int index = 1;
            for (Libro libro: top10Libros){
                System.out.printf("Libro %d: %s Autor: %s Descargas: %d\n",
                        index, libro.getTitulo(), libro.getAutores().getNombre(), libro.getCantidadDescargas());
                index++;
            }
        }
    }

    private void buscarAutorPorNombre() {
        System.out.println("Ingrese el nombre del autor que desea buscar: ");
        var escritor = sc.nextLine();
        Optional<Autor> escritorBuscado = autorRepository.findFirstByNombreContainsIgnoreCase(escritor);
        if (escritorBuscado != null) {
            System.out.println("\nEl autor encontrado es: " + escritorBuscado.get().getNombre());
        } else {
            System.out.println("\nNo se encontró un autor con el nombre '" + escritor + "'.");
        }
    }
}
