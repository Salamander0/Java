/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.model;

/**
 *
 * @author beh01
 */
public class Grenade extends ModelObject {

    private Tank tank;
    private int lifeTime = 75;

    public Grenade(Tank tank, int x, int y, int direction) {
        super(x, y, direction);
        this.tank = tank;
    }

    public Tank getTank() {
        return tank;
    }
    public boolean notVisible() {
        lifeTime--;
        return (lifeTime>0)? false:true;
    }

    public void move() {
        double angle = Math.PI * getDirection() / 180.0;
        int x = (int) (Math.round(GameData.GRENADE_SPEED * Math.cos(angle)));
        int y = (int) (Math.round(GameData.GRENADE_SPEED * Math.sin(angle)));
        move(x, y);
    }
}
