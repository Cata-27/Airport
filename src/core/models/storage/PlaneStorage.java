/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.storage;

import core.models.Plane;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author 57304
 */
public class PlaneStorage {

    private static PlaneStorage instance;
    private List<Plane> planes;
    private static final String FILENAME = "planes.json";

    private PlaneStorage() throws Exception {
        this.planes = loadFromJson();
    }

    public static PlaneStorage getInstance() throws Exception {
        if (instance == null) {
            instance = new PlaneStorage();
        }
        return instance;
    }

    private List<Plane> loadFromJson() throws Exception {
        List<Plane> planes = new ArrayList<>();
        JSONArray jsonArray = JsonManager.leerArregloJson(FILENAME);

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
        return planes;
    }

    public Plane getPlane(int id) {
        return planes.stream()
                .filter(plane -> plane.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Plane> getAllPlanes() {
        return new ArrayList<>(planes);
    }

    public boolean addPlane(Plane newPlane) {
        try {
            // Verificación adicional
            if (planes.stream().anyMatch(p -> p.getId().equals(newPlane.getId()))) {
                System.err.println("Ya existe un avión con ese ID");
                return false;
            }

            planes.add(newPlane);
            System.out.println("Avión añadido  " + newPlane.getId());
            return true;

        } catch (Exception e) {
            System.err.println("Error al añadir avión: " + e.getMessage());
            return false;
        }

    }

    public boolean delPlane(int idInt) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    Plane getPlaneById(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
