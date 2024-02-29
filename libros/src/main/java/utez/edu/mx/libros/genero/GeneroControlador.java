package utez.edu.mx.libros.genero;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.libros.utils.CustomResponse;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/generos")
@CrossOrigin(origins = {"*"})
public class GeneroControlador {

    @Autowired
    GeneroServicio servicio;

    @GetMapping("/")
    public ResponseEntity<CustomResponse<List<Genero>>> getAll(){
        return new ResponseEntity<>(this.servicio.getAll(), HttpStatus.OK);
    }
    @PostMapping("/")
    public ResponseEntity<CustomResponse<Genero>> insert(@Validated @RequestBody Genero genero){
        return new ResponseEntity<>(this.servicio.insert(genero),HttpStatus.CREATED);
    }
    @PutMapping("/")
    public ResponseEntity<CustomResponse<Genero>> update(@Validated @RequestBody Genero genero){
        return new ResponseEntity<>(this.servicio.update(genero),HttpStatus.CREATED);
    }
    @DeleteMapping("/")
    public ResponseEntity<CustomResponse<Genero>> delete(@Validated @RequestBody Map<String,Long> requets){
        Long id=requets.get("id");
        return new ResponseEntity<>(this.servicio.delete(id),HttpStatus.CREATED);
    }

}
