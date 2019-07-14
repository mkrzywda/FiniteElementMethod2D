import GeneratorMesh.Element;
import GeneratorMesh.Mesh;
import GeneratorMesh.Node;
import LocalElement.LocalSurface;

public class FiniteElementMethod {

	private static final int NUMBER_OF_NODES = 4;
	private static double[][] lokalnaMacierz = new double[4][2];
	private static LocalSurface[] localsurface = new LocalSurface[4];
	private double[][] localH;
	private double[] localP;
	private double[][] globalH;
	private double[] globalP;
	private GlobalData globalData;
	private Mesh mesh;

	public FiniteElementMethod(GlobalData globalData, double[][] globalH, double[] globalP, Mesh mesh) {
		this.globalData = globalData;
		this.globalH=globalH;
		this.globalP=globalP;
		this.mesh = mesh;

		fillLocalMatrixOptimalValue();
		shapeFunctionForIntegrationPoints();
	}
	

	private double N1_ksi ( double eta ) {
		return ( - ( 1.0 / 4.0 ) * ( 1 - eta ) );
	}
	private double N1_eta ( double ksi ) {
		return ( - ( 1.0 / 4.0 ) * ( 1 - ksi ) );
	}

	private double N2_ksi ( double eta ) {
		return ( ( 1.0 / 4.0 ) * ( 1 - eta ) );
	}
	private double N2_eta ( double ksi ) {
		return ( - ( 1.0 / 4.0 ) * ( 1 + ksi ) );
	}

	private double N3_ksi ( double eta ) {
		return ( ( 1.0 / 4.0 ) * ( 1 + eta ) );
	}
	private double N3_eta ( double ksi ) {
		return ( ( 1.0 / 4.0 ) * ( 1 + ksi ) );
	}

	private double N4_ksi ( double eta ) {
		return ( - ( 1.0 / 4.0 ) * ( 1 + eta ) );
	}
	private double N4_eta ( double ksi ) {
		return ( ( 1.0 / 4.0 ) * ( 1 - ksi ) );
	}


	private double N1 ( double ksi, double eta ) {
		return 0.25 * ( 1.0 - ksi ) * ( 1.0 - eta );
	}
	private double N2 ( double ksi, double eta ) {
		return 0.25 * ( 1.0 + ksi ) * ( 1.0 - eta );
	}
	private double N3 ( double ksi, double eta ) {
		return 0.25 * ( 1.0 + ksi ) * ( 1.0 + eta );
	}
	private double N4 ( double ksi, double eta ) {
		return 0.25 * ( 1.0 - ksi ) * ( 1.0 + eta );
	}

	private void shapeFunctionForIntegrationPoints() {

		//punkty calkowania znajdujace sie na krawedziach elementu
		localsurface[0] = new LocalSurface( new Node( - 1.0, 1.0 / Math.sqrt( 3 ),0.1,false ), new Node( - 1.0, - 1.0 / Math.sqrt( 3.0 ),0.1,false ) );
		localsurface[1] = new LocalSurface( new Node( - 1.0 / Math.sqrt( 3 ), - 1.0,0.1,false ), new Node( 1.0 / Math.sqrt( 3.0 ), - 1.0 ,0.1,false) );
		localsurface[2] = new LocalSurface( new Node( 1.0, - 1.0 / Math.sqrt( 3 ) ,0.1,false), new Node( 1.0, 1.0 / Math.sqrt( 3.0  ),0.1,false) );
		localsurface[3] = new LocalSurface( new Node( 1.0 / Math.sqrt( 3 ), 1.0 ,0.1,false), new Node( - 1.0 / Math.sqrt( 3.0 ), 1.0 ,0.1,false) );

		for ( int i = 0; i < 4; i++ ) {
			for ( int j = 0; j < 2; j++ ) {


				// Funkcje ksztaltu dla wezow znajdujacych sie na krawedziach elementow
				localsurface[i].N[j][0] = N1( localsurface[i].nodes[j].getX(), localsurface[i].nodes[j].getY() );
				localsurface[i].N[j][1] = N2( localsurface[i].nodes[j].getX(), localsurface[i].nodes[j].getY() );
				localsurface[i].N[j][2] = N3( localsurface[i].nodes[j].getX(), localsurface[i].nodes[j].getY() );
				localsurface[i].N[j][3] = N4( localsurface[i].nodes[j].getX(), localsurface[i].nodes[j].getY() );
			}
		}
	}

