package mytetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.*;

import mytetris.Shape.Tetrominoes;

public class SidePanel extends JPanel{
	final int PanelHeight = 5;
	final int PanelWidth = 5;
	
	JLabel label;
	
	public SidePanel(){
		setPreferredSize(new Dimension(100, 400));
		setBackground(Color.gray);
				
		//napis na bocnim panelu
		label = new JLabel("next piece");
        label.setLocation(0, 0);
        label.setSize(50, 40);
        label.setHorizontalAlignment(0);
        add(label, BorderLayout.NORTH);
	}
	
	public static void PrintShape(){
		
	}
	
}
