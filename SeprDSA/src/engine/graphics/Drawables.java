package engine.graphics;

import java.awt.Canvas;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import org.la4j.vector.Vector;
import org.la4j.vector.dense.BasicVector;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.Display;
import org.lwjgl.LWJGLException;

import engine.graphics.display.DisplayMode;
import engine.graphics.drawing.Drawing;

public class Drawables {
	private static List<Drawable> drawables;
	private static List<Drawing> drawings;
	static {
		drawables = new ArrayList<Drawable>();
	}

	public static void add(Drawable d) {
		drawables.add(d);
	}

	public static void remove(Drawable d) {
		for (int i = 0; i < drawables.size(); i++) {
			if (drawables.get(i) == d) {
				drawables.remove(i);
				if (drawings.size() > i)
					drawings.remove(i);
			}
		}
	}

	private static int width;
	private static int height;

	public static void initialise(DisplayMode d, int width, int height,
			Canvas canvas) {
		Drawables.width = width;
		Drawables.height = height;

		try {
			Display.setParent(canvas);
			d.set();
			Display.create();
			Display.setVSyncEnabled(true);
			Display.setResizable(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		resize();
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);

		GL11.glEnable(GL31.GL_TEXTURE_RECTANGLE);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
		
	}

	public static Vector windowCoords(Vector pos) {
		int dw = Display.getWidth();
		int dh = Display.getHeight();
		double widthr = (double) (dw) / (double) (width);
		double heightr = (double) (dh) / (double) (height);
		if (heightr < widthr) {
			widthr = heightr;
		}
		double x = Display.getX();
		double y = Display.getY();
		Vector ret = new BasicVector(new double[] {
				 (pos.get(0) - x) - (dw / 2.0), (pos.get(1) - y) - (dh / 2.0)  });
		return ret.divide(widthr);
	}

	private static void resize() {
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		int dw = Display.getWidth();
		int dh = Display.getHeight();
		GL11.glViewport(0, 0, dw, dh);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		// GL11.glOrtho(-width / 2, width / 2, -height / 2, height / 2, 1, -1);
		GL11.glOrtho(-dw / 2, dw / 2, -dh / 2, dh / 2, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		double widthr = (double) (dw) / (double) (width);
		double heightr = (double) (dh) / (double) (height);
		if (heightr < widthr) {
			widthr = heightr;
		}
		GL11.glScaled(widthr, widthr, 0.0f);
	}

	public static Vector virtualDisplaySize() {
		return new BasicVector(new double[] { width, height });
	}

	public static void deinitialise() {
		Display.destroy();
	}

	public static List<Drawable> collisions(Vector pos) {
		List<Drawable> ret = new ArrayList<Drawable>();
		for (int i = 0; i < drawings.size(); i++) {
			if (drawings.get(i).hit(pos)) {
				ret.add(drawables.get(i));
			}
		}
		return ret;
	}

	public static void logic() {
		if (Display.wasResized()) {
			resize();
		}
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		Collections.sort(drawables);
		drawings = new ArrayList<Drawing>();
		for (int i = 0; i < drawables.size(); i++) {
			drawings.add(drawables.get(i).draw());
			drawings.get(i).render();
		}
		Display.update();
	}
}
