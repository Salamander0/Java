package paint;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.*;
import java.awt.event.*;

import com.esotericsoftware.kryonet.Listener;

public class Paint extends Listener{
	static Network network = new Network();
	static HashMap<Integer, NewPlayer> players = new HashMap<Integer, NewPlayer>();
	
	
	public static void main(String[] args)throws Exception{
		PaintWindow frame = new PaintWindow();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);
		
		network.connect();
	}
}

@SuppressWarnings("serial")
class PaintWindow 	extends JFrame
					implements ChangeListener{
	public static String host;
	public static String name;


	public PaintWindow(){
		
		// Request the host from the user.
				String input = (String)JOptionPane.showInputDialog(null, "Host:", "Connect to chat server", JOptionPane.QUESTION_MESSAGE, null, null, "localhost");
				if (input == null || input.trim().length() == 0) System.exit(1);
				host = input.trim();

		// Request the user's name.
				input = (String)JOptionPane.showInputDialog(null, "Name:", "Connect to chat server", JOptionPane.QUESTION_MESSAGE, null, null, "Test");
				if (input == null || input.trim().length() == 0) System.exit(1);
				name = input.trim();
		
		setTitle("Net Paint");
		setSize(1100, 600);

		this.panel = new JPanel();
		this.drawPad = new PadDraw();
		PaintWindow.chatPanel = new ChatWindow();

		this.panel.setPreferredSize(new Dimension(800, 60));

		Container content = this.getContentPane();
		content.setLayout(new BorderLayout());

		content.add(this.panel, BorderLayout.SOUTH);
		content.add(this.drawPad, BorderLayout.CENTER);
		content.add(PaintWindow.chatPanel, BorderLayout.EAST);
		
		JButton eraseButton = new JButton("Eraser");
		eraseButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				PaintWindow.this.drawPad.changeColor(Color.WHITE, 0);
				//PaintWindow.this.drawPad.changeThickness(22);
			}
		});
		this.panel.add(eraseButton);
		
		makeColorButton(Color.WHITE,0);
		makeColorButton(Color.YELLOW,1);
		makeColorButton(Color.ORANGE,2);
		makeColorButton(Color.RED,3);
		makeColorButton(Color.MAGENTA,4);
		makeColorButton(Color.CYAN,5);
		makeColorButton(Color.BLUE,6);
		makeColorButton(Color.GREEN,7);
		makeColorButton(Color.GRAY,8);
		makeColorButton(Color.BLACK,9);
			
		JLabel label = new JLabel("\t     Thickness:");
        this.panel.add(label);		
		
		JSlider thickness = new JSlider(SwingConstants.HORIZONTAL,1, 25, 3);
		thickness.addChangeListener(this);
		thickness.setMajorTickSpacing(8);
		thickness.setMinorTickSpacing(2);
		thickness.setPaintTicks(true);
		thickness.setPaintLabels(true);
		thickness.setBorder(
                BorderFactory.createEmptyBorder(0,0,10,0));
        this.panel.add(thickness);
	
	}
	
	public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
            int thickness = (int)source.getValue();
            PaintWindow.this.drawPad.changeThickness(thickness);
        }
    }

	public void makeColorButton(final Color color, final int col){
		JButton tempButton = new JButton();
		tempButton.setPreferredSize(new Dimension(20, 20));
		tempButton.setBackground(color);
		tempButton.setOpaque(true);
		tempButton.setBorderPainted(false);

		tempButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				PaintWindow.this.drawPad.changeColor(color, col);
			}
		});
		this.panel.add(tempButton);
	}

	private JPanel panel;
	PadDraw drawPad;
	static ChatWindow chatPanel;
}

