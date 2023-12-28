package Object;

import Forces.FrictionForce;
import Surface.Surface;

public class CylinderShapedObject extends MainObject {
	
	private double radius;
	private double angularVelocity;
	FrictionForce frictionForce = new FrictionForce();
	public CylinderShapedObject(double mass, double velocity, double acceleration, double radius, double angularVelocity) {
		super(mass, velocity, acceleration);
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

	
	public void applyForce(CylinderShapedObject object, Surface surface) {
		double a = frictionForce.caculateFrictionForce(object, surface)/((object.getMass()*object.getRadius())/2);
		
		
	}
	
	

}
