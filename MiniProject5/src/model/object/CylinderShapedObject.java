package model.object;

import model.Surface;

public class CylinderShapedObject extends MainObject {
	
	private double radius;
	private double angularVelocity;

	public CylinderShapedObject(double mass, double velocity, double radius, double angularVelocity) {
		super(mass, velocity);
		// TODO Auto-generated constructor stub
		this.radius = radius;
		this.angularVelocity = angularVelocity;
	}
	
	@Override
	public void updateMovement(Surface surface)
	{   
		setVelocity(getVelocity() + getAcceleration(surface)*A);
		setAngularVelocity(getAngularVelocity() + calculateAngularAcceleration(surface)*A);
	}
	
	@Override
	public double getAngularVelocity() {
		return angularVelocity;
	}

	public void setAngularVelocity(double angularVelocity) {
		this.angularVelocity = angularVelocity;
		notifyObservers();
	}
	@Override
	public double calculateFrictionForce(Surface surface) {
	    double appliedForce = this.getAppliedForce();
	    double normalForce = this.calculateNormalForce();
	    double staticFrictionLimit = 3 * normalForce * surface.getStaticFrictionCoefficient();
	    double kineticFrictionForce = normalForce * surface.getKineticFrictionCoefficient();
	    if (Math.abs(appliedForce) <= Math.abs(staticFrictionLimit)) {
	        return appliedForce / 3;
	    } else {
	        return kineticFrictionForce * Math.signum(appliedForce);
	    }
	}

	@Override
	public double calculateAngularAcceleration(Surface surface) {
        // Tính torque
        double torque = this.calculateFrictionForce(surface) * this.radius;

        // calculating the angular acceleration
        return torque / (this.getMass() * this.radius * this.radius/2);
    }

	@Override
	public String getDimensionName() {
		return "radius";
	}

	@Override
	public double getDimension() {
		return radius;
	}

	@Override
	public void setDimension(double radius) {
		this.radius = radius;		
	}


	@Override
	public double getMaxDimension() {
		// TODO Auto-generated method stub
		return 50;
	}
	
	

	
	
	
	

}
