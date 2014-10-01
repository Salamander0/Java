/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import thegame.model.Explosion;
import thegame.model.GameData;
import thegame.model.Grenade;
import thegame.model.ModelObject;
import thegame.model.Tank;

/**
 *
 * @author beh01
 */
public class MainPanel extends javax.swing.JPanel {

    Image backBuffer;
    //BufferedImage background;
    BufferedImage tankImage;
    List<ModelObject> model;
    
    int currentWindowX = 0, currentWindowY = 0;

    /**
     * Creates new form MainPanel
     */
    public MainPanel() {
        initComponents();
        backBuffer = new BufferedImage(GameData.WINDOW_WIDTH, GameData.WINDOW_HEIGTH, BufferedImage.SCALE_DEFAULT);
        try {
            tankImage = ImageIO.read(getClass().getResource("/thegame/view/tank.png"));
        } catch (IOException ex) {
        }
    }
    
    
    
    public void addToCurrentWindowX(int increment) {
        this.currentWindowX += increment;
        if (currentWindowX < 0) {
            this.currentWindowX = 0;
        }
        if (currentWindowX > GameData.GAME_WIDTH - GameData.WINDOW_WIDTH) {
            currentWindowX = GameData.GAME_WIDTH - GameData.WINDOW_WIDTH;
        }
        
    }

    public void addToCurrentWindowY(int increment) {
        this.currentWindowY += increment;
        if (currentWindowY < 0) {
            currentWindowY = 0;
        }
        if (currentWindowY > GameData.GAME_HEIGTH - GameData.WINDOW_HEIGTH) {
            currentWindowY = GameData.GAME_HEIGTH - GameData.WINDOW_HEIGTH;
        }
        
    }
    public void setViewOnPoint(Point point){
        this.currentWindowX = point.x;
        point.x-=GameData.WINDOW_WIDTH/2;
        if (currentWindowX < 0) {
            this.currentWindowX = 0;
        }
        if (currentWindowX > GameData.GAME_WIDTH - GameData.WINDOW_WIDTH) {
            currentWindowX = GameData.GAME_WIDTH - GameData.WINDOW_WIDTH;
        }
        
        
        this.currentWindowY = point.y;
        point.y-=GameData.WINDOW_HEIGTH/2;
        if (currentWindowY < 0) {
            currentWindowY = 0;
        }
        if (currentWindowY > GameData.GAME_HEIGTH - GameData.WINDOW_HEIGTH) {
            currentWindowY = GameData.GAME_HEIGTH - GameData.WINDOW_HEIGTH;
        }
    }
    public void setViewOnTank(thegame.model.ModelPlayer player) {
        for (ModelObject o : model) {
            if (o instanceof Tank) {
                Tank tank = (Tank) o;
                if (tank.getPlayer().equals(player)) {
                    this.currentWindowX = tank.getX() - GameData.WINDOW_WIDTH / 2;
                    this.currentWindowY = tank.getY() - GameData.WINDOW_HEIGTH / 2;
                }
            }
        }

        if (currentWindowY < 0) {
            currentWindowY = 0;
        }
        if (currentWindowY > GameData.GAME_HEIGTH - GameData.WINDOW_HEIGTH) {
            currentWindowY = GameData.GAME_HEIGTH - GameData.WINDOW_HEIGTH;
        }

        if (currentWindowX < 0) {
            this.currentWindowX = 0;
        }
        if (currentWindowX > GameData.GAME_WIDTH - GameData.WINDOW_WIDTH) {
            currentWindowX = GameData.GAME_WIDTH - GameData.WINDOW_WIDTH;
        }
        
    }

    private int convertX(int x) {
        return x - currentWindowX;
    }

    private int convertY(int y) {
        return y - currentWindowY;
    }

    @Override
    public void paintComponent(Graphics graphic) {
        super.paintComponent(graphic);
        Graphics g = backBuffer.getGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, backBuffer.getWidth(null), backBuffer.getHeight(null));
        g.setColor(Color.red);
        g.drawRect(convertX(5), convertY(5),
                GameData.GAME_WIDTH - 10, GameData.GAME_HEIGTH - 10);

        if (model != null) {
            synchronized (model) {
                for (ModelObject o : model) {
                    if (o instanceof Tank) {
                        Tank tank = (Tank) o;
                        //BufferedImage tmp = new BufferedImage(50, 50, BufferedImage.BITMASK);
                        //Graphics2D g2D =(Graphics2D)tmp.getGraphics();
                        //g2D.drawImage(tankImage,0,0,50,50,null);
                        //g2D.rotate(tank.getRotation(),25,25);
                        //g.drawImage(tmp, tank.getX(), tank.getY() ,50,50, null);
                        AffineTransform tx = AffineTransform.getRotateInstance(
                                tank.getRotation(), tankImage.getWidth() / 2, tankImage.getHeight() / 2);
                        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
                        g.drawImage(op.filter(tankImage, null),
                                convertX(tank.getX() - GameData.TANK_SIZE / 2),
                                convertY(tank.getY() - GameData.TANK_SIZE / 2),
                                null);
                        g.drawString(tank.getPlayer().getRealPlayer().getName(),
                                convertX(tank.getX() - GameData.TANK_SIZE / 2),
                                convertY(tank.getY() - GameData.TANK_SIZE / 2));

                    }
                    if (o instanceof Grenade) {
                        g.setColor(Color.white);
                        g.drawOval(convertX(o.getX() - 2), convertY(o.getY() - 2), 4, 4);
                    }
                    if (o instanceof Explosion) {
                        g.setColor(Color.red);
                        g.fillOval(convertX(o.getX() - 30), convertY(o.getY() - 30), 60, 60);
                    }
                }
            }
        }

        //Graphics2D graphic2d = (Graphics2D) graphic;
        //graphic2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        //      RenderingHints.VALUE_ANTIALIAS_ON);
        graphic.drawImage(backBuffer, 0, 0, this.getWidth(), this.getHeight(), null);

    }

    public void updateModel(List<ModelObject> data) {
        this.model = data;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