	private void fillLocalMatrixOptimalValue() {

		// Wyliczamy wartosci dla ksi i dla eta
		lokalnaMacierz[0][0]=-1/Math.sqrt(3);
		lokalnaMacierz[0][1]=-1/Math.sqrt(3);
		lokalnaMacierz[1][0]=1/Math.sqrt(3);
		lokalnaMacierz[1][1]=-1/Math.sqrt(3);
		lokalnaMacierz[2][0]=-1/Math.sqrt(3);
		lokalnaMacierz[2][1]=1/Math.sqrt(3);
		lokalnaMacierz[3][0]=1/Math.sqrt(3);
		lokalnaMacierz[3][1]=1/Math.sqrt(3);
	}

	public void count() {
		double[] dNdX=new double[4];
		double[] dNdY=new double[4];
		double[] X;
		double[] Y;
		double[] temps;
		int[] id;
		Element el;

		double[][] dNdKsi= new double[4][4];
		double[][] dNdEta= new double[4][4];
		double[][] N = new double[4][4];

		int numberOfElements = mesh.getArrayElements().length;
		double localC=0;
		double intTemp;

		
		for ( int i = 0; i < NUMBER_OF_NODES; i++ ) {


            // Wyliczamy funkcje ksztaltu
            N[i][0] = N1( lokalnaMacierz[i][0], lokalnaMacierz[i][1] );
            N[i][1] = N2( lokalnaMacierz[i][0], lokalnaMacierz[i][1] );
            N[i][2] = N3( lokalnaMacierz[i][0], lokalnaMacierz[i][1] );
            N[i][3] = N4( lokalnaMacierz[i][0], lokalnaMacierz[i][1] );

			//wyliczami pochodne funkcji ksztaltu po ksi i po eta
			dNdKsi[i][0] = N1_ksi( lokalnaMacierz[i][0] );
			dNdKsi[i][1] = N2_ksi( lokalnaMacierz[i][0] );
			dNdKsi[i][2] = N3_ksi( lokalnaMacierz[i][0] );
			dNdKsi[i][3] = N4_ksi( lokalnaMacierz[i][0] );

			dNdEta[i][0] = N1_eta( lokalnaMacierz[i][1] );
			dNdEta[i][1] = N2_eta( lokalnaMacierz[i][1] );
			dNdEta[i][2] = N3_eta( lokalnaMacierz[i][1] );
			dNdEta[i][3] = N4_eta( lokalnaMacierz[i][1] );


		}

		//bierzemy kazdy element w siatce
		for(int i=0;i<numberOfElements;i++) {
			this.localH = new double[globalData.getNH()][globalData.getNB()];
			this.localP = new double[globalData.getNH()];
			X=new double[globalData.getNH()];
			Y=new double[globalData.getNB()];
			temps=new double[globalData.getNB()];
			el= mesh.getArrayElements()[i];
			id = el.getTabID();
			
			// tablice ktore odpowiadaja za wartosci X i Y w elementach
			for(int j=0;j<NUMBER_OF_NODES;j++) {
				X[j]= mesh.getArrayNode()[id[j]-1].getX();
				Y[j]= mesh.getArrayNode()[id[j]-1].getY();
				temps[j]= mesh.getArrayNode()[id[j]-1].getTemperature();
			}


			for(int pointIntegration = 0; pointIntegration<NUMBER_OF_NODES; pointIntegration++) {
								
								// liczymy jakobian
								double dXdKsi= lokalnaMacierz[pointIntegration][1]/4*(X[0] - X[1] +X[2] - X[3])
										+(-X[0]	+ X[1] + X[2] - X[3])/4;
								double dYdEta= lokalnaMacierz[pointIntegration][0]/4*(Y[0] - Y[1] + Y[2] - Y[3])
										+(-Y[0] - Y[1] + Y[2] + Y[3])/4;
								double dYdKsi= lokalnaMacierz[pointIntegration][1]/4*(Y[0] - Y[1] + Y[2] - Y[3])
										+(-Y[0] + Y[1] + Y[2] - Y[3])/4;
								double dXdEta= lokalnaMacierz[pointIntegration][0]/4*(X[0] - X[1] + X[2] - X[3])
										+(-X[0] - X[1] + X[2] + X[3])/4;
							
								// wyliczamy wyznacznik macierzy
								double detJ=dXdKsi * dYdEta - dYdKsi * dXdEta;
								intTemp = 0;

								// wyliczamy pochodne funkcji kszta?tu po X i po Y
								for(int k=0;k<NUMBER_OF_NODES;k++) {
									dNdX[k]=(dYdEta*dNdKsi[pointIntegration][k]+(-dXdEta)*dNdEta[pointIntegration][k])/detJ;
									dNdY[k]=((-dYdKsi)*dNdKsi[pointIntegration][k]+dXdKsi*dNdEta[pointIntegration][k])/detJ;
									intTemp += temps[k]*N[pointIntegration][k];
								}
								

								
								// wyliczanie dla
								// lokalnej macierzy H
								// macierzy C
								// wektora P
								for(int n=0;n<NUMBER_OF_NODES;n++) {
									for(int o=0;o<NUMBER_OF_NODES;o++) {
										localC = globalData.getSpecificHeat()*globalData.getDensity()*N[pointIntegration][n]*N[pointIntegration][o]*detJ;/////////lokalna macierz C
										localH[n][o] += globalData.getConductivity()*(dNdX[n]*dNdX[o]+dNdY[n]*dNdY[o])*detJ+localC/globalData.getTimeStamp();
										localP[n] += localC/globalData.getTimeStamp() * intTemp;
									}
								}

			}

			//warunek brzegowy
			for(int k=0;k<NUMBER_OF_NODES;k++) {
				int a=(k+1)%4;

				// sprawdzenie czy krawedz elementu jest na brzegu
				if(mesh.getArrayNode()[id[k % 4] - 1].getStatus() && mesh.getArrayNode()[id[(k + 1) % 4] - 1].getStatus()) {
					double polowaBoku = Math.sqrt( Math.pow( mesh.getArrayNode()[id[k%4]-1].getX() - mesh.getArrayNode()[id[(k+1)%4]-1].getX(), 2 ) + Math.pow( mesh.getArrayNode()[id[k%4]-1].getY() - mesh.getArrayNode()[id[(k+1)%4]-1].getY(), 2 ) ) / 2.0;


					if(i == 0) {
						if(a==0) {
							a=1;
						}else if(a == 1) {
							a=0;
						}
					}else if(i==2) {
						if(a==0) {
							a=3;
						}else if(a == 3) {
							a=0;
						}

					}

					// dodanie warunkow brzegowych
					for ( int p = 0; p < 2; p++ ) {
						for ( int n = 0; n < 4; n++ ) {
							for ( int l = 0; l < 4; l++ )
								localH[n][l] += globalData.getAlfa() * localsurface[(k+1)%4].N[p][n] * localsurface[(k+1)%4].N[p][l] * polowaBoku;
							localP[n] += globalData.getAlfa() * globalData.getAmbientTemp() * localsurface[a].N[p][n] * polowaBoku;
						}
					}
				}
			}

			
			// agregacja
			for(int n=0;n<NUMBER_OF_NODES;n++) {
				for(int o=0;o<NUMBER_OF_NODES;o++) {
					globalH[id[n]-1][id[o]-1]+=localH[n][o];
				}
				globalP[id[n]-1]+=localP[n];
			}	
		}

	}
}
