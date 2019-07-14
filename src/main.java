import Gauss.Gauss;
import GeneratorMesh.Mesh;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class main {

	private static final int NUMBER_OF_LINE_IN_FILE = 12;
	private static Mesh mesh;
	private static GlobalData globalData;
    private static double[][] globalH;
	private static double[] globalP;

    private static double[] data = new double[NUMBER_OF_LINE_IN_FILE];
	
	public static void main(String[] args) throws IOException {

		String filePath = "C:\\Users\\maciejKrzywda\\Desktop\\MaciejKrzywdaFEM2D\\MaciejKrzywdaFEM2D\\dane.txt";
		//String filePath = "C:\\Users\\maciejKrzywda\\Desktop\\MaciejKrzywdaFEM2D\\MaciejKrzywdaFEM2D\\dane2.txt";

		for(int i=0;i<NUMBER_OF_LINE_IN_FILE;i++){
			data[i] = 0;
		}

        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
            String numberAsString;

            for (int i = 0; i < 2; i++) {
                numberAsString = fileReader.readLine();
                data[i] = Integer.parseInt(numberAsString);
            }
            for (int i = 2; i < NUMBER_OF_LINE_IN_FILE; i++) {
                numberAsString = fileReader.readLine();
                data[i] = Double.parseDouble(numberAsString);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        globalData = new GlobalData((int)data[0],(int)data[1],data[2],data[3],data[4],data[5],
				data[6],data[7],data[8],data[9],data[10],data[11]);

		globalH = new double[globalData.getNH()*globalData.getNB()][globalData.getNH()*globalData.getNB()];
		globalP = new double[globalData.getNH()*globalData.getNB()];
		generateGrid();
		FiniteElementMethod fem;

        fem = new FiniteElementMethod(globalData, globalH, globalP, mesh);

        int k=0;

		double zmienna=0;

		//licznik=container.getSimTime()/container.getTimeStamp();

		System.out.println("\n\nMax and min temperature in each step");
		System.out.print("\nTime[s]");
		System.out.print("        Min Temp[s]");
		System.out.println("             Max Temp[s]");
		do {
			fem.count();
            double[] t = Gauss.count(globalData.getNH() * globalData.getNB(), globalH, globalP);

			//aktulaizacja temperatury
			for (int i = 0; i < globalData.getNH() * globalData.getNB(); i++)
                mesh.getArrayNode()[i].setTemp(t[i]);

			System.out.print((k + 1) * globalData.getTimeStamp() + "\t");
			System.out.print("       "+ getMin(t));
			System.out.print("      "+ getMax(t));
			System.out.println();
			k++;
			zmienna+=globalData.getTimeStamp();

			zeroData();
		}		while(zmienna<globalData.getSimTime());


	}

	public static double getMin ( double[] t ){
		return Arrays.stream( t ).min().getAsDouble();
	}

	public static double getMax ( double[] t ){
		return Arrays.stream( t ).max().getAsDouble();
	}

	private static void generateGrid() {

        mesh = new Mesh(globalData.getNH(),
                globalData.getNB(),
                globalData.getH(),
                globalData.getB(),
                globalData.getInitialTemp());
        mesh.pokazSiatke();
	}

	private static void zeroData() {
		for(int i=0;i<globalData.getNH()*globalData.getNB();i++) {
			for(int j=0;j<globalData.getNH()*globalData.getNB();j++) {
				globalH[i][j]=0;
			}
			globalP[i]=0;
		}
	}


}

	