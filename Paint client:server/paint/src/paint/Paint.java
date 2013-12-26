package paint;

import java.awt.*;

import javax.swing.*;

import java.util.*;
import java.awt.event.*;

import com.esotericsoftware.kryonet.Listener;

public class Paint extends Listener{
	static Network network = new Network();
	static Map<Integer,NewPlayer> players = new HashMap<Integer,NewPlayer>(); 
	
	public static void main(String[] args)throws Exception{
		PaintWindow frame = new PaintWindow();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		
		network.connect();
	}
}

@SuppressWarnings("serial")
class PaintWindow extends JFrame{ 
	public PaintWindow(){
		setTitle("Net Paint");
		setSize(800, 600);

		panel = new JPanel();
		drawPad = new PadDraw();

		panel.setPreferredSize(new Dimension(80, 40));

		Container content = this.getContentPane();
		content.setLayout(new BorderLayout());

		content.add(panel, BorderLayout.SOUTH);
		content.add(drawPad, BorderLayout.CENTER);
		
		makeColorButton(Color.YELLOW);
		makeColorButton(Color.ORANGE);
		makeColorButton(Color.RED);
		makeColorButton(Color.MAGENTA);
		makeColorButton(Color.CYAN);
		makeColorButton(Color.BLUE);
		makeColorButton(Color.GREEN);
		makeColorButton(Color.GRAY);
		makeColorButton(Color.BLACK);
		
		
		JButton eraseButton = new JButton("Erase");
		eraseButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				drawPad.changeColor(Color.WHITE);
				drawPad.ThisPlayer.thickness = 15;
				
				UpdateColor packet = new UpdateColor();
				packet.color = Color.WHITE;
				UpdateThickness packet2 = new UpdateThickness();
				packet2.thickness = 15;
				
				Paint.network.client.sendUDP(packet);
				Paint.network.client.sendUDP(packet2);
				}
		});
		panel.add(eraseButton);
		
	}

	public void makeColorButton(final Color color){
		JButton tempButton = new JButton();
		tempButton.setPreferredSize(new Dimension(20, 20));
		tempButton.setBackground(color);
		tempButton.setOpaque(true);
		tempButton.setBorderPainted(false);
		panel.add(tempButton);

		tempButton.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){
			drawPad.changeColor(color);
			drawPad.ThisPlayer.thickness = 2;
			
			UpdateColor packet = new UpdateColor();
			packet.color = color;
			UpdateThickness packet2 = new UpdateThickness();
			packet2.thickness = 2;
			
			Paint.network.client.sendUDP(packet);
			Paint.network.client.sendUDP(packet2);
			} });
	}

	private JPanel panel;
	private PadDraw drawPad;
}

