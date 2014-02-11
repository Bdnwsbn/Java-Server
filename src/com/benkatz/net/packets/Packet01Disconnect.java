package com.benkatz.net.packets;

import com.benkatz.net.GameServer;

public class Packet01Disconnect extends Packet {

	private String username;
	
	// For retrieving data
	public Packet01Disconnect(byte[] data) {
		super(01);
		this.username = readData(data);
	}
	
	// For sending data from client
	public Packet01Disconnect(String username) {
		super(01);
		this.username = username;
	}

	@Override
	public void writeData(GameServer server) {
		server.sendDataToAllClients(getData());
	}

	@Override
	public byte[] getData() {
		return ("01" + this.username).getBytes();
	}
	
	public String getUsername() {
		return username;
	}
	
	
}
