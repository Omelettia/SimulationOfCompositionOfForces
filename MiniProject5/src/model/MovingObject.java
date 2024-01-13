package model;

import model.object.MainObject;
import java.util.ArrayList;
import java.util.List;

public class MovingObject implements MainObject.Observer{
    //Class to represent the current object that is being acted on by the appliedforce
    private MainObject currentObject;
    private double appliedForce;
    private List<Observer> observers = new ArrayList<>();
    //Use observer to update information when there is change

    public MovingObject() {
    }

    public double getAppliedForce() {
        return appliedForce;
    }

    public void setAppliedForce(double appliedForce) {
        this.appliedForce = appliedForce;
        this.currentObject.setAppliedForce(appliedForce);
        notifyObservers();
    }

    public MovingObject(MainObject mainObject) {
        this.currentObject = mainObject;
        this.currentObject.setAppliedForce(appliedForce);
        this.currentObject.addObserver(this);
    }

    public MainObject getCurrentObject() {
        return currentObject;
    }

    public void addObject(MainObject mainObject) {
        this.currentObject = mainObject;
        this.currentObject.setAppliedForce(appliedForce);
        this.currentObject.addObserver(this);
        notifyObservers();
    }

    public void removeObject() {
        this.currentObject = null;
    }

    public void swapObject(MainObject newObject) {
        this.currentObject = newObject;
        this.currentObject.setAppliedForce(appliedForce);
        this.currentObject.addObserver(this);
        notifyObservers();
    }

    public double getMaxForce() {
        return 200;
    }

    // // Method relating to observer
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    // Observer interface
    public interface Observer {
        void update();
    }

	@Override
	public void update() {
		notifyObservers();
	}

	
}
