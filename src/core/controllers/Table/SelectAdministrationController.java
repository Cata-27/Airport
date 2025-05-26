/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers.Table;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.Passenger;
import core.models.storage.PassengerStorage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class SelectAdministrationController {

    private static final String PASSENGERS_JSON_PATH = "data/passengers.json";

    public static Response getAllPassengerIdsForSelection() {
        try {
            // Accede directamente al almacenamiento de pasajeros para obtener todos los objetos Passenger.
            List<Passenger> passengers = PassengerStorage.getAll();
            List<String> passengerIds = new ArrayList<>();

            // Verifica si no hay pasajeros.
            if (passengers == null || passengers.isEmpty()) {
                // Si no hay pasajeros, devuelve un estado NOT_FOUND pero con una lista vacía.
                return new Response("No hay pasajeros registrados para seleccionar.", Status.NOT_FOUND, passengerIds);
            }

            // Itera sobre los pasajeros y extrae sus IDs, convirtiéndolos a String.
            for (Passenger p : passengers) {
                passengerIds.add(String.valueOf(p.getId()));
            }

            // Devuelve una respuesta exitosa con la lista de IDs.
            return new Response("IDs de pasajeros obtenidos exitosamente para selección.", Status.OK, passengerIds);
        } catch (Exception ex) {
            // Captura cualquier excepción inesperada y devuelve un error interno del servidor.
            return new Response("Error inesperado al obtener los IDs de pasajeros para selección: " + ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }

}
