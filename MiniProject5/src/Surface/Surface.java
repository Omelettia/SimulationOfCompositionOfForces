package Surface;

public class Surface {
	
	private double staticFrictionCoefficient;
	private double kineticFrictionCoefficient;
	
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
	}
	public double getKineticFrictionCoefficient() {
		return kineticFrictionCoefficient;
	}
	public void setKineticFrictionCoefficient(double kineticFrictionCoefficient) {
		this.kineticFrictionCoefficient = kineticFrictionCoefficient;
	}
	public void setFrictionCoefficient(double staticFrictionCoefficient, double kineticFrictionCoefficient ) {
		if (staticFrictionCoefficient > kineticFrictionCoefficient) {
			this.kineticFrictionCoefficient = kineticFrictionCoefficient;
			this.staticFrictionCoefficient = staticFrictionCoefficient;
		}
		else {
			System.out.println("Loi");
		}
	}
	
	

}
