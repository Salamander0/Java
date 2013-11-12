package mytetris;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.*;

public class mytetris extends JFrame{
	
	JLabel statusbar, label;
	JPanel panel;
	
	public mytetris(){
		//vytvoreni statusbaru
		statusbar = new JLabel("Score: " + "0");
		add(statusbar, BorderLayout.SOUTH);
		
		//bocni panel -> next piece
		SidePanel panel = new SidePanel();
		add(panel, BorderLayout.EAST);
		
		//nova hraci plocha + start		
		Board board = new Board(this);
		add(board);
		board.start();
		
		setSize(300,400);
		setTitle("Game of Tetris");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public JLabel getStatusBar(){
		return statusbar;
	}
	
	public static void main(String[] args){
		
		mytetris game = new mytetris();
		game.setLocationRelativeTo(null);
		game.setVisible(true);
	}

}
