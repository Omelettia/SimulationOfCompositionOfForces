package model;

import java.util.ArrayList;
import java.util.List;

import model.MovingObject.Observer;

public class Surface {
	
	private double staticFrictionCoefficient;
	private double kineticFrictionCoefficient;
	private List<Observer> observers = new ArrayList<>();
	//Use observer to update information when there is change
	public Surface() {
		
	}
	
	public Surface(double staticFrictionCoefficient, double kineticFrictionCoefficient ) {
		this.kineticFrictionCoefficient = kineticFrictionCoefficient;
		this.staticFrictionCoefficient = staticFrictionCoefficient;
	}
	public double getStaticFrictionCoefficient() {
		return staticFrictionCoefficient;
	}
	public void setStaticFrictionCoefficient(double staticFrictionCoefficient) {
		this.staticFrictionCoefficient = staticFrictionCoefficient;
		notifyObservers();
	}
	public double getKineticFrictionCoefficient() {
		return kineticFrictionCoefficient;
	}
	public void setKineticFrictionCoefficient(double kineticFrictionCoefficient) {
		this.kineticFrictionCoefficient = kineticFrictionCoefficient;
		notifyObservers();
	}
	public void setFrictionCoefficient(double staticFrictionCoefficient, double kineticFrictionCoefficient ) {
		if (staticFrictionCoefficient > kineticFrictionCoefficient) {
			this.kineticFrictionCoefficient = kineticFrictionCoefficient;
			this.staticFrictionCoefficient = staticFrictionCoefficient;
			notifyObservers();
		}
		else {
			System.out.println("Loi");
		}
	}
	
	// Method relating to observer
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.updateSurface();
        }
    }

    // Observer interface
    public interface Observer {
        void updateSurface();
    }
	

}
