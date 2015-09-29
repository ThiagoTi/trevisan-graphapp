package application;

import export.SvgExporter;
import fileReading.AdjacencyMatrix;
import fileReading.EdgeList;
import forceDirectedAlgorithms.FR;
import forceDirectedAlgorithms.IForceDirectedAlgorithms;
import forceDirectedAlgorithms.Walshaw;
import graph.Graph;
import graph.Node;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import com.itextpdf.text.DocumentException;

public class AppController implements Initializable {

	@FXML
	private Button button;
	@FXML
	private Button start;
	@FXML
	private Label label;
	@FXML
	private Label result;
	@FXML
	private Label save;
	@FXML
	private TextField input;
	@FXML
	private AnchorPane pane;
	@FXML
	private RadioButton adjacencyMatrix;
	@FXML
	private RadioButton edgeList;
	@FXML
	private RadioButton none;
	@FXML
	private RadioButton fr;
	@FXML
	private RadioButton walshaw;

	String filePath;

	public static final int WIDTH = 800;
	public static final int HEIGHT = 800;

	@FXML
	private void browseFile(ActionEvent event) {
		File file = null;
		FileChooser fileChooser = new FileChooser();
		// Show open file dialog
		file = fileChooser.showOpenDialog(null);
		input.setText(file.getPath());
	}

	@FXML
	private void play() throws IOException, DocumentException {
		filePath = input.getText();
		result.setText(null);
		save.setText(null);

		if (adjacencyMatrix.isSelected()) {
			Graph graph = new AdjacencyMatrix().read(filePath);
			result.setText("Running...");

			try {
				FileChooser fileChooser = new FileChooser();
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SVG files (*.svg)", "*.svg");
				fileChooser.getExtensionFilters().add(extFilter);
				File file = fileChooser.showSaveDialog(null);

				if (!file.equals(null)) {
					String filePath = file.getPath();
					if (fr.isSelected()) {
						IForceDirectedAlgorithms fr = new FR();
						fr.proccess(graph, WIDTH, HEIGHT, filePath);
					}
					if (walshaw.isSelected()) {
						IForceDirectedAlgorithms walshaw = new Walshaw();
						walshaw.proccess(graph, WIDTH, HEIGHT, filePath);
					}
					SvgExporter exporter = new SvgExporter();
					exporter.run(graph, filePath);
					save.setText("Saved");
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (DocumentException e1) {
				e1.printStackTrace();
			}

			result.setText("Done");

			for (Node node : graph.getNodes()) {
				System.out.println("node: " + node.toString());
			}
		} else {
			Graph graph = new EdgeList().read(filePath);
			result.setText("Running...");

			try {
				FileChooser fileChooser = new FileChooser();
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("SVG files (*.svg)", "*.svg");
				fileChooser.getExtensionFilters().add(extFilter);
				File file = fileChooser.showSaveDialog(null);

				if (!file.equals(null)) {
					String filePath = file.getPath();
					if (fr.isSelected()) {
						IForceDirectedAlgorithms fr = new FR();
						fr.proccess(graph, WIDTH, HEIGHT, filePath);
					}
					if (walshaw.isSelected()) {
						IForceDirectedAlgorithms walshaw = new Walshaw();
						walshaw.proccess(graph, WIDTH, HEIGHT, filePath);
					}
					SvgExporter exporter = new SvgExporter();
					exporter.run(graph, filePath);
					save.setText("Saved");
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (DocumentException e1) {
				e1.printStackTrace();
			}

			result.setText("Done");
			
			for (Node node : graph.getNodes()) {
				System.out.println("node: " + node.toString());
			}
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		final ToggleGroup group = new ToggleGroup();
		adjacencyMatrix.setToggleGroup(group);
		edgeList.setToggleGroup(group);
		adjacencyMatrix.setSelected(true);

		final ToggleGroup group2 = new ToggleGroup();
		none.setToggleGroup(group2);
		fr.setToggleGroup(group2);
		walshaw.setToggleGroup(group2);
		none.setSelected(true);
	}

}
