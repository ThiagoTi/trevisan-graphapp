package graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Graph {

	private List<Node> nodes;
	private List<Edge> edges;
	private ArrayList<LinkedList<Edge>> neighbors;

	public Graph() {

	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}

	public void addEdge(int v1, int v2, Edge edge1) {
			neighbors(v1).add(edge1);
	}

	public LinkedList<Edge> neighbors(int v) {
		return getNeighbors().get(v);
	}

	public ArrayList<LinkedList<Edge>> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(ArrayList<LinkedList<Edge>> neighbors) {
		this.neighbors = neighbors;
	}

}
