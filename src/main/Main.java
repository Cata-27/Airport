package main;

import core.views.AirportFrame;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.UIManager;
import core.models.storage.PassengerStorage;
import core.models.storage.PlaneStorage;
import core.models.storage.LocationStorage;
import core.models.storage.FlightStorage;

public class Main {

    public static void main(String args[]) {
        System.setProperty("flatlaf.useNativeLibrary", "false");

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        // Cargar los JSON a memoria una sola vez al arrancar la app
        PassengerStorage.loadFromJson("json/passengers.json");
        PlaneStorage.loadFromJson("json/planes.json");
        LocationStorage.loadFromJson("json/locations.json");
        FlightStorage.loadFromJson("json/flights.json");

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AirportFrame().setVisible(true);
            }
        });
    }
}