package Object;



import Surface.Surface;

public class CylinderShapedObject extends MainObject {
	
	private double radius;
	private double angularVelocity;

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
	@Override
	public double caculateFrictionForce(Surface surface) {
		if (this.getAppliedForce() <= 3*this.caculateNormalForce()*surface.getStaticFrictionCoefficient()) {
			return this.getAppliedForce()/3;
		}else {
			return this.caculateNormalForce()*surface.getKineticFrictionCoefficient();
			
		}
		
	}
	public void applyCompositeForce(Surface surface) {
		this.acceleration = (this.caculateFrictionForce(surface)*2)/(this.getMass()*this.getRadius()*this.getRadius());
		
	}

	
	
	
	

}
