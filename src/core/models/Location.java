/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models;

import core.models.storage.FlightStorage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author edangulo
 */
public class Location {
    
    private final String airportId;
    private final String airportName;
    private final String airportCity;
    private final String airportCountry;
    private final double airportLatitude;
    private final double airportLongitude;

    public Location(String airportId, String airportName, String airportCity, String airportCountry, double airportLatitude, double airportLongitude) {
        this.airportId = airportId;
        this.airportName = airportName;
        this.airportCity = airportCity;
        this.airportCountry = airportCountry;
        this.airportLatitude = airportLatitude;
        this.airportLongitude = airportLongitude;
    }
    public String getAirportId() {
        return airportId;
    }

    public String getAirportName() {
        return airportName;
    }

    public String getAirportCity() {
        return airportCity;
    }

    public String getAirportCountry() {
        return airportCountry;
    }

    public double getAirportLatitude() {
        return airportLatitude;
    }

    public double getAirportLongitude() {
        return airportLongitude;
    }
    

}
