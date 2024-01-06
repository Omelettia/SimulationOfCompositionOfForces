package Object;

import Surface.*;

public class MainObject {
	
	
	protected double mass;
	protected double velocity;
	protected double acceleration;
	protected double appliedForce;
	
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
	}

	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public double getAcceleration(Surface surface) {
		return compositeHForce(surface)/mass;
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
	    
	    if (Math.abs(appliedForce) <= Math.abs(staticFrictionForce)) {
	        return appliedForce;
	    } else {
	        double kineticFrictionForce = normalForce * surface.getKineticFrictionCoefficient();
	        return kineticFrictionForce * Math.signum(appliedForce);
	    }
	}


	public double getAppliedForce() {
		return appliedForce;
	}

	public void setAppliedForce(double appliedForce) {
		this.appliedForce = appliedForce;
	}
	public double compositeVForce() {
		return this.calculateGravitationalForce() - this.calculateNormalForce();
	}
	public double compositeHForce(Surface surface) {
		return this.getAppliedForce() - this.calculateFrictionForce(surface);
	}
	public void applyCompositeForce(Surface surface) {
		
	}
    
	
	
	
	
	
	
	
	
	
	
	
	

}
