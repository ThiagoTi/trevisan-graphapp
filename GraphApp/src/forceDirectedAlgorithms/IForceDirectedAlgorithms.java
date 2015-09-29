package forceDirectedAlgorithms;

import java.io.IOException;

import com.itextpdf.text.DocumentException;

import graph.Graph;

public interface IForceDirectedAlgorithms {

	void proccess(Graph graph, int width, int height, String filePath) throws IOException, DocumentException;

	double cool(double t, double lambda);

}
