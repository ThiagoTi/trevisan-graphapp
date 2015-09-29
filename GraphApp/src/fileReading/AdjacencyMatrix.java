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

public class AdjacencyMatrix {

	public AdjacencyMatrix() {

	}

	public Graph read(String filePath) {

		Graph graph;
		Node node;
		Edge edge;
		List<Integer> list;
		List<Node> auxNodes;
		List<Edge> auxEdges;
		graph = new Graph();
		
		try {
			int length = 0;
			Scanner input = new Scanner(new File(filePath));
			Scanner file = new Scanner(new File(filePath));

			while (input.hasNextInt()) {
				input.nextInt();
				length++;
			}

			int size = (int) Math.sqrt(length);
			graph.setNeighbors(new ArrayList<LinkedList<Edge>>(size));

			auxNodes = new ArrayList<Node>();
			for (int i = 0; i < size; i++) {
				graph.getNeighbors().add(new LinkedList<Edge>());
				node = new Node();
				auxNodes.add(node);
			}
			graph.setNodes(auxNodes);

			list = new ArrayList<Integer>();
			auxEdges = new ArrayList<Edge>();
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					list.add(file.nextInt());
				}
				// System.out.println(list);
				for (int j = i; j < list.size(); j++) {
					if (list.get(j).equals(1)) {
						edge = new Edge(i, j);
						auxEdges.add(edge);
						graph.addEdge(i, j, edge);
						edge = new Edge(j, i);
						graph.addEdge(j, i, edge);
					}
				}
				graph.getNodes().get(i).setWeight(graph.neighbors(i).size());
				list = new ArrayList<Integer>();
			}
			graph.setEdges(auxEdges);
			
			List<String> vId = new LinkedList<String>();
			List<Double> v1 = new LinkedList<Double>();
			List<Double> v2 = new LinkedList<Double>();
			while (file.hasNext()) {
				vId.add(file.next());
				v1.add((double) file.nextInt());
				v2.add((double) file.nextInt());
			}
			for (Node node2 : graph.getNodes()) {
				int nodeIndex = graph.getNodes().indexOf(node2);
				if (!vId.isEmpty()) {
					graph.getNodes().get(nodeIndex).setId(vId.get(nodeIndex));
					graph.getNodes().get(nodeIndex).setX(v1.get(nodeIndex));
					graph.getNodes().get(nodeIndex).setY(v2.get(nodeIndex));

				}
			}

			for (Edge edge2 : graph.getEdges()) {
				edge2.setId("e" + graph.getEdges().indexOf(edge2));
			}
			input.close();
			file.close();

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
