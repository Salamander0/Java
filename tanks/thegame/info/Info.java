/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.info;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author beh01
 */
public class Info {
    private int x, y, direction;
    boolean canShoot;
    List<InfoDetail> enemies = new ArrayList<InfoDetail>();
    List<InfoDetail> grenades = new ArrayList<InfoDetail>();
    
    public Info(int x, int y, int direction, boolean canShoot) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.canShoot = canShoot;
    }

    public int getDirection() {
        return direction;
    }

    public List<InfoDetail> getEnemies() {
        return enemies;
    }

    public List<InfoDetail> getGrenades() {
        return grenades;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isCanShoot() {
        return canShoot;
    }
    
}
