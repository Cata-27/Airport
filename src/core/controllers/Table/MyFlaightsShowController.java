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

    public MyFlaightsShowController(JTable myFlightsTable1, String selectedPassengerId) {
    }
    private JTable myFlightsTable;
    private String currentPassengerId; // ID del pasajero logueado o seleccionado
//
//    public MyFlaightsShowControllerJTable myFlightsTable, String passengerId) {
//        this.myFlightsTable = myFlightsTable;
//        this.currentPassengerId = passengerId; // El ID del pasajero actual
//    }

    public void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) myFlightsTable.getModel();

        // Limpiar la tabla
        model.setRowCount(0);

        // Obtener los vuelos del pasajero actual, ya ordenados y como copias desde FlightStorage
//        List<Flight> mySortedFlights = FlightStorage.getInstance().getFlightsByPassengerId(currentPassengerId);

        // Formateador opcional para la fecha y hora si LocalDateTime no se muestra bien directamente
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Rellenar la tabla
//        for (Flight flight : mySortedFlights) {
//            model.addRow(new Object[]{
//                flight.getId(),
//                flight.getOrigin().getCity(),
//                flight.getDestination().getCity(),
//                flight.getScaleLocation() != null ? flight.getScaleLocation().getCity() : "N/A", // Manejar escala opcional
//                flight.getDepartureDate().toLocalDate().format(dateFormatter),
//                flight.getDepartureDate().toLocalTime().format(timeFormatter),
//                flight.getPlane().getModel()
//                // Añade todas las columnas relevantes de tu objeto Flight
//            });
//        }
    }
    
}
