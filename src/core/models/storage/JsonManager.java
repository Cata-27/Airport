
package core.models.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.json.JSONArray;

public class JsonManager {

    private static final String BASE_PATH = "json" + File.separator;

    public static JSONArray leerArregloJson(String nombreArchivo) throws Exception {
        File archivo = new File(BASE_PATH + nombreArchivo);

        if (!archivo.exists()) {
            throw new Exception("No se encontró el archivo: " + archivo.getAbsolutePath());
        }

        StringBuilder contenido = new StringBuilder();
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                contenido.append(linea);
            }
        }

        return new JSONArray(contenido.toString());
    }

    public static void escribirArregloJson(String nombreArchivo, JSONArray arreglo) throws Exception {
        File carpeta = new File(BASE_PATH);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        File archivo = new File(carpeta, nombreArchivo);
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivo))) {
            escritor.write(arreglo.toString(4)); // 4 espacios de indentación para que se vea bonito
        }
    }
}


