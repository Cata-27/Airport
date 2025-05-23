package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.Flight;
import core.models.Location;
import core.models.Plane;
import core.models.storage.FlightStorage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class FlightController {

    public static Response createFlight(
            String idStr, String planeIdStr,
            String departureLocationIdStr, String arrivalLocationIdStr, String scaleLocationIdStr,
            String yearStr, String monthStr, String dayStr, String hourStr, String minutesStr,
            String hoursDurationsArrivalStr, String minutesDurationsArrivalStr,
            String hoursDurationsScaleStr, String minutesDurationsScaleStr
    ) {
        try {
            long id;
            long planeId;
            int departureLocationId, arrivalLocationId;
            Integer scaleLocationId = null;
            int year, month, day, hour, minutes;
            int hoursDurationsArrival, minutesDurationsArrival;
            Integer hoursDurationsScale = null;
            Integer minutesDurationsScale = null;
            LocalDateTime departureDateTime;

            FlightStorage storage = FlightStorage.getInstance();

            try {
                id = Long.parseLong(idStr);
                if (id < 0) {
                    return new Response("Flight ID must be positive", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Flight ID must be numeric", Status.BAD_REQUEST);
            }

            try {
                planeId = Long.parseLong(planeIdStr);
                if (planeId < 0) {
                    return new Response("Plane ID must be positive", Status.BAD_REQUEST);
                }
                Plane plane = storage.getPlane(planeId);
                if (plane == null) {
                    return new Response("Plane with provided ID not found", Status.NOT_FOUND);
                }
            } catch (NumberFormatException ex) {
                return new Response("Plane ID must be numeric", Status.BAD_REQUEST);
            }

            try {
                departureLocationId = Integer.parseInt(departureLocationIdStr);
                if (departureLocationId < 0) {
                    return new Response("Departure location ID must be positive", Status.BAD_REQUEST);
                }
                Location depLoc = storage.getLocation(departureLocationId);
                if (depLoc == null) {
                    return new Response("Departure location not found", Status.NOT_FOUND);
                }
            } catch (NumberFormatException ex) {
                return new Response("Departure location ID must be numeric", Status.BAD_REQUEST);
            }

            try {
                arrivalLocationId = Integer.parseInt(arrivalLocationIdStr);
                if (arrivalLocationId < 0) {
                    return new Response("Arrival location ID must be positive", Status.BAD_REQUEST);
                }
                Location arrLoc = storage.getLocation(arrivalLocationId);
                if (arrLoc == null) {
                    return new Response("Arrival location not found", Status.NOT_FOUND);
                }
            } catch (NumberFormatException ex) {
                return new Response("Arrival location ID must be numeric", Status.BAD_REQUEST);
            }

            if (departureLocationId == arrivalLocationId) {
                return new Response("Departure and arrival locations cannot be the same", Status.BAD_REQUEST);
            }

            if (Objects.nonNull(scaleLocationIdStr) && !scaleLocationIdStr.trim().isEmpty()) {
                try {
                    scaleLocationId = Integer.parseInt(scaleLocationIdStr);
                    if (scaleLocationId < 0) {
                        return new Response("Scale location ID must be positive or empty", Status.BAD_REQUEST);
                    }
                    Location scaleLoc = storage.getLocation(scaleLocationId);
                    if (scaleLoc == null) {
                        return new Response("Scale location not found", Status.NOT_FOUND);
                    }
                    if (scaleLocationId == departureLocationId || scaleLocationId == arrivalLocationId) {
                        return new Response("Scale location cannot be the same as departure or arrival location", Status.BAD_REQUEST);
                    }
                } catch (NumberFormatException ex) {
                    return new Response("Scale location ID must be numeric or empty", Status.BAD_REQUEST);
                }
            }
            try {
                year = Integer.parseInt(yearStr);
                month = Integer.parseInt(monthStr);
                day = Integer.parseInt(dayStr);
                hour = Integer.parseInt(hourStr);
                minutes = Integer.parseInt(minutesStr);

                if (year < LocalDate.now().getYear() || year > (LocalDate.now().getYear() + 10)) { // Ejemplo: hasta 10 años en el futuro
                    return new Response("Departure year is invalid", Status.BAD_REQUEST);
                }
                if (month < 1 || month > 12) {
                    return new Response("Departure month is invalid", Status.BAD_REQUEST);
                }
                if (day < 1 || day > 31) {
                    return new Response("Departure day is invalid", Status.BAD_REQUEST);
                }
                if (hour < 0 || hour > 23) {
                    return new Response("Departure hour is invalid (0-23)", Status.BAD_REQUEST);
                }
                if (minutes < 0 || minutes > 59) {
                    return new Response("Departure minutes are invalid (0-59)", Status.BAD_REQUEST);
                }

                departureDateTime = LocalDateTime.of(year, month, day, hour, minutes);

                if (departureDateTime.isBefore(LocalDateTime.now())) {
                    return new Response("Departure date and time cannot be in the past", Status.BAD_REQUEST);
                }

            } catch (NumberFormatException ex) {
                return new Response("Departure date/time components must be numeric", Status.BAD_REQUEST);
            } catch (DateTimeParseException ex) {
                return new Response("Invalid departure date/time provided. Please check values.", Status.BAD_REQUEST);
            }

            try {
                hoursDurationsArrival = Integer.parseInt(hoursDurationsArrivalStr);
                minutesDurationsArrival = Integer.parseInt(minutesDurationsArrivalStr);

                if (hoursDurationsArrival < 0 || minutesDurationsArrival < 0 || minutesDurationsArrival > 59) {
                    return new Response("Arrival duration must be positive and minutes between 0-59", Status.BAD_REQUEST);
                }
                if (hoursDurationsArrival == 0 && minutesDurationsArrival == 0) {
                    return new Response("Arrival duration cannot be zero", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Arrival duration components must be numeric", Status.BAD_REQUEST);
            }

            // --- 6. Validación y parseo de Duración de Escala (Opcional) ---
            if (Objects.nonNull(hoursDurationsScaleStr) && !hoursDurationsScaleStr.trim().isEmpty()
                    && Objects.nonNull(minutesDurationsScaleStr) && !minutesDurationsScaleStr.trim().isEmpty()) {
                try {
                    hoursDurationsScale = Integer.parseInt(hoursDurationsScaleStr);
                    minutesDurationsScale = Integer.parseInt(minutesDurationsScaleStr);

                    if (hoursDurationsScale < 0 || minutesDurationsScale < 0 || minutesDurationsScale > 59) {
                        return new Response("Scale duration must be positive and minutes between 0-59, or empty", Status.BAD_REQUEST);
                    }
                    if (hoursDurationsScale == 0 && minutesDurationsScale == 0) {
                        return new Response("Scale duration cannot be zero if provided", Status.BAD_REQUEST);
                    }
                } catch (NumberFormatException ex) {
                    return new Response("Scale duration components must be numeric or empty", Status.BAD_REQUEST);
                }
            } else if ((Objects.nonNull(hoursDurationsScaleStr) && !hoursDurationsScaleStr.trim().isEmpty())
                    || (Objects.nonNull(minutesDurationsScaleStr) && !minutesDurationsScaleStr.trim().isEmpty())) {
                return new Response("Both hours and minutes for scale duration must be provided or both empty", Status.BAD_REQUEST);
            }
            int hoursDurationScale = 0;
            int minutesDurationScale = 0;
            int hoursDurationArrival = 0;
            int minutesDurationArrival =0;
            Flight newFlight = new Flight(
                    id,
                    planeId,
                    departureLocationId,
                    scaleLocationId, // Orden de scaleLocationId y arrivalLocationId según tu ejemplo
                    arrivalLocationId,
                    departureDateTime,
                    hoursDurationArrival,
                    minutesDurationArrival,
                    hoursDurationScale,
                    minutesDurationScale
            );


            if (!storage.addFlight(newFlight)) {
                return new Response("A flight with that ID already exists", Status.BAD_REQUEST);
            }
            return new Response("Flight created successfully", Status.CREATED);

        } catch (Exception ex) {
            return new Response("Unexpected error during flight creation: " + ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response readFlight(String idStr) {
        try {
            long id;
            try {
                id = Long.parseLong(idStr);
                if (id < 0) {
                    return new Response("Flight ID must be positive", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Flight ID must be numeric", Status.BAD_REQUEST);
            }

            FlightStorage storage = FlightStorage.getInstance();
            Flight flight = storage.getFlight(id);

            if (flight == null) {
                return new Response("Flight not found", Status.NOT_FOUND);
            }
            return new Response("Flight found", Status.OK, flight);

        } catch (Exception ex) {
            return new Response("Unexpected error during flight retrieval: " + ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response updateFlight(
            String idStr, String planeIdStr,
            String departureLocationIdStr, String arrivalLocationIdStr, String scaleLocationIdStr,
            String yearStr, String monthStr, String dayStr, String hourStr, String minutesStr,
            String hoursDurationsArrivalStr, String minutesDurationsArrivalStr,
            String hoursDurationsScaleStr, String minutesDurationsScaleStr
    ) {
        try {
            long id;
            long planeId;
            int departureLocationId, arrivalLocationId;
            Integer scaleLocationId = null;
            int year, month, day, hour, minutes;
            int hoursDurationsArrival, minutesDurationsArrival;
            Integer hoursDurationsScale = null;
            Integer minutesDurationsScale = null;
            LocalDateTime departureDateTime;

            FlightStorage storage = FlightStorage.getInstance();
            try {
                id = Long.parseLong(idStr);
                if (id < 0) {
                    return new Response("Flight ID must be positive", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Flight ID must be numeric", Status.BAD_REQUEST);
            }

            Flight existingFlight = storage.getFlightById(idStr);
            if (existingFlight == null) {
                return new Response("Flight not found", Status.NOT_FOUND);
            }
            try {
                planeId = Long.parseLong(planeIdStr);
                if (planeId < 0) {
                    return new Response("Plane ID must be positive", Status.BAD_REQUEST);
                }
                Plane plane = storage.getPlane(planeId);
                if (plane == null) {
                    return new Response("Plane with provided ID not found", Status.NOT_FOUND);
                }
            } catch (NumberFormatException ex) {
                return new Response("Plane ID must be numeric", Status.BAD_REQUEST);
            }
            try {
                departureLocationId = Integer.parseInt(departureLocationIdStr);
                if (departureLocationId < 0) {
                    return new Response("Departure location ID must be positive", Status.BAD_REQUEST);
                }
                Location depLoc = storage.getLocation(departureLocationId);
                if (depLoc == null) {
                    return new Response("Departure location not found", Status.NOT_FOUND);
                }
            } catch (NumberFormatException ex) {
                return new Response("Departure location ID must be numeric", Status.BAD_REQUEST);
            }

            try {
                arrivalLocationId = Integer.parseInt(arrivalLocationIdStr);
                if (arrivalLocationId < 0) {
                    return new Response("Arrival location ID must be positive", Status.BAD_REQUEST);
                }
                Location arrLoc = storage.getLocation(arrivalLocationId);
                if (arrLoc == null) {
                    return new Response("Arrival location not found", Status.NOT_FOUND);
                }
            } catch (NumberFormatException ex) {
                return new Response("Arrival location ID must be numeric", Status.BAD_REQUEST);
            }

            if (departureLocationId == arrivalLocationId) {
                return new Response("Departure and arrival locations cannot be the same", Status.BAD_REQUEST);
            }

            if (Objects.nonNull(scaleLocationIdStr) && !scaleLocationIdStr.trim().isEmpty()) {
                try {
                    scaleLocationId = Integer.parseInt(scaleLocationIdStr);
                    if (scaleLocationId < 0) {
                        return new Response("Scale location ID must be positive or empty", Status.BAD_REQUEST);
                    }
                    Location scaleLoc = storage.getLocation(scaleLocationId);
                    if (scaleLoc == null) {
                        return new Response("Scale location not found", Status.NOT_FOUND);
                    }
                    if (scaleLocationId == departureLocationId || scaleLocationId == arrivalLocationId) {
                        return new Response("Scale location cannot be the same as departure or arrival location", Status.BAD_REQUEST);
                    }
                } catch (NumberFormatException ex) {
                    return new Response("Scale location ID must be numeric or empty", Status.BAD_REQUEST);
                }
            }
            try {
                year = Integer.parseInt(yearStr);
                month = Integer.parseInt(monthStr);
                day = Integer.parseInt(dayStr);
                hour = Integer.parseInt(hourStr);
                minutes = Integer.parseInt(minutesStr);

                if (year < LocalDate.now().getYear() || year > (LocalDate.now().getYear() + 10)) {
                    return new Response("Departure year is invalid", Status.BAD_REQUEST);
                }
                if (month < 1 || month > 12) {
                    return new Response("Departure month is invalid", Status.BAD_REQUEST);
                }
                if (day < 1 || day > 31) {
                    return new Response("Departure day is invalid", Status.BAD_REQUEST);
                }
                if (hour < 0 || hour > 23) {
                    return new Response("Departure hour is invalid (0-23)", Status.BAD_REQUEST);
                }
                if (minutes < 0 || minutes > 59) {
                    return new Response("Departure minutes are invalid (0-59)", Status.BAD_REQUEST);
                }

                departureDateTime = LocalDateTime.of(year, month, day, hour, minutes);

                if (departureDateTime.isBefore(LocalDateTime.now())) {
                    return new Response("Departure date and time cannot be in the past", Status.BAD_REQUEST);
                }

            } catch (NumberFormatException ex) {
                return new Response("Departure date/time components must be numeric", Status.BAD_REQUEST);
            } catch (DateTimeParseException ex) {
                return new Response("Invalid departure date/time provided. Please check values.", Status.BAD_REQUEST);
            }
            try {
                hoursDurationsArrival = Integer.parseInt(hoursDurationsArrivalStr);
                minutesDurationsArrival = Integer.parseInt(minutesDurationsArrivalStr);

                if (hoursDurationsArrival < 0 || minutesDurationsArrival < 0 || minutesDurationsArrival > 59) {
                    return new Response("Arrival duration must be positive and minutes between 0-59", Status.BAD_REQUEST);
                }
                if (hoursDurationsArrival == 0 && minutesDurationsArrival == 0) {
                    return new Response("Arrival duration cannot be zero", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Arrival duration components must be numeric", Status.BAD_REQUEST);
            }
            if (Objects.nonNull(hoursDurationsScaleStr) && !hoursDurationsScaleStr.trim().isEmpty()
                    && Objects.nonNull(minutesDurationsScaleStr) && !minutesDurationsScaleStr.trim().isEmpty()) {
                try {
                    hoursDurationsScale = Integer.parseInt(hoursDurationsScaleStr);
                    minutesDurationsScale = Integer.parseInt(minutesDurationsScaleStr);

                    if (hoursDurationsScale < 0 || minutesDurationsScale < 0 || minutesDurationsScale > 59) {
                        return new Response("Scale duration must be positive and minutes between 0-59, or empty", Status.BAD_REQUEST);
                    }
                    if (hoursDurationsScale == 0 && minutesDurationsScale == 0) {
                        return new Response("Scale duration cannot be zero if provided", Status.BAD_REQUEST);
                    }
                } catch (NumberFormatException ex) {
                    return new Response("Scale duration components must be numeric or empty", Status.BAD_REQUEST);
                }
            } else if ((Objects.nonNull(hoursDurationsScaleStr) && !hoursDurationsScaleStr.trim().isEmpty())
                    || (Objects.nonNull(minutesDurationsScaleStr) && !minutesDurationsScaleStr.trim().isEmpty())) {
                return new Response("Both hours and minutes for scale duration must be provided or both empty", Status.BAD_REQUEST);
            }
            return new Response("Flight data updated successfully", Status.OK);

        } catch (Exception ex) {
            return new Response("Unexpected error during flight update: " + ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
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
    
    public static Response deleteFlight(String idStr) {
        try {
            long id;
            try {
                id = Long.parseLong(idStr);
                if (id < 0) {
                    return new Response("Flight ID must be positive", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Flight ID must be numeric", Status.BAD_REQUEST);
            }

            FlightStorage storage = FlightStorage.getInstance();
            if (!storage.delFlight(id)) {
                return new Response("Flight not found", Status.NOT_FOUND);
            }
            return new Response("Flight deleted successfully", Status.NO_CONTENT);

        } catch (Exception ex) {
            return new Response("Unexpected error during flight deletion: " + ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }

}
