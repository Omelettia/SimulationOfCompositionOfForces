package Object;
import Forces.*;
import Surface.*;

public class MainObject extends Force {
	
	
	private double mass;
	private double velocity;
	private double acceleration;
	
	public MainObject() {
		
	}
	
	public MainObject(double mass, double velocity, double acceleration) {
		this.mass = mass;
		this.velocity = velocity;
		this.acceleration = acceleration;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public double getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}
   
	
	
	
	
	
	
	
	
	
	
	
	

}
