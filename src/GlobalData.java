import java.util.Scanner;

public class GlobalData {
	private int nH;
	private int nB;
	private double simTime;
	private double timeStamp;
	private double initialTemp;
	private double ambientTemp;
	private double alfa;
	private double H;
	private double B;
	private double specificHeat;
	private double conductivity;
	private double density;
	
	public GlobalData(int nH, int nB, double simTime, double timeStamp, double initialTemp, double ambientTemp,
				double alfa, double H, double B, double specificHeat, double conductivity, double density ) {

			this.nH = nH;
			this.nB = nB;
			this.simTime = simTime;
			this.timeStamp = timeStamp;
			this.initialTemp = initialTemp;
			this.ambientTemp = ambientTemp;
			this.alfa = alfa;
			this.H = H;
			this.B= B;
			this.specificHeat = specificHeat;
			this.conductivity = conductivity;
			this.density = density;

			System.out.println(toString());

	}

	int getNH() {
		return nH;
	}
	int getNB() {
		return nB;
	}
	double getB() {
		return B;
	}
	double getH() {
		return H;
	}
	double getDensity() {
		return density;
	}
	double getSimTime() {
		return simTime;
	}
	double getTimeStamp() {
		return timeStamp;
	}
	double getSpecificHeat() {
		return specificHeat;
	}
	double getConductivity() {
		return conductivity;
	}
	double getInitialTemp() {
		return initialTemp;
	}
	double getAmbientTemp() {
		return ambientTemp;
	}
	double getAlfa() {
		return alfa;
	}
}
