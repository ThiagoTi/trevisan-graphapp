package graph;

import java.util.List;

public class Node {

	private String id;
	private double x;
	private double y;
	private List<Integer> neighbors;
	// displacement
	private double[] disp;
	private double weight;
	private boolean coarsened;
	private List<Integer> parents;

	public Node() {
		coarsened = false;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public List<Integer> getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(List<Integer> neighbors) {
		this.neighbors = neighbors;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double[] getDisp() {
		return disp;
	}

	public void setDisp(double[] d) {
		this.disp = d;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public boolean isCoarsened() {
		return coarsened;
	}

	public void setCoarsened(boolean coarsened) {
		this.coarsened = coarsened;
	}

	public List<Integer> getParents() {
		return parents;
	}

	public void setParents(List<Integer> parents) {
		this.parents = parents;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.id + ": (" + this.x + ", " + this.y + ") - weight: " + this.weight + " - parents: " + this.parents;
	}

}
