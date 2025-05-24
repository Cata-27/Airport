package core.models.storage;

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

public class FlightStorage {
    private static List<Flight> flights = new ArrayList<>();

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

    // 3. Obtener todos los vuelos ordenados por fecha (de más antiguo a más reciente)
    public static List<Flight> getAll() {
        flights.sort(Comparator.comparing(Flight::getDepartureDate));
        return new ArrayList<>(flights); // Copia defensiva
    }

    // 4. Retrasar un vuelo (usa el método delay() de Flight)
    public static void delayFlight(String flightId, int hours, int minutes) {
        Flight flight = findById(flightId);
        if (flight != null) {
            flight.delay(hours, minutes); // Usa el método existente en Flight
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
}