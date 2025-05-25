package core.models.storage;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.Flight;
import core.models.Location;
import core.models.Passenger;
import core.models.Plane;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileReader;
import java.util.Collections;

public class FlightStorage {

    private static List<Plane> planes = new ArrayList<>();
    private static List<Location> locations = new ArrayList<>();
    private static List<Flight> flights = new ArrayList<>();
    private static FlightStorage instance;

    // 1. Guardar vuelo (sin validaciones)
    public static void save(Flight flight) {
        flights.add(flight);
    }

    // 2. Buscar vuelo por ID
    public static Flight findById(String id) {
        for (Flight flight : flights) {
            if (flight.getId().equals(id)) {
                return flight;
            }
        }
        return null;
    }

    public static FlightStorage getInstance() {
        if (instance == null) {
            instance = new FlightStorage();
        }
        return instance;
    }

    // 3. Obtener todos los vuelos ordenados por fecha (de más antiguo a más reciente)
    public List<Flight> getAllFlights() {
        List<Flight> sortedFlights = new ArrayList<>(this.flights);
        Collections.sort(sortedFlights, new Comparator<Flight>() {
            @Override
            public int compare(Flight f1, Flight f2) {
                return f1.getDepartureDate().compareTo(f2.getDepartureDate());
            }
        });

        return sortedFlights;
    }

    public Response delayFlight(String flightId, String delayHours, String delayMinutes) {

        // Validar ID de vuelo
        if (flightId == null || !flightId.matches("^[A-Z]{3}\\d{3}$")) {
            return new Response("Flight ID must follow the format XXXYYY (3 uppercase letters and 3 digits)", Status.BAD_REQUEST);
        }
        FlightStorage storage = FlightStorage.getInstance();
        Flight flight = storage.getFlightById(flightId);
        if (flight == null) {
            return new Response("Flight not found", Status.NOT_FOUND);
        }

        int hours, minutes;
        try {
            hours = Integer.parseInt(delayHours);
            minutes = Integer.parseInt(delayMinutes);

            if (hours < 0 || minutes < 0 || (hours == 0 && minutes == 0)) {
                return new Response("Delay time must be greater than 00:00", Status.BAD_REQUEST);
            }

        } catch (NumberFormatException e) {
            return new Response("Delay time must be numeric", Status.BAD_REQUEST);
        }

        try {
            flight.delay(hours, minutes);
            return new Response("Flight delayed successfully", Status.OK, flight);
        } catch (Exception e) {
            return new Response("Error delaying flight", Status.INTERNAL_SERVER_ERROR);
        }
    }
    
    // 5. Añadir pasajero a un vuelo
    public static void addPassengerToFlight(String flightId, Passenger passenger) {
        Flight flight = findById(flightId);
        if (flight != null) {
            flight.addPassenger(passenger); // Usa el método de Flight
        }
    }

    // 6. Cargar datos iniciales desde JSON
    public static void loadFromJson(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            JSONArray jsonArray = new JSONArray(new org.json.JSONTokener(reader));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);

                // Obtener objetos necesarios (ej: Plane y Locations deben estar previamente cargados)
                Plane plane = PlaneStorage.findById(json.getString("planeId"));
                Location departureLoc = LocationStorage.findById(json.getString("departureLocation"));
                Location arrivalLoc = LocationStorage.findById(json.getString("arrivalLocation"));

                // Crear el vuelo (sin escala)
                Flight flight = new Flight(
                        json.getString("id"),
                        plane,
                        departureLoc,
                        arrivalLoc,
                        LocalDateTime.parse(json.getString("departureDate")),
                        json.getInt("hoursDurationArrival"),
                        json.getInt("minutesDurationArrival")
                );

                // Si tiene escala
                if (json.has("scaleLocation")) {
                    Location scaleLoc = LocationStorage.findById(json.getString("scaleLocation"));
                    flight = new Flight(
                            json.getString("id"),
                            plane,
                            departureLoc,
                            scaleLoc,
                            arrivalLoc,
                            LocalDateTime.parse(json.getString("departureDate")),
                            json.getInt("hoursDurationArrival"),
                            json.getInt("minutesDurationArrival"),
                            json.getInt("hoursDurationScale"),
                            json.getInt("minutesDurationScale")
                    );
                }

                flights.add(flight);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar vuelos desde JSON: " + e.getMessage());
        }
    }

    private FlightStorage() {
        this.flights = new ArrayList<>();
    }

    public Location getLocation(String airportId) {
        for (Location location : this.locations) {
            if (location.getAirportId().equals(airportId)) {
                return location;
            }
        }
        return null;
    }

    public Flight getFlightById(String idStr) {
        for (Flight flight : this.flights) {
            if (flight.getId().equals(idStr)) {
                return flight;
            }
        }
        return null;
    }

    public Plane getPlane(String Id) {
        for (Plane plane : this.planes) {
            if (plane.getId() == Id) {
                return plane;
            }
        }
        return null;
    }

    public boolean addFlight(Flight flight) {
        for (Flight f : this.flights) {
            if (f.getId().equals(flight.getId())) {
                return false;
            }
        }
        this.flights.add(flight);
        return true;
    }

    public Flight getFlight(String id) {
        for (Flight f : this.flights) {
            if (f.getId().equals(id)) {
                return f;
            }
        }
        return null; // Flight not found
    }

}
