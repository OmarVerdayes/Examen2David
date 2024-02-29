package utez.edu.mx.libros.libro;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import utez.edu.mx.libros.utils.AccionesArchivos;
import utez.edu.mx.libros.utils.CustomResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Service
public class LibroServicio {
    @Autowired
    LibroRepositorio repositorio;

    AccionesArchivos accionesArchivos =new AccionesArchivos();
    private String ruta="archivos//imagenes//";

    @Transactional(readOnly=true)
    public CustomResponse<List<Libro>> getAll(){

        try {
            return new CustomResponse<>(this.repositorio.findAll(),false,200, "OK");
        } catch (Exception e) {
            return new CustomResponse<>(null, true, 500, "Error al obtener los libros");
        }
    }

    @Transactional(readOnly=true)
    public CustomResponse<List<Libro>> getByNombre(String nombre){

        try {
            return new CustomResponse<>(this.repositorio.getByNombre(nombre),false,200, "OK");
        } catch (Exception e) {
            return new CustomResponse<>(null, true, 500, "Error al obtener los libros");
        }
    }
    @Transactional(readOnly=true)
    public CustomResponse<List<Libro>> getByAutor(String nombre){

        try {
            return new CustomResponse<>(this.repositorio.getByAutor(nombre),false,200, "OK");
        } catch (Exception e) {
            return new CustomResponse<>(null, true, 500, "Error al obtener los libros");
        }
    }
    @Transactional(readOnly=true)
    public CustomResponse<List<Libro>> getBygenero(String nombre){

        try {
            return new CustomResponse<>(this.repositorio.getByGenero(nombre),false,200, "OK");
        } catch (Exception e) {
            return new CustomResponse<>(null, true, 500, "Error al obtener los libros");
        }
    }
    @Transactional(readOnly=true)
    public CustomResponse<List<Libro>> getInOrden(){

        try {
            return new CustomResponse<>(this.repositorio.findAllOrderByAnioPublicacionDesc(),false,200, "OK");
        } catch (Exception e) {
            return new CustomResponse<>(null, true, 500, "Error al obtener los libros");
        }
    }

    @Transactional(readOnly=true)
    public CustomResponse<List<Libro>> getByAnio(int anioI, int anioF){

        try {
            if(anioI==0){
                anioI=1;
            }
            if(anioF==0){
                Date fechaActual = new Date();
                anioF=fechaActual.getYear() + 1900;
            }
            return new CustomResponse<>(this.repositorio.findByAnioPublicacionBetween(anioI,anioF),false,200, "OK");
        } catch (Exception e) {
            return new CustomResponse<>(null, true, 500, "Error al obtener los libros");
        }
    }

    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<Libro> insert(Libro libro, MultipartFile imagenArchivo ) throws IOException{
        try {
            if(imagenArchivo==null){
                return new CustomResponse<>(null,true,400,"La imagen va vacia");
            }

            libro.setImagen(accionesArchivos.guardar(imagenArchivo,ruta));
            return new CustomResponse<>(this.repositorio.saveAndFlush(libro), false,200,"Libro registrado");
        } catch (Exception e) {
            System.out.println(e);
            return new CustomResponse<>(null, true, 500, "Error al registrar el libro");
        }
    }
    @Transactional(rollbackFor =SQLException.class )
    public CustomResponse<Libro> update(Libro libro, MultipartFile imagenArchivo ) throws IOException{
        try {

            if(!this.repositorio.existsById(libro.getId())){
                return new CustomResponse<>(null,true,400,"El libro no existe");
            }
            Libro lib1=this.repositorio.findById(libro.getId()).get();

            if(imagenArchivo!=null){
                accionesArchivos.eliminarArchivo(lib1.getImagen());
                libro.setImagen(accionesArchivos.guardar(imagenArchivo,ruta));
            }else{
                libro.setImagen(lib1.getImagen());
            }
            return new CustomResponse<>(this.repositorio.saveAndFlush(libro),false,200,"Libro actualizado");

        } catch (Exception e) {
            return new CustomResponse<>(null, true, 500, "Error al actualizar el rol");
        }
    }
    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<Libro> delete(Long id){
        try {
            if(!this.repositorio.existsById(id))
                return new CustomResponse<>(null,true,400,"El libro no existe");
            Libro lib1=this.repositorio.findById(id).get();
            accionesArchivos.eliminarArchivo(lib1.getImagen());

            this.repositorio.deleteById(id);

            return new CustomResponse<>(null,false,200,"Libro eliminado coreectamente");
        } catch (Exception e) {
            return new CustomResponse<>(null, true, 500, "Libro al eliminar el rol");
        }
    }
}
