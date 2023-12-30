package Object;

import Surface.Surface;

public class CubeShapedObject extends MainObject {
	
	private double sideLength;

	public CubeShapedObject(double mass, double sideLength) {
		super(mass);
		
		// TODO Auto-generated constructor stub
		this.sideLength = sideLength;
	}

	public double getSideLength() {
		return sideLength;
	}

	public void setSideLength(double sideLength) {
		this.sideLength = sideLength;
	}
	@Override
	public double caculateFrictionForce(Surface surface) {
		if (this.getAppliedForce() <= this.caculateNormalForce()*surface.getStaticFrictionCoefficient()) {
			return this.getAppliedForce();
		}
		else {
			return this.caculateNormalForce()*surface.getKineticFrictionCoefficient();
		}
		
		
	}
	public void applyCompositeForce(Surface surface) {
		this.acceleration = Math.abs(this.compositeHForce(surface)/this.getMass());
		
	}
	
	
	
		
	

}
