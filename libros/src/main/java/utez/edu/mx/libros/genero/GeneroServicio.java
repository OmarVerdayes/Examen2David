package utez.edu.mx.libros.genero;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.libros.utils.CustomResponse;

import java.sql.SQLException;
import java.util.List;

@Service
public class GeneroServicio {
    @Autowired
    GeneroRepositorio repositorio;

    @Transactional(readOnly=true)
    public CustomResponse<List<Genero>> getAll(){

        try {
            return new CustomResponse<>(this.repositorio.findAll(),false,200, "OK");
        } catch (Exception e) {
            return new CustomResponse<>(null, true, 500, "Error al obtener los generos");
        }
    }

    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<Genero> insert(Genero genero){
        try {
            System.out.println(genero.getNombre());
            return new CustomResponse<>(this.repositorio.saveAndFlush(genero), false,200,"Genero registrado");
        } catch (Exception e) {
            return new CustomResponse<>(null, true, 500, "Error al registrar el genero");
        }
    }
    @Transactional(rollbackFor =SQLException.class )
    public CustomResponse<Genero> update(Genero genero){
        try {

            if(!this.repositorio.existsById(genero.getId())){
                return new CustomResponse<>(null,true,400,"El genero no existe");
            }

            return new CustomResponse<>(this.repositorio.saveAndFlush(genero),false,200,"Genero actualizado");

        } catch (Exception e) {
            return new CustomResponse<>(null, true, 500, "Error al actualizar el rol");
        }
    }
    @Transactional(rollbackFor = {SQLException.class})
    public CustomResponse<Genero> delete(Long id){
        try {
            if(!this.repositorio.existsById(id))
                return new CustomResponse<>(null,true,400,"El genero no existe");
            this.repositorio.deleteById(id);
            return new CustomResponse<>(null,false,200,"Genero eliminado coreectamente");
        } catch (Exception e) {
            return new CustomResponse<>(null, true, 500, "Genero al eliminar el rol");
        }
}
}
