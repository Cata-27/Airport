package core.models.storage;

import core.models.Location;
import core.observer.Observable;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class LocationStorage extends Observable{

    private static List<Location> locations = new ArrayList<>();
    private static LocationStorage instance;

    public static void loadFromJson(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            JSONArray jsonArray = new JSONArray(new org.json.JSONTokener(reader));
            locations.clear();
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

    public static LocationStorage getInstance() {
        if (instance == null) {
            instance = new LocationStorage();
        }
        return instance;
    }

    public static void save(Location location) {
        locations.add(location);
    }

    public boolean addLocation(Location Location) {
        for (Location p : this.locations) {
            if (p.getAirportId() == Location.getAirportId()) {

                return false;
            }
        }
        this.locations.add(Location);
        return true;
    }

    public Location getLocation(int id) {
        for (Location location : this.locations) {
            if (location.getAirportId().equals(id)) {
                return location;
            }
        }
        return null;
    }
    
    public boolean delLocation(int id) {
        for (Location location : this.locations) {
            if (location.getAirportId().equals(id)) {
                this.locations.remove(location);
                return true;
            }
        }
        return false;
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
