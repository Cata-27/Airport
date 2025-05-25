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

public class FlightsShowController {
    private JTable allFlightsTable; // La JTable que se va a actualizar

    public FlightsShowController(JTable allFlightsTable) {
        this.allFlightsTable = allFlightsTable;
    }

    public void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) allFlightsTable.getModel();

        // Limpiar la tabla
        model.setRowCount(0);

        // Obtener los datos de los vuelos, ya ordenados por fecha de salida desde FlightStorage
        List<Flight> flights = FlightStorage.getInstance().getAllFlights(); // Asume que getAllFlights() devuelve copias ordenadas

        // Formateadores para la fecha y hora
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

         //Rellenar la tabla
       for (Flight flight : flights) {
        model.addRow(new Object[]{
        flight.getId(),
        flight.getPlane().getModel(),
        flight.getScaleLocation() != null ? flight.getScaleLocation().getAirportId() : "-", // 
        flight.getDepartureDate().toLocalDate().format(dateFormatter),
        flight.getDepartureDate().toLocalTime().format(timeFormatter),
        flight.getHoursDurationArrival() + "h " + flight.getMinutesDurationArrival() + "m",
        flight.getScaleLocation() != null ? (flight.getHoursDurationScale() + "h " + flight.getMinutesDurationScale() + "m") : "N/A"
    });
}
    }
    
}
