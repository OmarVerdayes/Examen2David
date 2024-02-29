package utez.edu.mx.libros.libro;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LibroRepositorio extends JpaRepository<Libro,Long> {

    @Query("SELECT l FROM Libro l WHERE l.nombre LIKE CONCAT('%', :nombre, '%')")
    List<Libro> getByNombre(@Param("nombre") String nombre);

    @Query("SELECT l FROM Libro l WHERE l.autor LIKE CONCAT('%', :nombre, '%')")
    List<Libro> getByAutor(@Param("nombre") String nombre);


    @Query("SELECT l FROM Libro l WHERE l.genero.nombre LIKE CONCAT('%', :nombre, '%')")
    List<Libro> getByGenero(@Param("nombre") String nombre);

    @Query("SELECT l FROM Libro l ORDER BY l.anioPublicacion DESC")
    List<Libro> findAllOrderByAnioPublicacionDesc();

    @Query("SELECT l FROM Libro l WHERE l.anioPublicacion BETWEEN :anioInicio AND :anioFin")
    List<Libro> findByAnioPublicacionBetween(@Param("anioInicio") int anioInicio, @Param("anioFin") int anioFin);
}
