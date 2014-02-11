package com.benkatz.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.benkatz.models.PlayerMP;
import com.benkatz.net.packets.Packet;
import com.benkatz.net.packets.Packet.PacketTypes;
import com.benkatz.net.packets.Packet00Login;
import com.benkatz.net.packets.Packet01Disconnect;
import com.benkatz.net.packets.Packet02Move;

public class GameServer extends Thread {
		
	private DatagramSocket socket;
	private List<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();
	
	
	public GameServer() {
		try {
			this.socket = new DatagramSocket(5000);    // Listens to Port 5000
			
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);	

			try {
				socket.receive(packet);
				//System.out.println("packet received");
				
				//ByteArrayInputStream bais = new ByteArrayInputStream(data);
				//DataInputStream dais = new DataInputStream(bais);
				
				//System.out.println(dais.readInt());
				//System.out.println(dais.readInt());
				
				//bais.close();
				//dais.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}

			parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
			
		}
	}
	
	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
		Packet packet = null;
		switch(type) {
			default:
			case INVALID:
				break;
				
			case LOGIN:
				packet = new Packet00Login(data);
				System.out.println("["+address.getHostAddress()+":"+port+"] " + ((Packet00Login)packet).getUsername()+ " has connected...");

				// Create and add new player to List
				PlayerMP player = new PlayerMP(368, 208, ((Packet00Login) packet).getUsername(), address, port);
				this.addConnection(player, (Packet00Login) packet);
				break;
				
			case DISCONNECT:
				packet = new Packet01Disconnect(data);
				System.out.println("["+address.getHostAddress()+":"+port+"] " + ((Packet01Disconnect)packet).getUsername()+ " has disconnected...");

				// Remove player from player to List
				this.removeConnection((Packet01Disconnect) packet);
				break;
				
			case MOVE:
				packet = new Packet02Move(data);
				//System.out.println(((Packet02Move) packet).getUsername() +"has moved to" +((Packet02Move) packet).getY());
				this.handleMove(((Packet02Move) packet));
		}
	}

	private void addConnection(PlayerMP player, Packet00Login packet) {
		boolean alreadyConnected = false;
		//System.out.println("player IP: " + player.ipAddress.toString() + ", Port: " + player.port);
		for (PlayerMP p : this.connectedPlayers) {
			if (player.getUsername().equalsIgnoreCase(p.getUsername())) {
				if (p.ipAddress == null) {
					p.ipAddress = player.ipAddress;
				} 
				if (p.port == -1){
					p.port = player.port;
				}
				
				alreadyConnected = true;
			} else {
				// relay to current connected player there is a new player
				sendData(packet.getData(), p.ipAddress, p.port);
				
				
				// relay to new player that current connected player exists
				packet = new Packet00Login(p.getUsername(), p.x, p.y);
				sendData(packet.getData(), player.ipAddress, player.port);
			}
			//System.out.println("p = "+ p.username+ " IP: " + p.ipAddress.toString() + ", Port: " + p.port);
		}	
		if (!alreadyConnected) {
			System.out.println("Added " + player.getUsername() + " to list at " +player.ipAddress +":"+player.port);
			this.connectedPlayers.add(player);
		}
		System.out.println("# players connected: " + connectedPlayers.size());
	}

	public void removeConnection(Packet01Disconnect packet) {
		this.connectedPlayers.remove(getPlayerMPIndex(packet.getUsername()));
		packet.writeData(this);
	}
	
	public PlayerMP getPlayerMP(String username) {
		for (PlayerMP player : this.connectedPlayers) {
			if (player.getUsername().equals(username)) {
				return player;
			}
		}
		return null;
	}
	
	public int getPlayerMPIndex(String username) {
		int index = 0;
		for (PlayerMP player : this.connectedPlayers) {
			if (player.getUsername().equals(username)) {
				break;
			}
			index++;
		}
		return index;
	}

	public void sendData(byte[] data, InetAddress ipAddress, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, port);
		try {
			this.socket.send(packet);
			
			//System.out.println("Sent to " + ipAddress + ": " + new String(data));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendDataToAllClients(byte[] data) {
		//System.out.println("Sending to all clients");
		for (PlayerMP p : connectedPlayers) {
			sendData(data, p.ipAddress, p.port);
		}
	}
	
	private void handleMove(Packet02Move packet) {
		if (getPlayerMP(packet.getUsername()) != null) {
			int index = getPlayerMPIndex(packet.getUsername());
			PlayerMP player = this.connectedPlayers.get(index);
			player.x = packet.getX();
			player.y = packet.getY();
			player.setMovingLeft(packet.isMovingLeft());
			player.setMovingRight(packet.isMovingRight());
			player.setMovingUp(packet.isMovingUp());
			player.setMovingDown(packet.isMovingDown());
			packet.writeData(this);
		}
		
	}
}


