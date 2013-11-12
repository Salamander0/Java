/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.model;

/**
 *
 * @author beh01
 */
public abstract class ModelObject {
    private  int x, y, direction;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public int getDirection() {
        return direction;
    }
    public void addToDirection(int angle) {
        direction+=angle;
         if (direction < 0) {
            direction += 360;
        }
        if (direction >= 360) {
            direction -= 360;
        }
    }
    protected void move(int offsetX, int offsetY) {
        x+=offsetX;
        y+=offsetY;
    }
    public ModelObject(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction=direction;
    }
}
