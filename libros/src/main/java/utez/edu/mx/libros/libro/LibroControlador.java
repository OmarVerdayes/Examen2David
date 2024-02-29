package utez.edu.mx.libros.libro;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utez.edu.mx.libros.utils.CustomResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/libros")
@CrossOrigin(origins = {"*"})
public class LibroControlador {
    @Autowired
    LibroServicio servicio;
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<CustomResponse<List<Libro>>> getByNombre(@PathVariable("nombre") String nombre){
        return new ResponseEntity<>(this.servicio.getByNombre(nombre),HttpStatus.OK);
    }
    @GetMapping("/autor/{nombre}")
    public ResponseEntity<CustomResponse<List<Libro>>> getByAutor(@PathVariable("nombre") String nombre){
        return new ResponseEntity<>(this.servicio.getByAutor(nombre),HttpStatus.OK);
    }

    @GetMapping("/genero/{nombre}")
    public ResponseEntity<CustomResponse<List<Libro>>> getByGenero(@PathVariable("nombre") String nombre){
        return new ResponseEntity<>(this.servicio.getBygenero(nombre),HttpStatus.OK);
    }

    @GetMapping("/anio/{anioI}/{anioF}")
    public ResponseEntity<CustomResponse<List<Libro>>> getByGenero(@PathVariable("anioI") int anioI,@PathVariable("anioF") int anioF){
        return new ResponseEntity<>(this.servicio.getByAnio(anioI,anioF),HttpStatus.OK);
    }
    @GetMapping("/ordenado")
    public ResponseEntity<CustomResponse<List<Libro>>> getOrdenado(){
        return new ResponseEntity<>(this.servicio.getInOrden(),HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<CustomResponse<List<Libro>>> getAll(){
        return new ResponseEntity<>(this.servicio.getAll(), HttpStatus.OK);
    }
    @PostMapping("/")
    public ResponseEntity<CustomResponse<Libro>> insert(@Validated @ModelAttribute Libro libro,  MultipartFile imagenArchivo) throws IOException {
        return new ResponseEntity<>(this.servicio.insert(libro,imagenArchivo),HttpStatus.CREATED);
    }
    @PutMapping("/")
    public ResponseEntity<CustomResponse<Libro>> update(@Validated @ModelAttribute Libro libro,  MultipartFile imagenArchivo) throws IOException {
        return new ResponseEntity<>(this.servicio.update(libro,imagenArchivo),HttpStatus.CREATED);
    }
    @DeleteMapping("/")
    public ResponseEntity<CustomResponse<Libro>> delete(@Validated @RequestBody Map<String,Long> requets){
        Long id=requets.get("id");
        return new ResponseEntity<>(this.servicio.delete(id),HttpStatus.CREATED);
    }
}
