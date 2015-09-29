package forceDirectedAlgorithms;

import export.SvgExporter;
import graph.Edge;
import graph.Graph;
import graph.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;

import com.itextpdf.text.DocumentException;

public class Walshaw implements IForceDirectedAlgorithms {

	private static final double C = 0.2;
	private static final double LAMBDA = 0.89;
	private static double[] K;

	public Walshaw() {

	}

	public void proccess(Graph originalGraph, int width, int height, String filePath) throws IOException, DocumentException {

		boolean converged = false;
		List<Graph> graphs;
		graphs = converge(originalGraph);

		// vector of displacements
		double[] theta = new double[2]; // 2 positions: x and y
		double thetaModule = 0;
		// difference vector
		double[] delta = new double[2]; // 2 positions: x and y
		double deltaModule = 0;

		// graph index
		int l = graphs.size() - 1;
		Graph graph = graphs.get(l);
		Graph parentGraph;

		String parent = FilenameUtils.getFullPath(filePath);
		String basename = FilenameUtils.getBaseName(filePath);

		// random layout
		randomLayout(graph, width, height);

		try {
			SvgExporter exporter = new SvgExporter();
			exporter.run(graph, parent + basename + "\\original" + ".svg");
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}

		// Natural spring length, k
		naturalSpringLength(graph, l);

		// maximum distance over which repulsive forces will act
		double[] r = new double[K.length];
		for (int i = 0; i < r.length; i++) {
			r[i] = 2 * (l + 1) * K[i];
		}

		List<Double> oldPosnX = new LinkedList<Double>();
		List<Double> oldPosnY = new LinkedList<Double>();
		List<Double> posnX = new LinkedList<Double>();
		List<Double> posnY = new LinkedList<Double>();

		int graphNumber = l;
		graph = graphs.get(graphNumber);
		int iterations = 1;

		while (graphNumber >= 0) {
			// temperature
			double t = K[graphNumber];
			converged = false;
			System.out.println("##############################################################################");
			System.out.println("##############################################################################");
			System.out.println("graphNumber: " + graphNumber);

			posnX = new LinkedList<Double>();
			posnY = new LinkedList<Double>();
			for (Node v : graph.getNodes()) {
				System.out.println(v.toString());
				posnX.add(v.getX());
				posnY.add(v.getY());
			}
			while (!converged) {
				converged = true;
				oldPosnX = new LinkedList<Double>();
				oldPosnY = new LinkedList<Double>();

				for (Node v : graph.getNodes()) {
					oldPosnX.add(v.getX());
					oldPosnY.add(v.getY());
				}

				for (Node v : graph.getNodes()) {
					// initialisation
					theta[0] = 0;
					theta[1] = 0;
					int nodeV = graph.getNodes().indexOf(v);
					int neighbor = -1;

					// calculate (global) repulsive forces
					for (Node u : graph.getNodes()) {
						if (!u.equals(v)) {
							int nodeU = graph.getNodes().indexOf(u);
							delta[0] = posnX.get(nodeU) - posnX.get(nodeV);
							delta[1] = posnY.get(nodeU) - posnY.get(nodeV);

							deltaModule = Math.sqrt(delta[0] * delta[0] + delta[1] * delta[1]);
							if (deltaModule == 0) {
								System.out.println("posXU: " + posnX.get(nodeU) + " - posXV: " + posnX.get(nodeV));
								System.out.println("posYU: " + posnY.get(nodeU) + " - posYV: " + posnY.get(nodeV));
								System.out.println("errorFr");
								System.exit(0);
								deltaModule = 0.001;
							}

							double force = fr(deltaModule, u.getWeight(), K[graphNumber], r[graphNumber]);
							theta[0] += (delta[0] / deltaModule) * force;
							theta[1] += (delta[1] / deltaModule) * force;

						}
					}

					// calculate (local) attractive/spring forces

					// loop through the nodes adjacent to the node
					for (int j = 0; j < graph.neighbors(nodeV).size(); j++) {
						neighbor = graph.neighbors(nodeV).get(j).getTarget();
						delta[0] = posnX.get(neighbor) - posnX.get(nodeV);
						delta[1] = posnY.get(neighbor) - posnY.get(nodeV);
						deltaModule = Math.sqrt(delta[0] * delta[0] + delta[1] * delta[1]);
						if (deltaModule == 0) {
							System.out.println("posXneighbor: " + posnX.get(neighbor) + " - posXV: " + posnX.get(nodeV));
							System.out.println("posYneighbor: " + posnY.get(neighbor) + " - posYV: " + posnY.get(nodeV));
							System.out.println("errorFa");
							System.exit(0);
							deltaModule = 0.001;
						}

						double force = fa(deltaModule, K[graphNumber]);
						theta[0] += (delta[0] / deltaModule) * force;
						theta[1] += (delta[1] / deltaModule) * force;
					}

					// reposition of the node v
					thetaModule = Math.sqrt(theta[0] * theta[0] + theta[1] * theta[1]);

					if (thetaModule == 0) {
						System.out.println("errorReposition");
						System.exit(0);
						thetaModule = 0.001;
					}
					double posX = v.getX() + (theta[0] / thetaModule) * Math.min(thetaModule, t);
					double posY = v.getY() + (theta[1] / thetaModule) * Math.min(thetaModule, t);
					v.setX(posX);
					v.setY(posY);
					delta[0] = v.getX() - oldPosnX.get(nodeV);
					delta[1] = v.getY() - oldPosnY.get(nodeV);

					deltaModule = Math.sqrt(delta[0] * delta[0] + delta[1] * delta[1]);

					if (deltaModule > K[graphNumber] * 0.01)
						converged = false;
				}
				t = cool(t, LAMBDA);

				if (iterations % 5 == 0) {
					try {
						SvgExporter exporter = new SvgExporter();
						exporter.run(graph, parent + basename + "\\iteration" + iterations + ".svg");
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (DocumentException e1) {
						e1.printStackTrace();
					}
				}
				iterations++;

			}

			graphNumber--;
			System.out.println("graphNumber: " + graphNumber);

			if (graphNumber >= 0) {
				parentGraph = graphs.get(graphNumber);

				for (Node v : graph.getNodes()) {
					int parent0 = v.getParents().get(0);
					int parent1 = v.getParents().get(1);
					double X = v.getX();
					double Y = v.getY();
					double adjust = K[graphNumber] * 0.001;
					System.out.println(adjust);
					double aux;

					aux = X + plusOrMinusOne() * adjust;
					parentGraph.getNodes().get(parent0).setX(aux);

					aux = Y + plusOrMinusOne() * adjust;
					parentGraph.getNodes().get(parent0).setY(aux);

					parentGraph.getNodes().get(parent1).setX(X);

					parentGraph.getNodes().get(parent1).setY(Y);

					System.out.println(v.toString());
				}

				graph = graphs.get(graphNumber);
			}
		}
	}

	private double plusOrMinusOne() {
		if (Math.random() < 0.5)
			return 1.0;
		else
			return -1.0;
	}

	public double fr(double x, double w, double k, double R) {
		if (x <= R) {
			return -C * w * (k * k) / x;
		} else {
			return 0.0;
		}
	}

	// Natural spring length, k
	public void naturalSpringLength(Graph graph, int l) {

		double[] delta = new double[2];
		double deltaModule = 0;
		K = new double[l + 1];
		double sqrt = Math.sqrt(4.0 / 7.0);

		int count = l;
		while (count >= 0) {
			if (count < l) {
				K[count] = sqrt * K[count + 1];
			} else {
				for (Node v : graph.getNodes()) {
					for (Node u : graph.getNodes()) {
						if (!u.equals(v)) {
							delta[0] = u.getX() - v.getX();
							delta[1] = u.getY() - v.getY();
							deltaModule = Math.sqrt(delta[0] * delta[0] + delta[1] * delta[1]);
						}
					}
				}
				K[l] = deltaModule;
			}
			count--;
		}

	}

	@Override
	public double cool(double t, double lambda) {
		return lambda * t;
	}

	public double fa(double x, double k) {
		return x * x / k;
	}

	public void randomLayout(Graph graph, int width, int height) {
		Set<Double> coordinates = new LinkedHashSet<Double>();
		Random random = new Random();
		for (Node node2 : graph.getNodes()) {
			int nodeIndex = graph.getNodes().indexOf(node2);
			int flag = 0;
			boolean added = false;
			do {
				// random:----(min)------(max)-----(min)
				double randx = (1) + ((width - 1) - (1) + 1) * random.nextDouble();
				double randy = (1) + ((height - 1) - (1) + 1) * random.nextDouble();
				try {
					coordinates.add(randx);
					coordinates.add(randy);
					added = true;
				} catch (Exception e) {
					System.out.println(e);
				}
				if (added) {
					if (!graph.getNodes().contains(nodeIndex)) {
						graph.getNodes().get(nodeIndex).setX(randx);
						graph.getNodes().get(nodeIndex).setY(randy);
						flag = 1;
					}
				}
			} while (flag == 0);
			coordinates = new LinkedHashSet<Double>();

			node2.setId("n" + nodeIndex);
		}
	}

	public List<Graph> converge(Graph graph) {
		System.out.println("################################");
		System.out.println("entrou");

		System.out.println("passou");
		List<Graph> graphs = new ArrayList<Graph>();
		graphs.add(graph);
		Graph oldGraph = graph;

		do {
			System.out.println("####################################----------------------------------");
			System.out.println("new graph");
			System.out.println();
			Graph childGraph = new Graph();
			Node newNode;
			Edge newEdge;
			List<Node> nodeList = new ArrayList<Node>();
			List<Edge> edgeList = new ArrayList<Edge>();

			for (Node node : oldGraph.getNodes()) {
				// check if it is already coarsened
				if (!node.isCoarsened()) {
					double min = Double.MAX_VALUE;
					int target = -1;
					int nodeIndex = oldGraph.getNodes().indexOf(node);
					int neighbor = -1;
					System.out.println("teste: " + node.getId());

					// loop through the nodes adjacent to the node
					for (int i = 0; i < oldGraph.neighbors(nodeIndex).size(); i++) {
						neighbor = oldGraph.neighbors(nodeIndex).get(i).getTarget();
						System.out.println("-> " + neighbor);

						// looking for the node with the smallest weight that is
						// not coarsened yet
						if (!oldGraph.getNodes().get(neighbor).isCoarsened()) {
							if (oldGraph.getNodes().get(neighbor).getWeight() < min) {
								min = oldGraph.getNodes().get(neighbor).getWeight();
								target = oldGraph.getNodes().indexOf(oldGraph.getNodes().get(neighbor));
							}
						}
					}

					// if it fails to find a neighbour, it matches with itself
					if (target == -1) {
						target = nodeIndex;
					}

					oldGraph.getNodes().get(nodeIndex).setCoarsened(true);
					oldGraph.getNodes().get(target).setCoarsened(true);
					List<Integer> parents = new ArrayList<Integer>();
					parents.add(nodeIndex);
					parents.add(target);
					double weight = oldGraph.getNodes().get(nodeIndex).getWeight() + oldGraph.getNodes().get(target).getWeight();
					newNode = new Node();
					newNode.setParents(parents);
					newNode.setWeight(weight);
					nodeList.add(newNode);
				}
			}

			childGraph.setNodes(nodeList);
			int size = childGraph.getNodes().size();
			childGraph.setNeighbors(new ArrayList<LinkedList<Edge>>(size));
			childGraph.setEdges(new ArrayList<Edge>());

			for (int i = 0; i < size; i++) {
				childGraph.getNeighbors().add(new LinkedList<Edge>());
			}

			for (Node childNode : childGraph.getNodes()) {
				childNode.setId("n" + childGraph.getNodes().indexOf(childNode));
			}

			List<Integer> listV1 = new ArrayList<Integer>();
			List<Integer> listV2 = new ArrayList<Integer>();
			int count = 0;
			boolean ok = false;

			// after established the nodes
			// rearrange the edges
			for (Node oldNode : oldGraph.getNodes()) {
				int nodeIndex = oldGraph.getNodes().indexOf(oldNode);
				int neighbor = -1;
				for (Node childNode : childGraph.getNodes()) {
					if (childNode.getParents().contains(nodeIndex)) {
						for (int i = 0; i < oldGraph.neighbors(nodeIndex).size(); i++) {
							neighbor = oldGraph.neighbors(nodeIndex).get(i).getTarget();
							if (!childNode.getParents().contains(neighbor)) {
								for (Node childNeighborNode : childGraph.getNodes()) {
									if (childNeighborNode.getParents().contains(neighbor)) {
										int v1 = childGraph.getNodes().indexOf(childNode);
										int v2 = childGraph.getNodes().indexOf(childNeighborNode);
										if (count == 0) {
											listV1.add(v1);
											listV2.add(v2);
										} else {
											ok = true;
											for (int j = 0; j < listV1.size(); j++) {
												boolean testV1 = listV1.get(j).equals(v1);
												boolean testV2 = listV2.get(j).equals(v2);
												if (testV1 && testV2) {
													ok = false;
												}
											}
										}
										if (ok) {
											listV1.add(v1);
											listV2.add(v2);
										}
										count++;
									}
								}
							}
						}
					}

				}
			}

			for (int i = 0; i < listV1.size(); i++) {
				int v1 = listV1.get(i);
				int v2 = listV2.get(i);
				if (v1 < v2) {
					newEdge = new Edge(v1, v2);
					edgeList.add(newEdge);
					childGraph.addEdge(v1, v2, newEdge);
					newEdge = new Edge(v2, v1);
					childGraph.addEdge(v2, v1, newEdge);
				}
			}

			childGraph.setEdges(edgeList);

			for (Edge edge : childGraph.getEdges()) {
				edge.setId("e" + childGraph.getEdges().indexOf(edge));
			}

			graphs.add(childGraph);

			// for (Node node : childGraph.getNodes()) {
			// int nodeIndex = childGraph.getNodes().indexOf(node);
			// int neighbor = -1;
			// System.out.print("Node: " + node.toString() + " = {");
			// for (int i = 0; i < childGraph.neighbors(nodeIndex).size(); i++)
			// {
			// neighbor = childGraph.neighbors(nodeIndex).get(i).getTarget();
			// System.out.print(neighbor);
			// if (childGraph.neighbors(nodeIndex).size() > i + 1) {
			// System.out.print(", ");
			// } else {
			// System.out.println("}");
			// }
			// }
			// }

			oldGraph = new Graph();
			oldGraph = childGraph;

		} while (oldGraph.getNodes().size() > 2);

		return graphs;
	}
}
