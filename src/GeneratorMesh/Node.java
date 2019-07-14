package GeneratorMesh;

public class Node {
	private double x,y,temp;
	private boolean nodeStatus;

	public Node(double x, double y, double temp, boolean nodeStatus) {
		this.x=x;
		this.y=y;
		this.temp=temp;
		this.nodeStatus = nodeStatus;
	}

	public void setTemp(double temp) {

		this.temp = temp;
	}
	public boolean getStatus()
	{
		return this.nodeStatus;
	}
	public double getTemperature() {
		return this.temp;
	}
	public double getX() {
		return this.x;
	}
	public double getY() {
		return this.y;
	}
}
