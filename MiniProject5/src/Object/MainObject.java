package Object;

import Surface.*;

public class MainObject {
	
	
	protected double mass;
	protected double velocity;
	protected double acceleration;
	protected double appliedForce;
	
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
	
	public double caculateGravitationalForce() {
		return this.mass*10;
	}
	public double caculateNormalForce() {
		return this.caculateGravitationalForce();
	}
	public double caculateFrictionForce(Surface surface) {
		return 0;
	}

	public double getAppliedForce() {
		return appliedForce;
	}

	public void setAppliedForce(double appliedForce) {
		this.appliedForce = appliedForce;
	}
	public double compositeVForce() {
		return Math.abs(this.caculateGravitationalForce() - this.caculateNormalForce());
	}
	public double compositeHForce(Surface surface) {
		return Math.abs(this.getAppliedForce() - this.caculateFrictionForce(surface));
	}
	public void applyCompositeForce(Surface surface) {
		
	}
    
	
	
	
	
	
	
	
	
	
	
	
	

}
