package com.benkatz.net.packets;

import com.benkatz.net.GameServer;

public abstract class Packet {
	
	public static enum PacketTypes {
		INVALID(-1), LOGIN(00), DISCONNECT(01), MOVE(02);
		
		private int packetId;
		
		private PacketTypes(int packetId) {
			this.packetId = packetId;
		}
		
		public int getId() {
			return packetId;
		}
	}
	
	public byte packetId;
	
	public Packet(int packetId) {
		this.packetId = (byte) packetId;
	}
	
	public abstract void writeData(GameServer server);
	
	public String readData(byte [] data) {
		String message = new String(data).trim();
		return message.substring(2);
	}
	
	// Actual byte Array that is sent back and forth 
	public abstract byte[] getData();
	
	public static PacketTypes lookupPacket(String packetId) {
		try {
			return lookupPacket(Integer.parseInt(packetId));
		} catch (NumberFormatException e) {
			return PacketTypes.INVALID;
		}
	}
	
	
	// Determine packetType of packet
	public static PacketTypes lookupPacket(int id) {
		for (PacketTypes p : PacketTypes.values()) {
			if (p.getId() == id) {
				return p;
			}
		}
		return PacketTypes.INVALID;
	}
	
}
