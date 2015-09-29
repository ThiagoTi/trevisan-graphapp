package forceDirectedAlgorithms;

import export.SvgExporter;
import graph.Edge;
import graph.Graph;
import graph.Node;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;

import com.itextpdf.text.DocumentException;

public class FR implements IForceDirectedAlgorithms {

	private static final double LAMBDA = 0.9;

	public FR() {

	}

	@Override
	public void proccess(Graph graph, int width, int height, String filePath) {
		final double C = 0.2;
		int vertices = graph.getNodes().size();
		double area = height * width;
		double k = C * Math.sqrt(area / vertices);
		double[] delta = new double[2];
		double deltaModule = 0;
		double dispModule = 0;
		int iterations = 10 * vertices;
		double t = iterations;

		String parent = FilenameUtils.getFullPath(filePath);
		String basename = FilenameUtils.getBaseName(filePath);

		randomLayout(graph, width, height);

		try {
			SvgExporter exporter = new SvgExporter();
			exporter.run(graph, parent + basename +"\\original" + ".svg");
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		for (int i = 0; i < iterations; i++) {

			for (Node v : graph.getNodes()) {
				delta[0] = 0;
				delta[1] = 0;
				v.setDisp(delta);
			}

			// calculate (global) repulsive forces
			for (Node v : graph.getNodes()) {
				for (Node u : graph.getNodes()) {
					if (!u.equals(v)) {
						delta[0] = v.getX() - u.getX();
						delta[1] = v.getY() - u.getY();

						deltaModule = Math.sqrt(delta[0] * delta[0] + delta[1] * delta[1]);
						if (deltaModule == 0) {
							System.out.println("errorFr");
							System.exit(0);
							deltaModule = 0.001;
						}
						double force = fr(deltaModule, k);

						// disp = displacement
						double[] disp = new double[2];
						disp[0] = v.getDisp()[0] + delta[0] / deltaModule * force;
						disp[1] = v.getDisp()[1] + delta[1] / deltaModule * force;
						v.setDisp(disp);
					}
				}
			}

			// calculate (local) attractive/spring forces between the edges
			for (Edge e : graph.getEdges()) {
				// Source = v
				// Target = u
				delta[0] = graph.getNodes().get(e.getSource()).getX() - graph.getNodes().get(e.getTarget()).getX();
				delta[1] = graph.getNodes().get(e.getSource()).getY() - graph.getNodes().get(e.getTarget()).getY();
				deltaModule = Math.sqrt(delta[0] * delta[0] + delta[1] * delta[1]);
				if (deltaModule == 0) {
					System.out.println("errorFa");
					System.exit(0);
					deltaModule = 0.001;
				}
				double force = fa(deltaModule, k);
				double[] vDisp = new double[2];
				vDisp[0] = graph.getNodes().get(e.getSource()).getDisp()[0] - delta[0] / deltaModule * force;
				vDisp[1] = graph.getNodes().get(e.getSource()).getDisp()[1] - delta[1] / deltaModule * force;
				graph.getNodes().get(e.getSource()).setDisp(vDisp);
				double[] uDisp = new double[2];
				uDisp[0] = graph.getNodes().get(e.getTarget()).getDisp()[0] + delta[0] / deltaModule * force;
				uDisp[1] = graph.getNodes().get(e.getTarget()).getDisp()[1] + delta[1] / deltaModule * force;
				graph.getNodes().get(e.getTarget()).setDisp(uDisp);
			}

			// reposition of the node v
			for (Node v : graph.getNodes()) {
				dispModule = Math.sqrt(v.getDisp()[0] * v.getDisp()[0] + v.getDisp()[1] * v.getDisp()[1]);
				double posX = v.getX() + ((v.getDisp()[0] / dispModule) * Math.min(Math.abs(v.getDisp()[0]), t));
				double posY = v.getY() + ((v.getDisp()[1] / dispModule) * Math.min(Math.abs(v.getDisp()[1]), t));
				v.setX(posX);
				v.setY(posY);
			}

			t = cool(t, LAMBDA);

			if (i % 5 == 0) {
				try {
					SvgExporter exporter = new SvgExporter();
					exporter.run(graph, parent + basename + "\\iteration" + i + ".svg");
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (DocumentException e1) {
					e1.printStackTrace();
				}
			}

		}
	}

	public double fa(double deltaDistance, double k) {
		return deltaDistance * deltaDistance / k;
	}

	@Override
	public double cool(double t, double lambda) {
		return lambda * t;
	}

	public double fr(double deltaDistance, double k) {
		return k * k / deltaDistance;
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

}
