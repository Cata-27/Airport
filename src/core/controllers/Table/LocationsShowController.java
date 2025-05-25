/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers.Table;

import core.models.Location;
import core.models.storage.LocationStorage;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class LocationsShowController {

    private JTable allLocationsTable; // La JTable que se va a actualizar

    public LocationsShowController(JTable allLocationsTable) {
        this.allLocationsTable = allLocationsTable;
    }

    public void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) allLocationsTable.getModel();

        // Limpiar la tabla
        model.setRowCount(0);

        // Obtener los datos de las localizaciones, ya ordenadas por ID de aeropuerto desde LocationStorage
        List<Location> locations = LocationStorage.getInstance().getAll(); // Asume que getAll() devuelve copias ordenadas

//        // Rellenar la tabla
       for (Location location : locations) {
            model.addRow(new Object[]{
                location.getAirportId(),
                location.getAirportName(),
                location.getAirportCity(),
                location.getAirportCountry()
           
            });
        }
    }
}
