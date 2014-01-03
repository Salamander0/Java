package paintserver;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class PaintServer extends Listener{
	static Server server;
	static final int port = 27960;
	static Map<Integer, Player> players = new HashMap<Integer, Player>();
	
	public static void main(String[] args) throws IOException{
		server = new Server();
		Kryo kryo = server.getKryo();
		
		kryo.register(UpdateXY.class);
		kryo.register(ChatMessage.class);
		kryo.register(PlayerJoin.class);
		kryo.register(PlayerLeave.class);
		kryo.register(int.class);
		kryo.register(boolean.class);
		kryo.register(String.class);
		
		new Thread(server).start();
		try{
			server.bind(port, port);
		}catch(IOException e){
			e.printStackTrace();
		}
		
		server.addListener(new PaintServer());
		System.out.println("The server is ready");
		com.esotericsoftware.minlog.Log.set(com.esotericsoftware.minlog.Log.LEVEL_TRACE);
		
		JFrame frame = new JFrame("Chat Server");
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosed (WindowEvent evt) {
				server.stop();
			}
		});
		frame.getContentPane().add(new JLabel("Close to stop the chat server."));
		frame.setSize(320, 200);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void connected(Connection c){
		Player player = new Player();
		
		player.x = 0;
		player.y = 0;
		player.thickness = 1;
		player.color = 9;
		player.isdrawing = false;
		player.c = c;
		player.name = "newplayer";
		
		PlayerJoin packet = new PlayerJoin();
		packet.id = c.getID();
		server.sendToAllExceptTCP(c.getID(), packet);
		
		for(Player p : players.values()){
			PlayerJoin packet2 = new PlayerJoin();
			packet2.id = p.c.getID();
			c.sendTCP(packet2);
		}
		
		players.put(c.getID(), player);
		System.out.println("Connection Received-designated: "+c.getID());
	}
	
	public void received(Connection c, Object o){
		if(o instanceof UpdateXY){
			UpdateXY packet = (UpdateXY) o;
			players.get(c.getID()).x = packet.x;
			players.get(c.getID()).y = packet.y;
			players.get(c.getID()).isdrawing = packet.isdrawing;
			players.get(c.getID()).color = packet.color;
			players.get(c.getID()).thickness = packet.thickness;
			
			packet.id = c.getID();
			System.out.println("from "+packet.id+" data: "+packet.x+" "+packet.y+" "+packet.color+" "+packet.thickness);
			server.sendToAllExceptTCP(c.getID(), packet);
			
			return;
		}
		if(o instanceof ChatMessage){
			ChatMessage packet = (ChatMessage) o;
			packet.id = c.getID();
			System.out.println("from "+packet.id+" message:"+ packet.message);
			server.sendToAllExceptTCP(c.getID(), packet);
			return;
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
