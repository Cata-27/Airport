package core.models.storage;

import core.models.Plane;
import core.observer.Observable;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class PlaneStorage extends Observable {

    private static List<Plane> planes = new ArrayList<>();
    private static PlaneStorage instance;

    public static void loadFromJson(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            JSONArray jsonArray = new JSONArray(new org.json.JSONTokener(reader));
            planes.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                Plane plane = new Plane(
                        json.getString("id"),
                        json.getString("brand"),
                        json.getString("model"),
                        json.getInt("maxCapacity"),
                        json.getString("airline")
                );
                planes.add(plane);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar aviones: " + e.getMessage());
        }
    }

    public static void save(Plane plane) {
        planes.add(plane);
    }

    public static Plane findById(String id) {
        for (Plane p : planes) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public static List<Plane> getAll() {
        planes.sort((p1, p2) -> p1.getId().compareTo(p2.getId()));
        return new ArrayList<>(planes);
    }

    public static PlaneStorage getInstance() {
        if (instance == null) {
            instance = new PlaneStorage();
        }
        return instance;
    }

    public boolean delPlane(long id) {
        String idStr = String.valueOf(id);
        return planes.removeIf(plane -> plane.getId().equals(idStr));
    }

    public Plane getPlane(long id) {
        String idStr = String.valueOf(id);
        for (Plane plane : planes) {
            if (plane.getId().equals(idStr)) {
                return plane;
            }
        }
        return null;
    }

    public boolean addPlane(Plane plane) {
        if (plane == null || findById(plane.getId()) != null) {
            return false; // Ya existe uno con ese ID o el objeto es inválido
        }
        planes.add(plane);
        return true;
    }
}
