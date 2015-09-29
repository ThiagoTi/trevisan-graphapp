package export;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import opengl.Rescale;

import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.svg.SVGDocument;

import application.AppController;

import com.itextpdf.text.DocumentException;

public class SvgExporter {

	private static int[] xPoints;
	private static int[] yPoints;
	private static final int SIZE = 6;

	public SvgExporter() {
		xPoints = new int[SIZE];
		yPoints = new int[SIZE];
	}

	public void run(Graph graph, String string) throws IOException, DocumentException {

		List<Double> posnX = new LinkedList<Double>();
		List<Double> posnY = new LinkedList<Double>();
		for (Node v : graph.getNodes()) {
			posnX.add(v.getX());
			posnY.add(v.getY());
		}
		new Rescale().fit(posnX, posnY, AppController.WIDTH, AppController.HEIGHT);

		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		String svg = SVGDOMImplementation.SVG_NAMESPACE_URI;
		SVGDocument doc = (SVGDocument) impl.createDocument(svg, "svg", null);

		SVGGraphics2D graphics = new SVGGraphics2D(doc);

		for (Edge edges : graph.getEdges()) {

			int posXSource = posnX.get(edges.getSource()).intValue();
			int posYSource = posnY.get(edges.getSource()).intValue();
			int posXTarget = posnX.get(edges.getTarget()).intValue();
			int posYTarget = posnY.get(edges.getTarget()).intValue();

			graphics.setColor(Color.BLACK);
			// draw Node IDs
			graphics.drawString(graph.getNodes().get(edges.getSource()).getId(), (float) (posXSource + 5), (float) (posYSource - 5));
			graphics.drawString(graph.getNodes().get(edges.getTarget()).getId(), (float) (posXTarget + 5), (float) (posYTarget - 5));

			// draw Node coordinates
			for (int j = 0; j < SIZE; j++) {
				double theta = 2 * Math.PI * j / SIZE;// get the current angle
				double x = 2 * Math.cos(theta);// calculate the x component
				double y = 2 * Math.sin(theta);// calculate the y component
				double aux = x + posXSource;
				double aux2 = y + posYSource;
				xPoints[j] = (int) aux;
				yPoints[j] = (int) aux2;
			}
			graphics.fillPolygon(xPoints, yPoints, SIZE);
			xPoints = new int[SIZE];
			yPoints = new int[SIZE];
			for (int j = 0; j < SIZE; j++) {
				double theta = 2 * Math.PI * j / SIZE;// get the current angle
				double x = 2 * Math.cos(theta);// calculate the x component
				double y = 2 * Math.sin(theta);// calculate the y component
				double aux = x + posXTarget;
				double aux2 = y + posYTarget;
				xPoints[j] = (int) aux;
				yPoints[j] = (int) aux2;
			}
			graphics.fillPolygon(xPoints, yPoints, SIZE);
			xPoints = new int[SIZE];
			yPoints = new int[SIZE];

			// draw Edges
			graphics.setColor(Color.BLUE);
			graphics.drawLine(posXSource, posYSource, posXTarget, posYTarget);

		}

		String basename = FilenameUtils.getBaseName(string);
		String parent = FilenameUtils.getFullPath(string);

		File file = new File(parent);
		file.mkdirs();

		FileOutputStream stream = new FileOutputStream(file.getPath() + "\\" + basename + ".svg");
		Writer out = new OutputStreamWriter(stream);
		graphics.stream(out, true);
		out.flush();
		out.close();

		File file2 = new File(parent);
		file2.mkdirs();

		SvgToPdf svgToPdf = new SvgToPdf();
		svgToPdf.createPdf(file.getPath() + "\\" + basename + ".svg", file2.getPath() + "\\PDF" + basename + ".pdf");

	}
}
