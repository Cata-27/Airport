/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers.Table;

import core.models.Flight;
import core.models.storage.FlightStorage;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class MyFlaightsShowController {

    private JTable myFlightsTable;
    private String currentPassengerId; // ID del pasajero logueado o seleccionado

    // Descomenta este constructor
    public MyFlaightsShowController(JTable myFlightsTable, String passengerId) {
        this.myFlightsTable = myFlightsTable;
        this.currentPassengerId = passengerId;
    }

    public void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) myFlightsTable.getModel();

        // Limpiar la tabla
        model.setRowCount(0);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        List<Flight> flights = FlightStorage.getInstance().getAll();

        // Rellenar la tabla
        for (Flight flight : flights) {
            model.addRow(new Object[]{
                flight.getId(),
                flight.getDepartureLocation(),
                flight.getArrivalLocation(),
                flight.getScaleLocation() != null ? flight.getScaleLocation() : "N/A", // Manejar escala opcional
                flight.getDepartureDate().toLocalDate().format(dateFormatter),
                flight.getDepartureDate().toLocalTime().format(timeFormatter),
                flight.getPlane().getModel()
            });
        }
    }
}


