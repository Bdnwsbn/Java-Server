package com.benkatz.models;

import java.net.InetAddress;

public class PlayerMP {
	
	public String username;
	public InetAddress ipAddress;
	public int port;
	public int x;
	public int y;
	private boolean movingLeft = false;
	private boolean movingRight = false;
	private boolean movingDown = false;
	private boolean movingUp = false;
	
	public PlayerMP(int x, int y, String username, InetAddress ipAddress, int port) {
		super();
		this.x = x;
		this.y = y;
		this.username = username;
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	public InetAddress getInetAddress () {
		return this.ipAddress;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}

	public void setMovingLeft(boolean movingLeft) {
		// TODO Auto-generated method stub
		
	}

	public boolean isMovingRight() {
		return movingRight;
	}

	public void setMovingRight(boolean movingRight) {
		this.movingRight = movingRight;
	}

	public boolean isMovingDown() {
		return movingDown;
	}

	public void setMovingDown(boolean movingDown) {
		this.movingDown = movingDown;
	}

	public boolean isMovingUp() {
		return movingUp;
	}

	public void setMovingUp(boolean movingUp) {
		this.movingUp = movingUp;
	}

	public boolean isMovingLeft() {
		return movingLeft;
	}
	
}
