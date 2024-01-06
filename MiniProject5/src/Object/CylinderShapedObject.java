package Object;



import Surface.Surface;

public class CylinderShapedObject extends MainObject {
	
	private double radius;
	private double angularVelocity;

	public CylinderShapedObject(double mass, double velocity, double radius, double angularVelocity) {
		super(mass, velocity);
		// TODO Auto-generated constructor stub
		this.radius = radius;
		this.angularVelocity = angularVelocity;
	}
	
	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getAngularVelocity() {
		return angularVelocity;
	}

	public void setAngularVelocity(double angularVelocity) {
		this.angularVelocity = angularVelocity;
	}
	@Override
	public double calculateFrictionForce(Surface surface) {
	    double appliedForce = this.getAppliedForce();
	    double normalForce = this.calculateNormalForce();
	    double staticFrictionLimit = 3 * normalForce * surface.getStaticFrictionCoefficient();

	    if (Math.abs(appliedForce) <= Math.abs(staticFrictionLimit)) {
	        return appliedForce / 3;
	    } else {
	        double kineticFrictionForce = normalForce * surface.getKineticFrictionCoefficient();
	        return kineticFrictionForce * Math.signum(appliedForce);
	    }
	}

	
	public double calculateAngularAcceleration(Surface surface) {
        // Calculate torque
        double torque = this.calculateFrictionForce(surface) * this.radius;

        // Calculate angular acceleration
        return torque / (this.getMass() * this.radius * this.radius/2);
    }
	
	

	
	
	
	

}
