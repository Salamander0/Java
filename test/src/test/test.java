package test;

import javax.swing.*;
import java.awt.event.*;

public class test extends JFrame{
	public test()
	{
		super("Menu example");
		
		JMenu file = new JMenu("File");
		file.setMnemonic('F');
		JMenuItem newItem = new JMenuItem("New");
		newItem.setMnemonic('N');
		file.add(newItem);
		JMenuItem openItem = new JMenuItem("Open");
		openItem.setMnemonic('O');
		file.add(openItem);	
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic('x');
		file.add(exitItem);
		
		//adding action listener to menu items
		newItem.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e)
				{
					System.out.println("New is pressed");
				}
			}
		);
		openItem.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e)
				{
					int x = 4;
					int a = 5;
					a %= x;
					System.out.println(a);
				}
			}
		);
		exitItem.addActionListener(
			new ActionListener(){
				public void actionPerformed(ActionEvent e)
				{
					System.out.println("Exit is pressed");
				}
			}
		);						
		JMenuBar bar = new JMenuBar();
		setJMenuBar(bar);
		bar.add(file);
		
		getContentPane();
		setSize(200, 200);
		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		test app = new test();
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}