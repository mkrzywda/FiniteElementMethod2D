package GeneratorMesh;

public class Mesh {
	private int numberElements, numberNodes;
	private Element[] arrayElements;
	private Node[] arrayNode;
	private int nH;
	private int nB;
	private double H;
	private double B;
	private double temp;

	public Mesh(int nH, int nB, double H, double B, double temp) {
		this.temp=temp;
		this.H = H;
		this.B=B;
		this.nB=nB;
		this.nH=nH;
		this.numberElements =(nH-1)*(nB-1);
		this.numberNodes =nH*nB;
		arrayElements =new Element[numberElements];
		arrayNode =new Node[numberNodes];
		
		int nrElement=0;


		// stworzenie  siatki
		for(int i=0;i<nH-1;i++) {
			for(int j=0;j<nB-1;j++) {
				arrayElements[nrElement]=new Element();
				arrayElements[nrElement].setID(1+i*(nB)+j,
						1+(i+1)*(nB)+j,
						(1+(i+1)*(nB))+1+j,
						(1+i*(nB))+1+j);

				nrElement++;
			}
		}

		
		nrElement=0;
		// czy element znajduje sie na brzegu i ustawienie odpowiedniej flagi
		for(int i=0;i<nH;i++) {
			for(int j=0;j<nB;j++) {
				boolean edge=false;
				if(i==0||i==(nH-1)||j==0||j==(nB-1)) {
					edge=true;
				}else {
					edge=false;
				}

				arrayNode[nrElement]=new Node(H/(nH-1)*i,B/(nB-1)*j,temp,edge);
				nrElement++;
			}
		}
	}
	/////wypisywanie siatki
	public void pokazSiatke(){
		int number=1;
		for(int i=0;i<nH-1;i++) {
			for(int j=0;j<nB-1;j++) {
				System.out.println("GeneratorMesh.Element number: "+number);
				System.out.println(arrayElements[number-1].getID0()+"-"+ arrayElements[number-1].getID1());
				System.out.println("|  |");
				System.out.println(arrayElements[number-1].getID3()+"-"+ arrayElements[number-1].getID2());
				number++;
			}

		}

	}
	public Element[] getArrayElements() {
		return arrayElements;
	}
	public Node[] getArrayNode() {
		return arrayNode;
	}
}
