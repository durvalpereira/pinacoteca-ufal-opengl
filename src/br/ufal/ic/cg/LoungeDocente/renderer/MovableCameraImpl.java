package br.ufal.ic.cg.LoungeDocente.renderer;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.DebugGL2;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import br.ufal.ic.cg.LoungeDocente.objClasses.Extintor;
import br.ufal.ic.cg.LoungeDocente.objClasses.Janelas;
import br.ufal.ic.cg.LoungeDocente.objClasses.Cadeiras;
import br.ufal.ic.cg.LoungeDocente.objClasses.Luminaria;
import br.ufal.ic.cg.LoungeDocente.objClasses.Mesa;
import br.ufal.ic.cg.LoungeDocente.objClasses.ParedesExternas;
import br.ufal.ic.cg.LoungeDocente.objClasses.ParedesInternas;
import br.ufal.ic.cg.LoungeDocente.objClasses.Pilastras;
import br.ufal.ic.cg.LoungeDocente.objClasses.ParedesPilastrasFrente;
import br.ufal.ic.cg.LoungeDocente.objClasses.PisoExterno;
import br.ufal.ic.cg.LoungeDocente.objClasses.Placa;
import br.ufal.ic.cg.LoungeDocente.objClasses.PortaExterna;
import br.ufal.ic.cg.LoungeDocente.objClasses.TV;
import br.ufal.ic.cg.LoungeDocente.objClasses.Sofa;
import br.ufal.ic.cg.LoungeDocente.objClasses.TextoFachada;
import br.ufal.ic.cg.LoungeDocente.objClasses.PisoInterno;
import br.ufal.ic.cg.LoungeDocente.objects.AutoDrawnableObject;

/**
 * @author Durval Pereira
 * @author Lu�s Gustavo
 * 
 * @version 1.0
 */

public class MovableCameraImpl extends MovableCamera {

	private PortaExterna porta;
	
	private float[] ambientLight = { 1f, 1f, 1f, 1f };
	private float[] diffuseLight = { .2f, .2f, .2f, 1f };
	private float[] lightColorSpecular = { 0.8f, 0.8f, 0.8f, 1.0f };
	private float[] lightPos = { 10f, -10f, 8f, 1f };

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<AutoDrawnableObject> objects;
	
	

	/**
	 * 
	 */
	public MovableCameraImpl() {

		this.objects = new ArrayList<AutoDrawnableObject>();
	}

	/**
	 * 
	 * @param gl
	 */
	protected void initObjects(GL2 gl) {
		
		this.objects.add(porta = new PortaExterna(gl));
		this.objects.add(new ParedesPilastrasFrente(gl));
		this.objects.add(new PisoExterno(gl));
		this.objects.add(new ParedesExternas(gl));
		this.objects.add(new ParedesInternas(gl));
		this.objects.add(new PisoInterno(gl));
		this.objects.add(new Luminaria(gl));
		this.objects.add(new Sofa(gl));
		this.objects.add(new TextoFachada(gl));
		this.objects.add(new TV(gl));
		this.objects.add(new Janelas(gl));
		this.objects.add(new Extintor(gl));
		this.objects.add(new Placa(gl));

	}

	/**
	 * 
	 */
	@Override
	public void display(GLAutoDrawable drawable) {

		GL2 gl = drawable.getGL().getGL2();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		setCameraPos();

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPos, 0);

		gl.glEnable(GL2.GL_TEXTURE_2D);

		for (AutoDrawnableObject obj : this.objects) {
			// gl.glDisable(GL.GL_BLEND);
			gl.glPushMatrix();
			obj.bindTexture();
			obj.selfDraw(gl);
			obj.unbindTexture();
			gl.glPopMatrix();
		}

		gl.glTranslatef(lightPos[0], lightPos[1], lightPos[2]);
		gl.glColor3f(1f, 1f, 1f);

		gl.glFlush();
	}

	/**
	 * 
	 */
	@Override
	public void init(GLAutoDrawable drawable) {

		GL2 gl = drawable.getGL().getGL2();

		drawable.setGL(new DebugGL2(drawable.getGL().getGL2()));

		this.initObjects(gl);
		this.initLight(gl);

		gl.setSwapInterval(1);
		gl.glEnable(GL.GL_DEPTH_TEST);

		gl.glClearColor(135 / 255f, 206 / 255f, 235 / 255f, 0.0f);

	}

	/**
	 * 
	 */
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {

		GL2 gl = drawable.getGL().getGL2();
		GLU glu = new GLU();

		if (height <= 0) {
			height = 1;
		}
		final float h = (float) width / (float) height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(45.0f, h, 1.0, 200.0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub

	}

	private void initLight(GL2 gl) {
		gl.glEnable(GL2.GL_LIGHTING);

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightColorSpecular, 0);

		gl.glEnable(GL2.GL_LIGHT0);

		float[] rgba = { 1f, 1f, 1f };
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, rgba, 0);

		gl.glEnable(GL2.GL_COLOR_MATERIAL);

	}

	public void processKeyPressed(final char c) {
		new Thread(new Runnable() {			

			@Override
			public void run() {
				
				switch (c) {
				case 'o':
					porta.openDoor();
					break;
				case 'c':
					porta.closeDoor();
					break;

				}

			}
		}).start();
	}
}