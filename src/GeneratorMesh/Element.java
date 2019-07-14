package GeneratorMesh;

public class Element {
	private int tabID[];
	private int[] surface;
	Element() {
		tabID= new int[4];
		surface = new int[4];
	}
	void setID(int ID0, int ID1, int ID2, int ID3) {
		tabID[0]=ID0;
		tabID[1]=ID1;
		tabID[2]=ID2;
		tabID[3]=ID3;
	}

	int getID0() {

		return tabID[0];
	}
	int getID1()
	{
		return tabID[1];
	}
	int getID2() {

		return tabID[2];
	}
	int getID3() {

		return tabID[3];
	}
	public int[] getTabID()
	{
		return tabID;
	}
}


