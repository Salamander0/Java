/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.info;

/**
 *
 * @author beh01
 */
public class InfoDetail {
    private int x, y, direction;

    public InfoDetail(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDirection() {
        return direction;
    }
    
}
