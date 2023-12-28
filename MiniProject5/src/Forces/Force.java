package Forces;
import java.util.Scanner;

import Object.MainObject;
import Surface.*;

public class Force {
	
	Scanner scan = new Scanner(System.in);
	GravitationalForce gravitationalForce = new GravitationalForce();
	NormalForce normalForce = new NormalForce();
	AppliedForce appliedForce = new AppliedForce();
	FrictionForce frictionForce = new FrictionForce();
	public double compositeVForce(MainObject object) {
		double compositeVForce = gravitationalForce.caculateGravitationalForce(object) - normalForce.caculateNormalForce(object);
		return compositeVForce;
		
	}
	public double compositeHForce(MainObject object, Surface surface) {
		System.out.println("Nhap luc: ");
		double x = scan.nextDouble();
		appliedForce.setAppliedForce(x);
		double compositeHForce = Math.abs(appliedForce.getAppliedForce() - frictionForce.caculateFrictionForce(object, surface));
		return compositeHForce;
		
		
		
	}
	
	
	
	
	

}
