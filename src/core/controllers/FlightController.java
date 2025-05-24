package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.Flight;
import core.models.Location;
import core.models.Plane;
import core.models.storage.FlightStorage;
import core.models.storage.LocationStorage;
import core.models.storage.PassengerStorage;
import core.models.storage.PlaneStorage;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class FlightController {

    public static Response createFlight(
        String idStr, // ID del vuelo (String)
        String planeIdStr, // ID del avión (String)
        String departureLocationIdStr, // ID de la ubicación de salida (String)
        String arrivalLocationIdStr,   // ID de la ubicación de llegada (String)
        String scaleLocationIdStr,     // ID de la ubicación de escala (String)
        String yearStr, String monthStr, String dayStr, String hourStr, String minuteStr, // Componentes de fecha/hora (Strings)
        String hoursDurationsArrivalStr, String minutesDurationsArrivalStr, // Duración de llegada (Strings)
        String hoursDurationsScaleStr, String minutesDurationsScaleStr    // Duración de escala (Strings)
    ) {
        // DECLARAR VARIABLES AL INICIO PARA QUE ESTÉN EN EL ÁMBITO CORRECTO
        LocalDateTime departureDate; // Variable para la fecha y hora de salida
        int hoursDurationArrival;    // Variables para la duración de llegada
        int minutesDurationArrival;
        int hoursDurationScale = 0;  // Variables para la duración de escala (inicializadas a 0 por defecto)
        int minutesDurationScale = 0;

        Plane plane;                 // Variable para el objeto Plane
        Location origin;             // Variable para el objeto Location de origen
        Location destination;        // Variable para el objeto Location de destino
        Location scale = null;       // Variable para el objeto Location de escala (puede ser null)

        try {
            // Validar formato de ID de vuelo
            if (idStr == null || !idStr.matches("[A-Z]{3}[0-9]{3}")) {
                return new Response("Flight ID must follow format XXX999", Status.BAD_REQUEST);
            }

            FlightStorage storage = FlightStorage.getInstance();
            if (storage.getFlightById(idStr) != null) { // Usar idStr aquí
                return new Response("Flight with this ID already exists", Status.BAD_REQUEST);
            }

            // Validar avión
            // Asume que storage.getPlane(planeIdStr) devuelve un objeto Plane o null
            plane = storage.getPlane(planeIdStr);
            if (plane == null) {
                return new Response("Plane not found", Status.BAD_REQUEST);
            }

            // Validar localizaciones origen y destino
            // Asume que storage.getLocation(id) devuelve un objeto Location o null
            origin = storage.getLocation(departureLocationIdStr); // Usar departureLocationIdStr
            destination = storage.getLocation(arrivalLocationIdStr); // Usar arrivalLocationIdStr
            if (origin == null || destination == null) {
                return new Response("Origin or destination location not found", Status.BAD_REQUEST);
            }

            // Validar escala (opcional)
            if (scaleLocationIdStr != null && !scaleLocationIdStr.isBlank()) {
                scale = storage.getLocation(scaleLocationIdStr); // Usar scaleLocationIdStr
                if (scale == null) {
                    return new Response("Scale location not found", Status.BAD_REQUEST);
                }
                try {
                    hoursDurationScale = Integer.parseInt(hoursDurationsScaleStr); // Usar hoursDurationsScaleStr
                    minutesDurationScale = Integer.parseInt(minutesDurationsScaleStr); // Usar minutesDurationsScaleStr
                    if (hoursDurationScale < 0 || minutesDurationScale < 0 || minutesDurationScale >= 60) {
                        return new Response("Scale time must be positive and minutes less than 60", Status.BAD_REQUEST);
                    }
                } catch (NumberFormatException e) {
                    return new Response("Scale time must be numeric", Status.BAD_REQUEST);
                }
            }

            // Validar fecha de salida
            try {
                int year = Integer.parseInt(yearStr);
                int month = Integer.parseInt(monthStr);
                int day = Integer.parseInt(dayStr);
                int hour = Integer.parseInt(hourStr);
                int minute = Integer.parseInt(minuteStr);

                // Validación simple de rango para fecha/hora (puedes añadir más robustas)
                if (year < 1900 || month < 1 || month > 12 || day < 1 || day > 31 || hour < 0 || hour > 23 || minute < 0 || minute > 59) {
                     return new Response("Invalid date or time component.", Status.BAD_REQUEST);
                }
                departureDate = LocalDateTime.of(year, month, day, hour, minute);
            } catch (NumberFormatException e) {
                return new Response("Date and time components must be numeric.", Status.BAD_REQUEST);
            } catch (DateTimeParseException e) {
                return new Response("Invalid date and time format.", Status.BAD_REQUEST);
            }


            // Validar duración de llegada
            try {
                hoursDurationArrival = Integer.parseInt(hoursDurationsArrivalStr); // Usar hoursDurationsArrivalStr
                minutesDurationArrival = Integer.parseInt(minutesDurationsArrivalStr); // Usar minutesDurationsArrivalStr
                
                if (hoursDurationArrival < 0 || minutesDurationArrival < 0 || minutesDurationArrival >= 60 || (hoursDurationArrival == 0 && minutesDurationArrival == 0)) {
                    return new Response("Duration must be greater than 00:00 and minutes less than 60", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException e) {
                return new Response("Duration must be numeric", Status.BAD_REQUEST);
            }

            // CREAR EL VUELO - Ahora las variables están correctamente declaradas y asignadas
            Flight flight;
            if (scale == null) {
                // Constructor sin escala
                flight = new Flight(
                    idStr, // ID del vuelo (String)
                    plane,
                    origin,
                    destination,
                    departureDate, // LocalDateTime
                    hoursDurationArrival, // int
                    minutesDurationArrival // int
                );
            } else {
                // Constructor con escala
                flight = new Flight(
                    idStr, // ID del vuelo (String)
                    plane,
                    origin,
                    scale, // Ubicación de escala (Location)
                    destination, // Ubicación de llegada (Location)
                    departureDate, // LocalDateTime
                    hoursDurationArrival, // int
                    minutesDurationArrival, // int
                    hoursDurationScale, // int
                    minutesDurationScale // int
                );
            }

            if (!storage.addFlight(flight)) {
                return new Response("Error saving flight (possibly duplicate ID or internal storage issue)", Status.INTERNAL_SERVER_ERROR);
            }

            return new Response("Flight created successfully", Status.CREATED);

        } catch (Exception ex) {
            // Captura cualquier otra excepción inesperada
            ex.printStackTrace(); // Imprime el stack trace para depuración
            return new Response("An unexpected error occurred: " + ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response delayFlight(String id, String hours, String minutes) {
        try {
            FlightStorage storage = FlightStorage.getInstance();
            Flight flight = storage.getFlightById(id);

            if (flight == null) {
                return new Response("Flight not found", Status.NOT_FOUND);
            }

            int h, m;
            try {
                h = Integer.parseInt(hours);
                m = Integer.parseInt(minutes);
                if (h < 0 || m < 0 || m >= 60) { // Añadida validación para que horas/minutos no sean negativos y minutos < 60
                    return new Response("Delay time must be positive and minutes less than 60", Status.BAD_REQUEST);
                }
                if (h == 0 && m == 0) {
                    return new Response("Delay time must be greater than 00:00", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException e) {
                return new Response("Delay time must be numeric", Status.BAD_REQUEST);
            }

            // Asegúrate de que el método en tu clase Flight se llama 'delay'
            flight.delay(h, m);
            return new Response("Flight delayed successfully", Status.OK);

        } catch (Exception ex) {
            return new Response("Unexpected error during flight delay: " + ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }
}
