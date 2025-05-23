
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.Flight;
import core.models.Location;
import core.models.storage.FlightStorage;
import core.models.storage.LocationStorage;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LocationController {
    public static Response createLocation(String id, String city, String name, String country, String latitude, String longitude) {
        try {
            int idInt;
            double latitudeDbl, longitudeDbl;

            // --- Validación y parseo de ID ---
            try {
                idInt = Integer.parseInt(id);
                if (idInt < 0) {
                    return new Response("Id must be positive", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Id must be numeric", Status.BAD_REQUEST);
            }

            // --- Validación de City ---
            if (city.trim().isEmpty()) {
                return new Response("City must not be empty", Status.BAD_REQUEST);
            }
            
            // --- Validación de Name ---
            if (name.trim().isEmpty()) {
                return new Response("Name must not be empty", Status.BAD_REQUEST);
            }

            // --- Validación de Country ---
            if (country.trim().isEmpty()) {
                return new Response("Country must not be empty", Status.BAD_REQUEST);
            }

            // --- Validación y parseo de Latitude ---
            try {
                latitudeDbl = Double.parseDouble(latitude);
                // Opcional: Validar rango de latitud (-90 a 90)
                if (latitudeDbl < -90 || latitudeDbl > 90) {
                    return new Response("Latitude must be between -90 and 90", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Latitude must be numeric", Status.BAD_REQUEST);
            }

            // --- Validación y parseo de Longitude ---
            try {
                longitudeDbl = Double.parseDouble(longitude);
                // Opcional: Validar rango de longitud (-180 a 180)
                if (longitudeDbl < -180 || longitudeDbl > 180) {
                    return new Response("Longitude must be between -180 and 180", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Longitude must be numeric", Status.BAD_REQUEST);
            }

            // --- Lógica de negocio para crear Location ---
            LocationStorage storage = LocationStorage.getInstance();
            // Asume que Storage tiene un método addLocation que recibe un objeto Location
            if (!storage.addLocation(new Location(id, city, name, country, latitudeDbl, longitudeDbl))) {
                return new Response("A location with that id already exists", Status.BAD_REQUEST);
            }
            return new Response("Location created successfully", Status.CREATED);

        } catch (Exception ex) {
            // Manejo de cualquier otra excepción inesperada
            return new Response("Unexpected error: " + ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response readLocation(String id) {
        try {
            int idInt;

            // --- Validación y parseo de ID ---
            try {
                idInt = Integer.parseInt(id);
                if (idInt < 0) {
                    return new Response("Id must be positive", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Id must be numeric", Status.BAD_REQUEST);
            }

            // --- Lógica de negocio para leer Location ---
            LocationStorage storage = LocationStorage.getInstance();
            // Asume que Storage tiene un método getLocation que busca por ID
            Location location = storage.getLocation(idInt);
            if (location == null) {
                return new Response("Location not found", Status.NOT_FOUND);
            }
            return new Response("Location found", Status.OK, location);

        } catch (Exception ex) {
            return new Response("Unexpected error: " + ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response updateLocation(String id, String city, String name, String country, String latitude, String longitude) {
        try {
            int idInt;
            double latitudeDbl, longitudeDbl;

            // --- Validación y parseo de ID ---
            try {
                idInt = Integer.parseInt(id);
                if (idInt < 0) {
                    return new Response("Id must be positive", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Id must be numeric", Status.BAD_REQUEST);
            }
            LocationStorage storage = LocationStorage.getInstance();
            Location location = storage.getLocation(idInt);
            if (location == null) {
                return new Response("Location not found", Status.NOT_FOUND);
            }
            if (city.trim().isEmpty()) {
                return new Response("City must not be empty", Status.BAD_REQUEST);
            }
            
            if (name.trim().isEmpty()) {
                return new Response("Name must not be empty", Status.BAD_REQUEST);
            }

            if (country.trim().isEmpty()) {
                return new Response("Country must not be empty", Status.BAD_REQUEST);
            }

            try {
                latitudeDbl = Double.parseDouble(latitude);
                if (latitudeDbl < -90 || latitudeDbl > 90) {
                    return new Response("Latitude must be between -90 and 90", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Latitude must be numeric", Status.BAD_REQUEST);
            }

            try {
                longitudeDbl = Double.parseDouble(longitude);
                if (longitudeDbl < -180 || longitudeDbl > 180) {
                    return new Response("Longitude must be between -180 and 180", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Longitude must be numeric", Status.BAD_REQUEST);
            }

            // --- Actualización de los datos de la Location ---
//            location.setCity(city);
//            location.setName(name);
//            location.setCountry(country);
//            location.setLatitude(latitudeDbl);
//            location.setLongitude(longitudeDbl);

            return new Response("Location data updated successfully", Status.OK);

        } catch (Exception ex) {
            return new Response("Unexpected error: " + ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response deleteLocation(String id) {
        try {
            int idInt;

            // --- Validación y parseo de ID ---
            try {
                idInt = Integer.parseInt(id);
                if (idInt < 0) {
                    return new Response("Id must be positive", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Id must be numeric", Status.BAD_REQUEST);
            }

            // --- Lógica de negocio para eliminar Location ---
            LocationStorage storage = LocationStorage.getInstance();
            // Asume que Storage tiene un método delLocation que elimina por ID
            if (!storage.delLocation(idInt)) {
                return new Response("Location not found", Status.NOT_FOUND);
            }
            return new Response("Location deleted successfully", Status.NO_CONTENT);

        } catch (Exception ex) {
            return new Response("Unexpected error: " + ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }
        
    public static Response delayFlight(String flightIdStr, String hoursDelayStr, String minutesDelayStr) {
        // Declara variables para los valores parseados
        int hoursDelay;
        int minutesDelay;

        // Obtener la instancia del Storage.
        // Ahora el controlador interactúa directamente con el Storage.
        FlightStorage storage;
        try {
            storage = FlightStorage.getInstance(); // Tu getInstance() puede lanzar Exception
        } catch (Exception e) {
            return new Response("Error accessing FlightStorage: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }

        // 1. Validar si flightIdStr es null o vacío ANTES de intentar usar trim() o matches()
        if (Objects.isNull(flightIdStr) || flightIdStr.trim().isEmpty()) {
            return new Response("Flight ID cannot be empty.", Status.BAD_REQUEST);
        }

        // Ahora que sabemos que flightIdStr no es null ni vacío, podemos usar trim() y matches()
        String trimmedFlightId = flightIdStr.trim(); // Crea una variable para el ID limpio

        if (!trimmedFlightId.matches("^[A-Z]{3}\\d{3}$")) {
            return new Response("Flight ID must follow the format XXXYYY (3 uppercase letters and 3 digits)", Status.BAD_REQUEST);
        }

        // 2. Buscar el vuelo en el Storage
        // ¡¡¡AQUÍ ESTÁ LA CORRECCIÓN!!! Cambiar getFlight por getFlightById
        Flight flightToDelay = storage.getFlightById(trimmedFlightId);
        if (flightToDelay == null) {
            return new Response("Flight with ID " + trimmedFlightId + " not found.", Status.NOT_FOUND);
        }

        // 3. Validar y parsear las horas y minutos de retraso
        try {
            hoursDelay = Integer.parseInt(hoursDelayStr);
            minutesDelay = Integer.parseInt(minutesDelayStr);

            if (hoursDelay < 0 || minutesDelay < 0 || minutesDelay > 59) {
                return new Response("Delay hours must be non-negative and minutes between 0-59.", Status.BAD_REQUEST);
            }
            if (hoursDelay == 0 && minutesDelay == 0) {
                return new Response("Delay duration cannot be zero.", Status.BAD_REQUEST);
            }

        } catch (NumberFormatException e) {
            return new Response("Delay time must be numeric", Status.BAD_REQUEST);
        }

        // 4. Aplicar el retraso al vuelo
        try {
            flightToDelay.delay(hoursDelay, minutesDelay);

            // Opcional pero recomendado: persistir el cambio si tu almacenamiento es a disco (JSON)
            // Tu FlightStorage tiene un método updateFlight, lo que sugiere que necesitas llamarlo
            // para guardar el cambio en el archivo JSON.
            if (!storage.updateFlight(flightToDelay)) {
                // Esto podría ocurrir si el vuelo se eliminó justo antes de intentar actualizarlo
                return new Response("Failed to save updated flight (ID not found during update).", Status.INTERNAL_SERVER_ERROR);
            }


            return new Response("Flight " + trimmedFlightId + " delayed successfully by " + hoursDelay + " hours and " + minutesDelay + " minutes.", Status.OK, flightToDelay);

        } catch (Exception e) {
            return new Response("Error delaying flight: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }

   
}
