package edu.mccc.cos210.jp3;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.event.MouseInputAdapter;

public class JPPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Stroke stroke = new BasicStroke(3.0f);
	private Path2D overLay = new Path2D.Double();
	{
		for (int i = 1; i < 5; i++) {
			overLay.moveTo(i * 128.0, 0.0);
			overLay.lineTo(i * 128.0, 128.0 * JPModel.GRID_ROWS);
		}
		for (int i = 1; i < 5; i++) {
			overLay.moveTo(0.0, i * 128.0);
			overLay.lineTo(128.0 * JPModel.GRID_ROWS, i * 128.0);
		}
	}
	private BufferedImage bi;
	JPModel jpModel;
	JProgressBar progressBar;
	boolean clickEnabled = true;
	public JPPanel(JPModel model, JPClient client, JProgressBar jpb) {
		this.jpModel = model;
		this.progressBar = jpb;
		setBackground(new Color(196, 255, 196));
		setPreferredSize(new Dimension(JPModel.GRID_COLS * 128, JPModel.GRID_ROWS * 128));
		try {
			bi = ImageIO.read(new File("./images/dinos/Dinos.png"));
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(-1);
		}
		MouseHandler mh = new MouseHandler(client);
		addMouseListener(mh);
		addMouseMotionListener(mh);
		javax.swing.Timer t = new javax.swing.Timer(
			100,
			ae -> repaint()
		);
		t.start();
	}
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		synchronized (jpModel.getGrid()) {
			byte[] grid = jpModel.getGrid();
			for (int r = 0, gIndex = 0; r < JPModel.GRID_ROWS; r++) {
				for (int c = 0; c < JPModel.GRID_COLS; c++, gIndex++) {
					int imageIndex = grid[gIndex];
					if (imageIndex != -1)  {
						g2d.drawImage(
							bi,
							c * 128,
							r * 128,
							c * 128 + 128,
							r * 128 + 128,
							imageIndex * 128,
							0,
							imageIndex * 128 + 128,
							128,
							this
						);
					}
				}
			}
		}
		g2d.setPaint(new Color(0, 96, 0));
		g2d.setStroke(stroke);
		g2d.draw(overLay);
		g2d.dispose();
	}
	class MouseHandler extends MouseInputAdapter {
		private JPClient jpClient;
		javax.swing.Timer t = new javax.swing.Timer(
			750 / 10,
			ae -> {
				if (!clickEnabled) {
					progressBar.setValue(progressBar.getValue() + 10);
					if (progressBar.getValue() >= 100) {
						progressBar.setValue(100);
						clickEnabled = true;
					}
				}
			}
		);
		public MouseHandler(JPClient client) {
			this.jpClient = client;
		}
		@Override
		public void mousePressed(MouseEvent me) {
			if (clickEnabled) {
				clickEnabled = false;
				jpClient.send(
					new JPMessage(
						new byte[] {
							(byte) jpClient.jpModel.getIndex(),
							(byte) (me.getY() / 128),
							(byte) (me.getX() / 128)
						}
					)
				);
				progressBar.setValue(0);
				t.restart();
			}
		}
	}
}
