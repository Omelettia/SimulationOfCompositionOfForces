package model.object;



public class CubeShapedObject extends MainObject {
	
	private double sideLength;

	public CubeShapedObject(double mass, double velocity, double sideLength) {
		super(mass, velocity);
		
		// TODO Auto-generated constructor stub
		this.sideLength = sideLength;
	}

	public double getDimension() {
		return sideLength;
	}
    
	@Override
	public void setDimension(double sideLength) {
		this.sideLength = sideLength;
	}

	@Override
	public String getDimensionName() {
		// TODO Auto-generated method stub
		return "sideLength";
	}

	@Override
	public double getMaxDimension() {
		// TODO Auto-generated method stub
		return 100;
	}

	

		
	

}
