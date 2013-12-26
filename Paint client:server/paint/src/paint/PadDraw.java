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
	int delay = 1000;
	
	ActionListener taskPerformer = new ActionListener() {
		  public void actionPerformed(ActionEvent evt) {
		    repaint();
		  }
		};
	
	public PadDraw(){
		setDoubleBuffered(false);
				
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				ThisPlayer.oldx = e.getX();
				ThisPlayer.oldy = e.getY();
				
				UpdateXY packet = new UpdateXY();
								
				packet.x = ThisPlayer.oldx;
				packet.y = ThisPlayer.oldy;
				packet.isdrawing = true;

				Paint.network.client.sendUDP(packet);
			}
		});

		addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseDragged(MouseEvent e){
				ThisPlayer.x = e.getX();
				ThisPlayer.y = e.getY();
				
				UpdateXY packet = new UpdateXY();	
				packet.x = ThisPlayer.x;
				packet.y = ThisPlayer.y;
				Paint.network.client.sendUDP(packet);
				
				graphics2D.setStroke(new BasicStroke(ThisPlayer.thickness));
				graphics2D.drawLine(ThisPlayer.oldx, ThisPlayer.oldy, ThisPlayer.x, ThisPlayer.y);
				repaint();
				ThisPlayer.oldx = ThisPlayer.x;
				ThisPlayer.oldy = ThisPlayer.y;
			}
		});
		
		new Timer(delay, taskPerformer).start();
		
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
		if(image == null){
			image = createImage(getSize().width, getSize().height);
			graphics2D = (Graphics2D)image.getGraphics();
			network2D = (Graphics2D)image.getGraphics();
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			network2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			clear();
		}
		g.drawImage(image, 0, 0, null);
	}

	public void clear(){
		graphics2D.setPaint(Color.white);
		graphics2D.fillRect(0, 0, getSize().width, getSize().height);
		graphics2D.setPaint(Color.black);
		repaint();
	}

	public void changeColor(Color theColor){
		graphics2D.setPaint(theColor);
		repaint();
	}
	
	

}