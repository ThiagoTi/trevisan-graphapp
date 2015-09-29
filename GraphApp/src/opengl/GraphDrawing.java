package opengl;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glVertex2d;
import static org.lwjgl.system.MemoryUtil.NULL;
import graph.Edge;
import graph.Graph;
import graph.Node;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import application.AppController;

import com.itextpdf.text.DocumentException;

public class GraphDrawing {

	private void init() {

		glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));

		if (glfwInit() != GL11.GL_TRUE)
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);

		window = glfwCreateWindow(AppController.WIDTH + 10, AppController.HEIGHT + 10, "First Graph Drawing in OpenGL!", NULL, NULL);

		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
					glfwSetWindowShouldClose(window, GL_TRUE);
			}
		});

		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

		glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - AppController.WIDTH) / 2,
				(GLFWvidmode.height(vidmode) - AppController.HEIGHT) / 2);

		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);

		glfwShowWindow(window);

	}

	public void loop(Graph graph) throws IOException, DocumentException {

		GLContext.createFromCurrent();

		glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

		while (glfwWindowShouldClose(window) == GL_FALSE) {

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			draw(graph);

			glfwSwapBuffers(window);

			glfwPollEvents();

		}
	}

	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;

	private long window;

	public void run(Graph graph) throws IOException, DocumentException {
		// System.out.println("First Graph Drawing in OpenGL!");
		try {
			init();

			loop(graph);

			keyCallback.release();
		} finally {

			glfwTerminate();
			errorCallback.release();
		}
	}

	public void draw(Graph graph) {

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, 800, 0, 600, 1, -1);
		glMatrixMode(GL_MODELVIEW);

		List<Double> posnX = new LinkedList<Double>();
		List<Double> posnY = new LinkedList<Double>();
		for (Node v : graph.getNodes()) {
			posnX.add(v.getX());
			posnY.add(v.getY());
		}
		new Rescale().fit(posnX, posnY, AppController.WIDTH, AppController.HEIGHT);

		// Nodes
		glColor3f(0.8f, 0.0f, 0.0f);
		glBegin(GL_POINTS);
		for (int i = 0; i < posnX.size(); i++) {
			glVertex2d(posnX.get(i), posnY.get(i));
		}
		glEnd();

		// Nodes - circle
		for (int i = 0; i < posnX.size(); i++) {
			glBegin(GL_LINE_LOOP);
			for (int j = 0; j < 10; j++) {
				double theta = 2 * Math.PI * j / 10;// get the current angle
				double x = 2 * Math.cos(theta);// calculate the x component
				double y = 2 * Math.sin(theta);// calculate the y component
				glVertex2d((x + posnX.get(i)), (y + posnY.get(i)));
			}
			glEnd();
		}

		// Edges
		glColor3f(0.0f, 1.0f, 0.0f);
		for (Edge edges : graph.getEdges()) {
			glBegin(GL_LINES);
			glVertex2d(posnX.get(edges.getSource()), posnY.get(edges.getSource()));
			glVertex2d(posnX.get(edges.getTarget()), posnY.get(edges.getTarget()));
			glEnd();

		}

	}
}
