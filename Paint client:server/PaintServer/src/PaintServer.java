import java.awt.Color;
import java.io.IOException;
import java.util.*;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class PaintServer extends Listener{
	static Server server;
	static final int port = 27960;
	static Map<Integer, Player> players = new HashMap<Integer, Player>();
	
	public static void main(String[] args) throws IOException{
		server = new Server();
		server.getKryo().register(UpdateXY.class);
		server.getKryo().register(UpdateThickness.class);
		server.getKryo().register(UpdateColor.class);
		server.getKryo().register(PlayerJoin.class);
		server.getKryo().register(PlayerLeave.class);
		server.bind(port, port);
		server.start();
		server.addListener(new PaintServer());
		System.out.println("The server is ready");
	}
	
	public void connected(Connection c){
		Player player = new Player();
		player.x = 0;
		player.y = 0;
		player.thickness = 2;
		player.color = Color.BLACK;
		player.isdrawing = false;
		player.c = c;
		
		PlayerJoin packet = new PlayerJoin();
		packet.id = c.getID();
		server.sendToAllExceptTCP(c.getID(), packet);
		
		for(Player p : players.values()){
			PlayerJoin packet2 = new PlayerJoin();
			packet2.id = p.c.getID();
			c.sendTCP(packet2);
		}
		
		players.put(c.getID(), player);
		System.out.println("Connection Received");
	}
	
	public void received(Connection c, Object o){
		if(o instanceof UpdateXY){
			UpdateXY packet = (UpdateXY) o;
			players.get(c.getID()).x = packet.x;
			players.get(c.getID()).y = packet.y;
			players.get(c.getID()).isdrawing = packet.isdrawing;
			
			packet.id = c.getID();
			server.sendToAllExceptTCP(c.getID(), packet);
			System.out.println("received and sent update of XY coordinates");
			
		}else if(o instanceof UpdateColor){
			UpdateColor packet = (UpdateColor) o;
			players.get(c.getID()).color = packet.color;
			
			packet.id = c.getID();
			server.sendToAllExceptTCP(c.getID(), packet);
			System.out.println("received and sent update of color");
		}else if(o instanceof UpdateThickness){
			UpdateThickness packet = (UpdateThickness) o;
			players.get(c.getID()).thickness = packet.thickness;
			
			packet.id = c.getID();
			server.sendToAllExceptTCP(c.getID(), packet);
			System.out.println("received and sent update of thickness");
		}
	}
	
	public void disconnected(Connection c){
		players.remove(c.getID());
		PlayerLeave packet = new PlayerLeave();
		packet.id = c.getID();
		server.sendToAllExceptTCP(c.getID(), packet);
		System.out.println("Connection dropped");
	}
	
}
