package Forces;
import Object.MainObject;
import Surface.*;


public class FrictionForce {
	Surface surface = new Surface();
	NormalForce normalForce = new NormalForce();
	public double caculateFrictionForce(MainObject object, Surface surface) {
		return normalForce.caculateNormalForce(object)*surface.getKineticFrictionCoefficient();
	}
	
	
	
	
	
	

}
