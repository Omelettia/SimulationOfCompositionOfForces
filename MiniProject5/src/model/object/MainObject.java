package model.object;

import java.util.ArrayList;
import java.util.List;

import model.Surface;
import model.MovingObject.Observer;

public abstract class MainObject {

	protected double mass;
	protected double velocity;
	protected double acceleration;
	protected double appliedForce;
	private List<Observer> observers = new ArrayList<>();
	//Use observer to update information when there is change
	protected static final double A = 0.1;
	
	public MainObject() {
		
	}
	
	public MainObject(double mass, double velocity) {
		this.mass = mass;
		this.velocity = velocity;
	}
    

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
		notifyObservers();
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
		notifyObservers();
	}
	
	public void updateMovement(Surface surface)
	{
		setVelocity(getVelocity() + getAcceleration(surface)*A);
	}

	public double getAcceleration(Surface surface) {
		return compositeHForce(surface)/mass;
	}

	public double getAngularVelocity() {
		return 0;
	}
	
	public double calculateAngularAcceleration(Surface surface) {
        return 0;
    }
	
	public double calculateGravitationalForce() {
		return this.mass*10;
	}
	public double calculateNormalForce() {
		return this.calculateGravitationalForce();
	}
	public double calculateFrictionForce(Surface surface) {
	    double appliedForce = this.getAppliedForce();
	    double normalForce = this.calculateNormalForce();
	    
	    double staticFrictionForce = normalForce * surface.getStaticFrictionCoefficient();
	    double kineticFrictionForce = normalForce * surface.getKineticFrictionCoefficient();
	    if (Math.abs(appliedForce) <= Math.abs(staticFrictionForce)) 
	     {
	        return appliedForce;
	     }  
	    else {    
	        return kineticFrictionForce * Math.signum(appliedForce);
	    }
	}


	public double getAppliedForce() {
		return appliedForce;
	}

	public void setAppliedForce(double appliedForce) {
		this.appliedForce = appliedForce;
		notifyObservers();
	}
	public double compositeVForce() {
		return this.calculateGravitationalForce() - this.calculateNormalForce();
	}
	public double compositeHForce(Surface surface) {
		return this.getAppliedForce() - this.calculateFrictionForce(surface);
	}
	
	public double getMaxWeight()
	{
		return 100;
	}
	
	
	public abstract double getMaxDimension();

    public abstract String getDimensionName();

    public abstract double getDimension();

    public abstract void setDimension(double dimension);
    
 // Method relating to observer
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    protected void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    // Observer interface
    public interface Observer {
        void update();
    }
}
