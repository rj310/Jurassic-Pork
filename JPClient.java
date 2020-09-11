package edu.mccc.cos210.jp3;

import java.awt.EventQueue;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import edu.mccc.cos210.ds.IQueue;
import edu.mccc.cos210.ds.Queue;

public class JPClient implements Runnable {
	JPModel jpModel;
	private String jpServer;
	private IQueue<JPMessage> sendQ = new Queue<>();
	public JPClient(JPModel model, String server) {
		this.jpModel = model;
		this.jpServer = server;
		JPMessage login = new JPMessage(
			new byte[] {
				(byte) '*',
				(byte) jpModel.getIndex()
			}
		);
		send(login);
	}
	public void send(JPMessage msg) {
		synchronized (sendQ) {
			sendQ.enqueue(msg);
			sendQ.notify();
		}
	}
	@Override
	public void run() {
		try (DatagramSocket ssock = new DatagramSocket();
				DatagramSocket rsock = new DatagramSocket(jpModel.getPort())) {
			new Thread(new JPReceiver(rsock)).start();
			InetAddress saddr = InetAddress.getByName(jpServer);
			while (true) {
				JPMessage msgToSend;
				synchronized (sendQ) {
					while (sendQ.isEmpty()) {
						sendQ.wait();
					}
					msgToSend = sendQ.dequeue();
				}
				byte[] bufToSend = msgToSend.getMessage();
				DatagramPacket pktToSend = new DatagramPacket(bufToSend, bufToSend.length, saddr, 5972);
				ssock.send(pktToSend);
			}
		} catch (Exception ex) {
			System.err.println("client sender: " + ex.getMessage());
		}
	}
	class JPReceiver implements Runnable {
		DatagramSocket rsock;
		public JPReceiver(DatagramSocket rsock) {
			this.rsock = rsock;
		}
		@Override
		public void run() {
			try {
				while (true) {
					byte[] bufFromServer = new byte[4];
					DatagramPacket pktFromServer = new DatagramPacket(bufFromServer, bufFromServer.length);
					rsock.receive(pktFromServer);
					if (bufFromServer[0] != (byte) '*') {
						EventQueue.invokeAndWait(
							() -> jpModel.update(bufFromServer)
						);
					}
				}
			} catch (Exception ex) {
				System.err.println("client receiver: " + ex.getMessage());
			}
		}
	}
}
