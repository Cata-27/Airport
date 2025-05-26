/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Status;
import core.models.Plane;
import core.controllers.utils.Response;
import core.models.Passenger;
import core.models.storage.PassengerStorage;
import java.time.LocalDate;

public class PassengerController {

    public static Response createPassenger(String id, String firstName, String lastName, String year, String month, String day, String phoneCode, String phone, String country) {
        try {
            long idLong;
            int yearInt, monthInt, dayInt, phoneCodeInt;
            long phoneLong;
            // ID Validation
            try {
                idLong = Long.parseLong(id);
                if (idLong < 0) {
                    return new Response("ID must be positive or zero", Status.BAD_REQUEST);
                }
                if (id.length() > 15) {
                    return new Response("ID must have at most 15 digits", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("ID must be numeric", Status.BAD_REQUEST);
            }

            if (firstName.trim().isEmpty()) {
                return new Response("First name must not be empty", Status.BAD_REQUEST);
            }
            if (lastName.trim().isEmpty()) {
                return new Response("Last name must not be empty", Status.BAD_REQUEST);
            }
            try {
                yearInt = Integer.parseInt(year);
                monthInt = Integer.parseInt(month);
                dayInt = Integer.parseInt(day);
                LocalDate.of(yearInt, monthInt, dayInt); // Validates if the date is real
            } catch (NumberFormatException ex) {
                return new Response("Year, month, and day must be numeric", Status.BAD_REQUEST);
            } catch (Exception ex) {
                return new Response("Invalid date of birth", Status.BAD_REQUEST);
            }
            try {
                phoneCodeInt = Integer.parseInt(phoneCode);
                if (phoneCodeInt < 0) {
                    return new Response("Phone code must be positive or zero", Status.BAD_REQUEST);
                }
                if (phoneCode.length() > 3) {
                    return new Response("Phone code must have at most 3 digits", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Phone code must be numeric", Status.BAD_REQUEST);
            }

            try {
                phoneLong = Long.parseLong(phone);
                if (phoneLong < 0) {
                    return new Response("Phone must be positive or zero", Status.BAD_REQUEST);
                }
                if (phone.length() > 11) {
                    return new Response("Phone must have at most 11 digits", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Phone must be numeric", Status.BAD_REQUEST);
            }

            if (country.trim().isEmpty()) {
                return new Response("Country must not be empty", Status.BAD_REQUEST);
            }
            Passenger passenger = new Passenger(
            idLong, firstName, lastName,
            LocalDate.of(yearInt, monthInt, dayInt),
            phoneCodeInt, phoneLong, country
    );
            PassengerStorage.save(passenger); 
            return new Response("Passenger created successfully", Status.CREATED);
        } catch (Exception ex) {
            return new Response("Unexpected error: " + ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response readPlane(String id) {
        try {
            long idLong;
            try {
                idLong = Long.parseLong(id);
                if (idLong < 0) {
                    return new Response("ID must be positive or zero", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("ID must be numeric", Status.BAD_REQUEST);
            }

            PassengerStorage storage = PassengerStorage.getInstance();

            Plane plane = storage.getPlane(idLong);
            if (plane == null) {
                return new Response("Plane not found", Status.NOT_FOUND);
            }
            return new Response("Plane found", Status.OK, plane);
        } catch (Exception ex) {
            return new Response("Unexpected error: " + ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response updatePassenger(String id, String firstName, String lastName, String year, String month, String day, String countryCode, String phoneCode, String phone, String country) {
        try {
            long idLong;
            int yearInt, monthInt, dayInt, phoneCodeInt;
            long phoneLong;

            try {
                idLong = Long.parseLong(id);
                if (idLong < 0) {
                    return new Response("ID must be positive or zero", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("ID must be numeric", Status.BAD_REQUEST);
            }

            PassengerStorage storage = PassengerStorage.getInstance();

            Plane plane = storage.getPlane(idLong);
            if (plane == null) {
                return new Response("Plane not found", Status.NOT_FOUND);
            }

            if (firstName.trim().isEmpty()) {
                return new Response("First name must not be empty", Status.BAD_REQUEST);
            }

            if (lastName.trim().isEmpty()) {
                return new Response("Last name must not be empty", Status.BAD_REQUEST);
            }
            try {
                yearInt = Integer.parseInt(year);
                monthInt = Integer.parseInt(month);
                dayInt = Integer.parseInt(day);
                LocalDate.of(yearInt, monthInt, dayInt); // Validates if the date is real
            } catch (NumberFormatException ex) {
                return new Response("Year, month, and day must be numeric", Status.BAD_REQUEST);
            } catch (Exception ex) {
                return new Response("Invalid date of birth", Status.BAD_REQUEST);
            }

            if (countryCode.trim().isEmpty()) {
                return new Response("Country code must not be empty", Status.BAD_REQUEST);
            }

            try {
                phoneCodeInt = Integer.parseInt(phoneCode);
                if (phoneCodeInt < 0) {
                    return new Response("Phone code must be positive or zero", Status.BAD_REQUEST);
                }
                if (phoneCode.length() > 3) {
                    return new Response("Phone code must have at most 3 digits", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Phone code must be numeric", Status.BAD_REQUEST);
            }

            try {
                phoneLong = Long.parseLong(phone);
                if (phoneLong < 0) {
                    return new Response("Phone must be positive or zero", Status.BAD_REQUEST);
                }
                if (phone.length() > 11) {
                    return new Response("Phone must have at most 11 digits", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Phone must be numeric", Status.BAD_REQUEST);
            }

            if (country.trim().isEmpty()) {
                return new Response("Country must not be empty", Status.BAD_REQUEST);
            }
//
//            //Update Plane object attributes
//            passenger.setFirstName(firstName);
//            passenger.setLastName(lastName);
//            passenger.setYear(yearInt);
//            passenger.setMonth(monthInt);
//            passenger.setDay(dayInt);
//            passenger.setCountryCode(countryCode);
//            passenger.setPhoneCode(phoneCodeInt);
//            passenger.setPhone(phoneLong);
//            passenger.setCountry(country);

            return new Response("Plane data updated successfully", Status.OK);
        } catch (Exception ex) {
            return new Response("Unexpected error: " + ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response deletePlane(String id) {
        try {
            long idLong;

            try {
                idLong = Long.parseLong(id);
                if (idLong < 0) {
                    return new Response("ID must be positive or zero", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("ID must be numeric", Status.BAD_REQUEST);
            }

            PassengerStorage storage = PassengerStorage.getInstance();
            if (!storage.delPlane(idLong)) {
                return new Response("Plane not found", Status.NOT_FOUND);
            }
            return new Response("Plane deleted successfully", Status.NO_CONTENT);
        } catch (Exception ex) {
            return new Response("Unexpected error: " + ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }
}
