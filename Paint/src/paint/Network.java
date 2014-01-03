package paint;

import java.io.IOException;
import java.awt.Color;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;

public class Network extends Listener{
	Client client;
	int port = 27960;
	
	public void connect(){
		this.client = new Client();
		Kryo kryo = this.client.getKryo();
		
		kryo.register(UpdateXY.class);
		kryo.register(ChatMessage.class);
		kryo.register(PlayerJoin.class);
		kryo.register(PlayerLeave.class);
		kryo.register(int.class);
		kryo.register(boolean.class);
		kryo.register(String.class);
					
		new Thread(this.client).start();
		try{
			this.client.connect(5000, PaintWindow.host, this.port, this.port);
		}catch(IOException e){
			e.printStackTrace();
		}
		this.client.addListener(this);
		
		com.esotericsoftware.minlog.Log.set(com.esotericsoftware.minlog.Log.LEVEL_TRACE);
	}
	
	public void received(Connection c, Object o){
		if(o instanceof FrameworkMessage){
			return;	
		}else if(o instanceof PlayerJoin){
				PlayerJoin packet = (PlayerJoin) o;
				NewPlayer newPlayer = new NewPlayer();
				Paint.players.put(packet.id, newPlayer);
				System.out.println("registered"+packet.id+" / "+Paint.players.get(packet.id).id);
				return;
		}else if(o instanceof PlayerLeave){
				PlayerLeave packet = (PlayerLeave) o;
				System.out.println("received leave from"+packet.id);
				Paint.players.remove(packet.id);
				return;
		}else if(o instanceof UpdateXY){
			UpdateXY packet = (UpdateXY) o;
			NewPlayer active = Paint.players.get(packet.id);			
			active.x = packet.x;
			active.y = packet.y;
			active.isdrawing = packet.isdrawing;
			active.thickness = packet.thickness;
			active.name = packet.name;
			
			switch(packet.color){
			case 1: active.color = Color.YELLOW;
					break;
			case 2: active.color = Color.ORANGE;
					break;
			case 3: active.color = Color.RED;
					break;
			case 4: active.color = Color.MAGENTA;
					break;
			case 5: active.color = Color.CYAN;
					break;
			case 6: active.color = Color.BLUE;
					break;
			case 7:	active.color = Color.GREEN;
					break;				
			case 8: active.color = Color.GRAY;
					break;				
			case 9: active.color = Color.BLACK;
					break;
			case 0: active.color = Color.WHITE;
					break;
			default: break;
			}
			
			PadDraw.update();
			return;
		}else if(o instanceof ChatMessage){
			ChatMessage packet = (ChatMessage) o;
			PaintWindow.chatPanel.addMessage(packet.name + " : " + packet.message);
			return;
		}
	}
	
}
