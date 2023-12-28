package Forces;
import Object.*;

public class NormalForce  {
	
	GravitationalForce gravitationalForce = new GravitationalForce();
	public double caculateNormalForce(MainObject object) {
		return gravitationalForce.caculateGravitationalForce(object);
	}

}
