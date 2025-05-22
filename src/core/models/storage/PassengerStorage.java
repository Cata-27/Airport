package core.models.storage;

import core.models.Passenger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PassengerStorage {

    private static PassengerStorage instance;  // Singleton
    private List<Passenger> passengers;
    private final String FILENAME = "../json/passengers.json";
    private final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    // Constructor privado (Singleton)
    private PassengerStorage() throws Exception {
        passengers = new ArrayList<>();
        loadPassengersFromJson();
    }

    //  Método para obtener la instancia única
    public static PassengerStorage getInstance() throws Exception {
        if (instance == null) {
            instance = new PassengerStorage();
        }
        return instance;
    }

    //  Cargar pasajeros desde archivo JSON
    private void loadPassengersFromJson() throws Exception {
        JSONArray array = JsonManager.leerArregloJson(FILENAME);
        for (int i = 0; i < array.length(); i++) {
            JSONObject json = array.getJSONObject(i);
            Passenger p = new Passenger(
                    json.getLong("id"),
                    json.getString("firstname"),
                    json.getString("lastname"),
                    LocalDate.parse(json.getString("birthDate"), FORMATTER),
                    json.getInt("countryPhoneCode"),
                    json.getLong("phone"),
                    json.getString("country")
            );
            passengers.add(p);
        }
    }

    // Buscar pasajero por ID
    public Passenger getById(long id) {
        for (Passenger p : passengers) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    // Devolver todos los pasajeros
    public List<Passenger> getAll() {
        return new ArrayList<>(passengers);  // Copia defensiva
    }

    // Agregar un nuevo pasajero
    public boolean add(Passenger p) {
        if (getById(p.getId()) == null) { // Validar que no esté repetido
            passengers.add(p);
            System.out.println("Pasajero agregado: " + p.getFirstname());
            return true;
        } else {
            System.out.println("Ya existe un pasajero con el ID: " + p.getId());
            return false;
        }
    }
}
