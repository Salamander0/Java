/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package thegame.model;

/**
 *
 * @author beh01
 */
public class Explosion extends ModelObject{
    private int lifeTime = 10;

    public Explosion(int x, int y) {
        super(x, y, 0);
    }
    public boolean notVisible() {
        lifeTime--;
        return (lifeTime>0)? false:true;
    }
    
}
