package com.benkatz.net.packets;

import com.benkatz.net.GameServer;

public class Packet02Move extends Packet {

	private String username;
	private int x;
	private int y;
	private boolean isMovingLeft = false;
	private boolean isMovingRight = false;
	private boolean isMovingDown = false;
	private boolean isMovingUp = false;
	
	// For retrieving data
	public Packet02Move(byte[] data) {
		super(02);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		this.x = Integer.parseInt(dataArray[1]);
		this.y = Integer.parseInt(dataArray[2]);
		this.isMovingLeft = Integer.parseInt(dataArray[3]) == 1;
		this.isMovingRight = Integer.parseInt(dataArray[4]) == 1;
		this.isMovingUp = Integer.parseInt(dataArray[5]) == 1;
		this.isMovingDown = Integer.parseInt(dataArray[6]) == 1;
	}
	
	// For sending data from client
	public Packet02Move(String username, int x, int y, boolean movingLeft, boolean movingRight,
			boolean movingUp, boolean movingDown) {
		super(02);
		this.username = username;
		this.x = x;
		this.y = y;
		this.isMovingLeft = movingLeft;
		this.isMovingRight = movingRight;
		this.isMovingUp = movingUp;
		this.isMovingDown = movingDown;
	}

	@Override
	public void writeData(GameServer server) {
		server.sendDataToAllClients(getData());
	}

	@Override
	public byte[] getData() {
		return ("02" + this.username +","+this.x+","+this.y+","+(isMovingLeft ? 1 : 0)
				+","+(isMovingRight ? 1 : 0)+","+(isMovingUp ? 1 : 0)+","+(isMovingDown ? 1 : 0)).getBytes();
	}
	
	public String getUsername() {
		return username;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public boolean isMovingLeft() {
		return isMovingLeft;
	}

	public boolean isMovingRight() {
		return isMovingRight;
	}

	public boolean isMovingDown() {
		return isMovingDown;
	}

	public boolean isMovingUp() {
		return isMovingUp;
	}

}