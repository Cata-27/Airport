package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.Plane;
import core.models.storage.PlaneStorage;

public class PlaneController {
    public static Response createPlane(String id, String brand, String model, String maxCapacity, String airline) {
        try {
            int idInt;
            int maxCapacityInt;

            // Validación y parsing del ID
            try {
                idInt = Integer.parseInt(id);
                if (idInt <= 0) { // El ID debe ser positivo
                    return new Response("Id must be positive", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Id must be numeric", Status.BAD_REQUEST);
            }

            // Validaciones para campos no vacíos (Brand, Model, Airline)
            if (brand == null || brand.trim().isEmpty()) {
                return new Response("Brand must not be empty", Status.BAD_REQUEST);
            }

            if (model == null || model.trim().isEmpty()) {
                return new Response("Model must not be empty", Status.BAD_REQUEST);
            }

            // Validación y parsing de la capacidad máxima
            try {
                maxCapacityInt = Integer.parseInt(maxCapacity);
                if (maxCapacityInt <= 0) { // La capacidad debe ser positiva
                    return new Response("Max Capacity must be positive", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Max Capacity must be numeric", Status.BAD_REQUEST);
            }

            // Validación de la aerolínea (como código de aeropuerto de 3 letras mayúsculas)
            if (airline == null || airline.trim().isEmpty()) {
                return new Response("Airline must not be empty", Status.BAD_REQUEST);
            }
            if (!airline.matches("[A-Z]{3}")) { // Estrictamente 3 letras mayúsculas
                return new Response("Airline code must be exactly 3 uppercase letters", Status.BAD_REQUEST);
            }

            // Obtener instancia de Storage y agregar el avión
//            Storage storage = Storage.getInstance();
//            if (!storage.addPlane(new Plane(id, brand, model, maxCapacityInt, airline))) {
//                return new Response("An airplane with that id already exists", Status.BAD_REQUEST);
//            }

            return new Response("Airplane created successfully", Status.CREATED);
        } catch (Exception ex) {
            // Captura cualquier excepción inesperada
            return new Response("Unexpected error: " + ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Lee y recupera los datos de un avión por su ID.
     *
     * @param id String El identificador del avión a buscar.
     * @return Response Objeto de respuesta que contiene el avión encontrado o un mensaje de error.
     */
    public static Response readAirplane(String id) {
        try {
            int idInt;

            // Validación y parsing del ID
            try {
                idInt = Integer.parseInt(id);
                if (idInt <= 0) {
                    return new Response("Id must be positive", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Id must be numeric", Status.BAD_REQUEST);
            }

            // Obtener instancia de Storage y buscar el avión
            PlaneStorage storage = PlaneStorage.getInstance();
            Plane plane = storage.getPlane(idInt);

            if (plane == null) {
                return new Response("Airplane not found", Status.NOT_FOUND);
            }
            return new Response("Airplane found", Status.OK, plane);
        } catch (Exception ex) {
            return new Response("Unexpected error: " + ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }
    public static Response updateAirplane(String id, String brand, String model, String maxCapacity, String airline) {
        try {
            int idInt;
            int maxCapacityInt;

            // Validación y parsing del ID
            try {
                idInt = Integer.parseInt(id);
                if (idInt <= 0) {
                    return new Response("Id must be positive", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Id must be numeric", Status.BAD_REQUEST);
            }

            // Obtener instancia de Storage y verificar si el avión existe
            PlaneStorage storage = PlaneStorage.getInstance();
            Plane plane = storage.getPlane(idInt);
            if (plane == null) {
                return new Response("Airplane not found", Status.NOT_FOUND);
            }

            // Validaciones para campos no vacíos
            if (brand == null || brand.trim().isEmpty()) {
                return new Response("Brand must not be empty", Status.BAD_REQUEST);
            }

            if (model == null || model.trim().isEmpty()) {
                return new Response("Model must not be empty", Status.BAD_REQUEST);
            }

            try {
                maxCapacityInt = Integer.parseInt(maxCapacity);
                if (maxCapacityInt <= 0) {
                    return new Response("Max Capacity must be positive", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Max Capacity must be numeric", Status.BAD_REQUEST);
            }

            // Validación de la aerolínea (como código de aeropuerto de 3 letras mayúsculas)
            if (airline == null || airline.trim().isEmpty()) {
                return new Response("Airline must not be empty", Status.BAD_REQUEST);
            }
            if (!airline.matches("[A-Z]{3}")) { // Estrictamente 3 letras mayúsculas
                return new Response("Airline code must be exactly 3 uppercase letters", Status.BAD_REQUEST);
            }
            return new Response("Airplane data updated successfully", Status.OK);
        } catch (Exception ex) {
            return new Response("Unexpected error: " + ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }
    public static Response deleteAirplane(String id) {
        try {
            int idInt;

            // Validación y parsing del ID
            try {
                idInt = Integer.parseInt(id);
                if (idInt <= 0) {
                    return new Response("Id must be positive", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Id must be numeric", Status.BAD_REQUEST);
            }

            // Obtener instancia de Storage y eliminar el avión
            PlaneStorage storage = PlaneStorage.getInstance();
            if (!storage.delPlane(idInt)) {
                return new Response("Airplane not found", Status.NOT_FOUND);
            }
            return new Response("Airplane deleted successfully", Status.NO_CONTENT);
        } catch (Exception ex) {
            return new Response("Unexpected error: " + ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }
}
