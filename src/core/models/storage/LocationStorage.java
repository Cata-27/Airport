package core.models.storage;

import core.models.Location;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class LocationStorage {
    private static List<Location> locations = new ArrayList<>();

    public static void loadFromJson(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            JSONArray jsonArray = new JSONArray(new org.json.JSONTokener(reader));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                Location location = new Location(
                    json.getString("airportId"),
                    json.getString("airportName"),
                    json.getString("airportCity"),
                    json.getString("airportCountry"),
                    json.getDouble("airportLatitude"),
                    json.getDouble("airportLongitude")
                );
                locations.add(location);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar ubicaciones: " + e.getMessage());
        }
    }

    public static void save(Location location) {
        locations.add(location);
    }

    public static Location findById(String airportId) {
        for (Location loc : locations) {
            if (loc.getAirportId().equals(airportId)) {
                return loc;
            }
        }
        return null;
    }

    public static List<Location> getAll() {
        locations.sort((loc1, loc2) -> loc1.getAirportId().compareTo(loc2.getAirportId()));
        return new ArrayList<>(locations);
    }
}