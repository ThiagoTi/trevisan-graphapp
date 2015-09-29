package opengl;

import java.util.List;

import graph.Graph;
import graph.Node;

public class Rescale {

	public Rescale() {

	}

	public void fit(Graph graph, int width, int height) {

		double xMax = Double.MIN_VALUE;
		double yMax = Double.MIN_VALUE;
		double xMin = Double.MAX_VALUE;
		double yMin = Double.MAX_VALUE;

		for (Node node : graph.getNodes()) {
			double x = node.getX();
			double y = node.getY();
			xMax = Math.max(xMax, x);
			yMax = Math.max(yMax, y);
			xMin = Math.min(xMin, x);
			yMin = Math.min(yMin, y);
		}

		for (Node node : graph.getNodes()) {
			double X;
			double Y;
			X = node.getX() - xMin + 100;
			Y = node.getY() - yMin + 100;
			node.setX(X);
			node.setY(Y);
		}

		xMax = Double.MIN_VALUE;
		yMax = Double.MIN_VALUE;
		xMin = Double.MAX_VALUE;
		yMin = Double.MAX_VALUE;
		for (Node node : graph.getNodes()) {
			double x = node.getX();
			double y = node.getY();
			xMax = Math.max(xMax, x);
			yMax = Math.max(yMax, y);
			xMin = Math.min(xMin, x);
			yMin = Math.min(yMin, y);
		}

		double diffX = (width - 20) / xMax;
		double diffY = (height - 20) / yMax;

		double diff = Math.min(diffX, diffY);
		resize(graph, diff);

	}

	public void fit(List<Double> posnX, List<Double> posnY, int width, int height) {

		double xMax = Double.MIN_VALUE;
		double yMax = Double.MIN_VALUE;
		double xMin = Double.MAX_VALUE;
		double yMin = Double.MAX_VALUE;

		for (Double X : posnX) {
			xMax = Math.max(xMax, X);
			xMin = Math.min(xMin, X);
		}
		for (Double Y : posnY) {
			yMax = Math.max(yMax, Y);
			yMin = Math.min(yMin, Y);
		}

		for (Double X : posnX) {
			double x;
			x = X - xMin + 50;
			posnX.set(posnX.indexOf(X), x);
		}
		for (Double Y : posnY) {
			double y;
			y = Y - yMin + 50;
			posnY.set(posnY.indexOf(Y), y);
		}

		xMax = Double.MIN_VALUE;
		yMax = Double.MIN_VALUE;
		xMin = Double.MAX_VALUE;
		yMin = Double.MAX_VALUE;
		for (Double X : posnX) {
			xMax = Math.max(xMax, X);
			xMin = Math.min(xMin, X);
		}
		for (Double Y : posnY) {
			yMax = Math.max(yMax, Y);
			yMin = Math.min(yMin, Y);
		}

		double diffX = (width - 50) / xMax;
		double diffY = (height - 50d) / yMax;

		double diff = Math.min(diffX, diffY);
		resize(posnX, posnY, diff);

	}

	public void resize(Graph graph, double diff) {
		double X;
		double Y;
		for (Node node : graph.getNodes()) {
			X = node.getX() * diff;
			Y = node.getY() * diff;
			node.setX(X);
			node.setY(Y);
		}
	}

	public void resize(List<Double> posnX, List<Double> posnY, double diff) {
		double x;
		double y;
		for (Double X : posnX) {
			x = X * diff;
			posnX.set(posnX.indexOf(X), x);
		}
		for (Double Y : posnY) {
			y = Y * diff;
			posnY.set(posnY.indexOf(Y), y);
		}
	}

}
