package core.models.storage;

import core.models.Passenger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PassengerStorage {
    private static List<Passenger> passengers = new ArrayList<>();

    public static void loadFromJson(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            JSONArray jsonArray = new JSONArray(new org.json.JSONTokener(reader));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                Passenger passenger = new Passenger(
                    json.getLong("id"),
                    json.getString("firstname"),
                    json.getString("lastname"),
                    LocalDate.parse(json.getString("birthDate")),
                    json.getInt("countryPhoneCode"),
                    json.getLong("phone"),
                    json.getString("country")
                );
                passengers.add(passenger);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar pasajeros: " + e.getMessage());
        }
    }

    public static void save(Passenger passenger) {
        passengers.add(passenger);
    }

    public static Passenger findById(long id) {
        for (Passenger p : passengers) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public static List<Passenger> getAll() {
        passengers.sort((p1, p2) -> Long.compare(p1.getId(), p2.getId()));
        return new ArrayList<>(passengers);
    }
}