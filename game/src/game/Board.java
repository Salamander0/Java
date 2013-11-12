package game;


import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Board extends JPanel implements ActionListener {

    private Timer timer;
    private Craft craft;
    private ArrayList aliens;
    private boolean ingame;
    private int B_WIDTH;
    private int B_HEIGHT;
    Image backBuffer;
    BufferedImage background;

    private int[][] pos = { 
        {2380, 58}, {2500, 118}, {1380, 178},
        {780, 218}, {580, 278}, {680, 478}, 
        {790, 518}, {760, 100}, {790, 300},
        {980, 418}, {560, 90}, {510, 140},
        {930, 318}, {590, 160}, {530, 120},
        {940, 118}, {990, 60}, {920, 400},
        {900, 518}, {660, 100}, {540, 180},
        {810, 440}, {860, 40}, {740, 360},
        {820, 256}, {490, 340}, {700, 60}
     };

    public Board() {

        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        ingame = true;

        setSize(800, 600);
        
        backBuffer=new BufferedImage(800, 600, BufferedImage.SCALE_DEFAULT);
        try {
            background = ImageIO.read(getClass().getResource("background.jpg"));
        } catch (IOException ex) {
           
        }

        craft = new Craft();

        initAliens();

        timer = new Timer(5, this);
        timer.start();
    }
    
    public void paintComponent(Graphics graphic) {
        super.paintComponent(graphic);
        Graphics g = backBuffer.getGraphics();
        g.drawImage(background, 0, 0, backBuffer.getWidth(null),backBuffer.getHeight(null),null);
        Graphics2D graphic2d = (Graphics2D) graphic;
        graphic2d.drawImage(backBuffer, 0, 0,this.getWidth(),this.getHeight(), null);
    }


    public void addNotify() {
        super.addNotify();
        B_WIDTH = getWidth();
        B_HEIGHT = getHeight();   
    }

    public void initAliens() {
        aliens = new ArrayList();

        for (int i=0; i<pos.length; i++ ) {
            aliens.add(new Alien(pos[i][0], pos[i][1]));
        }
    }


    public void paint(Graphics g) {
        super.paint(g);

        if (ingame) {

            Graphics2D g2d = (Graphics2D)g;

            if (craft.isVisible())
                g2d.drawImage(craft.getImage(), craft.getX(), craft.getY(),
                              this);

            ArrayList ms = craft.getMissiles();

            for (int i = 0; i < ms.size(); i++) {
                Missile m = (Missile)ms.get(i);
                g2d.drawImage(m.getImage(), m.getX(), m.getY(), this);
            }

            for (int i = 0; i < aliens.size(); i++) {
                Alien a = (Alien)aliens.get(i);
                if (a.isVisible())
                    g2d.drawImage(a.getImage(), a.getX(), a.getY(), this);
            }

            g2d.setColor(Color.WHITE);
            g2d.drawString("Aliens left: " + aliens.size(), 5, 15);


        } else {
            String msg = "Game Over";
            Font small = new Font("Helvetica", Font.BOLD, 14);
            FontMetrics metr = this.getFontMetrics(small);

            g.setColor(Color.white);
            g.setFont(small);
            g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2,
                         B_HEIGHT / 2);
        }

        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }


    public void actionPerformed(ActionEvent e) {

        if (aliens.size()==0) {
            ingame = false;
        }

        ArrayList ms = craft.getMissiles();

        for (int i = 0; i < ms.size(); i++) {
            Missile m = (Missile) ms.get(i);
            if (m.isVisible()) 
                m.move();
            else ms.remove(i);
        }

        for (int i = 0; i < aliens.size(); i++) {
            Alien a = (Alien) aliens.get(i);
            if (a.isVisible()) 
                a.move();
            else aliens.remove(i);
        }

        craft.move();
        checkCollisions();
        repaint();  
    }

    public void checkCollisions() {

        Rectangle r3 = craft.getBounds();

        for (int j = 0; j<aliens.size(); j++) {
            Alien a = (Alien) aliens.get(j);
            Rectangle r2 = a.getBounds();

            if (r3.intersects(r2)) {
                craft.setVisible(false);
                a.setVisible(false);
                ingame = false;
            }
        }

        ArrayList ms = craft.getMissiles();

        for (int i = 0; i < ms.size(); i++) {
            Missile m = (Missile) ms.get(i);

            Rectangle r1 = m.getBounds();

            for (int j = 0; j<aliens.size(); j++) {
                Alien a = (Alien) aliens.get(j);
                Rectangle r2 = a.getBounds();

                if (r1.intersects(r2)) {
                    m.setVisible(false);
                    a.setVisible(false);
                }
            }
        }
    }


    private class TAdapter extends KeyAdapter {

        public void keyReleased(KeyEvent e) {
            craft.keyReleased(e);
        }

        public void keyPressed(KeyEvent e) {
            craft.keyPressed(e);
        }
    }
}
