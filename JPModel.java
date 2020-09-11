package edu.mccc.cos210.jp3;

import java.util.Arrays;

public class JPModel {
	public static final int GRID_ROWS = 5;
	public static final int GRID_COLS = 5;
	private byte[] grid = new byte[GRID_ROWS * GRID_COLS];
	private int index;
	private String name;
	private int port = 9999;
	public JPModel(String name) {
		this.name = name;
		for (int i = 0; i < JPDinos.getDinos().getSize(); i++) {
			if (name.equals(JPDinos.getDinos().get(i).getName())) {
				this.setIndex(JPDinos.getDinos().get(i).getIndex());
				this.port = JPDinos.getDinos().get(i).getPort();
			}
		}
		for (int i = 0; i < grid.length; i++) {
			grid[i] = -1;
		}
	}
	public byte[] getGrid() {
		return grid;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getName() {
		return name;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public void update(byte[] update) {
		System.out.println(Arrays.toString(update));
		synchronized (grid) {
			grid[update[1] * GRID_COLS + update[2]] = update[0];
		}
	}
}
