package edu.mccc.cos210.jp3;

public class JPMessage {
	private byte[] msg;
	public JPMessage(byte[] msg) {
		this.msg = msg;
	}
	public byte[] getMessage() {
		return msg;
	}
}
