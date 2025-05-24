/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.observer;

/**
 *
 * @author ASUS-E1504F
 */
public interface Observable {
    //Registra un Observer para recibir notificaciones de cambios.
    void addObserver(Observer observer);
    // Elimina un Observer para que deje de recibir notificaciones.
    void removeObserver(Observer observer);
     //Notifica a todos los Observers registrados que el estado del Observable ha cambiado.
    void notifyObservers();   
}
