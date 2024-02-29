package utez.edu.mx.libros.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@NoArgsConstructor
@Getter
@Setter
public class AccionesArchivos {
    public String generarNombreUnico(String nombreBase, int contador) {
        // Genera un nombre único agregando el contador antes de la extensión del archivo
        int puntoIndex = nombreBase.lastIndexOf(".");
        if (puntoIndex != -1) {
            String nombreSinExtension = nombreBase.substring(0, puntoIndex);
            String extension = nombreBase.substring(puntoIndex);
            return nombreSinExtension + "_" + contador + extension;
        } else {
            return nombreBase + "_" + contador;
        }
    }
    public String guardar(MultipartFile archivoOriginal, String ruta) throws IOException {
        String nombreArchivoOriginal = archivoOriginal.getOriginalFilename();
        nombreArchivoOriginal = nombreArchivoOriginal.replace(" ", "_");

        // Verifica si el archivo con el mismo nombre ya existe en la carpeta
        int contador = 0;
        String rutaCompleta;
        File archivo;
        do {
            contador++;
            rutaCompleta = ruta + generarNombreUnico(nombreArchivoOriginal, contador);
            archivo = new File(rutaCompleta);
        } while (archivo.exists());
        //guardamos el archivo
        byte[] bytes = archivoOriginal.getBytes();
        Path path = Paths.get(rutaCompleta);
        Files.write(path, bytes);
        //devolvemos la ruta del archivo
        return String.valueOf(path);
    }
    public void eliminarArchivo(String ruta){
        File file=new File(ruta);
        file.delete();
    }
}
