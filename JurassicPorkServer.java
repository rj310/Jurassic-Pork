package edu.mccc.cos210.jp3;

public class JurassicPorkServer {
	public static void main(String... args) {
		new JurassicPorkServer().doIt();
	}
	private void doIt() {
		new JPServer().serve();
	}
}
