package fileReading;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class EdgeList {

	public Graph read(String filePath) {

		Graph graph;
		Node node;
		Edge edge;
		List<Node> auxNodes;
		List<Edge> auxEdges;
		graph = new Graph();
		auxNodes = new ArrayList<Node>();
		auxEdges = new ArrayList<Edge>();
		int size = 0;

		try {
			Scanner scanner = new Scanner(new File(filePath));

			size = scanner.nextInt();
			graph.setNeighbors(new ArrayList<LinkedList<Edge>>(size));
			for (int i = 0; i < size; i++) {
				graph.getNeighbors().add(new LinkedList<Edge>());
				node = new Node();
				auxNodes.add(node);
			}
			graph.setNodes(auxNodes);

			while (scanner.hasNextInt()) {
				int v1 = scanner.nextInt();
				int v2 = scanner.nextInt();
				edge = new Edge(v1, v2);
				auxEdges.add(edge);
				graph.addEdge(v1, v2, edge);
				edge = new Edge(v2, v1);
				graph.addEdge(v2, v1, edge);
			}
			graph.setEdges(auxEdges);
			
			// Pattern pattern =
			// Pattern.compile("\\w\\d+\\s\\d{1,3}\\s\\d{1,3}");

			List<String> vId = new LinkedList<String>();
			List<Double> v1 = new LinkedList<Double>();
			List<Double> v2 = new LinkedList<Double>();
			while (scanner.hasNext()) {
				vId.add(scanner.next());
				v1.add((double) scanner.nextInt());
				v2.add((double) scanner.nextInt());
			}

			for (Node node2 : graph.getNodes()) {
				int nodeIndex = graph.getNodes().indexOf(node2);
				if (!vId.isEmpty()) {
					graph.getNodes().get(nodeIndex).setId(vId.get(nodeIndex));
					graph.getNodes().get(nodeIndex).setX(v1.get(nodeIndex));
					graph.getNodes().get(nodeIndex).setY(v2.get(nodeIndex));

				}
				graph.getNodes().get(nodeIndex).setWeight(graph.neighbors(nodeIndex).size());
			}
			for (Edge edge2 : graph.getEdges()) {
				edge2.setId("e" + graph.getEdges().indexOf(edge2));
			}
			scanner.close();

		} catch (FileNotFoundException e) {
			System.out.println("Error: could not find file ");
			System.exit(0);
		} catch (InputMismatchException e) {
			System.out.println("Error: incorrect format in ");
			System.exit(0);
		}
		return graph;
	}
}
