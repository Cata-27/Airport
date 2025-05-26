package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.Flight;
import core.models.Location;
import core.models.Plane;
import core.models.storage.FlightStorage;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
public class FlightController {

    public static Response createFlight(
        String idStr,
        String planeIdStr, 
        String departureLocationIdStr, 
        String arrivalLocationIdStr, 
        String scaleLocationIdStr,
        String yearStr, String monthStr, String dayStr, String hourStr, String minuteStr, 
        String hoursDurationsArrivalStr, String minutesDurationsArrivalStr,
        String hoursDurationsScaleStr, String minutesDurationsScaleStr
    ) {
        LocalDateTime departureDate; 
        int hoursDurationArrival; 
        int minutesDurationArrival;
        int hoursDurationScale = 0;  
        int minutesDurationScale = 0;

        Plane plane;    
        Location origin;  
        Location destination;  
        Location scale = null;      

        try {
            // Validar formato de ID de vuelo
            if (idStr == null || !idStr.matches("[A-Z]{3}[0-9]{3}")) {
                return new Response("Flight ID must follow format XXX999", Status.BAD_REQUEST);
            }

            FlightStorage storage = FlightStorage.getInstance();
            if (storage.getFlightById(idStr) != null) { 
                return new Response("Flight with this ID already exists", Status.BAD_REQUEST);
            }
            // Validar avión
            plane = storage.getPlane(planeIdStr);
            if (plane == null) {
                return new Response("Plane not found", Status.BAD_REQUEST);
            }
            // Validar localizaciones origen y destino
            origin = storage.getLocation(departureLocationIdStr);
            destination = storage.getLocation(arrivalLocationIdStr); 
            if (origin == null || destination == null) {
                return new Response("Origin or destination location not found", Status.BAD_REQUEST);
            }
            // Validar escala
            if (scaleLocationIdStr != null && !scaleLocationIdStr.isBlank()) {
                scale = storage.getLocation(scaleLocationIdStr); 
                if (scale == null) {
                    return new Response("Scale location not found", Status.BAD_REQUEST);
                }
                try {
                    hoursDurationScale = Integer.parseInt(hoursDurationsScaleStr); 
                    minutesDurationScale = Integer.parseInt(minutesDurationsScaleStr); 
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

                // Validación simple de rango para fecha/hora
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
                hoursDurationArrival = Integer.parseInt(hoursDurationsArrivalStr);
                minutesDurationArrival = Integer.parseInt(minutesDurationsArrivalStr); 
                
                if (hoursDurationArrival < 0 || minutesDurationArrival < 0 || minutesDurationArrival >= 60 || (hoursDurationArrival == 0 && minutesDurationArrival == 0)) {
                    return new Response("Duration must be greater than 00:00 and minutes less than 60", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException e) {
                return new Response("Duration must be numeric", Status.BAD_REQUEST);
            }
            Flight flight;
            if (scale == null) {
                flight = new Flight(
                    idStr,
                    plane,
                    origin,
                    destination,
                    departureDate,
                    hoursDurationArrival,
                    minutesDurationArrival 
                );
            } else {
                flight = new Flight(
                    idStr, 
                    plane,
                    origin,
                    scale, 
                    destination,
                    departureDate,
                    hoursDurationArrival,
                    minutesDurationArrival,
                    hoursDurationScale,
                    minutesDurationScale
                );
            }

            if (!storage.addFlight(flight)) {
                return new Response("Error saving flight (possibly duplicate ID or internal storage issue)", Status.INTERNAL_SERVER_ERROR);
            }

            return new Response("Flight created successfully", Status.CREATED);

        } catch (Exception ex) {
            ex.printStackTrace(); 
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
                if (h < 0 || m < 0 || m >= 60) { 
                    return new Response("Delay time must be positive and minutes less than 60", Status.BAD_REQUEST);
                }
                if (h == 0 && m == 0) {
                    return new Response("Delay time must be greater than 00:00", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException e) {
                return new Response("Delay time must be numeric", Status.BAD_REQUEST);
            }
            flight.delay(h, m);
            return new Response("Flight delayed successfully", Status.OK);

        } catch (Exception ex) {
            return new Response("Unexpected error during flight delay: " + ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }
}
