/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers.Table;

import core.models.Plane;
import core.models.storage.PlaneStorage;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PlaneShowController {
    private JTable allPlanesTable; // La JTable que se va a actualizar

    public PlaneShowController(JTable allPlanesTable) {
        this.allPlanesTable = allPlanesTable;
    }

    public void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) allPlanesTable.getModel();

        // Limpiar la tabla
        model.setRowCount(0);

        // Obtener los datos de los aviones, ya ordenados por ID desde PlaneStorage
        List<Plane> planes = PlaneStorage.getInstance().getAll(); // Asume que getAll() devuelve copias ordenadas

        // Rellenar la tabla
        for (Plane plane : planes) {
            model.addRow(new Object[]{
                plane.getId(),
                plane.getBrand(),
                plane.getModel(),
                plane.getMaxCapacity(),
                plane.getAirline()
                // Añade todas las columnas relevantes de tu objeto Plane
            });
        }
    }
}
