package edu.mccc.cos210.jp3;

public class JPDino {
	private int index;
	private String name;
	private int dinoPower;
	private int port;
	public JPDino(int index, String name, int dinoPower, int port) {
		this.index = index;
		this.name = name;
		this.dinoPower = dinoPower;
		this.port = port;
	}
	public int getIndex() {
		return index;
	}
	public String getName() {
		return name;
	}
	public int getDinoPower() {
		return dinoPower;
	}
	public int getPort() {
		return port;
	}
}
