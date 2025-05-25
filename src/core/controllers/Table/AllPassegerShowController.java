/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers.Table;

import core.models.Passenger;
import core.models.storage.PassengerStorage;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ASUS-E1504F
 */
public class AllPassegerShowController {
    private JTable allPassengersTable; // La JTable que se va a actualizar

    public AllPassegerShowController(JTable allPassengersTable) {
        this.allPassengersTable = allPassengersTable;
    }

    // Método que se llama desde el botón
    public void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) allPassengersTable.getModel();

        // Limpiar la tabla
        model.setRowCount(0);

        // *** Obtener los datos de los pasajeros, ya ordenados por ID ***
        List<Passenger> passengers = PassengerStorage.getInstance().getAll();

        // Rellenar la tabla
        for (Passenger passenger : passengers) {
            model.addRow(new Object[]{
                passenger.getId(),
                passenger.getFullname(),
                passenger.getBirthDate().toString(), // Asegúrate de manejar el formato de fecha adecuado
                passenger.calculateAge(),
                passenger.generateFullPhone(),
                passenger.getCountry(),
                passenger.getNumFlights()
            });
        }
    }
//    private JTable allPassengersTable; // La JTable que se va a actualizar
//
//    public PassengerTableController(JTable allPassengersTable) {
//        this.allPassengersTable = allPassengersTable;
//    }
//
//    // Método que se llama desde el botón
//    public void refreshTable() {
//        DefaultTableModel model = (DefaultTableModel) allPassengersTable.getModel();
//
//        // Limpiar la tabla
//        model.setRowCount(0);
//
//        // Obtener los datos de los pasajeros
//        List<Passenger> passengers = getAllPassengersFromDataSource();
//
//        // Rellenar la tabla
//        for (Passenger passenger : passengers) {
//            model.addRow(new Object[]{
//                passenger.getId(),
//                passenger.getFullname(),
//                passenger.getBirthDate().toString(), // Asegúrate de manejar el formato de fecha adecuado
//                passenger.calculateAge()
//            });
//        }
//    }    
}
