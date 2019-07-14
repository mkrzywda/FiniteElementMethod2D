package LocalElement;

import GeneratorMesh.Node;

public class LocalSurface {
	public double[][] N;
	public Node nodes[];

	public LocalSurface(Node n1, Node n2 ) {
		N = new double[2][4];
		nodes = new Node[2];
		nodes[0] = n1;
		nodes[1] = n2;
	}
}