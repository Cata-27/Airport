package core.models.storage;

import core.models.Location;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocationStorage {
    private static LocationStorage instance;
    private final List<Location> locationList;
    private static final String FILE_NAME = "locations.json";

    private LocationStorage() throws Exception {
        this.locationList = new ArrayList<>();
        loadLocations();
    }

    public static LocationStorage getInstance() throws Exception {
        if (instance == null) {
            instance = new LocationStorage();
        }
        return instance;
    }

    private void loadLocations() throws Exception {
        JSONArray data = JsonManager.leerArregloJson(FILE_NAME);
        for (int i = 0; i < data.length(); i++) {
            JSONObject obj = data.getJSONObject(i);
            Location loc = new Location(
                obj.getString("airportId"),
                obj.getString("airportName"),
                obj.getString("airportCity"),
                obj.getString("airportCountry"),
                obj.getDouble("airportLatitude"),
                obj.getDouble("airportLongitude")
            );
            locationList.add(loc);
        }
    }

    public static Location findById(String airportId) {
        try {
            return getInstance().locationList.stream()
                    .filter(loc -> loc.getAirportId().equals(airportId))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            System.err.println("Error al buscar ubicación: " + e.getMessage());
            return null;
        }
    }

    public List<Location> getAll() {
        return new ArrayList<>(locationList);
    }

    public boolean registerLocation(Location location) {
        if (findById(location.getAirportId()) != null) {
            System.err.println("Ya existe una ubicación con el ID: " + location.getAirportId());
            return false;
        }

        locationList.add(location);
        System.out.println("Ubicación registrada (no guardada aún en archivo): " + location.getAirportId());
        return true;
    }
}