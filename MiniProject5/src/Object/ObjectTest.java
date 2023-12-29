package Object;
import Surface.*;
public class ObjectTest {
	public static void main(String[] args) {
		
		MainObject o1 = new CylinderShapedObject(30, 30, 30 ,0.5, 30);
		System.out.println(o1.caculateNormalForce());
		Surface surface = new Surface(0.3, 0.2);
		o1.setAppliedForce(180);
		System.out.println(o1.caculateFrictionForce(surface));
		o1.applyCompositeForce(surface);
		System.out.println(o1.getAcceleration());
		
		
		
	}

}
