package edu.mccc.cos210.jp3;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import edu.mccc.cos210.ds.IQueue;
import edu.mccc.cos210.ds.Queue;

public class JPServer {
	private byte[] grid = new byte[JPModel.GRID_ROWS * JPModel.GRID_COLS];
	{
		for (int i = 0; i < grid.length; i++) {
			grid[i] = (byte) -1;
		}
	}
	private Map<Byte, InetSocketAddress> addrs = new HashMap<>();
	private IQueue<DatagramPacket> recvQ = new Queue<>();
	private IQueue<JPMessage> sendQ = new Queue<>();
	private void queueReceivedMsg(DatagramPacket pktFromClient) {
		try {
			synchronized (recvQ) {
				recvQ.enqueue(pktFromClient);
				recvQ.notify();
			}
		} catch (Exception ex) {
			System.err.println("server receiver: " + ex.getMessage());
		}
	}
	public void serve() {
		new Thread(new Notifier()).start();
		new Thread(new Processor()).start();
		try (DatagramSocket rsock = new DatagramSocket(5972)) {
			while (true) {
				byte[] bufFromClient = new byte[4];
				DatagramPacket pktFromClient = new DatagramPacket(bufFromClient, bufFromClient.length);
				rsock.receive(pktFromClient);
				queueReceivedMsg(pktFromClient);
			}
		} catch (Exception ex) {
			System.err.println("server receiver: " + ex.getMessage());
		}
	}
	public void send(JPMessage msg) {
		synchronized (sendQ) {
			sendQ.enqueue(msg);
			sendQ.notify();
		}
	}
	class Processor implements Runnable {
		@Override
		public void run() {
			try {
				while (true) {
					DatagramPacket pktFromClient;
					synchronized (recvQ) {
						while (recvQ.isEmpty()) {
							recvQ.wait();
						}
						pktFromClient = recvQ.dequeue();
					}
					byte[] dataFromClient = pktFromClient.getData();
					if (dataFromClient[0] == (byte) '*') {
						InetSocketAddress sockAddr = (InetSocketAddress) (pktFromClient.getSocketAddress());
						if (dataFromClient[1] < 1 || dataFromClient[1] > 27) {
							continue;
						}
						synchronized (addrs) {
							addrs.put(Byte.valueOf(dataFromClient[1]), sockAddr);
						}
						for (int i = 0; i < grid.length; i++) {
							byte[] g = new byte[] {
								grid[i],
								(byte) (i / JPModel.GRID_COLS),
								(byte) (i % JPModel.GRID_ROWS)
							};
							send(new JPMessage(g));
						}
						continue;
					}
					if (clean(dataFromClient)) {
						grid[dataFromClient[1] * JPModel.GRID_COLS + dataFromClient[2]] = dataFromClient[0];
						send(new JPMessage(dataFromClient));
					}
				}
			} catch (Exception ex) {
				System.err.println("server processor dequeue: " + ex.getMessage());
			}
		}
	}
	private boolean clean(byte[] g) {
		boolean b = true;
		if (g[0] < 1 || g[0] > 27) {
			b = false;
		}
		if (g[1] < 0 || g[1] > JPModel.GRID_ROWS) {
			b = false;
		}
		if (g[2] < 0 || g[2] > JPModel.GRID_COLS) {
			b = false;
		}
		return b;
	}
	class Notifier implements Runnable {
		@Override
		public void run() {
			try (DatagramSocket ssock = new DatagramSocket()) {
				while (true) {
					JPMessage msgToSend;
					synchronized (sendQ) {
						while (sendQ.isEmpty()) {
							sendQ.wait();
						}
						msgToSend = sendQ.dequeue();
					}
					byte[] bufToSend = msgToSend.getMessage();
					synchronized (addrs) {
						for (Byte index : addrs.keySet()) {
							int port = JPDinos.getDinos().get(index).getPort();
							InetSocketAddress addr = addrs.get(index);
							DatagramPacket pktToSend = new DatagramPacket(bufToSend, bufToSend.length, addr.getAddress(), port);
							ssock.send(pktToSend);
						}
					}
				}
			} catch (Exception ex) {
				System.err.println("server sender: " + ex.getMessage());
			}
		}
	}
}
