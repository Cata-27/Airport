package core.models.storage;

import core.models.Flight;
import core.models.Plane;
import core.models.Location;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

public class FlightStorage {

    private static FlightStorage instance;
    private final List<Flight> flights;
    private static final String FILE_NAME = "flights.json";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private FlightStorage() throws Exception {
        this.flights = loadFlights();
    }

    public static FlightStorage getInstance() throws Exception {
        if (instance == null) {
            instance = new FlightStorage();
        }
        return instance;
    }

    private List<Flight> loadFlights() throws Exception {
        List<Flight> list = new ArrayList<>();

        JSONArray array = JsonManager.leerArregloJson(FILE_NAME); // ⬅ Cambio aquí
        PlaneStorage planeDeposit = PlaneStorage.getInstance();
        LocationStorage locationDeposit = LocationStorage.getInstance();

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);

            String id = obj.getString("id");
            Plane plane = planeDeposit.getPlaneById(obj.getString("plane"));
            Location departure = LocationStorage.findById(obj.getString("departureLocation"));
            Location arrival = locationDeposit.findById(obj.getString("arrivalLocation"));
            LocalDateTime date = LocalDateTime.parse(obj.getString("departureDate"), FORMATTER);
            int hoursArrival = obj.getInt("hoursDurationArrival");
            int minsArrival = obj.getInt("minutesDurationArrival");

            Flight flight;

            if (obj.has("scaleLocation") && !obj.isNull("scaleLocation")) {
                Location scale = locationDeposit.findById(obj.getString("scaleLocation"));
                int hoursScale = obj.getInt("hoursDurationScale");
                int minsScale = obj.getInt("minutesDurationScale");

                flight = new Flight(id, plane, departure, scale, arrival, date, hoursArrival, minsArrival, hoursScale, minsScale);
            } else {
                flight = new Flight(id, plane, departure, arrival, date, hoursArrival, minsArrival);
            }

            list.add(flight);
        }

        return list;
    }

    public List<Flight> getAllFlights() {
        return new ArrayList<>(flights);
    }

    public Flight getFlightById(String id) {
        for (Flight f : flights) {
            if (f.getId().equals(id)) {
                return f;
            }
        }
        return null;
    }

    public boolean addFlight(Flight flight) {
        if (getFlightById(flight.getId()) != null) {
            System.out.println("Ya existe un vuelo con el ID: " + flight.getId());
            return false;
        }
        flights.add(flight);
        System.out.println("Vuelo agregado: " + flight.getId());
        return true;
    }

    public boolean updateFlight(Flight updated) {
        for (int i = 0; i < flights.size(); i++) {
            if (flights.get(i).getId().equals(updated.getId())) {
                flights.set(i, updated);
                System.out.println("Vuelo actualizado: " + updated.getId());
                return true;
            }
        }
        System.out.println("No se encontró vuelo con ID: " + updated.getId());
        return false;
    }
}
