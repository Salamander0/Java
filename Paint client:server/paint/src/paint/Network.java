package paint;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class Network extends Listener{
	Client client;
	String ip = "192.168.0.6";
	int port = 27960;
	
	public void connect(){
		client = new Client();
		client.getKryo().register(UpdateXY.class);
		client.getKryo().register(UpdateColor.class);
		client.getKryo().register(UpdateThickness.class);
		client.getKryo().register(PlayerJoin.class);
		client.getKryo().register(PlayerLeave.class);
		client.addListener(this);
		
		client.start();
		try{
			client.connect(5000, ip, port, port);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void received(Connection c, Object o){
		if(o instanceof PlayerJoin){
			PlayerJoin packet = (PlayerJoin) o;
			NewPlayer newPlayer = new NewPlayer();
			Paint.players.put(packet.id, newPlayer);
		}else if(o instanceof PlayerLeave){
			PlayerLeave packet = (PlayerLeave) o;
			Paint.players.remove(packet.id);			
		}else if(o instanceof UpdateXY){
			UpdateXY packet = (UpdateXY) o;
			Paint.players.get(packet.id).x = packet.x;
			Paint.players.get(packet.id).y = packet.y;
			Paint.players.get(packet.id).isdrawing = packet.isdrawing;
			PadDraw.update();
		}else if(o instanceof UpdateColor){
			UpdateColor packet = (UpdateColor) o;
			Paint.players.get(packet.id).color = packet.color;
		}else if(o instanceof UpdateThickness){
			UpdateThickness packet = (UpdateThickness) o;
			Paint.players.get(packet.id).thickness = packet.thickness;
		}
		
	}
	
}
