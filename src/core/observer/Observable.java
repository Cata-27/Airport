/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.observer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS-E1504F
 */
public abstract class Observable {

    private List<Observer> observers;
    public Observable() { 
        this.observers = new ArrayList<>();
    }
    public void addObserver(Observer observer) {
        if (observer == null) {
            throw new NullPointerException("El observador no puede ser nulo.");
        }
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
    public void notifyObservers(Object arg) {
        List<Observer> observersCopy = new ArrayList<>(this.observers);
        for (Observer observer : observersCopy) {
            observer.update(arg);
        }
    }
}
