package graph;

public class Edge {

	private String id;
	private int source;
	private int target;
	private double weight;
	private String label;

	public Edge() {
		// TODO Auto-generated constructor stub
	}

	public Edge(int source, int target) {
		super();
		this.source = source;
		this.target = target;
	}

	public Edge(String id, int source, int target, double weight, String label) {
		super();
		this.id = id;
		this.source = source;
		this.target = target;
		this.weight = weight;
		this.label = label;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.id + ": (" + this.source + ", " + this.target + ") - weight: " + this.weight;
	}

}
