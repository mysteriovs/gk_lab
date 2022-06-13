package lab5;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.*;

public class lab5 extends GLJPanel implements GLEventListener, KeyListener {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		JFrame window = new JFrame("Some Objects in 3D");
		lab5 panel = new lab5();
		window.setContentPane(panel);
		window.pack();
		window.setResizable(false);
		window.setLocation(50, 50);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}

	public lab5() {
		super(new GLCapabilities(null));
		setPreferredSize(new Dimension(700, 700));
		addGLEventListener(this);
		addKeyListener(this);
	}

	private int objectNumber = 1;

	private boolean useAnaglyph = false;

	private int rotateX = 0;
	private int rotateY = 0;
	private int rotateZ = 0;

	private void pyramidWalls(float n, GL2 gl2) {
		float deg = 360 / n;
		gl2.glBegin(GL.GL_TRIANGLE_FAN);
		gl2.glVertex3d(0, 0, 0);
		for (float i = 1; i <= n + 1; i++) {
			gl2.glVertex3d(Math.cos(Math.toRadians(i * deg)), Math.sin(Math.toRadians(i * deg)), 2);
		}
		gl2.glEnd();
	}

	private void pyramidBase(float n, GL2 gl2) {
		float deg = 360 / n;
		gl2.glBegin(GL.GL_TRIANGLE_FAN);
		gl2.glVertex3d(0, 0, 2);
		for (float i = 1; i <= n + 1; i++) {
			gl2.glVertex3d(Math.cos(Math.toRadians(i * deg)), Math.sin(Math.toRadians(i * deg)), 2);
		}
		gl2.glEnd();
	}

	private void pyramid(float n, float scale, GL2 gl2) {
		gl2.glColor3f(0, 0, (float) 0);
		gl2.glScalef(scale, scale, scale);
		gl2.glRotatef(0, 0, 90, 0);
		gl2.glTranslatef(0, 0, -1);

		pyramidWalls(n, gl2);
		pyramidBase(n, gl2);
	}

	private void corkscrew(int n, float scale, GL2 gl2) {
		gl2.glColor3f(0, 0, (float) 0);
		gl2.glScalef(scale, scale, scale);
		gl2.glLineWidth(5);
		gl2.glRotatef(0, 0, 90, 0);
		gl2.glTranslatef(0, 0, -1);

		gl2.glBegin(GL.GL_LINE_STRIP);
		int res = 36;
		float deg = 360 / res;

		for (float i = 1; i <= n * res; i++) {
			double x = Math.cos(Math.toRadians(i * deg));
			double y = Math.sin(Math.toRadians(i * deg));
			gl2.glVertex3d(x * (0.01f * i), y * (0.01f * i), (i / res) - (n / 2));
		}
		gl2.glEnd();
	}

	private void draw(GL2 gl2) {

		gl2.glRotatef(rotateZ, 0, 0, 1);
		gl2.glRotatef(rotateY, 0, 1, 0);
		gl2.glRotatef(rotateX, 1, 0, 0);

		switch (objectNumber) {
		case 1:
			corkscrew(15, 1, gl2);
			break;
		case 2:
			pyramid(15, 3, gl2);
			break;
		}

	}

	public void display(GLAutoDrawable drawable) {

		GL2 gl2 = drawable.getGL().getGL2();

		if (useAnaglyph) {
			gl2.glDisable(GL2.GL_COLOR_MATERIAL);
			gl2.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE, new float[] { 1, 1, 1, 1 }, 0);
		} else {
			gl2.glEnable(GL2.GL_COLOR_MATERIAL);
		}
		gl2.glNormal3f(0, 0, 1);

		gl2.glClearColor(0, 0, 0, 1);
		gl2.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

		if (useAnaglyph == false) {
			gl2.glLoadIdentity();
			gl2.glTranslated(0, 0, -15);
			draw(gl2);
		} else {
			gl2.glLoadIdentity();
			gl2.glColorMask(true, false, false, true);
			gl2.glRotatef(4, 0, 1, 0);
			gl2.glTranslated(1, 0, -15);
			draw(gl2);
			gl2.glColorMask(true, false, false, true);
			gl2.glClear(GL2.GL_DEPTH_BUFFER_BIT);
			gl2.glLoadIdentity();
			gl2.glRotatef(-4, 0, 1, 0);
			gl2.glTranslated(-1, 0, -15);
			gl2.glColorMask(false, true, true, true);
			draw(gl2);
			gl2.glColorMask(true, true, true, true);
		}

	}

	public void init(GLAutoDrawable drawable) {
		GL2 gl2 = drawable.getGL().getGL2();
		gl2.glMatrixMode(GL2.GL_PROJECTION);
		gl2.glFrustum(-3.5, 3.5, -3.5, 3.5, 5, 25);
		gl2.glMatrixMode(GL2.GL_MODELVIEW);
		gl2.glEnable(GL2.GL_LIGHTING);
		gl2.glEnable(GL2.GL_LIGHT0);
		gl2.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float[] { 0.7f, 0.7f, 0.7f }, 0);
		gl2.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, 1);
		gl2.glEnable(GL2.GL_DEPTH_TEST);
		gl2.glLineWidth(3);
	}

	public void dispose(GLAutoDrawable drawable) {
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
	}

	public void keyPressed(KeyEvent evt) {
		int key = evt.getKeyCode();
		boolean repaint = true;
		if (key == KeyEvent.VK_LEFT)
			rotateY -= 6;
		else if (key == KeyEvent.VK_RIGHT)
			rotateY += 6;
		else if (key == KeyEvent.VK_DOWN)
			rotateX += 6;
		else if (key == KeyEvent.VK_UP)
			rotateX -= 6;
		else if (key == KeyEvent.VK_PAGE_UP)
			rotateZ += 6;
		else if (key == KeyEvent.VK_PAGE_DOWN)
			rotateZ -= 6;
		else if (key == KeyEvent.VK_HOME)
			rotateX = rotateY = rotateZ = 0;
		else if (key == KeyEvent.VK_1)
			objectNumber = 1;
		else if (key == KeyEvent.VK_2)
			objectNumber = 2;
		else if (key == KeyEvent.VK_3)
			objectNumber = 3;
		else if (key == KeyEvent.VK_4)
			objectNumber = 4;
		else if (key == KeyEvent.VK_5)
			objectNumber = 5;
		else if (key == KeyEvent.VK_6)
			objectNumber = 6;
		else if (key == KeyEvent.VK_SPACE)
			useAnaglyph = !useAnaglyph;
		else
			repaint = false;
		if (repaint)
			repaint();
	}

	public void keyReleased(KeyEvent evt) {
	}

	public void keyTyped(KeyEvent evt) {
	}

}