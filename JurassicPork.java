package edu.mccc.cos210.jp3;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class JurassicPork {
	private static final String DINO_NAME = "SmokeyPitMaster";
	//private static final String SERVER_ADDRESS = "127.0.0.1";
	//private static final String SERVER_ADDRESS = "10.0.0.3";
	private static final String SERVER_ADDRESS = "172.16.32.239";
	public JurassicPork() {
		JPModel jpModel = new JPModel(DINO_NAME);
		JPClient jpClient = new JPClient(jpModel, SERVER_ADDRESS);
		initSwing(jpModel, jpClient);
		new Thread(jpClient).start();
	}
	public static void main(String... args) {
		EventQueue.invokeLater(JurassicPork::new);
	}
	private void initSwing(JPModel model, JPClient client) {
		JFrame jf = new JFrame("Jurassic Pork");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JProgressBar jpb = new JProgressBar(0, 100);
		jpb.setValue(100);
		jf.add(jpb, BorderLayout.NORTH);
		JPanel jp = new JPPanel(model, client, jpb);
		jf.add(jp, BorderLayout.CENTER);
		jf.pack();
		jf.setLocationRelativeTo(null);
		jf.setResizable(false);
		jf.setVisible(true);
	}
}
