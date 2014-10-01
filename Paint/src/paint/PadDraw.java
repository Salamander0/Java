package paint;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JComponent;
import javax.swing.Timer;

@SuppressWarnings("serial")
class PadDraw extends JComponent{
	NewPlayer ThisPlayer = new NewPlayer();
	Image image;
	Graphics2D graphics2D;
	static Graphics2D network2D;
	int delay = 200;
	
	ActionListener taskPerformer = new ActionListener() {
		  public void actionPerformed(ActionEvent e) {
		    repaint();
		  }
		};
	
	public PadDraw(){
		setDoubleBuffered(false);
		setSize(800, 540);
		
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				PadDraw.this.ThisPlayer.oldx = e.getX();
				PadDraw.this.ThisPlayer.oldy = e.getY();
				//PaintWindow.chatPanel.addMessage("You are drawing");
				
				UpdateXY packet = new UpdateXY();
				
				packet.name = PaintWindow.name;
				packet.x = PadDraw.this.ThisPlayer.oldx;
				packet.y = PadDraw.this.ThisPlayer.oldy;
				packet.isdrawing = true;
				packet.color = PadDraw.this.ThisPlayer.col;
				packet.thickness = PadDraw.this.ThisPlayer.thickness;

				Paint.network.client.sendUDP(packet);
				/*
				ChatMessage packet2 = new ChatMessage();
				packet2.name = PaintWindow.name;
				packet2.message = ("is drawing");
				Paint.network.client.sendUDP(packet2);
				*/
			}
		});

		addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseDragged(MouseEvent e){
				PadDraw.this.ThisPlayer.x = e.getX();
				PadDraw.this.ThisPlayer.y = e.getY();
				
				UpdateXY packet = new UpdateXY();
				
				packet.name = PaintWindow.name;
				packet.x = PadDraw.this.ThisPlayer.x;
				packet.y = PadDraw.this.ThisPlayer.y;
				packet.color = PadDraw.this.ThisPlayer.col;
				packet.thickness = PadDraw.this.ThisPlayer.thickness;
				packet.isdrawing = false;
				
				Paint.network.client.sendUDP(packet);
				
				PadDraw.this.graphics2D.drawLine(PadDraw.this.ThisPlayer.oldx, PadDraw.this.ThisPlayer.oldy, PadDraw.this.ThisPlayer.x, PadDraw.this.ThisPlayer.y);
				repaint();
				PadDraw.this.ThisPlayer.oldx = PadDraw.this.ThisPlayer.x;
				PadDraw.this.ThisPlayer.oldy = PadDraw.this.ThisPlayer.y;
			}
		});
		
		new Timer(this.delay, this.taskPerformer).start();
		
	}
	
	static public void update(){
		for(NewPlayer p : Paint.players.values()){
			if(p.isdrawing == true){
				p.oldx = p.x;
				p.oldy = p.y; 
				p.isdrawing = false;
			}else if((p.oldx !=p.x)||(p.oldy !=p.y)){
				network2D.setStroke(new BasicStroke(p.thickness));
				network2D.setColor(p.color);
				network2D.drawLine(p.oldx, p.oldy, p.x, p.y);
				p.oldx = p.x;
				p.oldy = p.y;
			}
		}
	}

	public void paintComponent(Graphics g){
		if(this.image == null){
			this.image = createImage(getSize().width, getSize().height);
			this.graphics2D = (Graphics2D)this.image.getGraphics();
			network2D = (Graphics2D)this.image.getGraphics();
			this.graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			network2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			clear();
		}
		g.drawImage(this.image, 0, 0, null);
	}

	public void clear(){
		this.graphics2D.setPaint(Color.white);
		this.graphics2D.fillRect(0, 0, getSize().width, getSize().height);
		this.graphics2D.setPaint(Color.black);
		repaint();
	}

	public void changeColor(Color theColor, int col){
		PadDraw.this.ThisPlayer.color = theColor;
		PadDraw.this.ThisPlayer.col = col;
		
		UpdateXY packet = new UpdateXY();
		packet.name = PaintWindow.name;
		packet.color = col;
		packet.thickness = PadDraw.this.ThisPlayer.thickness;
		packet.isdrawing = true;
		packet.x = PadDraw.this.ThisPlayer.x;
		packet.y = PadDraw.this.ThisPlayer.y;
		Paint.network.client.sendUDP(packet);
		
		
		this.graphics2D.setPaint(theColor);
	}
	
	public void changeThickness(int thickness){
		PadDraw.this.ThisPlayer.thickness = thickness;
		
		UpdateXY packet = new UpdateXY();
		packet.name = PaintWindow.name;
		packet.thickness = thickness;
		packet.isdrawing = true;
		packet.x = PadDraw.this.ThisPlayer.x;
		packet.y = PadDraw.this.ThisPlayer.y;
		packet.color = PadDraw.this.ThisPlayer.col;
		Paint.network.client.sendUDP(packet);
		
		this.graphics2D.setStroke(new BasicStroke(thickness));
	}
	

}